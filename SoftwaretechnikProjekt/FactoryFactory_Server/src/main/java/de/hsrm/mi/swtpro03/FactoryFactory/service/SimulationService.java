package de.hsrm.mi.swtpro03.FactoryFactory.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import de.hsrm.mi.swtpro03.FactoryFactory.controller.SimulationController;
import de.hsrm.mi.swtpro03.FactoryFactory.shareObject.FactoryMap;
import de.hsrm.mi.swtpro03.FactoryFactory.shareObject.MachineMap;
import de.hsrm.mi.swtpro03.FactoryFactory.shareObject.ProductMap;


@WebService
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class SimulationService {

	private static Logger logger = Logger.getLogger("SimulationService");
	
	private SimulationController simulationController;
	
	public SimulationService() {}
	
	public SimulationService(SimulationController simulationController) {
		this.simulationController = simulationController; 
	}
	
	public String createSimulation(long sessionID) {
		logger.log(Level.INFO, "initiiere Simulation");
		return simulationController.createSimulation(sessionID);
	}

	public ProductMap getProducts(int simulationId) {
		logger.log(Level.INFO, "getProducts für simulation " + simulationId + " aufgerufen");
		return simulationController.getProducts(simulationId);
	}

	public void produce(long sessionID, int simulationId, int timeInMilliseconds) {
		logger.log(Level.INFO, "User "+sessionID+" hat produce für simulation " + simulationId + " aufgerufen");
		long timeInMillis = timeInMilliseconds;
		simulationController.produce(sessionID, simulationId, timeInMillis);
	}
	
	/**@param sessionId
	 * @param simulationId
	 * @return -1 if simulation is still running, 0 if simulation has been deleted
	 */
	public int deleteSimulation(long sessionId, int simulationId){
		return simulationController.deleteSimulation(sessionId, simulationId);
	}
	
	public FactoryMap getSimulations(long sessionID){
		logger.log(Level.INFO, "getSimulations für sessionID " + sessionID);
		return simulationController.getSimulations(sessionID);
	}
		
	public void addMemberToSimulation(long sessionID, int simulationId, String login){
		logger.log(Level.INFO, "addMemberToSimulation - " + login + " zu factory " + simulationId);
		simulationController.addMemberToSimulation(sessionID, simulationId, login);
	}
	
	public void stopSimulation(int simulationId){
		logger.log(Level.INFO, "stopSimulation für simulationID " + simulationId);
		simulationController.cancelProduction(simulationId);
	}
	
	public MachineMap getSimulation(int simulationId){
		logger.log(Level.INFO, "getSimulation für simulationID " + simulationId);
		return simulationController.getMachineMap(simulationId);
	}
	
	public int getSimulationAreaWidth(int simulationId){
		return simulationController.getAreaWidth(simulationId);
	}
	
	public int getSimulationAreaHeight(int simulationId){
		return simulationController.getAreaHeight(simulationId);
	}
}