package de.hsrm.mi.swt.specs.begruesser;

// $Id: BegruesserTest.java 5 2012-03-27 21:22:09Z wweitz $

import org.concordion.integration.junit3.ConcordionTestCase;

import de.hsrm.mi.swt.begruesser.Begruesser;

///////////////////////////////////////////////////////////////////////
// Hinweise:
// Diese Datei als normalen JUnit-Test ausfuehren.
// Ergebnis (HTML) wird in Temporärverzeichnis abgelegt (Linux: /tmp/concordion/...)
// Der genaue Pfad erscheint nach dem Testlauf in der Eclipse-Console.
//
// Tipp: HTML-Ausgabeverzeichnis in Projektordner verlegen. Dazu 
// JUnit-Run configuration anlegen, unter Reiter "Arguments", in VM arguments: 
// -Dconcordion.output.dir=${project_loc}/concordion-output  eintragen.
// Nach JUnit-Lauf von "BegruesserTest" Projekt "Refresh", um Ordner sichtbar zu machen
///////////////////////////////////////////////////////////////////////

public class BegruesserTest extends ConcordionTestCase {
	private Begruesser begr = new Begruesser();

	public String begruesse(String name) {
		return begr.begruesse(name);
	}
	
	public int getZaehler() {
		return begr.getZaehler();
	}


}
