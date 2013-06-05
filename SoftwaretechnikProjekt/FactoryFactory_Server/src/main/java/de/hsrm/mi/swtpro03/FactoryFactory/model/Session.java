package de.hsrm.mi.swtpro03.FactoryFactory.model;


public class Session {

	private String name;
	private long sessionId;
	private int factoryId;

	public Session(){
	}
	
	public Session(String user, long sessionId){
		this.name = user;
		this.sessionId = sessionId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSessionID() {
		return sessionId;
	}

	public void setSessionID(long sessionID) {
		this.sessionId = sessionID;
	}
	
	public int getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}

	@Override
	public boolean equals(Object other){
		Session otherSession = (Session) other;
		return (name.equals(otherSession.getName())) ? true : false;
	}

	public void unsetFactory() {
		this.factoryId = -1;
	}

}
