package CamptureTheFlag.unittests.repo;

import java.util.List;

import org.junit.Test;

import CamptureTheFlag.config.GameObjects;
import CamptureTheFlag.config.NPCs;
import CamptureTheFlag.darstellung.TastaturHandler;
import CamptureTheFlag.logik.GameC;
import CamptureTheFlag.logik.entities.Campus;
import CamptureTheFlag.logik.entities.Eingang;
import CamptureTheFlag.logik.entities.Feld;
import CamptureTheFlag.logik.entities.GameObject;
import CamptureTheFlag.logik.entities.Ingame;
import CamptureTheFlag.logik.entities.NPC;
import CamptureTheFlag.logik.entities.Raum;
import CamptureTheFlag.logik.entities.Spieler;
import CamptureTheFlag.logik.entities.gegenstaende.Schein;
import CamptureTheFlag.logik.inits.CampusFactory;
import CamptureTheFlag.logik.inits.DialogFactory;
import CamptureTheFlag.logik.inits.GameFactory;
import CamptureTheFlag.logik.inits.GameRepository;
import CamptureTheFlag.logik.models.Dialog;
import CamptureTheFlag.logik.models.Richtung;

public class Serialisierung {

	@Test
	public void speichern() {
		Ingame spiel = new Ingame(GameFactory.VERSION, null);
		GameRepository.serialize(spiel);
	}
	
	@Test
	public void speichernLaden() {
		Spieler hans = new Spieler("Hans", null, "Hans guckt gern in die Luft", Richtung.NORD);
		Campus campus = CampusFactory.getCampusFromCTFMAP("./src/CamptureTheFlag/config/room_story.ppm");
		Ingame spiel = new Ingame(GameFactory.VERSION, hans, campus);
		String pfad = GameRepository.serialize(spiel);
		GameRepository.deserialize(pfad);
	}
	
	@Test
	public void speichernLadenSpieler() {
		TastaturHandler t = new TastaturHandler();
		GameC c = new GameC(t);
		Spieler hans = new Spieler("Hans", null, "Hans guckt gern in die Luft", Richtung.NORD);
		c.addPropertyChangeListener(hans);
		Campus campus = CampusFactory.getCampusFromCTFMAP("./src/CamptureTheFlag/config/room_story.ppm");
		Ingame spiel = new Ingame(GameFactory.VERSION, hans, campus);
		String pfad = GameRepository.serialize(spiel);
		GameRepository.deserialize(pfad);
	}
	
	@Test
	public void speichernLadenSpielerCampus() {
		TastaturHandler t = new TastaturHandler();
		GameC c = new GameC(t);
		Spieler hans = new Spieler("Hans", null, "Hans guckt gern in die Luft", Richtung.NORD);
		c.addPropertyChangeListener(hans);
		Campus campus = CampusFactory.getCampusFromCTFMAP("./src/CamptureTheFlag/config/room_story.ppm");
		hans.setAktFeld(campus.getStartFeld());
		Ingame spiel = new Ingame(GameFactory.VERSION, hans, campus);
		String pfad = GameRepository.serialize(spiel);
		GameRepository.deserialize(pfad);
	}
	
	@Test
	public void speichernLadenAlles() {
		TastaturHandler t = new TastaturHandler();
		GameC c = new GameC(t);
		Spieler hans = new Spieler("Hans", null, "Hans guckt gern in die Luft", Richtung.NORD);
		c.addPropertyChangeListener(hans);
		Campus campus = CampusFactory.getCampusFromCTFMAP("./src/CamptureTheFlag/config/room_story.ppm");
		hans.setAktFeld(campus.getStartFeld());
		
		List<Dialog> dialoge = DialogFactory.getDialoge();
		List<NPC> npcs = NPCs.getNPCs();
		List<GameObject> gos  = GameObjects.getGameObjects();
		
		Raum pruefung = campus.getRaeume().get(0);
		Raum besenkammer = campus.getRaeume().get(1);
		Raum flur = campus.getRaeume().get(2);
		
		Feld flur1 = flur.getFelder().get(2);
		Feld flur2 = flur.getFelder().get(4);
		Feld flur3 = flur.getFelder().get(5);		
		Feld besenkammer1 = besenkammer.getFelder().get(2);		
		Feld pruefung1 = pruefung.getFelder().get(6);
		Feld pruefung2 = pruefung.getFelder().get(3);
		
		((Eingang) flur.getFelder().get(1).getObjects().get(0)).setNummer(3);
		((Eingang) flur.getFelder().get(7).getObjects().get(0)).setNummer(4);
			
		NPC homer = npcs.get(0);
		NPC lisa = npcs.get(1);
		NPC berdux = npcs.get(2);
		NPC barth = npcs.get(3);
		NPC weitz = npcs.get(4);
		
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
		
		flur1.addGameObject(gos.get(0));
		flur2.addGameObject(gos.get(1));
		besenkammer1.addGameObject(gos.get(2));
		besenkammer1.addGameObject(gos.get(3));
		
		Ingame spiel = new Ingame(GameFactory.VERSION, hans, campus);
		spiel.setSpielziel(new Schein("Schein ADS", null, "Erfolgreich die Prüfung ADS bestanden."));
		String pfad = GameRepository.serialize(spiel);
		
		GameRepository.deserialize(pfad);
	}
}
