package de.hsrm.mi.swtpro03.FactoryFactory.util;

public interface IMessagePublisher {

	public abstract void sendMessage(String publisher, String topic,
			String text, String action);

	public abstract void shutdown();

}