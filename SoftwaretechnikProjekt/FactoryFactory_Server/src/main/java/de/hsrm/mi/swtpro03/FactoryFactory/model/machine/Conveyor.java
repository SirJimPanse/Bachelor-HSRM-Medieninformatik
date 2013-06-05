package de.hsrm.mi.swtpro03.FactoryFactory.model.machine;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.ProductOverflowException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Position;

public class Conveyor extends Machine {
	private static final String CONVEYOR = "Conveyor";
	private static Logger logger = Logger.getLogger(CONVEYOR);
	private static final long serialVersionUID = 6653733126877485499L;
	public static final int CLASS_ID = 5;
	
	public Conveyor(int instanceID, String name, Position position, int processingTime) {
		
		super(instanceID, name, position,processingTime);
		
		Position inputPos  = new Position(position, position.getDirection().opposing());
		Position inputPos1 = new Position(position, position.getDirection().rotateLeft());
		Position inputPos2 = new Position(position, position.getDirection().rotateRight());
		
		this.getOutput().add(position);
		this.getInput().add(inputPos);
		this.getInput().add(inputPos1);
		this.getInput().add(inputPos2);
	}

	public Conveyor(int instanceID, Position position){
		this(instanceID, CONVEYOR, position,1);
	}
	
	@Override
	public void take() throws ProductOverflowException{
		logger.log(Level.INFO, this.getName() + " leitet Produkte weiter");
		super.take();
	}

	@Override
	public int getClassId() {
		return CLASS_ID;
	}
}