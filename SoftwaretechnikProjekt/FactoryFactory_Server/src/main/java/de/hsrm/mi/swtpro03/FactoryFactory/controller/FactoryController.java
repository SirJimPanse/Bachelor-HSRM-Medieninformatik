package de.hsrm.mi.swtpro03.FactoryFactory.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.ws.Endpoint;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.AreaPositioningException;
import de.hsrm.mi.swtpro03.FactoryFactory.exception.FactoryDoesNotExistException;
import de.hsrm.mi.swtpro03.FactoryFactory.exception.ResizeMustNotDeleteMachinesException;
import de.hsrm.mi.swtpro03.FactoryFactory.exception.SessionDoesNotExistException;
import de.hsrm.mi.swtpro03.FactoryFactory.manager.FactoryManager;
import de.hsrm.mi.swtpro03.FactoryFactory.manager.SessionManager;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Factory;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Position;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Session;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Consumer;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.IScriptable;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Machine;
import de.hsrm.mi.swtpro03.FactoryFactory.service.FactoryService;
import de.hsrm.mi.swtpro03.FactoryFactory.shareObject.MachineMap;
import de.hsrm.mi.swtpro03.FactoryFactory.util.IMessagePublisher;

public class FactoryController {
	private static final Logger LOGGER = Logger.getLogger("FactoryController");

	private FactoryManager factoryManager;
	private SessionManager sessionManager;
	private IMessagePublisher mqPublisher;
	
	public FactoryController(SessionManager sessionManager, FactoryManager factoryManager, IMessagePublisher mqPublisher){
		this.mqPublisher = mqPublisher;
		this.sessionManager = sessionManager;
		this.factoryManager = factoryManager;
	}

	public void start() {
		startWebService();
	}
	
	public String createMachine(long sessionID, int classID, Position position) {
		Factory factory = getFactoryBySessionID(sessionID);
		int factoryID = factory.getFactoryID();
		String machineID = "";
		machineID = factory.placeMachine(classID, position);
		if(!machineID.equals("")){
			mqPublisher.sendMessage(String.valueOf(sessionID), String.valueOf(factoryID), machineID, "create");
			factory.getArea().show();
		}
		return machineID;
	}
	
	public boolean moveMachine(long sessionID, String machineID, int newX, int newY) {
		Factory factory = getFactoryBySessionID(sessionID);
		int factoryID = factory.getFactoryID();
		boolean moved = false;
		try {
			moved = factory.moveMachine(machineID, newX, newY);
			if (moved) {
				mqPublisher.sendMessage(String.valueOf(sessionID), String.valueOf(factoryID), machineID, "move");
			}
			factory.getArea().show();
		} catch (AreaPositioningException e) {
			LOGGER.log(Level.INFO, "moveMachine " + e.getMessage());
		}		
		return moved;
	}
		
	public void deleteMachines(long sessionID, String[] machineIDs){
		Factory factory = getFactoryBySessionID(sessionID);
		int factoryID = factory.getFactoryID();
		for(String machineID : machineIDs){
			deleteMachineOnFactory(sessionID, factory, factoryID, machineID);
		}
		factory.getArea().show();
	}

	private void deleteMachineOnFactory(long sessionID, Factory factory,
			int factoryID, String machineID) {
		if (factory.deleteMachine(machineID)) {
			mqPublisher.sendMessage(String.valueOf(sessionID), String.valueOf(factoryID), machineID, "delete");
		}
	}
	
	public MachineMap getMachineMap(long sessionID){
		Factory factory = getFactoryBySessionID(sessionID);
		MachineMap machineMap = new MachineMap();
		if(factory != null){
			for(String machineID : factory.getMachineMap().keySet()){
				Position position = factory.getMachineMap().get(machineID).getPosition();
				machineMap.addMachineToMap(machineID, position);
			}
		}
		return machineMap;
	}

	public Factory getFactoryBySessionID(long sessionId) {
		Factory factory = null;
		try {
			Session session = sessionManager.get(sessionId);
			int factoryId = session.getFactoryId();
			factory = factoryManager.getFactory(factoryId);
		} catch (SessionDoesNotExistException e) {
			LOGGER.log(Level.INFO, "getFactoryBySessionId " + e.getMessage());
		} catch (FactoryDoesNotExistException e) {
			LOGGER.log(Level.INFO, "getFactoryBySessionId " + e.getMessage());
		}
		return factory;
	}
	
	private void startWebService() {
		final String URL = "http://0.0.0.0:8080/FactoryService";
		Endpoint.publish(URL, new FactoryService(this));
		LOGGER.log(Level.INFO, "FactoryService: "+URL+"?wsdl");
	}

	public void rotateMachines(long sessionID, String[] machineIDs, String direction){
		for(String id : machineIDs){
			Machine machine = getMachine(sessionID, id);
			if(machine != null){
				if(direction == "right"){
					machine.rotateRight();
				}
				else { //direction == "left"
					machine.rotateLeft();
				}
				sendMQRotate(sessionID, id, direction);
			}
		}
	}
	
	private Machine getMachine(long sessionID, String machineID) {
		Factory factory = getFactoryBySessionID(sessionID);
		Machine machine = factory.getMachineByID(machineID);
		return machine;
	}
	
	public Position getMachinePosition(long sessionID, String machineID) {
		Machine machine = getMachine(sessionID, machineID);
		if(machine != null){
			return machine.getPosition();
		}
		return null;
	}
	
	private void sendMQRotate(long sessionID, String machineID, String direction) {
		Factory factory = getFactoryBySessionID(sessionID);
		int factoryID = factory.getFactoryID();
		mqPublisher.sendMessage(String.valueOf(sessionID), String.valueOf(factoryID), machineID, "rotate-"+direction);
	
	}

	public void saveFactory(long sessionID) {
		try {
			Session session = sessionManager.get(sessionID);
			int factoryId = session.getFactoryId();
			factoryManager.saveFactory(factoryId);
		} catch (SessionDoesNotExistException e) {
			LOGGER.log(Level.INFO, "saveFactory " + e.getMessage());
		}
	}
	
	public boolean addMemberToMemberAccess(long sessionID, int factoryID, String login){
		boolean added = false;
		try {
			added = factoryManager.addMemberToMemberAccess(factoryID, login);
			Session session = sessionManager.get(sessionID);
			Factory factory = factoryManager.getFactory(factoryID);
			mqPublisher.sendMessage(session.getSessionID()+"", login, factory.getFactoryID()+" - "+factory.getName(), "added-Factory");
			LOGGER.log(Level.INFO, "addMemberToMemberAccess - sendMessage: " + session.getName() + login + factory.getName() + "added-Factory");
		} catch (SessionDoesNotExistException e) {
			LOGGER.log(Level.INFO, "addMemberToMemberAccess " + e.getMessage());
		} catch (FactoryDoesNotExistException e) {
			LOGGER.log(Level.INFO, "addMemberToMemberAccess " + e.getMessage());
		}
		return added;
	}

	public void deleteFactory(long sessionID, int factoryID) {
		try {
			Session session = sessionManager.get(sessionID);
			String login = session.getName();
			factoryManager.deleteFactory(factoryID, login);
		} catch (SessionDoesNotExistException e) {
			LOGGER.log(Level.INFO, "deleteFactory " + e.getMessage());
		} 
	}
	
	public synchronized int createFactory(long sessionID, String login, Factory factory) {
		int factoryID = -1;
		factoryID = factoryManager.createFactory(login, factory);
//		if (factoryID != -1) {
//			addMemberToMemberAccess(sessionID, factoryID, login);
//		}
		return factoryID;
	}
	
	public Factory getFactoryByFactoryID(int factoryId) throws FactoryDoesNotExistException {
		return factoryManager.getFactory(factoryId);
	}

	public void scriptMachine(long sessionID, String machineID, String script) {
		Factory factory = getFactoryBySessionID(sessionID);
		Machine machine = factory.getMachineByID(machineID);
		if(machine instanceof IScriptable){
			((IScriptable)machine).setScript(script);
		}
	}

	public String getMachineScript(long sessionID, String machineID) {
		Factory factory = getFactoryBySessionID(sessionID);
		Machine machine = factory.getMachineByID(machineID);		
		if(machine instanceof IScriptable){
			return ((IScriptable)machine).getScript();
		} else {
			return "";
		}
	}

	public boolean resizeFactory(long sessionID, int width, int height) {
		Factory factory = getFactoryBySessionID(sessionID);
		try {
			factory.resize(width, height);
			mqPublisher.sendMessage(String.valueOf(sessionID), String.valueOf(factory.getFactoryID()), "-1", "resize");
			return true;
		} catch (ResizeMustNotDeleteMachinesException e) {
			return false;
		}
	}
	

	public int getAreaWidth(long sessionID){
		Factory factory = getFactoryBySessionID(sessionID);
		if(factory != null){
			return factory.getArea().getWidth();
		}
		return -1;
	}
	
	public int getAreaHeight(long sessionID){
		Factory factory = getFactoryBySessionID(sessionID);
		if(factory != null){
			return factory.getArea().getHeight();
		}
		return -1;
	}

	public void setMachineCapacity(long sessionID, String[] machineIDs, int noOfProducts) {
		Machine actMachine;
		Factory factory = getFactoryBySessionID(sessionID);
		if(factory != null && machineIDs != null){
			for(String id : machineIDs){
				actMachine = factory.getMachineByID(id);
				actMachine.setMaxCapacityInNumberOfProducts(noOfProducts);
			}
		}
	}

	public int getMachineCapacity(long sessionID, String machineId) {
		Factory factory = getFactoryBySessionID(sessionID);
		Machine machine = factory.getMachineByID(machineId);
		return machine.getMaxCapacityInNumberOfProducts();
	}

	public void sendChatMessage(long sessionID, String text) {
		Session session;
		try {
			session = sessionManager.get(sessionID);
			int factoryId = session.getFactoryId();
			String publisher = session.getName();
			mqPublisher.sendMessage(sessionID+"", factoryId+"", publisher + ": " + text, "chat");
		} catch (SessionDoesNotExistException e) {
			LOGGER.log(Level.INFO, "chat " + e.getMessage());
		}
	}

	public int getEmptyConsumerTickCount(long sessionID, String machineId) {
		Factory factory = getFactoryBySessionID(sessionID);
		Machine machine = factory.getMachineByID(machineId);
		if(machine instanceof Consumer){
			return ((Consumer) machine).getEmptyCounterMax();
		}
		return -1;
	}
	
	public void setEmptyConsumerTickCount(long sessionID, String machineId, int noOfTicks) {
		Factory factory = getFactoryBySessionID(sessionID);
		Machine machine = factory.getMachineByID(machineId);
		if(machine instanceof Consumer){
			((Consumer) machine).setEmptyCounterMax(noOfTicks);
		}
	}
}
