package de.hsrm.mi.swtpro03.FactoryFactory.util;

public interface IObservable {
	void addObserver(IObserver ob);
	void removeObserver(IObserver ob);
	void notifyObservers();
	void notifyObservers(Object o);
	
}
