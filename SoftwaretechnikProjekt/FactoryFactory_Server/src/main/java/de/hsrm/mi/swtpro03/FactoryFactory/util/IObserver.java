package de.hsrm.mi.swtpro03.FactoryFactory.util;

public interface IObserver {
	void update(IObservable ob);
	void update(IObservable ob, Object o);
}
