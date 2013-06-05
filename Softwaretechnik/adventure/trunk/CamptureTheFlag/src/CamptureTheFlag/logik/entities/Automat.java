package CamptureTheFlag.logik.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Mit dem Automat kann interagiert werden
 * er spuckt einen zufälligen Gegenstand aus seinem Vorrat aus
 * 
 * @author shard001
 */
public class Automat extends Objekt implements Serializable {
	
	private static final long serialVersionUID = -2728836634946039408L;
	private static Logger log = Logger.getLogger(Spieler.class.getName());
	
	private List<Gegenstand> vorrat;

	public Automat(String name, String img, String beschreibung, Feld aktFeld, List<Gegenstand> g) {
		super(name, img, beschreibung, aktFeld);
		this.vorrat = g;
	}

	/**
	 * wählt einen zufälligen Gegenstand aus dem Vorraut aus
	 * gibt diesen zurück und löscht ihn aus dem Vorrat
	 * 
	 * @return 		ein zufälliger Gegenstand aus dem Vorrat
	 */
	@Override
	public Gegenstand interagiere(Object o) {
		log.log(Level.INFO, "Benutze Automat");
		
		try {
			int i = new Random().nextInt(this.vorrat.size());
			Gegenstand ret = this.vorrat.get(i);
			this.vorrat.remove(i);
			return ret;
		} catch (Exception e) {
			log.log(Level.INFO, "Automat ist leer");
			return null;
		}
		
	}
}