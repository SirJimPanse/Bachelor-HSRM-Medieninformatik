package CamptureTheFlag.logik;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import CamptureTheFlag.darstellung.Fenster;
import CamptureTheFlag.darstellung.Pausemenu;
import CamptureTheFlag.darstellung.Sichtbereich;
import CamptureTheFlag.darstellung.SpielerV;
import CamptureTheFlag.darstellung.TastaturHandler;
import CamptureTheFlag.darstellung.TastaturHandler.Events;
import CamptureTheFlag.logik.inits.GameFactory;
import CamptureTheFlag.logik.inits.GameRepository;
import CamptureTheFlag.logik.models.Richtung;
import CamptureTheFlag.logik.entities.Feld;
import CamptureTheFlag.logik.entities.Ingame;

//TODO Dass GameC die Fenster hier referenziert ist schon böse ne.. denn das liegt ne Schicht drüber und die Fenster sollten den GameC referenzieren..
/**
 * Hauptbestandteil der Logik
 * Koordiniert das erstellen der Darstellung
 * Erstellt neue Spiele / Läd gespeicherte Spiele
 * 
 * @author shard001
 */
public class GameC implements PropertyChangeListener, Runnable {

	/**
	 * unterscheidet den aktuellen Zustand des Spiels
	 */
	public enum Zustand {
		HAUPTMENU, PAUSEMENU, OPTIONSMENU, INGAME, LADEMENU, GEWONNEN;
	}

	private static Logger log = Logger.getLogger(GameC.class.getName());

	private Zustand zustand;
	protected TastaturHandler keyHandler; // protected wegen GameCTest
	public Fenster fenster; //TODO wieder auf privat setzten (nur zum testen geändert)
	public Ingame game; //TODO wieder auf privat setzten (nur zum testen geändert)
	private PropertyChangeSupport notify;

	/**
	 * erstellt alle nötigen Elemente bis zum Hauptmenu
	 * alles weitere wird dann von neuesSpiel() oder spielLaden() gesteuert
	 * via createWorldAndPanels()
	 * 
	 * @param t		Ein TastaturHandler
	 */
	public GameC(TastaturHandler t) {
		this.keyHandler = t;
		this.notify = new PropertyChangeSupport(this);
		this.keyHandler.addPropertyChangeListener(this);
		
		log.log(Level.CONFIG, "Starte Game Controller");

		/*
		 * Reihenfolge zur Instanziierung (wichtig für Listener und
		 * Konstruktoren): Nummer gibt die Anzahl der möglichen Instanzen an
		 * (n-Mal)
		 * 
		 * 1 TastaturHandler()
		 * 1 GameC(TastaturHandler)
		 * 1 Fenster(GameC, TastaturHandler)
		 * 
		 * Alle Felder erstellen (Builder) n Feld(boolean gesperrt)
		 * 4 Wände berechnen - mit feld.setWaende(Map<Richtung, Boolean>) einstellen
		 * 4 Nachbarn berechnen - mit feld.setNachbarn(Map<Richtung, Feld>) einstellen
		 * 
		 * Alle GameObjects erstellen (Builder)
		 * n Gegenstand
		 * 1 Spielziel(Schein) erstellen
		 * n Eingang(Feld) - mit eigenem Feld und dem dahinter, evtl. Schlüssel-Nummer
		 * n Automat(List<Gegenstand>)
		 * n Dialog(Gegenstand) - mit evl. Geschenk n Erwiderung(Gegenstand, Dialog)
		 * 	zuvor erstellte Dialoge mit dialog.setErwiderung(List<Erwiderung>) füllen
		 * n NPC(Dialog)
		 * 	zuvor erstellte Felder mit feld.setSlots(List<GameObject>) füllen
		 * 
		 * 1 Spieler(Feld) - mit Start-Feld
		 * 1 Ingame(Schein) - mit Spielziel
		 * 
		 * 1 Hauptmenu(GameC)
		 * 1 Pausemenu(GameC)
		 * 1 Sichtbereich(Spieler)
		 * 1 SpielerV(Spieler, Ingame)
		 */

		this.fenster = new Fenster(this, this.keyHandler);
	}
	
	/**
	 * Durch starten der run Methode kommt man ins Hauptmenu - es läuft noch kein Spiel.
	 * Wird ein neues Spiel gestartet oder ein altes geladen, erstellen die Factories alles 
	 * weitere.
	 */
	@Override
	public void run() {
		log.log(Level.CONFIG, "Starte Programm");
		this.setZustand(Zustand.HAUPTMENU); // starte im Hauptmenu
	}

	/**
	 * ändert den Spielzustand
	 */
	public void setZustand(Zustand z) {
		this.notify.firePropertyChange(Events.ZUSTAND.toString(), this.zustand, z);
		this.zustand = z;
	}
	
	public Zustand getZustand() {
		return zustand;
	}

	/**
	 * wird nach neuesSpiel() oder spielLaden() aufgerufen
	 * erstellt via Builder alle Elemente der Welt
	 * erstellt alle JPanels udn übergibt sie an Fenster
	 * 
	 * protected wegen GameCTest
	 */
	protected void createPanels() {
		this.fenster.setPausemenu(new Pausemenu(this, this.fenster));
		this.fenster.setSichtBereich(new Sichtbereich(this.game.getSpieler(), this.game));
		this.fenster.setSpielerV(new SpielerV(this.game.getSpieler(), this.fenster));
		
		this.setZustand(Zustand.INGAME);
	}

	/**
	 * startet ein neues Spiel Objekte werden anhand der GameFactory erstellt
	 */
	public void neuesSpiel() {
		// objekte via factory erstellen
		this.game = GameFactory.getGameInstance(this.keyHandler, this);
		this.createPanels();
	}

	/**
	 * läd ein gespeichertes Spiel Objekte werden durch das GameRepository
	 * 
	 * @param s
	 */
	public void spielLaden(String filename) {
		// objekte via repository erstellen
		this.game = GameRepository.deserialize(filename);
		Feld aktPos = this.game.getSpieler().getAktFeld();
		Richtung aktRichtung = this.game.getSpieler().getBlick();
		this.game.getSpieler().setAktFeld(this.game.getCampus().getStartFeld()); //TODO: ganz schön hacky hier..  spieler muss erst auf startpos
		this.game.getSpieler().setBlick(Richtung.NORD);                          //      gesetzt werden, damit alle FeldVs erstellt werden können,
		this.keyHandler.addPropertyChangeListener(this.game.getSpieler());       //      dann auf ursprüngliche Position und Blickrichtung zurück
		this.createPanels();                                                     //      gesetzt werden, um dann ein neu Zeichnen zu veranlassen
//		this.game.getSpieler().setAktFeld(aktPos);                               //      indem einmal links, dann rechts gegangen wird;
//		this.game.getSpieler().setBlick(aktRichtung);                            //      Grund ist der draw() Aufruf im Konstruktor von FeldV
//		this.keyHandler.getEventFromKeyCode(this.keyHandler.getKeyCodeFromEvent(this.keyHandler.KEY_BEWEGEN_LINKS));
//		this.keyHandler.getEventFromKeyCode(this.keyHandler.getKeyCodeFromEvent(this.keyHandler.KEY_BEWEGEN_RECHTS));
	}

	/**
	 * speichert ein Spiel Das Ingame-Objekt wird serialisiert auf der
	 * Festplatte abgelegt
	 */
	public void spielSpeichern() { GameRepository.serialize(this.game); }
	
	/**
	 * @return		true, wenn ein Spiel läuft
	 */
	public boolean isGameRunning() {
		return (this.game != null) ? true : false;
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

	// durch Anmeldung nur für Events.MENU
	public void propertyChange(PropertyChangeEvent arg0) {
		log.log(Level.INFO, "got event '" + arg0.getPropertyName() + "' old: '"	+ arg0.getOldValue() + "' new: '" + arg0.getNewValue() + "'");

		// für gameC oder durchleiten?
		switch (Events.valueOf(arg0.getPropertyName())) {
		case MENU:
			// ESC gedrückt
			switch (this.zustand) {
			case INGAME:
				this.setZustand(Zustand.PAUSEMENU);
				break;
			case PAUSEMENU:
				this.setZustand(Zustand.INGAME);
				break;
			}
			break;
		
		// durchreichen, wenn INGAME
		default:
			if (this.zustand == Zustand.INGAME) {
				this.notify.firePropertyChange(arg0);
			}
			break;
		}
	}
}