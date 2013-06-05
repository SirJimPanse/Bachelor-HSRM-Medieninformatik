package de.hsrm.mi.swtpro03.FactoryFactory.persistence;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.SortedMap;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.BadLoginException;
import de.hsrm.mi.swtpro03.FactoryFactory.exception.FactoryDoesNotExistException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Area;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Factory;

public class JdbcDAOTest {

	private static DatabaseDAO dbDAO;
	private static DataSource datasource; 
	private String login = "test";
	private String password = "123";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String config = "testconfig.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(config);
		JdbcDAOTest.datasource = (DataSource) context.getBean("dataSource");
		JdbcDAOTest.dbDAO = (DatabaseDAO) context.getBean("databaseDAO");
	}

	@Before
	public void setUp() throws Exception {
		Connection con = JdbcDAOTest.datasource.getConnection();
		Statement stmt = con.createStatement();
		URL scriptFilePath;
		
		scriptFilePath =getClass().getResource("/database/factory_factory_drop.sql");
		executeScript(stmt, scriptFilePath);
	
		scriptFilePath =getClass().getResource("/database/factory_factory_create.sql");
		executeScript(stmt, scriptFilePath);
		
	}

	private void executeScript(Statement stmt, URL scriptFilePath)
			throws FileNotFoundException, IOException, SQLException {
		BufferedReader in  = new BufferedReader(new InputStreamReader(scriptFilePath.openStream()));
		String str;
		StringBuffer sb = new StringBuffer();
		while((str = in.readLine()) != null){
			sb.append(str + "\n");
		}
		
		in.close();
		stmt.executeUpdate(sb.toString());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void registerTest() throws SQLException {
		assertTrue(JdbcDAOTest.dbDAO.register(login, password));
		assertFalse(JdbcDAOTest.dbDAO.register(login, password));
	}
	
	@Test
	public void loginTest() throws SQLException, BadLoginException{
		assertTrue(JdbcDAOTest.dbDAO.register(login, password));
		JdbcDAOTest.dbDAO.login(login, password);
	}
	
	@Test(expected = BadLoginException.class)
	public void loginShouldFailTest() throws BadLoginException{
		JdbcDAOTest.dbDAO.login(login, password);
	}
	
	@Test
	public void createFactoryTest() throws BadLoginException, SQLException{
		assertTrue(JdbcDAOTest.dbDAO.register(login, password));
		JdbcDAOTest.dbDAO.login(login, password);
		
		String factoryName = "TestFactory";
		Area area = new Area(10,10);
		Factory testFactory = new Factory(1, factoryName, area);
		JdbcDAOTest.dbDAO.insertFactory(testFactory);
		
		assertNotNull(testFactory);
		assertEquals(factoryName, testFactory.getName());
	}
	
	@Test
	public void getFactoryTest() throws BadLoginException, FactoryDoesNotExistException, SQLException{
		assertTrue(JdbcDAOTest.dbDAO.register(login, password));
		JdbcDAOTest.dbDAO.login(login, password);
		
		String factoryName = "TestFactory";
		Area area = new Area(10,10);
		Factory testFactory = new Factory(1, factoryName, area);
		JdbcDAOTest.dbDAO.insertFactory(testFactory);
		
		assertNotNull(testFactory);
		
		Factory resultFactory = JdbcDAOTest.dbDAO.getFactory(testFactory.getFactoryID());
		
		assertEquals(testFactory, resultFactory);
	}
	
	@Test
	public void getFactoriesTest() throws BadLoginException, SQLException{
		int numberOfFactories = 5;
		assertTrue(JdbcDAOTest.dbDAO.register(login, password));
		JdbcDAOTest.dbDAO.login(login, password);
		
		String factoryName = "TestFactory";
		Area area = new Area(10,10);
		
		for(int i=0 ; i < numberOfFactories ; i++){
			Factory testFactory = new Factory(i, factoryName, area);
			JdbcDAOTest.dbDAO.insertFactory(testFactory);
			JdbcDAOTest.dbDAO.addMemberToMemberAccess(i, login);
		}
		 
		SortedMap<Integer, String> map = JdbcDAOTest.dbDAO.getFactories(login);
		
		assertEquals(numberOfFactories, map.size());
	}
	
}
