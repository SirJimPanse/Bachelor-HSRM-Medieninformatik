package de.hsrm.mi.swtpro03.FactoryFactory.exception;

public class NotLoggedInException extends Error {

	private static final long serialVersionUID = -2378238752549637810L;

	public NotLoggedInException(){
		super("User is not logged in");
	}
}
