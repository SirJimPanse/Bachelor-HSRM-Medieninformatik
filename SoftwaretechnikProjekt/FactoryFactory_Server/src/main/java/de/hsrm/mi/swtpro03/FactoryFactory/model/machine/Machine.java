package de.hsrm.mi.swtpro03.FactoryFactory.model.machine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.ProductOverflowException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Direction;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Position;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Product;
import de.hsrm.mi.swtpro03.FactoryFactory.util.PropertyTuple;

public abstract class Machine implements ITaker, Serializable{
	
	private static Logger logger = Logger.getLogger("Machine");
	private static final long serialVersionUID = 6835282919476041907L;

	protected String name;
	protected Position position;
	protected int width;
	protected int height;
	protected int processingTime;
	protected List<Product> products;
	protected List<Position> input;
	protected List<Position> output;
	protected List<Machine> followers;
	protected List<Machine> predecessors;
	protected int maxCapacityInNumberOfProducts;
	protected List<Product> logList;
	protected Map<String, PropertyTuple> statisticsList;
	protected boolean gotTailback = false;
	protected final int instanceID;
	private int takeCounter;
	private List<Product> productCache;
	
	public Machine(int instanceID) {
		this.instanceID = instanceID;
		width = 1;
		height = 1;
		position = new Position(-1,-1,Direction.NORTH);
		products = new ArrayList<Product>();
		input = new ArrayList<Position>();
		output = new ArrayList<Position>();
		followers = new ArrayList<Machine>();
		predecessors = new ArrayList<Machine>();
		logList = new ArrayList<Product>();
		statisticsList = new HashMap<String, PropertyTuple>();
		setMaxCapacityInNumberOfProducts(3);
		takeCounter = 0;
		productCache = new ArrayList<Product>();
	}

	public Machine(int instanceID, String name, Position position, int processingTime) {
		this(instanceID);
		this.name = name;
		this.position = position;
		this.processingTime = processingTime;
	}
	
	public boolean hasMatchingOutput(Direction direction) {
		for (Position iter : this.output) {
			if (iter.getDirection() == direction) {
				return true;
			}
		}
		return false;
	}

	public boolean hasMatchingInput(Direction direction) {
		for (Position iter : this.input) {
			if (iter.getDirection() == direction) {
				return true;
			}
		}
		return false;
	}

	public void showProducts() {
		for (Product act : this.products) {
			logger.log(Level.INFO, act.getName());
		}
	}

	/**
	 * 
	 * @param Produkte
	 * @return List of added product-items
	 * @throws ProductOverflowException if maxCapacity is exceeded
	 */
	protected void addProducts(List<Product> products)
			throws ProductOverflowException {
	}

	public boolean maxCapacityIsReached() {
		return this.products.size() >= maxCapacityInNumberOfProducts;
	}

	public void removeProducts(List<Product> products) {
		this.products.removeAll(products);
	}

	/**
	 * Nimmt Produkte der Vorgängermaschine auf, solange die eigene Kapazität
	 * nicht überschritten ist. Löscht jedes aufgenommene Produkt beim Vorgänger.
	 * 
	 * @param machine Vorgänger
	 * @throws ProductOverflowException wenn Kapazität überschritten wurde
	 */
	protected void takeProducts(Machine machine) throws ProductOverflowException {
		Product actProduct;
		List<Product> machineProducts = machine.getProducts();

		while (!machineProducts.isEmpty()) {
			if ((this.products.size() + this.productCache.size()) >= maxCapacityInNumberOfProducts) {
				throw new ProductOverflowException(" Maximale Kapazität erreicht bei Maschine: " + this.name + ". ");
			}
			actProduct = machineProducts.get(0);
			this.productCache.add(actProduct);
			updateStatisticsList(actProduct);
			machineProducts.remove(0);
		}
	}

	public void addFollower(Machine machine) {
		this.followers.add(machine);
	}

	public void take() throws ProductOverflowException {
		takeCounter += 1;
		
		if(predecessors.size() == takeCounter){ 		// Erst wenn von allen Vorgängern produkte genommen wurden
			takeCounter = 0;
			
			if(!followers.isEmpty() && followers.get(0) != null){				// Wenn Nachfolger vorhanden
				try{
					followers.get(0).take();			// und nachfolger bescheid sagen
				} catch (ProductOverflowException ex){
					logger.log(Level.INFO, "ProductOverflowException in " + followers.get(0));
				}
			} 
			
			for(Machine predecessor : predecessors){ 	// und dann produkte von vorgängern holen
				this.takeProducts(predecessor);
			}
		}
	}

	public void writeCacheToProdudctList(){
		if(productCache != null){
			if(!productCache.isEmpty()){
				products.addAll(productCache);
				productCache = new ArrayList<Product>();
			}
		}
	}
	
	private void rotate(boolean rotLeft) {
		this.position = rotDirectionOfPosition(position,rotLeft);
		this.input    = rotAll(input,rotLeft);
		this.output   = rotAll(output,rotLeft);
	}
	
	private List<Position> rotAll(List<Position> lisPos,boolean rotLeft) {
		Position newPosition;
		for ( int i = 0; i < lisPos.size(); i += 1) {
			newPosition = rotDirectionOfPosition(lisPos.get(i), rotLeft);
			lisPos.set(i, newPosition);
		}
		return lisPos;
	}
	
	private Position rotDirectionOfPosition(Position p, boolean rotLeft) {
		if (rotLeft) {
			return new Position(p.getX(),p.getY(),p.getDirection().rotateLeft());
		}
		return new Position(p.getX(),p.getY(),p.getDirection().rotateRight());
	}

	public void rotateRight() { rotate(false); }
	public void rotateLeft() { rotate(true); }

	protected void updateStatisticsList(Product product) {
		String key = product.getName();
		if(statisticsList.containsKey(key)){
			int c = statisticsList.get(key).getCount();
			double w = statisticsList.get(key).getWeight();
			double p = statisticsList.get(key).getPrice();
			statisticsList.get(key).setCount(c+1);
			statisticsList.get(key).setWeight(w += product.getWeight());
			statisticsList.get(key).setPrice(p += product.getPrice());
		}else{
			PropertyTuple pt = new PropertyTuple(1, product.getWeight(), product.getPrice());
			statisticsList.put(key,pt);
		}
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(int processingTime) {
		this.processingTime = processingTime;
	}

	public Position getPosition() {
		return position;
	}

	/**hier wird nur die neue x,y-Position gesetzt, die alte Richtung
	 * bleibt erhalten.
	 * Gedreht wird über die rotate() Methode
	 * @param position neue Position
	 */
	public void setPosition(Position position) {
		this.position = new Position(position.getX(), position.getY(), this.position.getDirection());
		newPositionSameDirectionForEveryone(position, input);
		newPositionSameDirectionForEveryone(position, output);
	}

	private void newPositionSameDirectionForEveryone(Position position, List<Position> ioput) {
		Position actPos, newPos;
		for (int i = 0; i < ioput.size(); i++) {
			actPos = ioput.get(i);
			newPos = new Position(position.getX(), position.getY(), actPos.getDirection());
			ioput.set(i, newPos);
		}
	}

	public List<Product> getProducts() {
		if (products == null) {
			return new java.util.ArrayList<Product>();
		}
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<Machine> getFollowers() {
		return followers;
	}

	public void setFollowers(List<Machine> followers) {
		this.followers = followers;
	}

	public List<Machine> getPredecessors() {
		return predecessors;
	}

	public void setPredecessors(List<Machine> predecessors) {
		this.predecessors = predecessors;
	}

	public int getInstanceID() {
		return instanceID;
	}

	public List<Position> getInput() {
		return input;
	}

	public void setInput(List<Position> input) {
		this.input = input;
	}

	public List<Position> getOutput() {
		return output;
	}

	public void setOutput(List<Position> output) {
		this.output = output;
	}

	public int getMaxCapacityInNumberOfProducts() {
		return maxCapacityInNumberOfProducts;
	}

	public void setMaxCapacityInNumberOfProducts(int maxCapacityInNumberOfProducts) {
		this.maxCapacityInNumberOfProducts = maxCapacityInNumberOfProducts;
	}

	public Map<String, PropertyTuple> getStatisticsList() {
		return statisticsList;
	}
	
	public List<Product> getLogList() {
		return logList;
	}
}