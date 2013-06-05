package de.hsrm.mi.swtpro03.FactoryFactory.model.machine;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.ProductOverflowException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Position;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Product;

public class Consumer extends Machine {
	private static final String CONSUMER = "Consumer";
	private static Logger logger = Logger.getLogger(CONSUMER);
	private static final long serialVersionUID = 8541307205500554576L;
	public static final int CLASS_ID = 2;
	private int emptyCounterMax;
	private int emptyCounter;
	
	public Consumer(int instanceID, String name, Position position, int processingTime) {		
		super(instanceID, name, position, processingTime);		
		Position inputPos = new Position(position.getX(), position.getY(), position.getDirection().opposing());
		this.getInput().add(inputPos);
		this.emptyCounterMax = 0;
		this.emptyCounter = 0;
	}

	public Consumer(int instanceID, Position position) {
		this(instanceID, CONSUMER, position, 1);
	}
	
	@Override
	public void take() throws ProductOverflowException {
		logger.log(Level.INFO, this.getName() + " konsumiert Produkte");
		if(maxCapacityIsReached()){
			logger.log(Level.INFO, "Maximale Kapazität erreicht: " + this.maxCapacityInNumberOfProducts);
			for(Product act : products){
				logger.log(Level.INFO, "Name: " + act.getName() + ", Typ: " + act.getType() + ", Gewicht: " + act.getWeight() + ", Preis: " + act.getPrice());
			}
		}else{
			for(Machine machine : predecessors){
				this.takeProducts(machine);
			}
		}
	}

	public void clear(){
		this.getProducts().clear();
	}
	
	public boolean isReadyToClear(){
		return emptyCounter == emptyCounterMax;
	}
	
	public void incEmptyCounter(){
		emptyCounter += 1;
	}
	
	public int getEmptyCounterMax() {
		return emptyCounterMax;
	}

	public void setEmptyCounterMax(int emptyCounterMax) {
		this.emptyCounterMax = emptyCounterMax;
	}
	
	public int getEmptyCounter() {
		return emptyCounter;
	}

	public void resetEmptyCounter() {
		this.emptyCounter = 0;
	}
	
	public int getClassId() {
		return CLASS_ID;
	}
}