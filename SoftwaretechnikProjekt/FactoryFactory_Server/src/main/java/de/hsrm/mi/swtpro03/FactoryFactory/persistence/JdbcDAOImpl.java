package de.hsrm.mi.swtpro03.FactoryFactory.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.BadLoginException;
import de.hsrm.mi.swtpro03.FactoryFactory.exception.FactoryDoesNotExistException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Factory;
import de.hsrm.mi.swtpro03.FactoryFactory.util.SQLUtils;
import de.hsrm.mi.swtpro03.FactoryFactory.util.SetUp;

public class JdbcDAOImpl implements DatabaseDAO {
	private static Logger logger = Logger.getLogger("JdbcDAOImpl");
	private DataSource source;

	public JdbcDAOImpl(DataSource source) {
		this.source = source;
	}

	@Override
	public boolean register(String login, String password){
		Connection con = null;	//NOPMD
		PreparedStatement stmt = null;	//NOPMD
		try {
			con = source.getConnection();
			String qry = "INSERT INTO member VALUES (?, ?);";
			stmt = con.prepareStatement(qry);
			
			stmt.setString(1, login);
			stmt.setString(2, password);

			stmt.execute();
			
			return true;

		} catch (SQLException e) {
			logger.log(Level.INFO, "Register - SQLException " + e.getMessage());
			return false;
		} finally {
			SQLUtils.closeQuietly(con);
			SQLUtils.closeQuietly(stmt);
		}
			
	}
	
	@Override
	public void login(String login, String password) throws BadLoginException{
		Connection con = null;		//NOPMD
		PreparedStatement stmt = null;		//NOPMD
		ResultSet result = null;	//NOPMD
		try {
			con = source.getConnection();
			String qry = "SELECT * FROM member WHERE login=? AND password=?;";
			stmt = con.prepareStatement(qry);
			
			stmt.setString(1, login);
			stmt.setString(2, password);

			result = stmt.executeQuery();
			
			if(!result.next()){
				throw new BadLoginException();
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "Login - SQLException " + e.getMessage());
			throw new BadLoginException();
		} finally {
			SQLUtils.closeQuietly(con);
			SQLUtils.closeQuietly(stmt);
			SQLUtils.closeQuietly(result);
		}
	}

	@Override
	public Factory getFactory(int factoryID) throws FactoryDoesNotExistException{
		Factory factory = null;
		Connection con = null;		//NOPMD
		PreparedStatement stmt = null;		//NOPMD
		ResultSet result = null;	//NOPMD
		
		try {
			con = source.getConnection();
			String qry = "SELECT data FROM factory WHERE factory_id=?;";
			stmt = con.prepareStatement(qry);

			stmt.setInt(1, factoryID);
			result = stmt.executeQuery();
			
			int pos = 1;

			if (result.next()) {
				byte[] st = (byte[]) result.getObject(pos);
				ByteArrayInputStream baip = new ByteArrayInputStream(st);
				ObjectInputStream ois = new ObjectInputStream(baip);
				factory = (Factory) ois.readObject();
				return factory;
			}

		} catch (SQLException e) {
			logger.log(Level.INFO, "getFactory - SQLException " + e.getMessage());
			throw new FactoryDoesNotExistException();
		} catch (IOException e) {
			logger.log(Level.INFO, "getFactory - IOException " + e.getMessage());
			throw new FactoryDoesNotExistException();
		} catch (ClassNotFoundException e) {
			logger.log(Level.INFO, "getFactory - ClassNotFoundException " + e.getMessage());
			throw new FactoryDoesNotExistException();
		} finally {
			SQLUtils.closeQuietly(con);
			SQLUtils.closeQuietly(stmt);
			SQLUtils.closeQuietly(result);
		}
		return factory;
	}

	@Override
	public SortedMap<Integer, String> getFactories(String login) {
		SortedMap<Integer, String> returnMap = new TreeMap<Integer, String>();
		Connection con = null;		//NOPMD
		PreparedStatement stmt = null;		//NOPMD
		ResultSet result = null;	//NOPMD

		try {
			con = source.getConnection();
			String qry = "SELECT factory_id, name FROM factory NATURAL JOIN member_access WHERE login=?;";
			stmt = con.prepareStatement(qry);
			stmt.setString(1, login);
			
			result = stmt.executeQuery();

			int factoryIdPos = result.findColumn("factory_id");
			int namePos = result.findColumn("name");
			while (result.next()) {
				String name = result.getString(namePos);
				int factoryId = result.getInt(factoryIdPos);
				returnMap.put(factoryId, name);
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "getFactories - SQLException " + e.getMessage());
		} finally {
			SQLUtils.closeQuietly(con);
			SQLUtils.closeQuietly(stmt);
			SQLUtils.closeQuietly(result);
		}
		return returnMap;
	}

	@Override
	public void insertFactory(Factory factory){
		Connection con = null;			//NOPMD
		PreparedStatement stmt = null;	//NOPMD

		try {
			con = source.getConnection();

		String qry = "INSERT INTO factory VALUES (?, ?, ?);";
		
		stmt = con.prepareStatement(qry);
		stmt.setInt(1, factory.getFactoryID());
		stmt.setString(2, factory.getName());
		stmt.setBytes(3, createFactoryBlob(factory));

		stmt.execute();
		} catch (SQLException e) {
			logger.log(Level.INFO, "insertFactory - SQLException " + e.getMessage());
		}
		SQLUtils.closeQuietly(con);
		SQLUtils.closeQuietly(stmt);
		
	}

	@Override
	public boolean addMemberToMemberAccess(int factoryId, String login) {
		Connection con = null;	//NOPMD
		PreparedStatement stmt = null;	//NOPMD
		String qry;

		try {
			con = source.getConnection();
			qry = "INSERT INTO member_access VALUES (?,?);";
			stmt = con.prepareStatement(qry);
			stmt.setString(1, login);
			stmt.setInt(2, factoryId);
			
			stmt.execute();
			return true;
		} catch (SQLException e) {
			logger.log(Level.INFO, "addMemberToMemberAccess - SQLException " + e.getMessage());
		} finally {
			SQLUtils.closeQuietly(con);
			SQLUtils.closeQuietly(stmt);
		}
		return false;
	}

	private byte[] createFactoryBlob(Factory dbFactory) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(dbFactory);

			oos.flush();
			oos.close();
		} catch (IOException e1) {
			logger.log(Level.INFO, "createFactoryBlob - IOException " + e1.getMessage());
		}
		
		return baos.toByteArray();
	}

	@Override
	public boolean saveFactory(Factory factory) {
		Connection con = null;			//NOPMD
		PreparedStatement stmt = null;	//NOPMD

		String qry = "UPDATE factory SET data=? WHERE factory_id=?;";

		byte[] blob = createFactoryBlob(factory);
		try {
			con = source.getConnection();
			stmt = con.prepareStatement(qry);
			stmt.setBytes(1, blob);
			stmt.setInt(2, factory.getFactoryID());

			stmt.execute();
			return true;
		} catch (SQLException e) {
			logger.log(Level.INFO, "saveFactory - SQLException " + e.getMessage());
		} finally {
			SQLUtils.closeQuietly(con);
			SQLUtils.closeQuietly(stmt);
		}
		return false;
	}

	@Override
	public boolean delete(int factoryID, String login) {
		Connection con = null; //NOPMD
		PreparedStatement stmt = null; //NOPMD
		String qry;

		try {
			con = source.getConnection();
			qry = "DELETE FROM member_access WHERE login=? AND factory_id=?;";
			stmt = con.prepareStatement(qry);
			stmt.setString(1,login);
			stmt.setInt(2,factoryID);

			stmt.execute();
			SQLUtils.closeQuietly(stmt);
			logger.log(Level.INFO, "delete - member_access - Benutzer: " + login + " wurden die Rechte für Factory " + factoryID + " entzogen");
			
			qry = "DELETE FROM factory WHERE factory_id=?;";
			stmt = con.prepareStatement(qry);
			stmt.setInt(1, factoryID);
			stmt.execute();
			logger.log(Level.INFO, "delete - factory - Factory " + factoryID + " wurde aus Datenbank gelöscht");

			return true;
		} catch (SQLException e) {
			logger.log(Level.INFO, "delete - factory - Factory " + factoryID + " wird nicht gelöscht, da weitere Benutzer Rechte dafür besitzen");
			return false;
		} finally {
			SQLUtils.closeQuietly(con);
			SQLUtils.closeQuietly(stmt);
		}
	}

	@Override
	public SortedMap<Integer, Factory> getAllFactories() {
			Factory factory = null;
			Connection con = null;		//NOPMD
			Statement stmt = null;		//NOPMD
			ResultSet result = null;	//NOPMD
			SortedMap<Integer, Factory> map = new TreeMap<Integer, Factory>();
			try {
				con = source.getConnection();
				stmt = con.createStatement();

				String qry = "SELECT factory_id, data FROM factory;";
				result = stmt.executeQuery(qry);
				
				int idPos = 1;
				int dataPos = 2;

				while (result.next()) {
					int factoryId = result.getInt(idPos);
					byte[] st = (byte[]) result.getObject(dataPos);
					ByteArrayInputStream baip = new ByteArrayInputStream(st);
					ObjectInputStream ois = new ObjectInputStream(baip);
					factory = (Factory) ois.readObject();
					map.put(factoryId, factory);
				}
				return map;

			} catch (SQLException e) {
				logger.log(Level.INFO, "getFactory - SQLException " + e.getMessage());
			} catch (IOException e) {
				logger.log(Level.INFO, "getFactory - IOException " + e.getMessage());
			} catch (ClassNotFoundException e) {
				logger.log(Level.INFO, "getFactory - ClassNotFoundException " + e.getMessage());
			} finally {
				SQLUtils.closeQuietly(con);
				SQLUtils.closeQuietly(stmt);
				SQLUtils.closeQuietly(result);
			}

		return map;
	}
	
	public void initDB(){
		SetUp.checkDatabase(source);
	}

}
