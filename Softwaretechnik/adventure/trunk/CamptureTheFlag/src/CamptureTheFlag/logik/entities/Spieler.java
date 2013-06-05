package CamptureTheFlag.logik.entities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import CamptureTheFlag.logik.entities.Feld;
import CamptureTheFlag.darstellung.TastaturHandler.Events;
import CamptureTheFlag.logik.models.Drehrichtung;
import CamptureTheFlag.logik.models.Richtung;

/**
 * Zentrales Element der Spielmechanik
 * 
 * Der Spieler reagiert im INGAME-Zustand auf Eingaben der Tastatur
 * und bewegt sich dann, dreht sich, interagiert mit Feldern, NPC usw.
 * 
 * Bei jeder Statusänderung benachrichtigt er die Darstellung
 * 
 * @author shard001
 */
public class Spieler extends Charakter implements PropertyChangeListener {

	private static final long serialVersionUID = 2767205418221800992L;
	private static Logger log = Logger.getLogger(Spieler.class.getName());

	private Richtung blick;
	
	public Spieler(String name, String img, String beschreibung, Richtung blick) {
		super(name, img, beschreibung);
		this.blick = blick;
	}
	

	public Richtung getBlick() {
		return blick;
	}
	
	public void setBlick(Richtung r) {
		this.blick = r;
	}
	
	/**
	 * beendet eine Interaktion des Spielers
	 * Benachrichtigt Listener
	 */
	private void endeInteraktion() {
		if (this.interaktionMit != null) {
			this.notify.firePropertyChange(Events.INTERAGIERE.toString(), this.interaktionMit, null); // Interaktion beendet
		}
		this.interaktionMit = null;
	}
	
	/**
	 * Nimmt den übergebenen Gegenstand in die Hand des Spielers auf
	 * Benachrichtigt Listener
	 * 
	 * @param g		Der aufzunehmende Gegenstand
	 */
	private void inHandNehmen(Gegenstand g) {
		if (g != null && this.hand == null) { // überhaupt was zu nehmen? hand frei?
			Gegenstand alt = this.hand;
			this.hand = g; // Geschenk in Hand nehmen, danach mit AUFNEHMEN-Taste ins Inventar legen oder mit ABLEGEN auf Feld legen
			this.notify.firePropertyChange(Events.INHAND.toString(), alt, g); // Hand hat sich geändert
		} else {
			log.log(Level.WARNING, "Hand nicht leer oder kein Gegenstand aufzunehmen");
		}
	}
	
	/**
	 * Geht zum übergebenen Feld
	 * Benachrichtigt Listener
	 * 
	 * @param f		Das Feld, wo es hingehen soll
	 */
	private void geheZuFeld(Feld f) {
		Feld alt = this.aktFeld;
		Feld neu = f;
		this.aktFeld = neu;
		this.notify.firePropertyChange(Events.BEWEGT.toString(), alt, neu);
	}
	
	/**
	 * überschrieben
	 * Benachrichtigt Listener
	 */
	public void setHand(Gegenstand g) {
		Gegenstand alt = this.hand;
		this.hand = g;
		this.notify.firePropertyChange(Events.INHAND.toString(), alt, g);
	}
	
	// hört auf verschiedene Events
	public void propertyChange(PropertyChangeEvent arg0) {
		log.log(Level.INFO, "got event '" + arg0.getPropertyName() + "' old: '" + arg0.getOldValue() + "' new: '" + arg0.getNewValue() + "'");
		
		switch (Events.valueOf(arg0.getPropertyName())) {
		
			/*
			 * Drehrichtung - dreht den Spieler, benachrichtigt Events.BEWEGT 
			 * Richtung - setzt Spieler ein Feld weiter, falls möglich, benachrichtigt Events.BEWEGT
			 */
			case BEWEGEN:
				endeInteraktion(); // erstmal evtl. bestehende Interaktion abbrechen
				
				if (arg0.getNewValue() instanceof Drehrichtung) {
					// dreht Blickrichtung im Kreis (links-, rechtsrum)
					Richtung alt;
					Richtung neu;
					Drehrichtung gedrehtNach;
					switch ((Drehrichtung)arg0.getNewValue()) {
						case LINKS:
							alt = this.blick;
							neu = Richtung.values()[(this.blick.ordinal()+Richtung.values().length-1) % Richtung.values().length];
							gedrehtNach = Drehrichtung.LINKS;
							this.blick = neu;
							this.notify.firePropertyChange(Events.BEWEGT.toString(), alt, gedrehtNach);
							break;
						case RECHTS:
							alt = this.blick;
							neu = Richtung.values()[(this.blick.ordinal()+1) % Richtung.values().length];
							gedrehtNach = Drehrichtung.RECHTS;
							this.blick = neu;
							this.notify.firePropertyChange(Events.BEWEGT.toString(), alt, gedrehtNach);
							break;
					}
				} else if (arg0.getNewValue() instanceof Richtung) {
					/*
					 * frage angrenzendes Feld in angepasster Richtung (Karte ist nach NORD ausgerichtet), ob passierbar
					 * wenn passierbar, kann Spieler bewegt werden
					 */
					Richtung r = null; // berechnete Richtung (ausgehend von 4 Richtungen)
					switch ((Richtung)arg0.getNewValue()) {
						case NORD:
							r = this.blick; // immer dort, wo man hinschau					
							break;
						case SUED:
							r = Richtung.values()[(this.blick.ordinal()+2) % 4]; // entgegengesetzte Blickrichtung (2 mal rechts drehen)
							break;
						case WEST:
							r = Richtung.values()[(this.blick.ordinal()+3) % 4]; // links von Blickrichtung (3 mal rechts drehen)
							break;
						case OST:
							r = Richtung.values()[(this.blick.ordinal()+1) % 4]; // rechts von Blickrichtung (1 mal rechts drehen)
							break;
					}
					
					// nächstes Feld prüfen und Spieler ggf. weitersetzten
					if (r != null && this.aktFeld.isNachbarBegehbar(r)) {
						geheZuFeld(this.aktFeld.getNachbar(r));
					} else {
						log.log(Level.INFO, "Kein Feld erreichar");
					}
				}
				break;
				
			/*
			 * wird interagieren (I-Taste) gedrückt, wird zuerst getAktFeld().getObjects() angezeigt
			 * der Spieler muss erkennen können, ob interagiert wird, wenn danach eine Nummer gedrückt wird
			 * Dazu wird interaktionMit auf aktFeld gesetzt - ist dies der Fall wird eine Nummer erwartet
			 */
			case INTERAGIERE:
				if (this.interaktionMit == null) { // nur, wenn gerade keine interaktion läuft
					this.interaktionMit = this.aktFeld;
					this.notify.firePropertyChange(Events.INTERAGIERE.toString(), false, this.aktFeld); // benachrichtige SpielerV (Feld)
				}
				break;
			
			/*
			 * wird eine Nummer eingegeben und der Spieler befindet sich in einer Interaktion wird
			 * interagiere() mit dem Objekt am index der nummer in getAktFeld().getObjects() aufgerufen
			 * 
			 * befindet man sich im Dialog mit einem NPC wird die Erwiderung mit dieser Nummer gewählt
			 * 
			 * befindet sich der spieler im normalen modus, wird der Gegenstand mit der gedrückten Nummer aus dem Inventar in die Hand gelegt
			 */
			case NUMMER:
				if (this.interaktionMit == this.aktFeld) { // Feld ansehen?
					if ((Integer)arg0.getNewValue() != 0) { // nicht abbrechen?
						
						// Das GameObject (go) vom Feld holen, Listener benachrichtigen, interagiere() damit ausführen
						GameObject go = null;
						try {
							go = this.aktFeld.getObjects().get((Integer)arg0.getNewValue()-1); // 0 wäre abbruch
							this.notify.firePropertyChange(Events.INTERAGIERE.toString(), this.interaktionMit, go); // benachrichtige SpielerV (GameObject)
						} catch (IndexOutOfBoundsException e) {
							log.log(Level.WARNING, "Nummer nicht möglich");
						}
						
						if (go instanceof Gegenstand) { // ist das GameObject interagierbar oder direkt ein Gegenstand?
							inHandNehmen(this.aktFeld.take((Integer)arg0.getNewValue()-1));
							endeInteraktion();
						} else if (go instanceof Interagieren) { // es ist interagierbar, also inteagiere()
							this.interaktionMit = go;
							Object ret = ((GameObject) this.interaktionMit).interagiere(this);
							
							/*
							 * interagiert man mit einem Objekt (nicht mit NPC) kann ein Gegenstand erhalten werden
							 * danach ist die Interaktion beendet
							 */
							if (ret instanceof Gegenstand) {
								inHandNehmen((Gegenstand) ret);
								endeInteraktion();
							} else if (ret instanceof Feld) { // mit Eingang interagiert, gehe zu Feld dahinter
								geheZuFeld((Feld)ret);
								endeInteraktion();
							} else if (ret == null) { // z.B. bei verschlossenem Eingang
								endeInteraktion();
							}
						} // kein else, sonst exception
						
					} else {
						// abbrechen
						endeInteraktion();
					}
					
				} else if (this.interaktionMit instanceof NPC) { // Im Dialog?
					Object ret = ((NPC) this.interaktionMit).interagiere((Integer)arg0.getNewValue()); // NPC verwaltet Interaktion (Gespräch bei NPC) und evtl. Abbruch
					
					// Rückgabe von interagiere() verwalten
					if (ret == null) { // Dialog beendet?
						endeInteraktion();
					} else if (ret instanceof Gegenstand) { // Gegenstand erhalten?
						inHandNehmen((Gegenstand) ret);
						
						//TODO wenn Hand schon voll und Geschenk bekommen - was dann?
						//TODO wenn Inventar und Feld voll - evtl. zerstören?
						
					} else {
						// Nummer akzeptiert - neu zeichnen lassen (wegen evtl. geändertem Dialog)
						// ProperyChange macht nichts, wenn old und new gleich sind!
						this.notify.firePropertyChange(Events.INTERAGIERE.toString(), false, this.interaktionMit); // benachrichtige SpielerV (GameObject)
					}
				
				} else { // Gegenstand vom Inventar in die Hand
					inHandNehmen(this.inv.get((Integer)arg0.getNewValue()-1)); // 0 nicht möglich - dann passiert einfach nichts	
				}
				break;
				
			/*
			 * Der Gegenstand in der hand soll auf aktFeld abgelegt werden und die Hand wird frei
			 */
			case ABLEGEN:
				if (this.interaktionMit == null && this.hand != null) { // nur, wenn gerade keine interaktion läuft
					if (this.aktFeld.drop(this.hand)) {
						this.hand = null;
						this.notify.firePropertyChange(Events.ABLEGEN.toString(), this.hand, null); // benachrichtige SpielerV
					}
				} else {
					log.log(Level.INFO, "Erst Interaktion beenden oder Hand füllen");
				}
				break;
				
			/*
			 * Der Gegenstand in der Hand soll in das Inventar des Spielers aufgenommen werden
			 * Falls dies fehlschägt, muss niemand benachrichtigt werden
			 */
			case AUFNEHMEN:
				if (this.interaktionMit == null && this.hand != null) { // nur, wenn gerade keine interaktion läuft
					if (this.inv.put(this.hand)) {
						this.hand = null;
						this.notify.firePropertyChange(Events.AUFNEHMEN.toString(), null, this.hand); // benachrichtige SpielerV
					}
				} else {
					log.log(Level.INFO, "Erst Interaktion beenden oder Hand füllen");
				}
				break;
		}
	}
	
	public Feld getAktFeld(){
		return this.aktFeld;
	}
	
	// wird erst bei Multiplayer o.Ä. benötigt
	@Override
	public Object interagiere(Object o) {
		return null;
	}
}