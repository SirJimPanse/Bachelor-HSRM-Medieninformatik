package de.hsrm.mi.swtpro03.FactoryFactory.exception;

/** Eine AreaPositioningException wird geworfen, wenn es eine Ausnahme
 *  im Zusammenhang mit Positionen von Machines auf der Area gab. **/
public class AreaPositioningException extends Exception {

	private static final long serialVersionUID = 349275491935050663L;
	private static final String MSG = "Fehler beim Zugriff auf diese Position. Indices falsch?";

	public AreaPositioningException() { super(MSG); }
	public AreaPositioningException(String arg0) { 	super(arg0); }
	public AreaPositioningException(Throwable arg0) { super(arg0); }
	public AreaPositioningException(String arg0, Throwable arg1) { 	super(arg0, arg1); }
}
