package de.hsrm.mi.swtpro03.FactoryFactory.exception;

public class AccessDeniedException extends Error {
	
	private static final long serialVersionUID = 3879208371315778214L;

	public AccessDeniedException(){
		super("Access denied for user");
	}
	
}
