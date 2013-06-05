package CamptureTheFlag.logik.entities.gegenstaende;

import CamptureTheFlag.logik.entities.Gegenstand;

/**
 * Scheine sind wichtig für Story
 * NPC können dem Spieler über Ihre Dialoge Scheine geben
 * 
 * Ein Bestimmter Schein führt zum Erreichen des Spielziels
 * 
 * @author shard001
 */
public class Schein extends Gegenstand {

	private static final long serialVersionUID = -8311207867186568873L;

	public Schein(String name, String img, String beschreibung) {
		super(name, img, beschreibung);
	}

	@Override
	public Object interagiere(Object o) {
		return null;
	}
}