package de.hsrm.mi.swtpro03.FactoryFactory.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.hsrm.mi.swtpro03.FactoryFactory.model.Area;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Factory;

public class SimulationTest {

	Area testArea;
	Factory testFactory; 
	Factory testSimulation;
	
	@Before
	public void setup(){
		testArea = new Area(20, 20);
		testFactory = new Factory(412, "testFactory", testArea);
		testSimulation = testFactory.clone();
	}
	
	@Test
	public void TestFactoryCloning(){
		assertNotNull(testSimulation);
		assertTrue(testSimulation.equals(testFactory));
		assertFalse(testSimulation == testFactory);
		assertTrue(testSimulation.isSimulation());
		assertFalse(testFactory.isSimulation());
	}
}
