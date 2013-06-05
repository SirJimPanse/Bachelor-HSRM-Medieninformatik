package CamptureTheFlag.unittests.logik;

import org.concordion.integration.junit3.ConcordionTestCase;
import CamptureTheFlag.darstellung.TastaturHandler;
import CamptureTheFlag.logik.GameC;
import CamptureTheFlag.logik.GameCTest;
import CamptureTheFlag.logik.entities.Feld;
import CamptureTheFlag.logik.entities.GameObject;
import CamptureTheFlag.logik.entities.Gegenstand;
import CamptureTheFlag.logik.entities.NPC;
import CamptureTheFlag.logik.entities.Spieler;
import CamptureTheFlag.logik.models.Dialog;
import CamptureTheFlag.logik.models.Erwiderung;
import CamptureTheFlag.logik.models.Richtung;

/**
 * JUnittest mit Concordion
 * Erstellt eine HTML-Seite mit textueller Beschreibung des Spielablaufs und -verhaltens.
 * Prüft dabei mit Hilfe der Spiele-Logik, ob alles korrekt ausgeführt wird.
 * 
 * @author shard001
 */
public class logikTest extends ConcordionTestCase {
	
	TastaturHandler keyHandler = new TastaturHandler();
	GameC controller = new GameCTest(keyHandler); // mit GameCTest
	
	public String getAktFeld() {
		controller.neuesSpiel();
		return controller.game.getSpieler().getAktFeld().toString().substring(0, 10);
	}
	
	public String getSpielerName() {
		controller.neuesSpiel();
		return controller.game.getSpieler().getName().toString(); 
	}
	
	public String interagiereFeld() {
		controller.neuesSpiel();
		
		String ret = "0 abbrechen\n";
		int i = 1;
		for (GameObject o : controller.game.getSpieler().getAktFeld().getObjects()) {
			ret += i++ + " " + o.getName();
			if (o.getBeschreibung() != null) {
				ret += " (" + o.getBeschreibung() + ")";
			}
			ret += "\n";
		}
		
		return ret;
	}
	
	public String interagiereMitEingang(String num) {
		int n = Integer.parseInt(num)-1;
		
		controller.neuesSpiel();
		Spieler spieler = controller.game.getSpieler();
		spieler.setAktFeld((Feld) spieler.getAktFeld().getObjects().get(n).interagiere(spieler));
		return spieler.getAktFeld().toString().substring(0, 10);
	}
	
	public String interagiereMitGegenstand(String num) {
		int n = Integer.parseInt(num)-1;
		
		controller.neuesSpiel();
		Spieler spieler = controller.game.getSpieler();
		spieler.setHand((Gegenstand) spieler.getAktFeld().getObjects().get(n));
		
		String ret = spieler.getHand().getName();
		if (spieler.getHand().getBeschreibung() != null) {
			ret += " (" + spieler.getHand().getBeschreibung() + ")";
		}
		
		return ret;
	}
	
	public String getGegenstandAndShowInventar(String num) {
		int n = Integer.parseInt(num)-1;
		
		controller.neuesSpiel();
		Spieler spieler = controller.game.getSpieler();
		spieler.setHand((Gegenstand) spieler.getAktFeld().getObjects().get(n));
		
		keyHandler.getEventFromKeyCode(78); // AUFNEHMEN drücken
		
		String ret = "";
		int i = 1;
		for (Gegenstand o : spieler.getInventoryVorrat()) {
			ret += i++ + " " + o.getName();
			if (o.getBeschreibung() != null) {
				ret += " (" + o.getBeschreibung() + ")";
			}
			ret += "\n";
		}
		
		return ret;
	}
	
	public String getGegenstandAndShowHand(String num) {
		getGegenstandAndShowInventar(num);
		Spieler spieler = controller.game.getSpieler();
		return ( spieler.getHand() == null ) ? "leer" : "voll";
	}
	
	public String getGegenstandFromInventar(String num) {
		getGegenstandAndShowInventar(num);
		Spieler spieler = controller.game.getSpieler();
		
		keyHandler.getEventFromKeyCode(49); // 1 drücken, nun ist Gegenstand in der Hand
		
		String ret = spieler.getHand().getName();
		if (spieler.getHand().getBeschreibung() != null) {
			ret += " (" + spieler.getHand().getBeschreibung() + ")";
		}
		
		return ret;
	}
	
	public String dropGegenstandAufFeld(String num) {
		int eingangNr = 0;
		
		int n = Integer.parseInt(num)-1;
		
		controller.neuesSpiel();
		Spieler spieler = controller.game.getSpieler();
		spieler.setHand((Gegenstand) spieler.getAktFeld().getObjects().get(n)); // in hand
		
		// benutze Eingang, werde auf nächstes Feld gesetzt
		// steht nun auf Feld #89
		spieler.setAktFeld((Feld) spieler.getAktFeld().getObjects().get(eingangNr).interagiere(spieler));
		
		keyHandler.getEventFromKeyCode(32); // ABLEGEN drücken
		
		String ret = "0 abbrechen\n";
		int i = 1;
		for (GameObject o : spieler.getAktFeld().getObjects()) {
			ret += i++ + " " + o.getName();
			if (o.getBeschreibung() != null) {
				ret += " (" + o.getBeschreibung() + ")";
			}
			ret += "\n";
		}
		
		return ret;		
	}
	
	public String interagiereMitNPC(String num) {
		int n = Integer.parseInt(num)-1;
		
		controller.neuesSpiel();
		Spieler spieler = controller.game.getSpieler();
		
		// spreche NPC an
		NPC npc = (NPC) spieler.getAktFeld().getObjects().get(n);
		npc.interagiere(spieler);
		Dialog d = npc.getAktDialog();
		
		String ret = d.getText() + "\n\n";		
		
		ret += "0 abbrechen\n";
		int i = 1;
		for (Erwiderung e : d.getErwiderungen()) {
			ret += i++ + " " + e.getText() + "\n";
		}
		
		return ret;
	}
	
	public String interagiereMitNPCWeiter(String num) {
		String NpcNr = "4";
		controller.neuesSpiel();
		Spieler spieler = controller.game.getSpieler();
		
		// spreche NPC an
		int n = Integer.parseInt(NpcNr)-1;
		NPC npc = (NPC) spieler.getAktFeld().getObjects().get(n);
		npc.interagiere(spieler);
		spieler.setInteraktionMit(npc);
		
		keyHandler.getEventFromKeyCode(50); // 2 drücken, nun wird der folgedialog gewählt
		
		Dialog d = npc.getAktDialog();
		String ret = d.getText() + "\n\n";		
		
		ret += "0 abbrechen\n";
		if ( d.getErwiderungen() != null) {
			int i = 1;
			for (Erwiderung e : d.getErwiderungen()) {
				ret += i++ + " " + e.getText() + "\n";
			}
		}
		
		return ret;
	}
	
	public String getSpielerBlick() {
		controller.neuesSpiel();
		Spieler spieler = controller.game.getSpieler();
		return spieler.getBlick().toString();
	}
	
	public String dreheSpielerLinks() {
		controller.neuesSpiel();
		
		keyHandler.getEventFromKeyCode(81); // Q drücken
		
		Spieler spieler = controller.game.getSpieler();
		return spieler.getBlick().toString();
	}
	
	public String dreheSpielerRechts() {
		controller.neuesSpiel();
		
		keyHandler.getEventFromKeyCode(81); // Q drücken
		keyHandler.getEventFromKeyCode(69); // E drücken
		
		Spieler spieler = controller.game.getSpieler();
		return spieler.getBlick().toString();
	}
	
	public String laufe(String richtung) {
		Richtung r = Richtung.valueOf(richtung);
		controller.neuesSpiel();
		Spieler spieler = controller.game.getSpieler();
		return ( spieler.getAktFeld().isNachbarBegehbar(r) ) ? "frei" : "Wand";
	}

	public String nutzeEingangUndLaufe(String richtung) {
		int EingangNr = 0;
		Richtung r = Richtung.valueOf(richtung);
		
		controller.neuesSpiel();
		Spieler spieler = controller.game.getSpieler();
		spieler.setAktFeld((Feld) spieler.getAktFeld().getObjects().get(EingangNr).interagiere(spieler));
		
		/*
		 * spieler hat eingang verwendet und steht nun auf feld dahinter
		 * nun nach WEST laufen
		 */
		
		spieler.setAktFeld(spieler.getAktFeld().getNachbar(r));
		
		return spieler.getAktFeld().toString().substring(0, 10);
	}
}
