package de.hsrm.mi.swtpro03.FactoryFactory.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

public final class SetUp {
	private static Logger logger = Logger.getLogger("SetUp");
	
	private SetUp(){}

	private static void executeScript(Statement stmt, URL scriptFilePath) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(scriptFilePath.openStream()));
			String str;
			
			StringBuffer sb = new StringBuffer();
			
			while((str = in.readLine()) != null){
				if(str.contains(";")){
					sb.append(str + "\n");
					try {
						stmt.executeUpdate(sb.toString());
					} catch (SQLException e) {
						logger.log(Level.INFO, "executeScript - " + e.getMessage());
					}
					sb = new StringBuffer();
				}
				else {
					sb.append(str + "\n");
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			logger.log(Level.INFO, "executeScript - " + e.getMessage());
		} catch (IOException e) {
			logger.log(Level.INFO, "executeScript - " + e.getMessage());
		}
	}

	public static void checkDatabase(DataSource dataSource) {
		Connection con = null;

		try {
			con = dataSource.getConnection();
			Statement stmt = con.createStatement();
			String qry = "SELECT * FROM member; " +
					"SELECT * FROM member_access; " +
					"SELECT * FROM factory;";
			stmt.execute(qry);
		} catch (SQLException e) {
			logger.log(Level.INFO, "checkDatabase - Datenbank nicht aufgesetzt");
			setUpDatabase(dataSource);
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				logger.log(Level.INFO, "checkDatabase - " + e.getMessage());
			}
		}
	}
	
	private static void setUpDatabase(DataSource dataSource) {
		logger.log(Level.INFO, "setUpDatabase - Datenbank wird neu aufgesetzt");
		try {
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
			URL scriptFilePath;
			
			scriptFilePath = SetUp.class.getResource("/database/factory_factory_drop.sql");
			SetUp.executeScript(stmt, scriptFilePath);
			
			scriptFilePath =SetUp.class.getResource("/database/factory_factory_create.sql");
			SetUp.executeScript(stmt, scriptFilePath);
			con.close();
		} catch (SQLException e) {
			logger.log(Level.INFO, "setUpDatabase - " + e.getMessage());
		} 
	}	
}
