package de.hsrm.mi.swtpro03.FactoryFactory.model.machines;

import org.junit.Before;
import org.junit.Test;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.AreaPositioningException;
import de.hsrm.mi.swtpro03.FactoryFactory.exception.ProductOverflowException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Area;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Direction;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Position;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Product;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Changer;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Consumer;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Conveyor;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Machine;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Producer;

public class TakerTest {
	public Area testArea;

	public Producer producer;
	public Machine band1;
	public Changer changer;
	public Machine band2;
	public Machine band3;
	public Consumer consumer;
	
	@Before
	public void setUp() throws AreaPositioningException {
		testArea = new Area(10, 10);

		producer = new Producer(1, "Bananaaaas", new Position(1, 1, Direction.EAST), 1);
		producer.setMaxCapacityInNumberOfProducts(5);

		band1 = new Conveyor(2, "band1", new Position(2, 1, Direction.SOUTH), 1);
		band1.setMaxCapacityInNumberOfProducts(5);

		changer = new Changer(3, "OtherBananaaas", new Position(2, 2, Direction.SOUTH), 1);
		changer.setMaxCapacityInNumberOfProducts(5);

		band2 = new Conveyor(4, "band2", new Position(2, 3, Direction.SOUTH), 1);
		band2.setMaxCapacityInNumberOfProducts(5);

		band3 = new Conveyor(5, "band3", new Position(2, 4, Direction.EAST), 1);
		band3.setMaxCapacityInNumberOfProducts(5);

		consumer = new Consumer(6, "NoooBananaaaas", new Position(3, 4,Direction.EAST), 1);
		consumer.setMaxCapacityInNumberOfProducts(5);

		testArea.setMachine(band1,band1.getPosition());
		testArea.setMachine(band2,band2.getPosition());
		testArea.setMachine(band3,band3.getPosition());
		
		testArea.setMachine(producer,producer.getPosition());
		testArea.setMachine(changer,changer.getPosition());
		testArea.setMachine(consumer,consumer.getPosition());

		testArea.globalConnections();
	}

	@Test(expected = ProductOverflowException.class)
	public void testWorkflow() throws ProductOverflowException {
		System.out.println("Producer: " + producer.getFollowers());
		producer.setNoProductsPerProduction(3);
		producer.setProduct(new Product("Apfel", 0.3, 1.0, "Obst"));

		testArea.show();
		while(true) {
			producer.produce();
		}
	}
}