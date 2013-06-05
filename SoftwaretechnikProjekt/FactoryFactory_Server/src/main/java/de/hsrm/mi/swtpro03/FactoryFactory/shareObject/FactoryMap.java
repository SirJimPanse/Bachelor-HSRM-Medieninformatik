package de.hsrm.mi.swtpro03.FactoryFactory.shareObject;

import java.util.SortedMap;
import java.util.TreeMap;

public class FactoryMap {
	public SortedMap<Integer, String> map;
	
	public FactoryMap(){
		map = new TreeMap<Integer, String>();
	}

	public FactoryMap(SortedMap<Integer, String> map) {
		this.map = map;
	}
	
	public SortedMap<Integer, String> getFactories() {
		return map;
	}

	public void setFactories(SortedMap<Integer, String> map) {
		this.map = map;
	}
	
	public int numberOfUserFactories(){
		return map.size();
	}
}
