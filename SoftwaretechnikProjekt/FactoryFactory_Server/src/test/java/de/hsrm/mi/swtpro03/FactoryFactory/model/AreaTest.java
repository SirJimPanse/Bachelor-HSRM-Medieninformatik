package de.hsrm.mi.swtpro03.FactoryFactory.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.AreaPositioningException;
import de.hsrm.mi.swtpro03.FactoryFactory.exception.ResizeMustNotDeleteMachinesException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Changer;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Consumer;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Conveyor;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Machine;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Positioner;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Producer;

public class AreaTest {

	public Area testArea;

	public Producer producer;
	public Machine band1;
	public Changer changer;
	public Machine band2;
	public Machine band3;
	public Consumer consumer;
	public Positioner positi;

	public List<Position> testOutputProducer = new ArrayList<Position>();
	public List<Position> testInputBand1 = new ArrayList<Position>();
	public List<Position> testOutputBand1 = new ArrayList<Position>();
	public List<Position> testInputChanger = new ArrayList<Position>();
	public List<Position> testOutputChanger = new ArrayList<Position>();
	public List<Position> testInputBand2 = new ArrayList<Position>();
	public List<Position> testOutputBand2 = new ArrayList<Position>();
	public List<Position> testInputBand3 = new ArrayList<Position>();
	public List<Position> testOutputBand3 = new ArrayList<Position>();
	public List<Position> testInputConsumer = new ArrayList<Position>();
	
	public int height;
	public int width;
	public int machineCount;

	private void add(List<Position> band, int x, int y, Direction dir) {
		band.add(new Position(x, y, dir));
	}

	@Before
	public void setUp() throws AreaPositioningException {
		height = 19;
		width = 11;
		testArea = new Area(width, height);
		
		producer= new Producer(1, "ProductProducer", new Position(0, 0), 1);
		band1 	= new Conveyor(2, "Conveyor1", 		new Position(2, 1, Direction.EAST), 1);
		changer = new Changer(3,  "ProductChanger", new Position(3, 1), 1);
		band2 	= new Conveyor(4, "Conveyor2", 		new Position(3, 3, Direction.EAST), 1);
		band3 	= new Conveyor(5, "Conveyor3", 		new Position(3, 4, Direction.EAST), 1);
		consumer= new Consumer(6, "ProductConsumer", new Position(3, 5), 1);
		positi 	= new Positioner(7,new Position(0,height-1));

		testArea.setMachine(producer,producer.getPosition());
		testArea.setMachine(band1,band1.getPosition());
		testArea.setMachine(band2,band2.getPosition());
		testArea.setMachine(band3,band3.getPosition());
		testArea.setMachine(changer,changer.getPosition());
		testArea.setMachine(positi,positi.getPosition());
		testArea.setMachine(consumer,consumer.getPosition());
		
		addMachineInputAndOutputDirections();
		machineCount = 7; // Bitte anpassen, falls sich setUp ändert!
	}

	private void addMachineInputAndOutputDirections() {
		add(testOutputProducer, 1, 1, Direction.EAST);
		add(testInputBand1, 2, 1, Direction.WEST);
		add(testOutputBand1, 2, 1, Direction.EAST);
		add(testInputChanger, 3, 1, Direction.WEST);
		add(testOutputChanger, 3, 2, Direction.SOUTH);
		add(testInputBand2, 3, 3, Direction.NORTH);
		add(testOutputBand2, 3, 3, Direction.SOUTH);
		add(testInputBand3, 3, 4, Direction.NORTH);
		add(testOutputBand3, 3, 4, Direction.SOUTH);
		add(testInputConsumer, 3, 5, Direction.NORTH);
		
		producer.setOutput(testOutputProducer);
		band1.setInput(testInputBand1);
		band1.setOutput(testOutputBand1);
		band2.setInput(testInputBand2);
		band2.setOutput(testOutputBand2);
		band3.setInput(testInputBand3);
		band3.setOutput(testOutputBand3);
		changer.setInput(testInputChanger);
		changer.setOutput(testOutputChanger);
		consumer.setInput(testInputConsumer);
	}

	@Test
	public void testMovementToFreeSpace() throws AreaPositioningException {

		Position oldPos = new Position(consumer.getPosition().getX(), consumer.getPosition().getY(), consumer.getPosition().getDirection());
		Position newPos = new Position(width-1, height-1, oldPos.getDirection());

		testArea.moveMachine(consumer, newPos.getX(), newPos.getY());
		System.out.println("consiiiiii: " + consumer.getPosition());
		Position p = new Position(consumer.getPosition().getX(), consumer.getPosition().getY(), consumer.getPosition().getDirection());
		System.out.println("newP: " + consumer.getPosition().getX() + consumer.getPosition().getY());
		System.out.println("getmach: " + testArea.getMachine(p));
		assertTrue(testArea.fieldIsEmpty(oldPos));
		assertFalse(testArea.fieldIsEmpty(consumer.getPosition()));
		assertEquals(newPos, consumer.getPosition());

		System.out.println("Area after movement to free space:");
		testArea.show();
	}

	@Test
	public void testMovementToOccupiedSpace() throws AreaPositioningException {
		
		Position oldPos = new Position(consumer.getPosition().getX(), consumer.getPosition().getY(), consumer.getPosition().getDirection());
		Position newPos = new Position(producer.getPosition().getX(), producer.getPosition().getY(), producer.getPosition().getDirection());
		
		testArea.moveMachine(consumer, newPos.getX(), newPos.getY());

		assertFalse(testArea.fieldIsEmpty(oldPos));
		assertFalse(testArea.fieldIsEmpty(newPos));
		assertEquals(oldPos, consumer.getPosition());
		
		System.out.println("Area after movement to occupied space:");
		testArea.show();
	}

	@Test
	public void testMovementToSelfOccupiedSpace() throws AreaPositioningException {

		Position oldPos = new Position(consumer.getPosition().getX(), consumer.getPosition().getY(), consumer.getPosition().getDirection());
		Position newPos = new Position(consumer.getPosition().getX()+1, consumer.getPosition().getY()+1, consumer.getPosition().getDirection());
	
		testArea.moveMachine(consumer, newPos.getX(), newPos.getY());
	
		assertTrue(testArea.fieldIsEmpty(oldPos));
		assertFalse(testArea.fieldIsEmpty(consumer.getPosition()));
		assertEquals(newPos, consumer.getPosition());

		System.out.println("Area after movement to self-occupied space:");
		testArea.show();
	}

	@Test
	public void testDeletion() throws AreaPositioningException {

		Position oldPos = new Position(consumer.getPosition().getX(), consumer.getPosition().getY(), consumer.getPosition().getDirection());

		testArea.delete(consumer.getPosition());
		
		for (int i = oldPos.getX(); i < oldPos.getX() + consumer.getWidth(); i++) {
			for (int j = oldPos.getY(); j < oldPos.getY() + consumer.getHeight(); j++) {
				assertTrue(testArea.fieldIsEmpty(oldPos));
			}
		}
		
		assertNotNull(consumer);
		
		System.out.println("Area after deletion:");
		testArea.show();
	}

	@Test
	public void testFieldChecking() {
		for (int i = 0; i < testArea.getWidth(); i++) {
			for (int j = 0; j < testArea.getHeight(); j++) {
				Position p = new Position(i, j, null);
				try {
					if (testArea.getMachine(p) == null) {
						assertTrue(testArea.fieldIsEmpty(p));
					} else {
						assertFalse(testArea.fieldIsEmpty(p));
					}
				} catch (AreaPositioningException e) { 
					/* Position(i,j) not in bounds */
					assertFalse(testArea.fieldIsEmpty(p));
				}
			}
		}
	}
	
	@Test
	public void resizeShouldIncreaseSizeOfEmptyFactory() throws Exception {
		int actWidth = 10;
		int actHeight = 10;
		int newWidth = actWidth + 2;
		int newHeight = actHeight + 2;
		Area testArea = new Area(actWidth,actHeight);
		
		testArea.resizeDefaultArea(newWidth, newHeight);
		testArea.show();
		assertEquals(newWidth, testArea.getWidth());
		assertEquals(newHeight, testArea.getHeight());
	}
	
	@Test
	public void resizeShouldIncreaseSizeOfFactory() throws Exception {
		int actWidth = 10;
		int actHeight = 10;
		int newWidth = actWidth + 2;
		int newHeight = actHeight + 2;
		Area testArea = new Area(actWidth,actHeight);
		
		testArea.resizeDefaultArea(newWidth, newHeight);
		testArea.show();
		assertEquals(newWidth, testArea.getWidth());
		assertEquals(newHeight, testArea.getHeight());
	}
	
	@Test
	public void resizeShouldShrinkArea() throws Exception {
		int actWidth = 10;
		int actHeight = 10;
		int newWidth = actWidth - 2;
		int newHeight = actHeight - 2;
		Area testArea = new Area(actWidth,actHeight);
		
		testArea.resizeDefaultArea(newWidth, newHeight);
		testArea.show();
		assertEquals(newWidth, testArea.getWidth());
		assertEquals(newHeight, testArea.getHeight());
	}
	
	@Test(expected = ResizeMustNotDeleteMachinesException.class)
	public void resizeMustNotDeleteMachines() throws Exception {
		int actWidth = 10;
		int actHeight = 10;
		int newWidth = actWidth - 1;
		int newHeight = actHeight - 1;
		Area testArea = new Area(actWidth,actHeight);
		Position highestPos = new Position(newWidth, newHeight);
		
		testArea.setMachine(new Producer(10, highestPos), highestPos);
		testArea.resizeDefaultArea(newWidth, newHeight);
	}
	
	@Test
	public void areaShouldBeResizableToDifferentSizes() throws Exception {
		int actWidth = 10;
		int actHeight = 10;
		int min = 1;
		int max = 120;
		Area testArea = new Area(actWidth,actHeight);
		
		for (int i = 0; i < 100; i += 1) {
			int newWidth = createRandomNumber(min, max);
			int newHeight = createRandomNumber(min, max);
			testArea.resizeDefaultArea(newWidth, newHeight);
			assertEquals(newWidth, testArea.getWidth());
			assertEquals(newHeight, testArea.getHeight());
		}
	}

	private int createRandomNumber(int min, int max) { return (int) (Math.random()*(max-min)) + min; }
	
	@Test
	public void testMachineIterator() throws Exception {
		testArea.show();
		int numberOfMachines = 0;
		for (Machine m : testArea) {
			m.toString();
			++numberOfMachines;
		}
		assertEquals(machineCount,numberOfMachines);
	}
}