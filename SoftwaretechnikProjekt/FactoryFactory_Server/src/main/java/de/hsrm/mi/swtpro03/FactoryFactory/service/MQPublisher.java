package de.hsrm.mi.swtpro03.FactoryFactory.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import de.hsrm.mi.swtpro03.FactoryFactory.util.IMessagePublisher;

public final class MQPublisher implements IMessagePublisher{

    private Connection connection;
    private static final String BROKERURL = "tcp://localhost:61616";
    private static Logger logger = Logger.getLogger("MQPublisher");
    
    private MQPublisher(){
		BrokerService broker = new BrokerService();
		try {
			broker.addConnector("tcp://0.0.0.0:61616");
			broker.start();
			broker.waitUntilStarted();
		} catch (Exception e) {
			logger.log(Level.INFO, "startActiveMQ - " + e.getMessage());
		}
    	ActiveMQConnectionFactory mqFactory = new ActiveMQConnectionFactory(BROKERURL);
		try {
			connection = mqFactory.createConnection();
		} catch (JMSException e) {
			e.printStackTrace();
		}
    }

	/* (non-Javadoc)
	 * @see de.hsrm.mi.swtpro03.FactoryFactory.service.IMessagePublisher#sendMessage(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMessage(String publisher, String topic, String text, String action) {
						
		Session session = null;
		MessageProducer producer = null;
		try {
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Topic connectionType = session.createTopic(topic);
			producer = session.createProducer(connectionType);
			
	    	TextMessage tm = session.createTextMessage();
	    	tm.setStringProperty("Action", action);
	    	tm.setStringProperty("Publisher", publisher);
	    	tm.setText(text);
	    	producer.send(tm);
		} catch (JMSException e) {
			logger.log(Level.INFO, "SendMQMessage " + e.getMessage());
		}
		finally {
			closeSession(session, producer);
		}
	}

	private void closeSession(Session session, MessageProducer producer){
		try {
			producer.close();
			session.close();
		} catch (JMSException e) {
			logger.log(Level.INFO, "closeSession " + e.getMessage());
		} catch (NullPointerException e) {
			logger.log(Level.INFO, "closeSession " + e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see de.hsrm.mi.swtpro03.FactoryFactory.service.IMessagePublisher#shutdown()
	 */
	@Override
	public void shutdown(){
		try {
			connection.close();
		} catch (JMSException e) {
			logger.log(Level.INFO, "shutdown " + e.getMessage());
		}
	}
}
