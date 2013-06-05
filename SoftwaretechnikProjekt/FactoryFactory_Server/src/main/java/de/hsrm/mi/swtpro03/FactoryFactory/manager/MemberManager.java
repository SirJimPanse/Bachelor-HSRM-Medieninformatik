package de.hsrm.mi.swtpro03.FactoryFactory.manager;

import java.util.SortedMap;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.BadLoginException;
import de.hsrm.mi.swtpro03.FactoryFactory.persistence.DatabaseDAO;

public class MemberManager {
	private DatabaseDAO dbDAO;
	
	public MemberManager(DatabaseDAO dbDAO){
		this.dbDAO = dbDAO;
	}

	public SortedMap<Integer, String> getFactories(String login){
		return dbDAO.getFactories(login);
	}

	public synchronized void login(String username, String password) throws BadLoginException {
		dbDAO.login(username, password);		
	}

	public synchronized boolean register(String login, String password) {
		return dbDAO.register(login, password);
	}

}
