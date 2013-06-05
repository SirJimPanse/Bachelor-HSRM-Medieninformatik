package de.hsrm.mi.swtpro03.FactoryFactory.exception;

public class SessionDoesNotExistException extends Exception {
	private static final long serialVersionUID = 4177651039369035299L;

	public SessionDoesNotExistException(){
		super("Factory doesn't exist");
	}
	
}
