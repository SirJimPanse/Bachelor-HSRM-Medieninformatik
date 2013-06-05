package de.test;

import java.util.List;

public class ChangerProxy {

	private Changer changer;
	
	public ChangerProxy(Changer changer){
		this.changer = changer;
	}
	
	public List<String> getInputProducts(){
		return changer.getInputProducts();
	}
	
	public void setInputProducts(List<String> inputs){
		changer.setInputProducts(inputs);
	}
	
	public String getOutputProduct(){
		return changer.getOutputProduct();
	}
	
	public void setOutputProduct(String output){
		changer.setOutputProduct(output);
	}
	
	public List<String> getProducts(){
		return changer.getProducts();
	}
	
	public void setProducts(List<String> products){
		changer.setProducts(products);
	}
	
	
}

