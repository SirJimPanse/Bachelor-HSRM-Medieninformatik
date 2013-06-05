package de.hsrm.mi.swtpro03.FactoryFactory.model.machines;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.hsrm.mi.swtpro03.FactoryFactory.exception.ProductOverflowException;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Direction;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Position;
import de.hsrm.mi.swtpro03.FactoryFactory.model.Product;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Consumer;
import de.hsrm.mi.swtpro03.FactoryFactory.model.machine.Producer;

public class ConsumerTest {
	Consumer consumer;
	MyProducer myProducer;
	private Product product;

	@Before
	public void setup() {
		consumer = new Consumer(1, new Position(1,1,Direction.EAST) );
		myProducer = new MyProducer();
		product = new Product("Testat", 0.1, 1.0, "Krrram");
		myProducer.setProduct(product);
		myProducer.addFollower(consumer);
		consumer.getPredecessors().add(myProducer);
	}
	
	@Test(expected = ProductOverflowException.class)
	public void consumerShouldThrowOverflowExceptionIfCapacityIsExceeded() throws Exception {
		consumer.setMaxCapacityInNumberOfProducts(4);
		myProducer.setNoProductsPerProduction(5);
		myProducer.setMaxCapacityInNumberOfProducts(20);
		myProducer.produce();
	}
	
	@Test(expected = ProductOverflowException.class)
	public void consumerShouldThrowOverflowExceptionIfCapacityIsExceededAfterTwoSteps() throws Exception {
		consumer.setMaxCapacityInNumberOfProducts(9);
		myProducer.setNoProductsPerProduction(5);
		myProducer.setMaxCapacityInNumberOfProducts(20);
		myProducer.produce();
		myProducer.produce();
	}
	
	@Test
	@Ignore
	public void consumerShouldStoreRightNumberOfProducts() throws Exception {
		int noProductsPerProduction = 5;
		int maxNoStorableProductions = 4;
		consumer.setMaxCapacityInNumberOfProducts(maxNoStorableProductions * noProductsPerProduction);
		myProducer.setNoProductsPerProduction(noProductsPerProduction);
		myProducer.setMaxCapacityInNumberOfProducts(10);

		for (int i = 0; i < maxNoStorableProductions; i+=1) {
			myProducer.produce();
			assertEquals((i+1)*noProductsPerProduction, consumer.getProducts().size());
		}
	}
	
	@Test
	@Ignore
	public void consumerShouldStoreRightProduct() throws Exception {
		consumer.setMaxCapacityInNumberOfProducts(10);
		myProducer.setNoProductsPerProduction(1);
		myProducer.setMaxCapacityInNumberOfProducts(1);
		myProducer.produce();
		assertEquals(product, consumer.getProducts().get(0));
	}
	
	private class MyProducer extends Producer {
		private static final long serialVersionUID = -3580826899438968330L;
		public MyProducer() {
			super(2, new Position(0,1,Direction.EAST));
		}
	
		@Override
		public void take() throws ProductOverflowException {
			followers.get(0).take();
		}
	}
}