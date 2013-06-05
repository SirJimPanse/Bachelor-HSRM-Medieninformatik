package de.hsrm.mi.swtpro03.FactoryFactory.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.ws.Endpoint;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.BadLoginException;
import de.hsrm.mi.swtpro03.FactoryFactory.exception.FactoryDoesNotExistException;
import de.hsrm.mi.swtpro03.FactoryFactory.exception.SessionDoesNotExistException;
import de.hsrm.mi.swtpro03.FactoryFactory.manager.FactoryManager;
import de.hsrm.mi.swtpro03.FactoryFactory.manager.MemberManager;
import de.hsrm.mi.swtpro03.FactoryFactory.manager.SessionManager;
import de.hsrm.mi.swtpro03.FactoryFactory.manager.SimulationManager;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Factory;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Session;
import de.hsrm.mi.swtpro03.FactoryFactory.service.LoginService;
import de.hsrm.mi.swtpro03.FactoryFactory.shareObject.FactoryMap;

public class LoginController{
	private static Logger logger = Logger.getLogger("LoginController");

	private SessionManager sessionManager;
	private FactoryManager factoryManager;
	private MemberManager memberManager;
	private SimulationManager simulationManager;
	
	public LoginController(SessionManager sessionManager, FactoryManager factoryManager, MemberManager memberManager, SimulationManager simulationManager){
		this.sessionManager = sessionManager;
		this.factoryManager = factoryManager;
		this.memberManager = memberManager;
		this.simulationManager = simulationManager;
	}

	public void start() {
		startWebService();
	}

	public long login(String login, String password) {
		try{
			memberManager.login(login, password);
			Session session = sessionManager.createSession(login, password);
			simulationManager.addMemberToMemberMap(login);
			return session.getSessionID();
		} catch(BadLoginException e){
			logger.log(Level.INFO, "Login: " + e.getMessage());
		}
		return -1;	
	}

	public FactoryMap getFactories(long sessionId) {
		FactoryMap map = new FactoryMap();
		try {
			Session session = sessionManager.get(sessionId);
			String login = session.getName();
			map.setFactories(memberManager.getFactories(login));
		} catch (SessionDoesNotExistException e) {
			logger.log(Level.INFO, "GetFactories: " + e.getMessage());
		}
		return map;
	}

	public boolean connectToFactory(long sessionId, int factoryId){
		try{
			Session session = sessionManager.get(sessionId);
			Factory factory = factoryManager.getFactory(factoryId);
			disconnectFromFactory(sessionId);
			session.setFactoryId(factory.getFactoryID());
			return true;
		} catch(FactoryDoesNotExistException e){
			logger.log(Level.INFO, "connectToFactroy: " + e.getMessage());
		} catch (SessionDoesNotExistException e) {
			logger.log(Level.INFO, "connectToFactroy: " + e.getMessage());
		}
		return false;
	}
	
	public synchronized int createFactory(long sessionId, String name, int x, int y) {
		try {
			Session session = sessionManager.get(sessionId);
			String login = session.getName();
			int factoryId = factoryManager.createFactory(login, name, x, y);
			session.setFactoryId(factoryId);
			return factoryId;
		} catch (SessionDoesNotExistException e) {
			logger.log(Level.INFO, "connectToFactory: " + e.getMessage());
		}
		return -1;
	}

	private void startWebService() {
		final String URL = "http://0.0.0.0:8080/LoginService";
		Endpoint.publish(URL, new LoginService(this));
		logger.log(Level.INFO, "LoginService: "+URL+"?wsdl");
	}


	public boolean disconnect(long sessionId) {
		return sessionManager.remove(sessionId);
	}
	
	public void disconnectFromFactory(long sessionId){
		try {
			Session session = sessionManager.get(sessionId);
			session.unsetFactory();
		} catch (SessionDoesNotExistException e) {
			logger.log(Level.INFO, "DisconnectFromFactory: " + e.getMessage());
		}
	}

	public boolean register(String login, String password) {
		return memberManager.register(login, password);
	}

}
