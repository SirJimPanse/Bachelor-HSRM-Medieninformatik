package CamptureTheFlag.logik.entities.gegenstaende;

import CamptureTheFlag.logik.entities.Gegenstand;

/**
 * Neben Scheinen werden Items benötigt, 
 * um gewöhnliche Gegenstände zu unterscheiden
 * 
 * @author shard001
 */
public abstract class Item extends Gegenstand {

	private static final long serialVersionUID = -3250863483599475100L;

	public Item(String name, String img, String beschreibung) {
		super(name, img, beschreibung);
	}
}