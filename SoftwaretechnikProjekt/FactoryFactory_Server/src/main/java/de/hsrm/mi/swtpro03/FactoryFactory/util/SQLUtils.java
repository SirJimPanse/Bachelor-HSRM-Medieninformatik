package de.hsrm.mi.swtpro03.FactoryFactory.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SQLUtils {
	
	// Nicht instanziierbar
	private SQLUtils(){	}
	
	private static Logger logger = Logger.getLogger("SQLUtils");

	public static void closeQuietly(Connection connection){
		try {
			if(connection != null){
				connection.close();
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "Fehler beim Schließen der Verbindung " + e.getMessage());		
		}
	}
	
	public static void closeQuietly(ResultSet resultSet){
		try {
			if(resultSet != null){
				resultSet.close();
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "Fehler beim Schließen der Verbindung " + e.getMessage());
		}
	}
	
	public static void closeQuietly(Statement statement){
		try {
			if(statement != null){
				statement.close();
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "Fehler beim Schließen der Verbindung " + e.getMessage());
		}
	}
}
