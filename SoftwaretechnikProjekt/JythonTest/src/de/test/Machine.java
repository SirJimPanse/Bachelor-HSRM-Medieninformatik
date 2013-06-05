package de.test;
import java.util.ArrayList;
import java.util.List;

public class Machine {

	protected String name;
	protected int id;
	protected int processingTime;
	protected int maxCapacityInNumberOfProducts;
	protected List<String> products;
	
	public Machine(String name, int id, int processingTime, int maxCapacityInNumberOfProducts) {
		this.name = name;
		this.id = id;
		this.processingTime = processingTime;
		this.maxCapacityInNumberOfProducts = maxCapacityInNumberOfProducts;
		this.products = new ArrayList<String>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProcessingTime() {
		return processingTime;
	}
	public void setProcessingTime(int processingTime) {
		this.processingTime = processingTime;
	}
	public int getMaxCapacityInNumberOfProducts() {
		return maxCapacityInNumberOfProducts;
	}
	public void setMaxCapacityInNumberOfProducts(int maxCapacityInNumberOfProducts) {
		this.maxCapacityInNumberOfProducts = maxCapacityInNumberOfProducts;
	}
	public List<String> getProducts() {
		return products;
	}
	public void setProducts(List<String> products) {
		this.products = products;
	}
}
