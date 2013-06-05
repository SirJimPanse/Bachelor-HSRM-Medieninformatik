package de.hsrm.mi.swtpro03.FactoryFactory.manager;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.FactoryDoesNotExistException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Factory;
import de.hsrm.mi.swtpro03.FactoryFactory.util.User;

public class SimulationManager {
	
	private static Logger logger = Logger.getLogger("SimulationManager");
	
	private SortedMap<Integer, Factory> simulationMap;
	private SortedMap<String, User> memberMap;
	
	public SimulationManager() {
		simulationMap = new TreeMap <Integer, Factory>();
		memberMap = new TreeMap <String, User>();
	}
	
	public void addMemberToMemberMap(String login){
		if(!memberMap.containsKey(login)){
			User user = new User(login);
			memberMap.put(login, user);
		}
	}
	
	public Factory getSimulation(int simulationId) throws FactoryDoesNotExistException {
		Factory factory = simulationMap.get(simulationId);
		if(factory != null){
			return factory;
		}
		throw new FactoryDoesNotExistException();
	}
	
	public void putSimulation(Factory simulation) {
		simulationMap.put(simulation.getFactoryID(), simulation);
	}

	/**deletes the simulation if it is not running and if there are no other users
	 * in the simulation.
	 * if there are many users the user is being removed from the access-list
	 * @return false if there are still others members in the simulation else true
	 */
	public boolean deleteSimulation(String login, int simulationId, boolean simulationIsRunning) {
		User user = memberMap.get(login);
		int usersInSimulation = 0;
		
		for(User act : memberMap.values()){
			if(act.getSimulationList().contains(simulationId)){
				usersInSimulation += 1;
				
				if (usersInSimulation > 1) {
					user.getSimulationList().remove(Integer.valueOf(simulationId));
					return false;
				}
			}
		}
		
		if(!simulationIsRunning){
			user.deleteSimulationFromAccess(simulationId);
			simulationMap.remove(simulationId);
			logger.log(Level.INFO, "Simulation wurde gelöscht.");
		}
		return true;
	}

	
	
	public synchronized int createFactory(String login, Factory simulation, long sessionID) {
		int simulationId = getNextSimulationId();
		simulation.setFactoryID((simulationId));
		putSimulation(simulation);
		
		addMemberToMemberAccess(simulationId, login);
		return simulationId;
	}
	
	public SortedMap<Integer, String> getSimulations(String login){
		String factName;
		SortedMap<Integer, String> userSimulationMap = new TreeMap<Integer, String>();
		User user = memberMap.get(login);
		List<Integer> userSimulationIds = user.getSimulationList();
		
		for(Integer actId : userSimulationIds){
			factName = getFactoryName(actId);
			userSimulationMap.put(actId, factName);
		}
		
		return userSimulationMap;
	}
	
	private String getFactoryName(int factoryId){
		return simulationMap.get(factoryId).getName();
	}
	
	public synchronized void addMemberToMemberAccess(int simulationId, String login) {
		memberMap.get(login).addFactoryAccess(simulationId);
	}
	
	private synchronized int getNextSimulationId() {
		if(!simulationMap.isEmpty()){
			return (simulationMap.firstKey()-1);
		}else{
			return -1;
		}
	}
}
