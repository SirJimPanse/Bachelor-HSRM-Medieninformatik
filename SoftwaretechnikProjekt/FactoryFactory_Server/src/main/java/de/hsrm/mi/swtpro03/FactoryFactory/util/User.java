package de.hsrm.mi.swtpro03.FactoryFactory.util;

import java.util.ArrayList;
import java.util.List;

public class User {
	
	private String login;
	
	private List<Integer> factoryAccessList;
	
	public User(final String login){
		this.login = login;
		this.factoryAccessList = new ArrayList<Integer>();
	}

	public String getLogin() {
		return login;
	}

	public List<Integer> getSimulationList() {
		return factoryAccessList;
	}
	
	public void addFactoryAccess(int factoryId){
		factoryAccessList.add(factoryId);
	}
	
	public void deleteSimulationFromAccess(int simulationId) {
		Integer actId;
		for (int i = 0; i < factoryAccessList.size(); i++) {
			actId = factoryAccessList.get(i);
			if ((int)actId == simulationId) {
				factoryAccessList.remove(i);
				return;
			}
		}
	}
}
