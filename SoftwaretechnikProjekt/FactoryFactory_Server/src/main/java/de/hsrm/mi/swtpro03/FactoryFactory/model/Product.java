package de.hsrm.mi.swtpro03.FactoryFactory.model;

import java.io.Serializable;

public class Product implements Serializable, Cloneable {
	private static final long serialVersionUID = 5130354919517856492L;
	private String name;
	private double weight;
	private double price;
	private String type;
	private static long instanceCount = 0;
	public long id;

	public Product(String name, double weight, double price, String type) {
		this.name = name;
		this.weight=weight;
		this.price=price;
		this.type=type;
		this.incrementInstanceCount();
		this.id = Product.instanceCount;
	}
	
	private synchronized void incrementInstanceCount(){
		Product.instanceCount += 1;
	}
	
	/*DO NOT DELETE!!*/
	public Product() {
		this("a product", 0, 0, "a type");
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
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public long getId(){
		return id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		temp = Double.doubleToLongBits(weight);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Product)) {
			return false;
		}
		Product other = (Product) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (Double.doubleToLongBits(price) != Double
				.doubleToLongBits(other.price)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		if (Double.doubleToLongBits(weight) != Double
				.doubleToLongBits(other.weight)) {
			return false;
		}
		return true;
	}

	@Override
	public Product clone() {
		return new Product(name, weight, price, type);
	}
}
