package de.hsrm.mi.swtpro03.FactoryFactory.model.machines;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.ProductOverflowException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Direction;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Position;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Product;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Machine;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Producer;

public class ProducerTest {
	Producer producer;
	Product product;
	MyTaker myTaker;
	int noProductsPerProduction;
	
	@Before
	public void setup() {
		product = new Product("Testat", 0.3, 0.0, "Krrraaam");
		noProductsPerProduction = 3;
		Position defaultPos = new Position(1,1,Direction.WEST);
		producer = new Producer(0, "abc", defaultPos, 1);
		
		addTakerAsFollower();
	}

	private void addTakerAsFollower() {
		myTaker = new MyTaker();
		ArrayList<Machine> followers = new ArrayList<Machine>();
		followers.add(myTaker);
		producer.setFollowers(followers);
	}
	
	@Test
	@Ignore
	public void producerShouldProduceGivenNumberOfNewProducts() throws Exception {
		int noProductsPerProduction = 3;
		producer.setNoProductsPerProduction(noProductsPerProduction);
		producer.setMaxCapacityInNumberOfProducts(noProductsPerProduction);
		myTaker.setMaxCapacityInNumberOfProducts(noProductsPerProduction+10);
		producer.produce();
		assertEquals(noProductsPerProduction, myTaker.getProducts().size());
	}
	
	
	@Test (expected = ProductOverflowException.class)
	public void producerMustNotProduceMoreProductsThanCapacityAllows() throws Exception {
		int noProductsPerProduction = 5;
		int maximumProductCapacity = 3; 
		myTaker.setMaxCapacityInNumberOfProducts(noProductsPerProduction+1);
		producer.setMaxCapacityInNumberOfProducts(maximumProductCapacity);
		producer.setNoProductsPerProduction(noProductsPerProduction);
		producer.produce();
	}
	
	@Test
	@Ignore
	public void producerShouldProduceCloneOfGivenProduct() throws Exception {
		double epsilon = 0.001;
		myTaker.setMaxCapacityInNumberOfProducts(3);
		producer.setProduct(product);
		producer.produce();
		for (Product actProduct : myTaker.getProducts()) {
			assertFalse(actProduct == product);
			assertEquals(product.getName(), actProduct.getName());
			assertEquals(product.getType(), actProduct.getType());
			assertEquals(product.getPrice(), actProduct.getPrice(), epsilon);
			assertEquals(product.getWeight(), actProduct.getWeight(), epsilon);
		}
	}
	
	@Test(expected = ProductOverflowException.class)
	@Ignore
	public void producerShouldOverflowOnSecondProduction() throws Exception {
		myTaker.setMaxCapacityInNumberOfProducts(3);
		producer.setNoProductsPerProduction(5);
		producer.setMaxCapacityInNumberOfProducts(5);
		producer.produce();
		producer.produce();
	}
	
	private class MyTaker extends Machine {
		private static final long serialVersionUID = -7688913847151554174L;
		public MyTaker() { 	super(2); }
		@Override
		public void take() throws ProductOverflowException {
			takeProducts(followers.get(0));
		}
		@Override
		public int getClassId() {
			return 0;
		}
	}
}
