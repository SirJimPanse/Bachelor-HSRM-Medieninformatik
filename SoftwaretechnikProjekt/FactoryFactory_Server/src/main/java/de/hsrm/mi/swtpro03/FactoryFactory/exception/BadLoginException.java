package de.hsrm.mi.swtpro03.FactoryFactory.exception;

public class BadLoginException extends Exception {

	private static final long serialVersionUID = 870174936326003785L;

	public BadLoginException(){
		super("Login fehlgeschlagen");
	}
	
	public BadLoginException(String s){
		super(s);
	}
}
