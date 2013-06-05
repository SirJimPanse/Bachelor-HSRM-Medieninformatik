package CamptureTheFlag.logik.entities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import CamptureTheFlag.darstellung.TastaturHandler.Events;
import CamptureTheFlag.logik.entities.gegenstaende.Schein;

/**
 * Verwaltet ein gesamtes Spiel
 * 
 * Soll gespeichert werden, wird das Ingame-Objekt serialisiert
 * Es beinhaltet daher Referenzen zu allen Objekten im Spiel
 * 
 * Reagiert auf Änderungen der Hand des Spielers um das Erreichen
 * des Spielziels zu bemerken und der Darstellung bescheid zu geben
 * 
 * @author shard001
 */
public class Ingame implements PropertyChangeListener, Serializable, Iterable<GameObject> {
	
	private static final long serialVersionUID = 7837798742734919480L;
	private static Logger log = Logger.getLogger(Spieler.class.getName());
	
	private String  spielversion;
	private Spieler spieler;
	private Campus  campus;
	private Schein  spielziel; // wurde dieser Schein erhalten, ist das Spiel gewonnen
	public  boolean gewonnen;
	private transient PropertyChangeSupport notify;
	
	private Ingame() {
		this.gewonnen = false;
		this.notify = new PropertyChangeSupport(this);
	}
	
	private Ingame(String spielversion) {
		this();
		this.spielversion = spielversion;
	}
	
	/** 
	 * Minimaler von außen erreichbarer Konstruktor.
	 * @param spielversion
	 * @param s
	 */
	public Ingame(String spielversion, Spieler s) {
		this(spielversion);
		if (s != null) {
			this.spieler = s;
			this.spieler.addPropertyChangeListener(Events.INHAND.toString(), this);
		}
	}
	
	public Ingame(String spielversion, Spieler s, Campus campus) {
		this(spielversion, s);
		this.campus = campus;
	}

	/**
	 * @param spielziel		wird auf getName() geprüft
	 */
	public void setSpielziel(Schein spielziel) {
		this.spielziel = spielziel;
	}
	
	public boolean isGewonnen() {
		return gewonnen;
	}
	
	public Spieler getSpieler() {
		return spieler;
	}
	
	public void setNotify(PropertyChangeSupport pcs) {
		this.notify = pcs;
	}
	
	public Campus getCampus() {
		return this.campus;
	}

	public String getSpielversion() {
		return this.spielversion;
	}
	
	// PropertyChangeSupport
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.notify.addPropertyChangeListener(propertyName, listener);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.notify.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.notify.removePropertyChangeListener(propertyName, listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.notify.removePropertyChangeListener(listener);
	}
	
	
	// hört auf INHAND von Spieler
	public void propertyChange(PropertyChangeEvent arg0) {
		log.log(Level.INFO, "got event '" + arg0.getPropertyName() + "' old: '" + arg0.getOldValue() + "' new: '" + arg0.getNewValue() + "'");
	
		/*
		 * das Spiel ist gewonnen, wenn der Spieler das spielziel in seine Hand aufnimmt
		 * und getName() des Scheins gleich dem von spielziel ist
		 */
		if (!this.gewonnen && arg0.getNewValue() instanceof Schein && ((Schein) arg0.getNewValue()).getName().equals(this.spielziel.getName())) {
			log.log(Level.INFO, "Spielziel erreicht - Gewonnen!");
			this.notify.firePropertyChange(Events.GEWONNEN.toString(), false, true);
			this.gewonnen = true;
		}
	}

	@Override
	public Iterator<GameObject> iterator() {
		List<GameObject> gos = new ArrayList<GameObject>();
		List<Raum> raeume = this.campus.getRaeume();
		List<Feld> felder = null;

		for (Raum raum : raeume) {
			felder = raum.getFelder();
			for (Feld f : felder)
				gos.addAll(f.getObjects());
		}
		
		return gos.iterator();
	}
}