package de.hsrm.mi.swt.begruesser;

/**
 * Die Begruesserklasse fuer Informatikerparties (haha).
 * 
 * @author $Author$
 * @version $Date$
 * @see $Id$
 */
public class Begruesser {
	private int zaehler;

	public String begruesse(String name) {
		return "Als Gast Nr. "+ ++zaehler +" begruessen wir "+ (endsWithVocal(name) ? "Frau " : "Herr ") + name;
	}
	
	public int getZaehler() {
		return zaehler;
	}
	
	private boolean endsWithVocal(String s) {
		return s.endsWith("a") || s.endsWith("e") || s.endsWith("i") || s.endsWith("o") || s.endsWith("u");
	}
}
