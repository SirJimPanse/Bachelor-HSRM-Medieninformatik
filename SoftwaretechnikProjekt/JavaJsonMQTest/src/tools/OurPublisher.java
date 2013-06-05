package tools;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OurPublisher{

	// JMS-relevante Objekte
    private Connection connection;
    private Session session;
    private MessageProducer publisher;
    private String destinationName;
    private ConnectionType connectionType;

    // ActiceMQ Server
    final private String BROKERURL = "tcp://localhost:61616";
    
    public OurPublisher(String destinationName, ConnectionType connectionType){
    	this.destinationName = destinationName;
    	this.connectionType = connectionType;
    }
    
    public void sendASchnorchel(Object o) throws JMSException, JsonProcessingException {
    	createConnection(connectionType);
        
    	String s = serialize(o);
    	TextMessage tm = session.createTextMessage(s);
    	tm.setStringProperty("Typ", "Schnorchel");
    	publisher.send(tm);

        session.close();
        connection.close();
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
        publisher = session.createProducer(connectionType);
        publisher.setDeliveryMode(DeliveryMode.PERSISTENT);
	}


	private String serialize(Object o) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(o);
	}

    
}
