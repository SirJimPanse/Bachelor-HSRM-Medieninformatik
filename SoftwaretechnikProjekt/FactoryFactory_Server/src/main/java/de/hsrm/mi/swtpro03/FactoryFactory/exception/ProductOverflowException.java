package de.hsrm.mi.swtpro03.FactoryFactory.exception;

public class ProductOverflowException extends Exception {
	private static final long serialVersionUID = 8335251357844448926L;

	public ProductOverflowException() {
		super("Not enough space for new products.");
	}

	public ProductOverflowException(String message) {
		super(message);
	}
}
