package de.hsrm.mi.swtpro03.FactoryFactory.model.machine.proxy;

import de.hsrm.mi.swtpro03.FactoryFactory.model.Product;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Producer;

public class ProducerProxy {

	private Producer producer;
	
	public ProducerProxy(Producer producer){
		this.producer = producer;
	}
	
	public void setProduct(Product product){
		producer.setProduct(product);
	}
	
	public Product getProduct(){
		return producer.getProduct();
	}
	
	public int getNoProductsPerProduction() {
		return producer.getNoProductsPerProduction();
	}

	public void setNoProductsPerProduction(int noProductsPerProduction) {
		producer.setNoProductsPerProduction(noProductsPerProduction);
	}
	
	public int getCounter(){
		return producer.getCounter();
	}
	
	public void setCounter(int counter){
		producer.setCounter(counter);
	}
}

