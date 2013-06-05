package CamptureTheFlag.logik.inits;

import java.util.List;
import java.util.Map;

import CamptureTheFlag.config.GameObjects;
import CamptureTheFlag.config.NPCs;
import CamptureTheFlag.darstellung.TastaturHandler;
import CamptureTheFlag.logik.GameC;
import CamptureTheFlag.logik.P;
import CamptureTheFlag.logik.entities.Campus;
import CamptureTheFlag.logik.entities.Eingang;
import CamptureTheFlag.logik.entities.Feld;
import CamptureTheFlag.logik.entities.GameObject;
import CamptureTheFlag.logik.entities.Ingame;
import CamptureTheFlag.logik.entities.NPC;
import CamptureTheFlag.logik.entities.Raum;
import CamptureTheFlag.logik.entities.Spieler;
import CamptureTheFlag.logik.entities.gegenstaende.Doughnut;
import CamptureTheFlag.logik.entities.gegenstaende.GluecksKeks;
import CamptureTheFlag.logik.entities.gegenstaende.Schein;
import CamptureTheFlag.logik.entities.gegenstaende.Spicker;
import CamptureTheFlag.logik.models.Dialog;
import CamptureTheFlag.logik.models.Richtung;

/**
 * Erstellt alle Elemente der Spielwelt.
 * Aktuell baut sie statisch eine einfache Story zusammen.
 * 
 * @author shard001, dgens001
 * @version 0.2
 */
public class GameFactory {

	public final static String VERSION = "01"; // ohne Punkt (Teil der Spec)

	/** 
	 *  Baut eine Ingameinstanz und gibt diese fertig spielbar zurück. Momentan sind viele Teile
	 *  hardcoded (NPCs, Dialoge, Objekte etc.), nur die Map ist konfigurierbar (war direkte 
	 *  Anforderung). Später könnte man die jeweiligen Teile aber z.B. in XML auslagern und die 
	 *  Factories diese dann parsen lassen.
	 *  
	 * @param t
	 * @return
	 */
	public static Ingame getGameInstance(TastaturHandler t, GameC c) {
		
		Ingame ingame = null;
		
		if (VERSION.equals("01")) {
			Campus campus = CampusFactory.getCampusFromCTFMAP("./src/CamptureTheFlag/config/room_story.ppm");
			
			// TODO Name, Blickrichtung usw. in Builder/Config
			Spieler spieler = new Spieler("Frettchen", "Jana.jpg", "Der Muster-Student", Richtung.NORD);
			c.addPropertyChangeListener(spieler); // spieler bei GameC anmelden
			spieler.setAktFeld(campus.getStartFeld());
			
			List<Dialog> dialoge = DialogFactory.getDialoge();
			List<NPC> npcs = NPCs.getNPCs();
			List<GameObject> gos  = GameObjects.getGameObjects();
			
	
			/*
			 * Simple Story für Test / Abgabe
			 * Siehe story.txt
			 * 
			 * Lässt sich beliebig erweitern / dynamisch erzeugen
			 */
			Raum pruefung = campus.getRaeume().get(0);
			Raum besenkammer = campus.getRaeume().get(1);
			Raum flur = campus.getRaeume().get(2);
			
			Feld flur1 = flur.getFelder().get(2);
			Feld flur2 = flur.getFelder().get(4);
			Feld flur3 = flur.getFelder().get(5);		
			Feld besenkammer1 = besenkammer.getFelder().get(2);		
			Feld pruefung1 = pruefung.getFelder().get(3);
			Feld pruefung2 = pruefung.getFelder().get(2);
			
			// Eingänge Schlüssel-Nummern zuweisen
			((Eingang) flur.getFelder().get(1).getObjects().get(0)).setNummer(3); // Eingang Besenkammer
			((Eingang) flur.getFelder().get(7).getObjects().get(0)).setNummer(4); // Eingang Prüfungsraum
				
			NPC homer = npcs.get(0);
			NPC lisa = npcs.get(1);
			NPC berdux = npcs.get(2);
			NPC barth = npcs.get(3);
			NPC weitz = npcs.get(4);
			
			// NPC platzieren und mit Dialog versehen
			homer.setAktFeld(flur2);
			flur2.addGameObject(homer);
			homer.setStartDialog(dialoge.get(1));
			
			lisa.setAktFeld(flur1);
			flur1.addGameObject(lisa);
			lisa.setStartDialog(dialoge.get(0));
			
			berdux.setAktFeld(flur3);
			flur3.addGameObject(berdux);
			berdux.setStartDialog(dialoge.get(2));
			
			barth.setAktFeld(pruefung1);
			pruefung1.addGameObject(barth);
			barth.setStartDialog(dialoge.get(3));
			
			weitz.setAktFeld(pruefung2);
			pruefung2.addGameObject(weitz);
			weitz.setStartDialog(dialoge.get(4));
			
			// Gegenstände auf Feldern verteilen
			flur1.addGameObject(gos.get(0)); // GluecksKeks
			flur2.addGameObject(gos.get(1)); // Doughnut
			besenkammer1.addGameObject(gos.get(2)); // Doughnut
			besenkammer1.addGameObject(gos.get(3)); // Spicker
			
			
			ingame = new Ingame(VERSION, spieler, campus);
			ingame.setSpielziel(new Schein("Schein Rechnernetze", "schein_netze.png", "Erfolgreich die Prüfung Rechnernetze bestanden."));
			
		} else if (VERSION.equals("02")) {
			
			Campus campus = CampusFactory.getCampusFromCTFMAP("./src/CamptureTheFlag/config/room_storyMI.ppm");
			Spieler spieler = new Spieler("Frettchen", "Jana.jpg", "Der Muster-Student", Richtung.NORD);
			c.addPropertyChangeListener(spieler);
			spieler.setAktFeld(campus.getStartFeld());
			
			List<Dialog> dialoge = DialogFactory.getDialoge();
			List<NPC> npcs = NPCs.getNPCs();
			
			Map<Integer,Feld> npcPos = campus.getPosNPCs();
			Map<Integer,List<Feld>> gegenstaendePos = campus.getPosGegenstaende();
			
			int schluesselNummer = 0;
			
			// Eingänge Schlüssel-Nummern zuweisen
			for (P<Raum,Raum> rr : campus.getVerbindungen().keySet()) {
				Eingang e = campus.getVerbindungen().get(rr);
				if (!e.getOffen())
					e.setNummer(schluesselNummer++); 
			}

//			((Eingang) flur.getFelder().get(1).getObjects().get(0)).setNummer(3); // Eingang Besenkammer
//			((Eingang) flur.getFelder().get(7).getObjects().get(0)).setNummer(4); // Eingang Prüfungsraum
			
			NPC homer = npcs.get(0);
			NPC lisa = npcs.get(1);
			NPC berdux = npcs.get(2);
			NPC barth = npcs.get(3);
			NPC weitz = npcs.get(4);
			
			homer.setStartDialog(dialoge.get(1));
			lisa.setStartDialog(dialoge.get(0));
			berdux.setStartDialog(dialoge.get(2));
			barth.setStartDialog(dialoge.get(3));
			weitz.setStartDialog(dialoge.get(4));
			
			npcPos.get(CampusFactory.HOMER).addGameObject(homer);
			npcPos.get(CampusFactory.LISA).addGameObject(lisa);
			npcPos.get(CampusFactory.BERDUX).addGameObject(berdux);
			npcPos.get(CampusFactory.BARTH).addGameObject(barth);
			npcPos.get(CampusFactory.WEITZ).addGameObject(weitz);
			
			for (Feld f : gegenstaendePos.get(CampusFactory.GLUECKSKEKS))
				f.addGameObject(new GluecksKeks("Keks", "glueckskeks.png", "Weise, du musst sein"));
			for (Feld f : gegenstaendePos.get(CampusFactory.DOUGHNUT))
				f.addGameObject(new Doughnut("Doughnut", "donut.png", "Nicht sofort essen"));
			for (Feld f : gegenstaendePos.get(CampusFactory.SPICKER))
				f.addGameObject(new Spicker("Spicker", "spicker.png", "Damit besteht man manche Prüfungen"));
					
			ingame = new Ingame(VERSION, spieler, campus);
			ingame.setSpielziel(new Schein("Schein Rechnernetze", "schein_netze.png", "Erfolgreich die Prüfung Rechnernetze bestanden."));
		}
		
		return ingame;
	}
}