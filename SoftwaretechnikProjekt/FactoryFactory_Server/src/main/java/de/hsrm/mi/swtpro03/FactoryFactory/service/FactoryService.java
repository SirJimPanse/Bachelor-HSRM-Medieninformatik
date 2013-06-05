package de.hsrm.mi.swtpro03.FactoryFactory.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import de.hsrm.mi.swtpro03.FactoryFactory.controller.FactoryController;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Direction;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Position;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Changer;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Positioner;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Producer;
import de.hsrm.mi.swtpro03.FactoryFactory.shareObject.MachineMap;

@WebService
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class FactoryService {
	private static Logger logger = Logger.getLogger("FactoryService");
	private FactoryController factoryController;
	
	public FactoryService(){}
	
	public FactoryService(FactoryController factoryController){
		this.factoryController = factoryController; 
	}
	
	public int getEmptyConsumerTickCount(long sessionID, String machineId){
		logger.log(Level.INFO, "getEmptyConsumerTickCount für sessionID " + sessionID);
		return factoryController.getEmptyConsumerTickCount(sessionID, machineId);
	}
	
	public void setEmptyConsumerTickCount(long sessionID, String machineId, int noOfTicks){
		logger.log(Level.INFO, "setEmptyConsumerTickCount " + noOfTicks + " gesetzt.");
		factoryController.setEmptyConsumerTickCount(sessionID, machineId, noOfTicks);
	}
	
	//public void wtf(long i, int j, String[] k) { } // sowas hier lässt auf C# Seite alles abstürzen
	
	/** IDs derjenigen Maschinenklassen, die IScriptable implementieren **/
	public static int[] SCRIPTABLE_ITEMS = { Changer.CLASS_ID, Positioner.CLASS_ID, Producer.CLASS_ID };
	
	public int[] getScriptableMachines() { return SCRIPTABLE_ITEMS; }
	
	public int getMachineCapacity(long sessionID, String machineId){
		logger.log(Level.INFO, "getMaxCapacityInNumberOfProducts für sessionID " + sessionID);
		return factoryController.getMachineCapacity(sessionID, machineId);
	}
	
	public void setMachineCapacity(long sessionID, String[] machineIDs, int noOfProducts){
		logger.log(Level.INFO, "setMaxCapacityInNumberOfProducts auf " + noOfProducts + " gesetzt.");
		factoryController.setMachineCapacity(sessionID, machineIDs, noOfProducts);
	}
	
	public MachineMap getFactory(long sessionID){
		logger.log(Level.INFO, "getFactory für sessionID " + sessionID);
		return factoryController.getMachineMap(sessionID);
	}
	
	public String createMachine(long sessionID, int classID, int x, int y, Direction dir){
		Position position = new Position(x, y, dir);
		logger.log(Level.INFO, "Erstelle Maschine der Klasse " + classID + " an Position " + position + " für sessionID " + sessionID);
		return factoryController.createMachine(sessionID, classID, position);
	}

	public boolean moveMachine(long sessionID, String machineID, int newX, int newY){
		logger.log(Level.INFO, "Verschiebe Maschine " + machineID + " an Position " + newX + "," + newY + " für sessionID " + sessionID);
		return factoryController.moveMachine(sessionID,machineID, newX, newY);
	}
	
	public void deleteMachines(long sessionID, String[] machineIDs){
		logger.log(Level.INFO, "Lösche Maschinen für sessionID " + sessionID);
		factoryController.deleteMachines(sessionID, machineIDs);
	}
	
	public void rotateMachinesLeft(long sessionID, String[] machineIDs){
		logger.log(Level.INFO, "Client "+sessionID+" ruft rotateMachinesLeft für " + machineIDs.length + "Maschinen auf auf.");
		factoryController.rotateMachines(sessionID, machineIDs, "left");
	}
	
	public void rotateMachinesRight(long sessionID, String[] machineIDs){
		logger.log(Level.INFO, "Client "+sessionID+" ruft rotateMachinesRight für " + machineIDs.length + "Maschinen auf auf.");
		factoryController.rotateMachines(sessionID, machineIDs, "right");
	}
	
	public Position getMachinePosition(long sessionID, String machineID){
		logger.log(Level.INFO, "getPosition für " + machineID + " sessionID: " + sessionID);
		Position pos = factoryController.getMachinePosition(sessionID, machineID);
		if (pos == null) {
			return new Position(-1,-1,Direction.NORTH);
		}
		return pos;
	}
	
	public void saveFactory(long sessionID){
		logger.log(Level.INFO, "SaveFactory");
		factoryController.saveFactory(sessionID);
	}
	
	public boolean addMemberToMemberAccess(long sessionID, int factoryID, String login){
		logger.log(Level.INFO, "addMemberToMemberAccess - " + login + " zu factory " + factoryID);
		return factoryController.addMemberToMemberAccess(sessionID, factoryID, login);
	}

	public void deleteFactory(long sessionID, int factoryId){
		logger.log(Level.INFO, "deleteFactory " + factoryId);
		factoryController.deleteFactory(sessionID, factoryId);
	}
	
	public int getAreaWidth(long sessionID){
		return factoryController.getAreaWidth(sessionID);
	}
	
	public int getAreaHeight(long sessionID){
		return factoryController.getAreaHeight(sessionID);
	}
	
	public void scriptMachine(long sessionID, String machineID, String script){
		logger.log(Level.INFO, "scriptMachine: "+script+" Maschine : " + machineID);
		factoryController.scriptMachine(sessionID, machineID, script);
	}
	
	public String getMachineScript(long sessionID, String machineID){
		return factoryController.getMachineScript(sessionID, machineID);
	}
	
	public void sendChatMessage(long sessionID, String text){
		factoryController.sendChatMessage(sessionID, text);
	}
	
	public boolean resizeFactory(long sessionID, int width, int height) {
		logger.log(Level.INFO, "resize factory for session "+sessionID+", width:"+width+", height:"+height);
		return factoryController.resizeFactory(sessionID, width, height);
	}
}
