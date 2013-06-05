package tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import util.IObservable;
import util.IObserver;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import entities.Schnorchel;

public class OurListener implements MessageListener, IObservable {

	// JMS-relevante Objekte
	private Connection connection;
	private Session session;
	private String destinationName;
	private ConnectionType connectionType;
	private List<IObserver> observers;
	
	// Kontaktinfos ActiveMQ Messagebroker - ggf. anpassen
	private String BROKERURL = "tcp://localhost:61616";
	
	// eingebetteten oder separaten Broker nutzen
	// (ggf. bei letzterem ggf. BROKERURL anpassen)
	boolean use_embedded_broker = false;
	
	
  /* Der ActiveMQ-Server kann auch in eine Applikation eingebettet
   * und so automatisch mit dieser gestartet werden.
   */
	BrokerService eingebetteterMessageBroker = null;
	
	public OurListener(String destinationName, ConnectionType connectionType) {
		this.observers = new ArrayList<IObserver>();
		this.destinationName = destinationName;
		this.connectionType = connectionType;
		
		if (use_embedded_broker) {
			eingebetteterMessageBroker = new BrokerService();
			try {
				eingebetteterMessageBroker.addConnector("tcp://0.0.0.0:61616");
				eingebetteterMessageBroker.start();				
				BROKERURL = "vm://localhost";
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	public void start() throws JMSException{
		createConnection(connectionType);
		System.out.println("Nachrichten auf "+BROKERURL+" über " + destinationName + " werden abgehört");
	}
	
	private void createConnection(ConnectionType ct) throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKERURL);
		Destination connectionType;
		connection = factory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		switch(ct){
		case QUEUE:
			connectionType = session.createQueue(destinationName);
			break;
		case TOPIC:
			connectionType = session.createTopic(destinationName);
			break;
		default:
			throw new JMSException("No Connection Type chosen");
		}
			
		MessageConsumer consumer = session.createConsumer(connectionType);
		consumer.setMessageListener(this);
		connection.start();
	}
	
	private Object deserialize(String s, Class<?> c) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Object o = mapper.readValue(s, c);
		return o;
	}

	public void onMessage(Message message) {	
		System.out.println("\n\n*** Nachricht empfangen: " + message.toString());
		
		try {
			checkTextMessage((TextMessage)message);
		} catch (JMSException e) {
			System.out.println("Error checkTextMessage " + e.getMessage());
		} catch (JsonParseException e) {
			System.out.println("Error JsonParse " + e.getMessage());
		} catch (JsonMappingException e) {
			System.out.println("Error JsonMapping " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error IOException " + e.getMessage());
		}
	}

	private void checkTextMessage(TextMessage message) throws JMSException, JsonParseException, JsonMappingException, IOException {
		String propertyName = "Typ";
		String propertyValue = (String) message.getObjectProperty(propertyName);
		System.out.println("Name: " + propertyName + " Value: " + propertyValue);
		if(propertyValue.equals("Text")){
			
		}
		else if(propertyValue.equals("Schnorchel")){
			Schnorchel schnorchi = (Schnorchel)deserialize(message.getText(), Schnorchel.class);
			notifyObservers(schnorchi);
		}
	}

	@SuppressWarnings("unchecked")
	private void getAllProperties(TextMessage message) throws JMSException {
		Enumeration<String> properties = (Enumeration<String>)message.getPropertyNames();
		while (properties.hasMoreElements()) {
			String propertyName = properties.nextElement();
			String propertyValue = (String)message.getObjectProperty(propertyName);
			System.out.println("Name:  " + propertyName + "   Value:  " + propertyValue);
		}
	}

	public boolean isUse_embedded_broker() {
		return use_embedded_broker;
	}

	public void setUse_embedded_broker(boolean use_embedded_broker) {
		this.use_embedded_broker = use_embedded_broker;
	}

	@Override
	public void addObserver(IObserver ob) {
		if(!observers.contains(ob)){
			observers.add(ob);
		}
	}

	@Override
	public void removeObserver(IObserver ob) {
		if(observers.contains(ob)){
			observers.remove(ob);
		}
	}

	@Override
	public void notifyObservers() {
		for(IObserver ob : observers){
			ob.update(this);
		}
	}

	@Override
	public void notifyObservers(Object o) {
		for(IObserver ob : observers){
			ob.update(this, o);
		}
	}

}

