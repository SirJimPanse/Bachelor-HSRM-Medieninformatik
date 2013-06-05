package de.hsrm.mi.swtpro03.FactoryFactory.service;

import de.hsrm.mi.swtpro03.FactoryFactory.util.IMessagePublisher;

public class NOPMessagePublisher implements IMessagePublisher {

	@Override
	public void sendMessage(String publisher, String topic, String text,
			String action) {

	}

	@Override
	public void shutdown() {

	}

}
