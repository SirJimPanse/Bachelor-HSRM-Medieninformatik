package de.hsrm.mi.swt.specs.sparschwein;

import org.concordion.integration.junit3.ConcordionTestCase;

import de.hsrm.mi.swt.SchweinUeberzogenException;
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
// Nach JUnit-Lauf von "EntsparenTest" Projekt "Refresh", um Ordner sichtbar zu machen
///////////////////////////////////////////////////////////////////////

public class EntsparenTest extends ConcordionTestCase {
	private Sparschwein waldi = new SparschweinImpl();

	
	public void spende(String spenderName, String betrag) {
		waldi.spare(spenderName, Integer.parseInt(betrag));
	}

	public boolean entspare(String betrag) {
		try {
			waldi.entspare(Integer.parseInt(betrag));
		} catch (SchweinUeberzogenException e) {
			return false;
		}
		return true;
	}
	
	public int getGuthaben() {
		return waldi.getGuthaben();
	}
}
