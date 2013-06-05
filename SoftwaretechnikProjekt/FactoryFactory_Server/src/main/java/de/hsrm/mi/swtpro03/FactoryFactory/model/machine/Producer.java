package de.hsrm.mi.swtpro03.FactoryFactory.model.machine;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.python.util.PythonInterpreter;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.ProductOverflowException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Position;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Product;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.proxy.ProducerProxy;

public class Producer extends Machine implements IScriptable{
	private static final String PRODUCER = "Producer";
	private static Logger logger = Logger.getLogger(PRODUCER);
	private static final long serialVersionUID = -2482290554478897716L;
	public static final int CLASS_ID = 4;

	private int noProductsPerProduction;
	private Product product;
	private int counter;
	private String script;
	private boolean producing;
		
	public Producer(int instanceID, String name, Position position, int processingTime) {
		super(instanceID, name, position, processingTime);
		this.getOutput().add(position);
		this.setNoProductsPerProduction(0);
		this.setProduct(new Product("Apfel", 1.0, 1.0, "Obst"));
		this.script = getDefaultScript();
		this.counter = 0;
		this.producing = true;
	}

	public Producer(int instanceID, Position position) {
		this(instanceID,PRODUCER,position,1);
	}

	public void produce() throws ProductOverflowException {
		for (int i = 0; i < getNoProductsPerProduction(); i += 1) {
			if (maxCapacityIsReached()) {
				this.setProducing(false);
				throw new ProductOverflowException(" Maximale Kapazität erreicht bei Maschine: " + this.name + ". ");
			}
			this.products.add(getProduct().clone());
			this.updateStatisticsList(getProduct());
			logger.log(Level.INFO, getProduct().getName());
		}
		this.take();
	}
	
	@Override
	public void take() throws ProductOverflowException{
		logger.log(Level.INFO, this.getName() + " produziert Produkte");
		work();
		followers.get(0).take();
	}
	
	public int getNoProductsPerProduction() {
		return noProductsPerProduction;
	}

	public void setNoProductsPerProduction(int noProductsPerProduction) {
		this.noProductsPerProduction = noProductsPerProduction;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public void work() {
		if(script != ""){
			ProducerProxy proxy = new ProducerProxy(this);
			PythonInterpreter interpreter = new PythonInterpreter();
			interpreter.exec("from de.hsrm.mi.swtpro03.FactoryFactory.model import Product");
			interpreter.set("producer", proxy);
			interpreter.exec(script);
			interpreter.exec("produce()");
		}
	}

	@Override
	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public String getScript() {
		return script;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public boolean isProducing() {
		return producing;
	}

	public void setProducing(boolean producing) {
		this.producing = producing;
	}
	
	private String getDefaultScript() {
		return "def produce():" +
				"\n\tproducer.product = Product('Kiste', 5, 7, 'Default')"+
				"\n\tproducer.noProductsPerProduction = 1";
	}

	public int getClassId() {
		return CLASS_ID;
	}

}