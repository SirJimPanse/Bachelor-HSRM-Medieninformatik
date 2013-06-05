package de.hsrm.mi.swtpro03.FactoryFactory.model.machine;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.ProductOverflowException;

public interface ITaker {
	
	void take() throws ProductOverflowException;
	int getClassId();
}
