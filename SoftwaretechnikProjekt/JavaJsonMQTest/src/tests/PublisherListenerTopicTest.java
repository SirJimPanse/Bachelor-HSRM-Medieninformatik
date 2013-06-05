package tests;


import static org.junit.Assert.assertEquals;

import javax.jms.JMSException;

import org.junit.Before;
import org.junit.Test;

import tools.ConnectionType;
import tools.OurListener;
import tools.OurPublisher;
import util.IObservable;
import util.IObserver;

import com.fasterxml.jackson.core.JsonProcessingException;

import entities.Hobby;
import entities.Schnorchel;

public class PublisherListenerTopicTest {
	private Object sendedObject;
	private OurListener listener;
	private OurPublisher publisher;
	private String topicName;
	private ObserverTestClass obTest;
	
	@Before
	public void setUp(){
		obTest = new ObserverTestClass();
		topicName= "factory";
		publisher = new OurPublisher(topicName,ConnectionType.TOPIC);
		listener  = new OurListener(topicName, ConnectionType.TOPIC);
		listener.addObserver(obTest);
		listener.setUse_embedded_broker(true);
		sendedObject = createASchnorchel();
	}

	@Test
	public void listenerShouldBeStartedByTopic() throws JMSException {
		listener.start();		
	}
	
	@Test
	public void objectShouldBeSendedByTopic() throws JsonProcessingException, JMSException {
		listener.start();	
		publisher.sendASchnorchel(sendedObject);
	}
	
	@Test
	public void objectShouldBeReceivedByTopic() throws JsonProcessingException, JMSException, InterruptedException {
		listener.start();	
		publisher.sendASchnorchel(sendedObject);
		//TODO: equals des Objects nutzen anstatt toString() überprüfen
		Thread.sleep(50);
		String send = sendedObject.toString();
		String recv = obTest.getReceivedObject().toString();
		assertEquals(send,recv);
	}
	
	private Schnorchel createASchnorchel(){
		// Datenobjekt fuellen
		Schnorchel schnorch = new Schnorchel();
		schnorch.setAlter(17);
		schnorch.setName("Heliodor");
		schnorch.getHobbies().add(new Hobby("reiten", false));
		schnorch.getHobbies().add(new Hobby("schwimmen", false));
		schnorch.getHobbies().add(new Hobby("lesen", true));
		schnorch.getFaehigkeiten().put("huepfen", true);
		schnorch.getFaehigkeiten().put("weidomieren", false);
		schnorch.getFaehigkeiten().put("folloppen", true);

		System.out.println("Ausgangsobjekt:");
		System.out.println(schnorch);
		
		return schnorch;
	}


	
	private class ObserverTestClass implements IObserver{
		public boolean updateCalled;
		public Object receivedObject;
		
		public ObserverTestClass(){
			 updateCalled = false;
			 receivedObject = null;
		}
		
		public boolean isUpdateCalled() {
			return updateCalled;
		}
		
		public Object getReceivedObject() {
			return receivedObject;
		}
		
		@Override
		public void update(IObservable ob) {
			updateCalled = true;
		}
		
		@Override
		public void update(IObservable ob, Object o) {
			updateCalled = true;
			receivedObject = o;
		}
		
	}
	
}
