package CamptureTheFlag.darstellung;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import CamptureTheFlag.logik.models.Drehrichtung;
import CamptureTheFlag.logik.models.Richtung;

/**
 * Beinhaltet das Mapping zwischen Tastendruck und ausgelöstem Event
 * 
 * @author shard001
 *
 */
public class TastaturHandler {

	private static Logger log = Logger.getLogger(TastaturHandler.class.getName());
	
	/*
	 * Listener meldet sich für den propertyName an (Events vom folgendem Typ mit .toString()) und erhält dann nur passende Nachrichten
	 */	
	public enum Events {
		//TODO Events in Spec beschreiben
		MENU, BEWEGEN, BEWEGT, ZUSTAND, INTERAGIERE, NUMMER, ABLEGEN, AUFNEHMEN, INHAND, GEWONNEN;
	}
	
	/**
	 * Den Tasten sollen nur vordefinierte Events zugewiesen werden können
	 * und nicht jedes beliebige PropertyChangeEvent
	 * 
	 * Dazu werden die hier im Anschluss erstellten TastaturEvents verwendet
	 * 
	 * @author shard001
	 */
	public class TastaturEvent implements Serializable {
		private static final long serialVersionUID = -8849544111642028732L;
		private PropertyChangeEvent event;
		private TastaturEvent(PropertyChangeEvent e) {
			this.event = e;
		}
		public PropertyChangeEvent getEvent() {	return event; }
	}
	
	public final TastaturEvent KEY_MENU = new TastaturEvent(new PropertyChangeEvent(this, Events.MENU.toString(), false, true));
	public final TastaturEvent KEY_BEWEGEN_LINKS = new TastaturEvent(new PropertyChangeEvent(this, Events.BEWEGEN.toString(), false, Drehrichtung.LINKS));
	public final TastaturEvent KEY_BEWEGEN_RECHTS = new TastaturEvent(new PropertyChangeEvent(this, Events.BEWEGEN.toString(), false, Drehrichtung.RECHTS));
	public final TastaturEvent KEY_BEWEGEN_NORD = new TastaturEvent(new PropertyChangeEvent(this, Events.BEWEGEN.toString(), false, Richtung.NORD));
	public final TastaturEvent KEY_BEWEGEN_WEST = new TastaturEvent(new PropertyChangeEvent(this, Events.BEWEGEN.toString(), false, Richtung.WEST));
	public final TastaturEvent KEY_BEWEGEN_SUED = new TastaturEvent(new PropertyChangeEvent(this, Events.BEWEGEN.toString(), false, Richtung.SUED));
	public final TastaturEvent KEY_BEWEGEN_OST = new TastaturEvent(new PropertyChangeEvent(this, Events.BEWEGEN.toString(), false, Richtung.OST));
	public final TastaturEvent KEY_INTERAGIERE = new TastaturEvent(new PropertyChangeEvent(this, Events.INTERAGIERE.toString(), false, true));
	public final TastaturEvent KEY_NUM_0 = new TastaturEvent(new PropertyChangeEvent(this, Events.NUMMER.toString(), false, 0));
	public final TastaturEvent KEY_NUM_1 = new TastaturEvent(new PropertyChangeEvent(this, Events.NUMMER.toString(), false, 1));
	public final TastaturEvent KEY_NUM_2 = new TastaturEvent(new PropertyChangeEvent(this, Events.NUMMER.toString(), false, 2));
	public final TastaturEvent KEY_NUM_3 = new TastaturEvent(new PropertyChangeEvent(this, Events.NUMMER.toString(), false, 3));
	public final TastaturEvent KEY_NUM_4 = new TastaturEvent(new PropertyChangeEvent(this, Events.NUMMER.toString(), false, 4));
	public final TastaturEvent KEY_NUM_5 = new TastaturEvent(new PropertyChangeEvent(this, Events.NUMMER.toString(), false, 5));
	public final TastaturEvent KEY_NUM_6 = new TastaturEvent(new PropertyChangeEvent(this, Events.NUMMER.toString(), false, 6));
	public final TastaturEvent KEY_NUM_7 = new TastaturEvent(new PropertyChangeEvent(this, Events.NUMMER.toString(), false, 7));
	public final TastaturEvent KEY_NUM_8 = new TastaturEvent(new PropertyChangeEvent(this, Events.NUMMER.toString(), false, 8));
	public final TastaturEvent KEY_NUM_9 = new TastaturEvent(new PropertyChangeEvent(this, Events.NUMMER.toString(), false, 9));
	public final TastaturEvent KEY_ABLEGEN = new TastaturEvent(new PropertyChangeEvent(this, Events.ABLEGEN.toString(), false, true));
	public final TastaturEvent KEY_AUFNEHMEN = new TastaturEvent(new PropertyChangeEvent(this, Events.AUFNEHMEN.toString(), false, true));
	
	private PropertyChangeSupport notify;
	private Map<Integer, PropertyChangeEvent> steuerungMap;
	private String mappingFile = "./src/CamptureTheFlag/config/keyMapping.conf";
	
	public TastaturHandler() {
		this.notify = new PropertyChangeSupport(this);
		
		/*
		 * Das KeyMapping liegt in einer Datei
		 * existiert diese Datei, wird das Mapping daraus verwendet
		 * wenn nicht, wird das default-Mapping hinein geschrieben
		 */
		if (new File(mappingFile).exists()) {
			this.steuerungMap = loadKeyMapFromFile();
		} else {
			// standard mapping erzeugen
			this.steuerungMap = new HashMap<Integer, PropertyChangeEvent>();
			this.setKeyForEvent(27, KEY_MENU); // ESC
			this.setKeyForEvent(81, KEY_BEWEGEN_LINKS); // Q
			this.setKeyForEvent(69, KEY_BEWEGEN_RECHTS); // E
			this.setKeyForEvent(87, KEY_BEWEGEN_NORD); // W
			this.setKeyForEvent(65, KEY_BEWEGEN_WEST); // A
			this.setKeyForEvent(83, KEY_BEWEGEN_SUED); // S
			this.setKeyForEvent(68, KEY_BEWEGEN_OST); // D
			this.setKeyForEvent(73, KEY_INTERAGIERE); // I
			this.setKeyForEvent(48, KEY_NUM_0); // 0
			this.setKeyForEvent(49, KEY_NUM_1); // 1
			this.setKeyForEvent(50, KEY_NUM_2); // 2
			this.setKeyForEvent(51, KEY_NUM_3); // 3
			this.setKeyForEvent(52, KEY_NUM_4); // 4
			this.setKeyForEvent(53, KEY_NUM_5); // 5
			this.setKeyForEvent(54, KEY_NUM_6); // 6
			this.setKeyForEvent(55, KEY_NUM_7); // 7
			this.setKeyForEvent(56, KEY_NUM_8); // 8
			this.setKeyForEvent(57, KEY_NUM_9); // 9
			this.setKeyForEvent(32, KEY_ABLEGEN); // space
			this.setKeyForEvent(78, KEY_AUFNEHMEN); // n
		}
	}
	
	/**
	 * Setzt den KeyCode für ein TastaturEvent
	 * Falls diesem Event schon eine Taste zugewiesen wurde, wird der Eintrag überschrieben
	 * 
	 * @param c		die zu drückende Taste (KeyCode)
	 * @param e		das auszulösende TastaturEvents
	 */
	public void setKeyForEvent(Integer c, TastaturEvent event) {
		try {
			this.steuerungMap.put(c, event.getEvent());
		} catch (Exception e) {
			log.log(Level.WARNING, "Tastenbelegung nicht möglich");
		}
		
		// Mapping-Datei aktualisieren
		writeMapToFile();
	}
	
	/**
	 * @param i		die gedrückte Taste (KeyCode)
	 * @return		das angeforderte PropertyChangeEvent
	 */
	public void getEventFromKeyCode(int i) {
		//TODO catch wieder reinnehmen, wenn alle exceptions bis hier abgefangen wurden
		try {
			this.notify.firePropertyChange(steuerungMap.get(i));
		} catch (Exception e) {
			log.log(Level.INFO, "Taste nicht gefunden");
		}
//		System.out.println("Steuerungsmap: " + steuerungMap.get(i));
//		this.notify.firePropertyChange(steuerungMap.get(i));
	}
	
	/**
	 * Geht alle möglichen KeyCodes durch, bis gesuchtes Event gefunden wird
	 * 
	 * @param event		Das gewünschte Event, für den der KeyCode ausgegeben werden soll
	 * @return			Der gefundene KeyCode
	 */
	public int getKeyCodeFromEvent(TastaturEvent event) {
		for(int i = 0; i<256; i++) {
			if ( this.steuerungMap.get(i) == event.getEvent()) {
				return i;
			}
		}
		return -1; // nicht gefunden
	}
	
	/**
	 * Liest aus einer bestehenden (serialisierten) Datei
	 * das KeyMapping aus
	 * 
	 * @param dateiPfadUndName		serialisierte Datei
	 * @return						Fertige steuerungMap
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, PropertyChangeEvent> loadKeyMapFromFile() {
		Map<Integer, PropertyChangeEvent> ret = null;
		
		try {
		    FileInputStream file = new FileInputStream(this.mappingFile);
		    ObjectInputStream o = new ObjectInputStream(file);
		    ret = (Map<Integer, PropertyChangeEvent>) o.readObject();
		    o.close();
		} catch (Exception e) { System.err.println(e); }
		
		return ret;
	}
	
	/**
	 * schreibt das aktuelle KeyMapping in die Mapping-Datei
	 */
	private void writeMapToFile() {
		try {
			FileOutputStream file = new FileOutputStream(this.mappingFile);
			ObjectOutputStream o = new ObjectOutputStream(file);
			o.writeObject(this.steuerungMap);
			o.close();
	    } catch (IOException e) {
	    	if (e instanceof NotSerializableException) {
	    		log.log(Level.WARNING, "Serialisierung fehlgeschlagen");
	    		log.log(Level.WARNING, e.getMessage());
	    		e.printStackTrace();
	    	} else {
	    		log.log(Level.WARNING, "Mapping-Datei schreiben fehlgeschlagen:");
	    		log.log(Level.WARNING, e.getMessage());
	    		e.printStackTrace();
	    	}
	    }
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
}