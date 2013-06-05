package de.hsrm.mi.swt.specs.sparschwein;

import org.concordion.integration.junit3.ConcordionTestCase;

import de.hsrm.mi.swt.Sparschwein;
import de.hsrm.mi.swt.SparschweinImpl;

///////////////////////////////////////////////////////////////////////
// Hinweise:
// Diese Datei als normalen JUnit-Test ausfuehren.
// Ergebnis (HTML) wird in Temporärverzeichnis abgelegt (Linux: /tmp/concordion/...)
// Der genaue Pfad erscheint nach dem Testlauf in der Eclipse-Console.
//
// Tipp: HTML-Ausgabeverzeichnis in Projektordner verlegen. Dazu 
// JUnit-Run configuration anlegen, unter Reiter "Arguments", in VM arguments: 
// -Dconcordion.output.dir=${project_loc}/concordion-output  eintragen.
// Nach JUnit-Lauf von "SparschweinTest" Projekt "Refresh", um Ordner sichtbar zu machen
///////////////////////////////////////////////////////////////////////

public class SparschweinTest extends ConcordionTestCase {
	private Sparschwein waldi = new SparschweinImpl();

	// Concordion gibt Strings herein - die müssen ggf. in die
	// richtigen Zieltypen (wie int) umgewandelt werden.
	
	public void spende(String spenderName, String betrag) {
		waldi.spare(spenderName, Integer.parseInt(betrag));
	}

	public void entspare(String betrag) {
		waldi.entspare(Integer.parseInt(betrag));
	}
	
	public int getGuthaben() {
		return waldi.getGuthaben();
	}

	public String getTopspender() {
		return waldi.getTopspender();
	}
	
	public int getTopspende() {
		return waldi.getTopspende();
	}
}
