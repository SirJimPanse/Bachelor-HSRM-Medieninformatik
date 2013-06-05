package de.hsrm.mi.swtpro03.FactoryFactory.persistence;

import java.util.SortedMap;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.BadLoginException;
import de.hsrm.mi.swtpro03.FactoryFactory.exception.FactoryDoesNotExistException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Factory;

public interface DatabaseDAO {
	boolean register(String login, String password);
	void login(String username, String password) throws BadLoginException;
	Factory getFactory(int factoryID) throws FactoryDoesNotExistException;
	SortedMap<Integer, String> getFactories(String username);
	void insertFactory(Factory factory);
	boolean saveFactory(Factory factory);
	boolean delete(int factoryID, String login);
	boolean addMemberToMemberAccess(int factoryId, String login);
	SortedMap<Integer, Factory> getAllFactories();
	void initDB();
}
