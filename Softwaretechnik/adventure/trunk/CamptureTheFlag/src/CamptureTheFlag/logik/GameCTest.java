package CamptureTheFlag.logik;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import CamptureTheFlag.darstellung.TastaturHandler;
import CamptureTheFlag.logik.entities.Campus;
import CamptureTheFlag.logik.entities.Feld;
import CamptureTheFlag.logik.entities.GameObject;
import CamptureTheFlag.logik.entities.Ingame;
import CamptureTheFlag.logik.entities.NPC;
import CamptureTheFlag.logik.entities.Spieler;
import CamptureTheFlag.logik.entities.gegenstaende.GluecksKeks;
import CamptureTheFlag.logik.entities.gegenstaende.Schein;
import CamptureTheFlag.logik.entities.gegenstaende.Schluessel;
import CamptureTheFlag.logik.inits.CampusFactory;
import CamptureTheFlag.logik.models.Dialog;
import CamptureTheFlag.logik.models.Erwiderung;
import CamptureTheFlag.logik.models.Richtung;

/**
 * Überladung des GameC für UnitTest
 * 
 * @author shard001
 */
public class GameCTest extends GameC {
	
	private final static String VERSION = "0.1TEST";

	public GameCTest(TastaturHandler t) {
		super(t);
	}
	
	/** Ein Listenkonstruktor mit varargs (machts bisschen lesbarer) **/
	public static <T> List<T> l(T... array) { return new ArrayList<T>(Arrays.asList(array)); }
	
	/**
	 * überschrieben für UnitTest
	 * erstellt Test-Szenario
	 */
	public void neuesSpiel() {
		
		List<NPC> npcs = new ArrayList<NPC>();
		npcs.add(new NPC("Homer", null, "Fettwanst"));
		npcs.add(new NPC("Lisa", null, "kleiner Schlauberger"));
		
		List<GameObject> gos = new ArrayList<GameObject>();
		gos.add(new Schluessel("Goldener Schlüssel", null, "öffnet Raum XY", 2));
		gos.add(new GluecksKeks("Keks 1", null, "Weise, du musst sein."));
		
		Dialog lisa = new Dialog("Hallo wie gehts dir?");
		lisa.setErwiderungen(l(
			new Erwiderung("Schlecht :(", 
				new Dialog("Oh wieso?",	l(
								new Erwiderung("Weil!", new Dialog("Oh..")),
								new Erwiderung("Hörst du nicht zu?",lisa),
								new Erwiderung("Ach uninteressant..", new Dialog("Naja dann nicht.."))
				))
	        ),
			new Erwiderung("Naja..",new Dialog("Cool!")),
	        new Erwiderung("Gut!",  new Dialog("Cool!"))
		));

		
		Campus campus = CampusFactory.getCampusFromCTFMAP("./src/CamptureTheFlag/config/room_storyTest.ppm");
		Spieler spieler = new Spieler("Frettchen", "Jana.jpg", "Der Muster-Student", Richtung.NORD);
		this.keyHandler.addPropertyChangeListener(spieler);
		spieler.setAktFeld(campus.getStartFeld().getNachbar(Richtung.NORD));

		Feld f1 = campus.getStartFeld().getNachbar(Richtung.NORD);

		NPC n1 = npcs.get(0);
		n1.setAktFeld(f1);
		n1.setStartDialog(lisa);
		
		gos.add(n1);
		f1.addGameObjects(gos);
		
		Ingame ingame = new Ingame(VERSION, spieler, campus);
		ingame.setSpielziel(new Schein("Schein ADS", null, "Erfolgreich die Prüfung ADS bestanden."));
		
		// wieder wie GameC
		this.game = ingame;
		this.createPanels();
	}

}
