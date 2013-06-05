package CamptureTheFlag.logik.entities;

import CamptureTheFlag.logik.entities.gegenstaende.Schluessel;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Eingänge verbinden zwei Räume miteinander, die sonst durch eine Wand getrennt sind
 * Interagiert man mit ihnen geben sie das Feld dahinter preis
 * (soweit man den passenden Schlüssel hat)
 * 
 * @author shard001
 */
public class Eingang extends Objekt implements Serializable {
	
	private static final long serialVersionUID = -3303072049120421616L;
	private static Logger log = Logger.getLogger(Eingang.class.getName());
	
	private Feld dahinter;
	private Boolean offen;
	private Integer nummer;

	public Eingang(String name, String img, String beschreibung, Feld aktFeld, Feld dahinter, Boolean offen) {
		super("Eingang", "stargate.png", beschreibung, aktFeld);
		this.dahinter = dahinter;
		this.setOffen(offen);
	}
	
	/**
	 * öffnet oder schließt den Eingang
	 * 
	 * @param istZuOeffnen		soll der Eingang geöffnet oder geschlossen werden?
	 */
	public void setOffen(boolean istZuOeffnen) {
		if(istZuOeffnen) {
			this.offen = true;
			this.beschreibung = "offen";
		} else {
			this.offen = false;
			this.beschreibung = "geschlossen";
		}
	}

	/**
	 * interagiere() wird vom Spieler mit sich selbst aufgerufen (this)
	 * ist der Eingang verschlossen, wird die Hand des Spieler auf einen Schlüssel geprüft.
	 * 
	 * Ist der Eingang offen oder passt der Schlüssel, wird das dahinter liegende Feld entsperrt, zurückgegeben 
	 * und der Spieler weitergesetzt.
	 */
	@Override
	public Object interagiere(Object o) {
		try {
			// offen oder Schlüssel mit passender Nummer in Hand des Spielers?
			if (this.offen || ((Schluessel)((Spieler) o).getHand()).getNummer() == this.nummer ) {
				/*
				 * Eingang kann geöffnet / verwendet werden
				 * Der Eingang auf der gegenüberliegenden Seite muss ebenfalls geöffnet werden
				 * Der Spieler bekommt das Feld dahinter und setzt sich selbst weiter
				 */
				log.log(Level.INFO, "öffne Durchgang");
				this.setOffen(true);
				//TODO Eingang auf dem Feld dahinter holen verbessern (aktuell: wird an Stelle 0 erwartet)
				((Eingang) this.dahinter.getObjects().get(0)).setOffen(true); // Eingang auf Feld dahinter auch öffnen
				
				return this.dahinter;
			} else {
				log.log(Level.INFO, "Kein passender Schlüssel in der Hand");
				return null; // kein Durchlass
			}
		} catch(Exception e) {
			log.log(Level.WARNING, "Kein Schlüssel in der Hand");
			return null;
		}
	}

	public Feld getDahinter() {
		return dahinter;
	}
	
	public boolean getOffen() {
		return this.offen;
	}
	
	public void setDahinter(Feld f) {
		this.dahinter = f;
	}

	public void setNummer(Integer nummer) {
		this.nummer = nummer;
	}
	
}