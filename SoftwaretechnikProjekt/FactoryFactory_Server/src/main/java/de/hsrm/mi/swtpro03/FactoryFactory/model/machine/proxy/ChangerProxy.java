package de.hsrm.mi.swtpro03.FactoryFactory.model.machine.proxy;

import java.util.List;

import de.hsrm.mi.swtpro03.FactoryFactory.model.Product;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Changer;

public class ChangerProxy {

	private Changer changer;
	
	public ChangerProxy(Changer changer){
		this.changer = changer;
	}
	
	public List<Product> getProducts(){
		return changer.getProducts();
	}
	
	public void setProducts(List<Product> products){
		changer.setProducts(products);
	}

	public int getMaxCapacityInNumberOfProducts(){
		return changer.getMaxCapacityInNumberOfProducts();
	}
	
	public void setMaxCapacityInNumberOfProducts(int numberOfProducts){
		changer.setMaxCapacityInNumberOfProducts(numberOfProducts);
	}
	
	public boolean maxCapacityIsReached(){
		return changer.maxCapacityIsReached();
	}
	
}

