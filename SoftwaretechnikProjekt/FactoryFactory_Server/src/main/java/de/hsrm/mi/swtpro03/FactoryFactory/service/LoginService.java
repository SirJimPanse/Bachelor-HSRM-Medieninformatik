package de.hsrm.mi.swtpro03.FactoryFactory.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import de.hsrm.mi.swtpro03.FactoryFactory.controller.LoginController;
import de.hsrm.mi.swtpro03.FactoryFactory.shareObject.FactoryMap;


@WebService
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class LoginService {
	
	private static Logger logger = Logger.getLogger("LoginService");
	private LoginController loginController;

	public LoginService(){}
	
	public LoginService(LoginController loginController){
		this.loginController = loginController;
	}
	
	public boolean welcome() {
		logger.log(Level.INFO, "Welcome: unser server liefert true.");
		return true;
	}

	public boolean register(String login, String password) {
		logger.log(Level.INFO, "Register: user: " + login + " password: " + password);
		return loginController.register(login, password);
	}
	
	public long connect(String login, String password) {
		logger.log(Level.INFO, "Loginversuch: user: " + login + " password: " + password);
		return loginController.login(login, password);
	}

	public FactoryMap getFactories(long sessionID){
		logger.log(Level.INFO, "getFactories für sessionID " + sessionID);
		return loginController.getFactories(sessionID);
	}

	public boolean connectToFactory(long sessionID, int factoryID){
		logger.log(Level.INFO, "Verbindung zu Factory " + factoryID + " mit sessionID " + sessionID + " aufbauen");
		return loginController.connectToFactory(sessionID,factoryID);
	}

	public int createFactory(long sessionID, String name, int x, int y){
		logger.log(Level.INFO, "Erstelle Factory " + name + " für sessionID " + sessionID); 
		return loginController.createFactory(sessionID, name, x, y);
	}

	public boolean disconnect(long sessionID){
		logger.log(Level.INFO,  "has disconnected");
		return loginController.disconnect(sessionID);
	}

	public void disconnectFromFactory(long sessionID){
		logger.log(Level.INFO, "disconnected from Factory");
		loginController.disconnectFromFactory(sessionID);
	}
}