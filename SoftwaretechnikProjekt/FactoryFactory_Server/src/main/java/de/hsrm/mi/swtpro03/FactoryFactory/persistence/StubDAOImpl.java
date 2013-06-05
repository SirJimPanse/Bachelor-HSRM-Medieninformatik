package de.hsrm.mi.swtpro03.FactoryFactory.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.BadLoginException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Area;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Direction;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Factory;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Position;

public class StubDAOImpl implements DatabaseDAO {

	private List<Factory> factories;
	private static final String USER = "test";
	private static final String PASSWORD = "123";
	private int factoryCounter = 0;

	public StubDAOImpl(){
		factories = new ArrayList<Factory>();
		factories.add(new Factory(++factoryCounter, "Eastwoods_Seine", new Area(10,10)));
		factories.add(new Factory(++factoryCounter, "Eastwoods_Seine_Zweite", new Area(10,10)));
		factories.add(createFactoryWithMachines());
	}

	private Factory createFactoryWithMachines() {
		Factory schoeneFactory = new Factory(++factoryCounter, "EineSchoeneFactory", new Area(10,10));
		schoeneFactory.placeMachine(1, new Position(0,8, Direction.WEST));
		schoeneFactory.placeMachine(2, new Position(3,4, Direction.SOUTH));
		schoeneFactory.placeMachine(3, new Position(0,0, Direction.EAST));
		schoeneFactory.placeMachine(4, new Position(7,5, Direction.NORTH));
		schoeneFactory.placeMachine(5, new Position(5,4, Direction.NORTH));
		return schoeneFactory;
	}
	
	public boolean register(String login, String password){
		return true;
	}
	
	
	@Override
	public void login(String username, String password) throws BadLoginException{
		if(!(USER.equals(username) && PASSWORD.equals(password))) {
			throw new BadLoginException();
		}
	}
	
	@Override
	public Factory getFactory(int factoryID) {
		for(Factory fac : factories){
			if(fac.getFactoryID() == factoryID){
				return fac;				
			}
		}
		return null;
	}

	@Override
	public void insertFactory(Factory factory) {
		factories.add(factory);
	}

	@Override
	public boolean delete(int factoryID, String login) {
		for(Factory factory : factories){
			if(factory.getFactoryID() == factoryID){
				factories.remove(factory);
				return true;
			}
		}
		return false;
	}

	@Override
	public SortedMap<Integer, String> getFactories(String username) {
		SortedMap<Integer, String> map = new TreeMap<Integer, String>();
		for(Factory f : factories){
			map.put(f.getFactoryID(), f.getName());
		}
		return map;
	}

	@Override
	public boolean saveFactory(Factory factory) {
		return true;
	}

	@Override
	public boolean addMemberToMemberAccess(int factoryId, String login) {
		return true;
	}

	@Override
	public SortedMap<Integer, Factory> getAllFactories() {
		SortedMap<Integer, Factory> map = new TreeMap<Integer, Factory>();
		for(Factory factory : factories){
			map.put(factory.getFactoryID(), factory);
		}
		return map;
	}
	
	public void initDB(){};
}
