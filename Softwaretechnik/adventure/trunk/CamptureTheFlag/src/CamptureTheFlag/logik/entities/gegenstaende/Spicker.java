package CamptureTheFlag.logik.entities.gegenstaende;

import CamptureTheFlag.logik.entities.Gegenstand;

/**
 * Spicker können als Tribut einer Erwiderung eingesetzt werden
 * und helfen z.B. bei Prüfungungen zum Erlangen eines Scheins
 * 
 * @author shard001
 */
public class Spicker extends Gegenstand {

	private static final long serialVersionUID = -6548978219596186809L;

	public Spicker(String name, String img, String beschreibung) {
		super(name, img, beschreibung);
	}

	@Override
	public Object interagiere(Object o) {
		return null;
	}
}