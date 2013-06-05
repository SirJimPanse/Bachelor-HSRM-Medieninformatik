package de.test;

public class App {

	public static void main(String[] args) {
		Changer c = new Changer("steven", 1, 10, 10);
		
		c.addProduct("Apfel");
		c.addProduct("Banana");
		c.addProduct("Apfel");
		c.addProduct("Hirsch");
		c.addProduct("Apfel");
		c.addProduct("Birne");
		c.addProduct("Schraube");
		c.addProduct("Traube");

		for(String s : c.getProducts()){
			System.out.print(s + ", ");
		}
		System.out.println();
		
		c.simulate();
		
		for(String s : c.getInputProducts()){
			System.out.print(s + ". ");
		}
		System.out.println();
		
		for(String s : c.getProducts()){
			System.out.print(s + ", ");
		}
		System.out.println();
	}
}
