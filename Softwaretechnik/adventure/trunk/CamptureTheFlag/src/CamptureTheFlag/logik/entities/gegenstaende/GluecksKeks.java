package CamptureTheFlag.logik.entities.gegenstaende;


import java.io.Serializable;

/**
 * Ein Glückskeks
 * Bereichert den Spieler mit einer Weisheit
 * 
 * @author shard001
 */
public class GluecksKeks extends Item implements Serializable {
	
	private static final long serialVersionUID = 5159795763238205474L;

	public GluecksKeks(String name, String img, String beschreibung) {
		super(name, img, beschreibung);
	}

	@Override
	public Object interagiere(Object o) {
		return null;
	}

}