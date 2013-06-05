package de.hsrm.mi.swtpro03.FactoryFactory.manager;

import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.FactoryDoesNotExistException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Area;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Factory;
import de.hsrm.mi.swtpro03.FactoryFactory.persistence.DatabaseDAO;

public class FactoryManager{

	private SortedMap<Integer, Factory> factoryMap;
	private DatabaseDAO dbDAO;
	private static final Logger LOGGER = Logger.getLogger("FactoryManager");
	
	/** 
	 * Die Variable mqPublisher war dazu gedacht, das Herunterfahren des Servers an den Client zu melden (per Shutdown-Hooks).
	 * Leider funktioniert das mit activemq nicht so ganz zuverlässig, so dass sie nun ohne Funktion ist. 
	 **/
	public FactoryManager(DatabaseDAO dbDAO) {
		this.dbDAO = dbDAO;
		factoryMap = dbDAO.getAllFactories();
	}
		
	public void start() {
		factoryMap = dbDAO.getAllFactories();
	}
	
	public Factory getFactory(int factoryId) throws FactoryDoesNotExistException {
		Factory factory = factoryMap.get(factoryId);
		if(factory != null){
			return factory;
		}
		throw new FactoryDoesNotExistException();
	}

	public void putFactory(Factory factory) {
		factoryMap.put(factory.getFactoryID(), factory);
	}

	public void deleteFactory(int factoryId) {
		factoryMap.remove(factoryId);
	}
	
	private synchronized int getNextFactoryID() {
		if(!factoryMap.isEmpty()){
			return (factoryMap.lastKey()+1);
		}else{
			return 0;
		}
	}

	public synchronized int createFactory(String login, String name, int x, int y) {
		Area area = new Area(x, y);
		int factoryID = getNextFactoryID();
		Factory factory = new Factory(factoryID, name, area);
		
		dbDAO.insertFactory(factory);
		putFactory(factory);
		addMemberToMemberAccess(factoryID, login);
		return factoryID;
	}
	
	public synchronized int createFactory(String login, Factory factory) {
		factory.setFactoryID(getNextFactoryID());
		putFactory(factory);

		dbDAO.insertFactory(factory);
		
		addMemberToMemberAccess(factory.getFactoryID(), login);
		return factory.getFactoryID();
	}
	
	public synchronized void saveFactory(int factoryId) {
		Factory factory = factoryMap.get(factoryId);
		dbDAO.saveFactory(factory);
	}

	public synchronized void deleteFactory(int factoryId, String login) {
		dbDAO.delete(factoryId, login);		
	}
	
	public synchronized boolean addMemberToMemberAccess(int factoryID, String login) {
		return dbDAO.addMemberToMemberAccess(factoryID, login);
	}



	public void saveAlleFactories() {
		for(Factory factory : factoryMap.values()){	
			dbDAO.saveFactory(factory);
		}
	}
	
	public void close(){
		saveAlleFactories();
		LOGGER.log(Level.INFO, "########## factoryManager shutdown-method ##########");
	}
}