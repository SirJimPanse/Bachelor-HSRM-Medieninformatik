package de.hsrm.mi.swtpro03.FactoryFactory.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.SessionDoesNotExistException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Session;
import de.hsrm.mi.swtpro03.FactoryFactory.util.IMessagePublisher;

public class SessionManager {

	private Map<Long, Session> sessions;
	private IMessagePublisher mqPublisher;
	private static final Logger LOGGER = Logger.getLogger("SessionManager");


	public SessionManager(IMessagePublisher mqPublisher) {
		sessions = new HashMap<Long, Session>();
		this.mqPublisher = mqPublisher;
	}

	public int countSessions() {
		return sessions.size();
	}

	public String getSessionsAsString() {
		StringBuilder b = new StringBuilder();
		for (Session s : sessions.values()) {
			b.append(s.getSessionID() + " " + s.getName() + "\n");
		}
		return null;
	}

	public boolean contains(long sessionId) {
		return sessions.containsKey(sessionId);
	}

	public Session get(long sessionId) throws SessionDoesNotExistException {
		if (contains(sessionId)) {
			return sessions.get(sessionId);
		}
		throw new SessionDoesNotExistException();
	}

	public boolean remove(long sessionId) {
		if (sessions.containsKey(sessionId)) {
			sessions.remove(sessionId);
			return true;
		}
		return false;
	}

	public synchronized Session createSession(String username, String password) {
		long sessionId;

		do {
			sessionId = (long) ((Math.random()) * Long.MAX_VALUE);
		} while (sessions.containsKey(sessionId));

		Session session = new Session(username, sessionId);
		sessions.put(sessionId, session);
		return session;
	}

	public void tellAllMembers(String publisher, String text, String action) {
		for (long session : sessions.keySet()) {
			mqPublisher.sendMessage(publisher, String.valueOf(session), text,
					action);
		}

	}

	public void close() {
		tellAllMembers("-17", "Server wurde beendet", "shutdown");
		LOGGER.log(Level.INFO, "########## sessionManager shutdown-method ##########");
	}

}
