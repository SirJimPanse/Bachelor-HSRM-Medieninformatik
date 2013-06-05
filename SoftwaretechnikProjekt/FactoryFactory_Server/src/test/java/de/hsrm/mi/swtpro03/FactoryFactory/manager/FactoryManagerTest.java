package de.hsrm.mi.swtpro03.FactoryFactory.manager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.FactoryDoesNotExistException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Area;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Factory;
import de.hsrm.mi.swtpro03.FactoryFactory.persistence.DatabaseDAO;

public class FactoryManagerTest {
	
	FactoryManager manager;
	private DatabaseDAO dbDAO;

	@Before
	public void setUp() throws Exception {
		dbDAO = mockDbDAO();
	}

	@Test(expected=FactoryDoesNotExistException.class)
	public void testGetFactoryWithException() throws Exception {
		when(dbDAO.getAllFactories()).thenReturn(new TreeMap<Integer,Factory>());
		manager = new FactoryManager(dbDAO);
		manager.getFactory(0);
	}
	
	@Test
	public void testGetFactoryWithNoException() throws Exception {
		when(dbDAO.getAllFactories()).thenReturn(buildFactoryMapWithTwoFactories());
		manager = new FactoryManager(dbDAO);
		Factory fab17 = manager.getFactory(17);
		assertEquals("seventeen",fab17.getName());
	}

	
	private DatabaseDAO mockDbDAO(){
		DatabaseDAO mocked = mock(DatabaseDAO.class);
		return mocked;
	}
	
	private SortedMap<Integer,Factory> buildFactoryMapWithTwoFactories(){
		SortedMap<Integer,Factory> factoryMap = new TreeMap<Integer,Factory>();
		factoryMap.put(17, new Factory(17,"seventeen",new Area()));
		factoryMap.put(42, new Factory(42,"fortytwo",new Area()));
		return factoryMap;
	}
}
