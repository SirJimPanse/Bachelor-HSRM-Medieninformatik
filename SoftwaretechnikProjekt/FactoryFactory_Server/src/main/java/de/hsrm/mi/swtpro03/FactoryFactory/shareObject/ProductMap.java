package de.hsrm.mi.swtpro03.FactoryFactory.shareObject;

import java.util.HashMap;

import de.hsrm.mi.swtpro03.FactoryFactory.model.Position;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Product;

public class ProductMap {

	private HashMap<Position,Product[]> productMap;

	public ProductMap() {
		this.productMap = new java.util.HashMap<Position, Product[]>();
	}
	
	public ProductMap(HashMap<Position, Product[]> productMap) {
		super();
		this.productMap = productMap;
	}
	
	public void setAll(Position pos, Product pros[]) {
		if (pros == null) 
			productMap.remove(pos);
		productMap.put(pos, pros);
	}
	
	public HashMap<Position, Product[]> getProductMap() {
		return productMap;
	}
	
	public void setProductMap(HashMap<Position, Product[]> productMap) {
		this.productMap = productMap;
	}
}
