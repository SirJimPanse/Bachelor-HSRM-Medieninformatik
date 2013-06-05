package de.hsrm.mi.swtpro03.FactoryFactory.exception;

public class FactoryDoesNotExistException extends Exception {

	private static final long serialVersionUID = 2403585406131943889L;
	
	public FactoryDoesNotExistException(){
		super("Factory doesn't exist");
	}
}
