package de.hsrm.mi.swtpro03.FactoryFactory.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.AreaPositioningException;
import de.hsrm.mi.swtpro03.FactoryFactory.exception.ResizeMustNotDeleteMachinesException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Changer;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Consumer;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Conveyor;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Machine;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Positioner;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Producer;

public class Factory implements Serializable, Cloneable {
	private static final long serialVersionUID = -4793266656281959034L;
	private static final Logger LOGGER = Logger.getLogger(Factory.class.getSimpleName());
	private int factoryID;
	private String name;
	private Map<String, Machine> machineMap;
	private Area area;
	private boolean isSimulation;
	private boolean statsAlreadyShown = false;
	
	private int machineInstanceCounter;
	
	public Factory() {
		machineInstanceCounter = 0;
	}

	public Factory(int factoryID, String name, Area area) {
		this();
		this.setSimulation(false);
		this.factoryID = factoryID;
		this.name = name;
		this.area = area;
		machineMap = new HashMap<String, Machine>();
	}

	private void addMachineToMachineMap(String machineID, Machine machine) {
		machineMap.put(machineID, machine);
	}

	private void removeMachineFromMachineMap(String machineID) {
		machineMap.remove(machineID);
	}

	private synchronized Machine createMachineByID(int classID, Position position) {
		Machine m;
		switch (classID) {
		case Changer.CLASS_ID:
			m =  new Changer(machineInstanceCounter,position);break;
		case Consumer.CLASS_ID:
			m =  new Consumer(machineInstanceCounter,position);break;
		case Positioner.CLASS_ID:
			m =  new Positioner(machineInstanceCounter, position);break;
		case Producer.CLASS_ID:
			m =  new Producer(machineInstanceCounter, position);break;
		case Conveyor.CLASS_ID:
			m =  new Conveyor(machineInstanceCounter, position);break;
		default:
			return null;
		}
		machineInstanceCounter++;
		return m;
	}

	public synchronized String placeMachine(int classID, Position position) {
		try {
			LOGGER.log(Level.INFO, "position is: "+position.getX()+","+position.getY()+", direction: "+position.getDirection());
			Machine machine = createMachineByID(classID, position);
			area.setMachine(machine,position);
			String machineID = getMachineID(classID, machine.getInstanceID());
			LOGGER.log(Level.INFO, "Maschine mit der classID " + classID + " auf Position " + position + " gesetzt.");
			
			addMachineToMachineMap(machineID, machine);
			return machineID;
		} catch (AreaPositioningException ape) {
			LOGGER.log(Level.WARNING, "Fehler beim setzen der Maschine mit der classID " + classID + " auf Position " + position);
		}
		return "";
	}

	private static String getMachineID(int classID, int instanceID) {
		return classID + "-" + instanceID;
	}
	
	public static String getMachineID(Machine machine) {
		int result = -1;
		if (machine instanceof Producer) {
			result = Producer.CLASS_ID;
		} else if (machine instanceof Positioner) {
			result = Positioner.CLASS_ID;
		} else if (machine instanceof Changer) {
			result = Changer.CLASS_ID;
		} else if (machine instanceof Conveyor) {
			result = Conveyor.CLASS_ID;
		} else if (machine instanceof Consumer) {
			result = Consumer.CLASS_ID;
		}
		return getMachineID(result,machine.getInstanceID());
	}

	public synchronized boolean moveMachine(String machineID, int newX, int newY) throws AreaPositioningException {
		Machine machine = machineMap.get(machineID);
		if (machine == null) {
			return false;
		}
		boolean moved = area.moveMachine(machine, newX, newY);

		return moved;
	}

	public synchronized boolean deleteMachine(String machineID) {
		Machine machine = machineMap.get(machineID);
		Position p = null;
		if (machine != null) {
			p = machine.getPosition();
			try {
				if (area.delete(p)) {
					removeMachineFromMachineMap(machineID);
					LOGGER.log(Level.INFO, "Maschine mit der machineID " + machineID + " auf Position " + p + " gelöscht.");
					return true;
				}
			} catch (AreaPositioningException ape) {
				LOGGER.log(Level.WARNING, "Fehler beim löschen der Maschine mit der machineID " + machineID + " auf Position " + p+ " (outOfBounds)");
			}
		} else {
			LOGGER.log(Level.WARNING, "Maschine mit der machineID " + machineID + " nicht existent!");
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Machine getMachineByID(String machineID) {
		return machineMap.get(machineID);
	}

	public Map<String, Machine> getMachineMap() {
		return machineMap;
	}

	public Area getArea() {
		return area;
	}

	public int getFactoryID() {
		return factoryID;
	}

	public void setFactoryID(int factoryID) {
		this.factoryID = factoryID;
	}
	 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		result = prime * result + factoryID;
		result = prime * result
				+ ((machineMap == null) ? 0 : machineMap.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Factory))
			return false;
		Factory other = (Factory) obj;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		if (factoryID != other.factoryID)
			return false;
		if (machineMap == null) {
			if (other.machineMap != null)
				return false;
		} else if (!machineMap.equals(other.machineMap))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public Factory clone() {
		
		Factory simulationFactory = null;
		
		ByteArrayOutputStream output = null;
		ByteArrayInputStream input = null;

		try {
			output = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(output);
			oos.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			input = new ByteArrayInputStream(output.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(input);
			simulationFactory = (Factory) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		simulationFactory.setSimulation(true);
		
		return simulationFactory;
	}

	public boolean isSimulation() {
		return isSimulation;
	}

	public void setSimulation(boolean isSimulation) {
		this.isSimulation = isSimulation;
	}

	public void resize(int width, int height) throws ResizeMustNotDeleteMachinesException {
		this.area.resizeDefaultArea(width, height);
	}

	public boolean isStatsAlreadyShown() {
		return statsAlreadyShown;
	}

	public void setStatsAlreadyShown(boolean statsAlreadyShown) {
		this.statsAlreadyShown = statsAlreadyShown;
	}
}