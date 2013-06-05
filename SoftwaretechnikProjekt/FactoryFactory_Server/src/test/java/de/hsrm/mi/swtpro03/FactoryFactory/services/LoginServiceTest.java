package de.hsrm.mi.swtpro03.FactoryFactory.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.SortedMap;

import org.junit.Before;
import org.junit.Test;

import de.hsrm.mi.swtpro03.FactoryFactory.controller.LoginController;
import de.hsrm.mi.swtpro03.FactoryFactory.exception.SessionDoesNotExistException;
import de.hsrm.mi.swtpro03.FactoryFactory.manager.FactoryManager;
import de.hsrm.mi.swtpro03.FactoryFactory.manager.MemberManager;
import de.hsrm.mi.swtpro03.FactoryFactory.manager.SessionManager;
import de.hsrm.mi.swtpro03.FactoryFactory.manager.SimulationManager;
import de.hsrm.mi.swtpro03.FactoryFactory.persistence.DatabaseDAO;
import de.hsrm.mi.swtpro03.FactoryFactory.persistence.StubDAOImpl;
import de.hsrm.mi.swtpro03.FactoryFactory.service.LoginService;
import de.hsrm.mi.swtpro03.FactoryFactory.service.NOPMessagePublisher;
import de.hsrm.mi.swtpro03.FactoryFactory.shareObject.FactoryMap;
import de.hsrm.mi.swtpro03.FactoryFactory.util.IMessagePublisher;

public class LoginServiceTest {

	private LoginService loginService;
	private LoginController loginController;
	private DatabaseDAO dbDAO;
	private SessionManager sessionManager;
	private FactoryManager factoryManager;
	private MemberManager memberManager;
	private SimulationManager simulationManager;
	private IMessagePublisher publisher;
	
	@Before
	public void setUp() throws Exception {
		this.publisher = new NOPMessagePublisher();
		this.sessionManager = new SessionManager(publisher);
		this.dbDAO = new StubDAOImpl();
		this.factoryManager = new FactoryManager(dbDAO);
		this.memberManager = new MemberManager(dbDAO);
		this.simulationManager = new SimulationManager();
		
		this.loginController = new LoginController(sessionManager, factoryManager, memberManager, simulationManager);
		this.loginService = new LoginService(loginController);
	}

	@Test
	public void shouldBeConnected(){
		long sessionID = loginService.connect("test", "123");
		System.out.println("KRASS " + sessionID);
		assertTrue(sessionID != -1);
	}
	
	@Test
	public void shouldNotBeConnected(){
		long sessionID = loginService.connect("falscher", "benutzer");
		assertTrue(sessionID == -1);
	}
	
	@Test
	public void getFactoriesTest(){
		long sessionID = loginService.connect("test", "123");
		SortedMap<Integer, String> map = dbDAO.getFactories("test");
		FactoryMap testMap = new FactoryMap(map);
		FactoryMap factoryMap = loginService.getFactories(sessionID);
		
		for (Integer id : testMap.getFactories().keySet()){
			System.out.println(id);
			assertTrue(factoryMap.getFactories().containsKey(id));
			String name1 = testMap.getFactories().get(id);
			String name2 = factoryMap.getFactories().get(id);
			assertEquals(name1, name2);
		}
	}
	
	@Test
	public void shouldBeConnectedToFactory(){
		long sessionID = loginService.connect("test", "123");
		FactoryMap factoryMap = loginService.getFactories(sessionID);
		for (Integer id : factoryMap.getFactories().keySet()){
			System.out.println("ID " + id + " Name " + factoryMap.getFactories().get(id)); 
			assertTrue(loginService.connectToFactory(sessionID, id));
			loginService.disconnectFromFactory(sessionID);
		}		
	}
	
	@Test
	public void factoryShouldBeCreated() throws SessionDoesNotExistException{
		long sessionID = loginService.connect("test", "123");
		int oldSize = loginController.getFactories(sessionID).getFactories().size();
		int factoryID = loginService.createFactory(sessionID, "testFactory", 17, 17);
		int newSize = loginController.getFactories(sessionID).getFactories().size();
		assertEquals(oldSize+1, newSize);
		int factoryID2 = sessionManager.get(sessionID).getFactoryId();
		assertEquals(factoryID, factoryID2);
	}
}
