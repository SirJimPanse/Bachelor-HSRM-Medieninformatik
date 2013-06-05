package de.test;
import java.util.ArrayList;
import java.util.List;

import org.python.util.PythonInterpreter;

public class Changer extends Machine{

	private List<String> inputProducts;
	private String outputProduct;
	private PythonInterpreter interpreter;
	private String script = 
				"def edit():" +
					"\n\tchanger.inputProducts = ['Apfel', 'Schraube']" +
					"\n\tfor product in changer.products:" +
						"\n\t\tif product in changer.inputProducts:" +
							"\n\t\t\tchanger.products += [product+'saft']" +
							"\n\t\t\tchanger.products.remove(product)";
	
	public Changer(String name, int id, int processingTime, int maxCapacityInNumberOfProducts) {
		super(name, id, processingTime, maxCapacityInNumberOfProducts);
        interpreter = new PythonInterpreter();
        inputProducts = new ArrayList<String>();
        outputProduct = "";
	}
	
	public void addProduct(String s){
		products.add(s);
	}
	
	public void addInputProduct(String s){
		inputProducts.add(s);
	}
	
	public void removeProduct(String s){
		products.remove(s);
	}
	
	public void callUserMethods(){
		ChangerProxy changerProxy = new ChangerProxy(this);
		interpreter.set("changer", changerProxy);
		interpreter.exec(script);
		interpreter.exec("edit()");
	}
	
	public void callUserMethods_change(){
		ChangerProxy changerProxy = new ChangerProxy(this);
		interpreter.set("changer", changerProxy);
		interpreter.exec("products = list(changer.products)");
		interpreter.exec("inputProducts = list(changer.inputProducts)");
		interpreter.exec("outputProduct = changer.outputProduct");
		interpreter.execfile("src/UserMethods.py");
		interpreter.exec("change()");
		interpreter.exec("changer.products = products");
		interpreter.exec("changer.inputProducts = inputProducts");
		interpreter.exec("changer.outputProduct = outputProduct");
	}
	
	public void simulate(){
		callUserMethods();
	}
	
	public List<String> getInputProducts() {
		return inputProducts;
	}

	public void setInputProducts(List<String> inputProducts) {
		this.inputProducts = inputProducts;
	}

	public String getOutputProduct() {
		return outputProduct;
	}

	public void setOutputProduct(String outputProduct) {
		this.outputProduct = outputProduct;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
		
	}
}
