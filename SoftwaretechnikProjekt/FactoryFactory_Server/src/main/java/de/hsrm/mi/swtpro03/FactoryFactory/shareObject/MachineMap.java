package de.hsrm.mi.swtpro03.FactoryFactory.shareObject;

import java.util.HashMap;

import de.hsrm.mi.swtpro03.FactoryFactory.model.Position;

public class MachineMap {
	public HashMap<String, Position> map;
	
	public MachineMap(){
		map = new HashMap<String, Position>();
	}
	
	public void addMachineToMap(String machineID, Position position){
		map.put(machineID, position);
	}
	
	public void removeMachineFromMap(String machineID){
		map.remove(machineID);
	}
	
	public int numberOfMachines(){
		return map.size();
	}

	public HashMap<String, Position> getMap() {
		return map;
	}
}
