package CamptureTheFlag.logik.entities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import CamptureTheFlag.darstellung.TastaturHandler.Events;
import CamptureTheFlag.logik.models.Richtung;

/**
 * Auf einem Feld kann sich der Spieler befinden
 * Es können bis zu SLOTSSIZE GameObjects abgelegt werden
 * 
 * Der Spieler kann Gegenstände von seiner Hand auf dem Feld ablegen
 * Ebenso kann er mit allen GameObjects vom Feld interagieren
 * 
 * @author shard001
 */
public class Feld implements Serializable {
	
	private static final long serialVersionUID = 5615551880458305051L;
	private static Logger log = Logger.getLogger(Feld.class.getName());

	private final int SLOTSSIZE = 9;
	private List<GameObject> slots;
	private Map<Richtung, Boolean> waende; // ist in dieser Richtung eine Wand?
	private Map<Richtung, Feld> nachbarn;
	private transient PropertyChangeSupport notify;
	private CamptureTheFlag.logik.P<Integer,Integer> pos;
	
	public Feld() {
		this(null);
	}
	
	public Feld(CamptureTheFlag.logik.P<Integer,Integer> pos) {
		this.pos = pos;
		this.slots = new ArrayList<GameObject>();
		this.notify = new PropertyChangeSupport(this);
	}
	
	/**
	 * @param waende	Boolean-Map für die 4 Wände des Felds
	 */
	public void setWaende(Map<Richtung, Boolean> waende) {
		this.waende = waende;
	}
	
	/**
	 * @param nachbarn	Feld-Map für die 4 Nachbar-Felder
	 */
	public void setNachbarn(Map<Richtung, Feld> nachbarn) {
		this.nachbarn = nachbarn;
	}
	
	public void setNotify(PropertyChangeSupport pcs) {
		this.notify = pcs;
	}
	
	/**
	 * Legt einen einzelnen Gegenstand auf dem Feld ab
	 * 
	 * @param go		Der einzelne Gegenstand
	 */
	public void addGameObject(GameObject go) {
		List<GameObject> gos = new ArrayList<GameObject>();
		gos.add(go);
		addGameObjects(gos);
	}
	
	/**
	 * Die Slots eines Felds werden im Builder im ZWEITEN Durchlauf gefüllt
	 * (Das Feld braucht Gegenstände, die Gegenstände aber evtl. das Feld ~ Reihenfolge beim Erstellen)
	 * 
	 * @param gos		Liste von GameObjects, welche auf dem Feld liegen
	 */
	public void addGameObjects(List<GameObject> gos) {
		// fülle slots mit neuen GameObjects
		for (GameObject go : gos) {
			if (this.slots.size() < SLOTSSIZE+1) { // nur, falls noch Platz frei ist / rest abschneiden
				this.slots.add(go);
			}
		}
		
		this.notify.firePropertyChange(Events.ABLEGEN.toString(), false, true); // benachrichtige FeldV
	}

	/**
	 * @param r		Richtung
	 * @return		Das Feld, welches in dieser Richtung angrenzt
	 */
	public Feld getNachbar(Richtung r) {
		return this.nachbarn.get(r);
	}
	
	public CamptureTheFlag.logik.P<Integer,Integer> getPos() {
		return this.pos;
	}

	/**
	 * lässt einen Gegenstand aufnehmen
	 * 
	 * @param i		Index der Liste aller Gegenstände {@link getObjects()}
	 * @return		Der gewünschte Gegenstand
	 */
	public Gegenstand take(Integer i) {
		try {
			Gegenstand ret = (Gegenstand) this.slots.get(i);
			this.slots.remove(ret); // löscht erstes Vorkommen dieses Gegenstands
			this.notify.firePropertyChange(Events.AUFNEHMEN.toString(), false, true); // benachrichtige FeldV
			return ret;
		} catch (IndexOutOfBoundsException e) {
			log.log(Level.WARNING, "Index nicht möglich");
			return null;
		}
	}

	/**
	 * lässt einen Gegenstand auf dem Feld ablegen
	 * nur, wenn noch Platz vorhanden ist
	 * 
	 * @param go	Der abzulegende Gegenstand
	 * @return		Ablegen erfolgreich?
	 */
	public Boolean drop(Gegenstand go) {
		try {
			if ( this.slots.size() < SLOTSSIZE ) {
				this.slots.add(go);
				this.notify.firePropertyChange(Events.ABLEGEN.toString(), false, true); // benachrichtige FeldV
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			// gar kein Gegenstand übergeben
			return false;
		}
	}
	
	/**
	 * @return		Liste aller GameObject auf dem Feld (auch NPC)
	 */
	public List<GameObject> getObjects() {
		return this.slots;
	}

	/**
	 * In der gesuchten Richtung darf keine Wand liegen und es muss ein Feld angrenzen
	 * 
	 * @param r		zu prüfende Richtung
	 * @return		true, wenn das Feld in dieser Richtung begehbar ist
	 */
	public Boolean isNachbarBegehbar(Richtung r) {
		try {
			return (!this.waende.get(r) && this.nachbarn.get(r) != null) ? true : false;
		} catch (NullPointerException e) {
			log.log(Level.INFO, "Kein angrenzendes Feld oder keine Wände zum prüfen vorhanden");
			return false;
		}
	}
	
	public boolean contains(Object o) { return this.slots.contains(o); }

	
	public boolean hatNachbarn() {
		return this.nachbarn != null && this.nachbarn.size() != 0;
	}
	
	// PropertyChangeSupport
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		notify.addPropertyChangeListener(propertyName, listener);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		notify.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		notify.removePropertyChangeListener(propertyName, listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		notify.removePropertyChangeListener(listener);
	}
	
	@Override
	public String toString() {
		return "Feld " + this.pos + " mit " + ((this.slots != null && this.slots.size() != 0) ? this.slots.toString() : "keinen Elementen.");
	}
}