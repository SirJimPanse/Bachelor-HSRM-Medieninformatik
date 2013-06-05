package de.hsrm.mi.swtpro03.FactoryFactory.util;

import java.io.Serializable;

public class PropertyTuple implements Serializable{

	private static final long serialVersionUID = 3055656521789778282L;
	private int count; 
	private double weight, price;
	
	public PropertyTuple(int count, double weight, double price){
		this.count = count;
		this.weight = weight;
		this.price = price;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}