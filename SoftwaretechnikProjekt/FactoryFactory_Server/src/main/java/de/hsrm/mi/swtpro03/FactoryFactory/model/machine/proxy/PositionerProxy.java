package de.hsrm.mi.swtpro03.FactoryFactory.model.machine.proxy;

import java.util.List;

import de.hsrm.mi.swtpro03.FactoryFactory.model.Product;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Machine;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Positioner;

public class PositionerProxy {

	private Positioner positioner;
	
	public PositionerProxy(Positioner positioner){
		this.positioner = positioner;
	}
	
	public List<Product> getProducts(){
		return positioner.getProducts();
	}
	
	public void setProducts(List<Product> products){
		positioner.setProducts(products);
	}
	
	public Machine getOrigin(){
		return positioner.getOrigin();
	}
	
	public void setOrigin(String machineID){
		positioner.setOrigin(machineID);
	}
	
	public void addFollower(String machineId){
		positioner.addFollower(machineId);
	}
	
	public void chooseDestination(String machineId){
		positioner.chooseDestination(machineId);
	}
}

