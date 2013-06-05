package de.hsrm.mi.swtpro03.FactoryFactory.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.hsrm.mi.swtpro03.FactoryFactory.controller.FactoryController;
import de.hsrm.mi.swtpro03.FactoryFactory.controller.LoginController;
import de.hsrm.mi.swtpro03.FactoryFactory.manager.FactoryManager;
import de.hsrm.mi.swtpro03.FactoryFactory.manager.MemberManager;
import de.hsrm.mi.swtpro03.FactoryFactory.manager.SessionManager;
import de.hsrm.mi.swtpro03.FactoryFactory.manager.SimulationManager;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Direction;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Position;
import de.hsrm.mi.swtpro03.FactoryFactory.persistence.DatabaseDAO;
import de.hsrm.mi.swtpro03.FactoryFactory.persistence.StubDAOImpl;
import de.hsrm.mi.swtpro03.FactoryFactory.service.FactoryService;
import de.hsrm.mi.swtpro03.FactoryFactory.service.LoginService;
import de.hsrm.mi.swtpro03.FactoryFactory.service.NOPMessagePublisher;
import de.hsrm.mi.swtpro03.FactoryFactory.util.IMessagePublisher;

public class FactoryServiceTest {

	private FactoryService factoryService;
	private FactoryController factoryController;
	private LoginController loginController;
	private LoginService loginService;
	private DatabaseDAO dbDAO;
	private long sessionID;
	private int factoryID;
	private FactoryManager factoryManager;
	private MemberManager memberManager;
	private SessionManager sessionManager;
	private SimulationManager simulationManager;
	private IMessagePublisher publisher;
	@Before
	public void setUp() throws Exception {
		this.publisher = new NOPMessagePublisher();
		this.dbDAO = new StubDAOImpl();
		this.sessionManager = new SessionManager(publisher);
		this.factoryManager = new FactoryManager(dbDAO);
		this.memberManager = new MemberManager(dbDAO);
		this.simulationManager = new SimulationManager();
		
		this.factoryController = new FactoryController(sessionManager, factoryManager,publisher);
		this.factoryService = new FactoryService(this.factoryController);
		
		this.loginController = new LoginController(sessionManager, factoryManager, memberManager, simulationManager);
		this.loginService = new LoginService(loginController);

		this.sessionID = loginService.connect("test", "123");
		
		Object []factoryIDs = loginService.getFactories(sessionID).getFactories().keySet().toArray();
		factoryID = (Integer) factoryIDs[0];
		loginService.connectToFactory(sessionID, factoryID);
	}

	@After
	public void tearDown(){
		loginService.disconnect(sessionID);
	}
	
	@Test
	public void machineShouldBeCreated() {
		Position pos = new Position(0, 0, Direction.WEST);

		int oldNoM = factoryService.getFactory(sessionID).numberOfMachines();
		String machineID = factoryService.createMachine(sessionID, 1, pos.getX(), pos.getY(), pos.getDirection());
		int newNoM = factoryService.getFactory(sessionID).numberOfMachines();
		
		assertEquals(oldNoM+1, newNoM);
		
		Position pos2 = factoryService.getFactory(sessionID).getMap().get(machineID);
		
		assertEquals(pos, pos2);
	}
	
	@Test
	public void machineShouldBeMoved() {
		Position oldPos = new Position(0, 0, Direction.WEST);
		int oldX = oldPos.getX();
		int oldY = oldPos.getY();
		Position newPos = new Position(5, 5, Direction.WEST);
		
		String machineID = factoryService.createMachine(sessionID, 1, oldPos.getX(), oldPos.getY(), oldPos.getDirection());
				
		assertTrue(factoryService.moveMachine(sessionID, machineID, newPos.getX(), newPos.getY()));
		
		assertTrue(factoryController.getFactoryBySessionID(sessionID).getArea().fieldIsEmpty(new Position(oldX, oldY, null)));
		assertFalse(factoryController.getFactoryBySessionID(sessionID).getArea().fieldIsEmpty(newPos));
		
		Position newPos2 = factoryService.getFactory(sessionID).getMap().get(machineID);
		
		assertEquals(newPos, newPos2);
	}
	
	@Test
	public void machineShouldBeDeleted() {
		Position pos = new Position(0, 0, Direction.WEST);

		int oldNoM = factoryService.getFactory(sessionID).numberOfMachines();
		
		String[] machineIDs = new String[]{factoryService.createMachine(sessionID, 1, pos.getX(), pos.getY(), pos.getDirection())};
		
		factoryService.deleteMachines(sessionID, machineIDs);
		
		int newNoM = factoryService.getFactory(sessionID).numberOfMachines();
		
		assertEquals(oldNoM, newNoM);
		
		assertTrue(factoryController.getFactoryBySessionID(sessionID).getArea().fieldIsEmpty(pos));
		
	}
}
