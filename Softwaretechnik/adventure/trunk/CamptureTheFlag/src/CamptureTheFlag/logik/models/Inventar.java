package CamptureTheFlag.logik.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import CamptureTheFlag.logik.entities.Gegenstand;

/**
 * Das Inventar für Charaktäre
 * Es können bis zu INVENTARSIZE Gegenstände abgelegt werden
 * 
 * @author shard001
 */
public class Inventar implements Serializable {

	private static final long serialVersionUID = -3637377642788134512L;
	private static Logger log = Logger.getLogger(Inventar.class.getName());
	
	private final int INVENTARSIZE = 9;
	private List<Gegenstand> vorrat;

	// kann leer oder mit fertiger Liste instanziiert werden
	public Inventar() {
		this(new ArrayList<Gegenstand>());
	}
	public Inventar(List<Gegenstand> g) {
		this.vorrat = g;
	}

	/**
	 * Gibt den Gegenstand an der Stelle i zurück und löscht ihn aus dem Inventar
	 * 
	 * @param i		Index des Gegenstands
	 * @return		Der herausgenommene Gegenstand
	 */
	public Gegenstand get(Integer i) {
		try {
			Gegenstand ret = this.vorrat.get(i);
			this.vorrat.remove(ret);
			return ret;
		} catch (IndexOutOfBoundsException e) {
			log.log(Level.WARNING, "Index nicht möglich");
			return null;
		}
	}

	/**
	 * Legt einen Gegenstand von der Hand des Spielers im Inventar ab, wenn noch Platz vorhanden ist
	 * 
	 * @param g		Der Gegenstand in der Hand
	 * @return		true, wenn erfolgreich / false, wenn kein Platz vorhanden ist
	 */
	public Boolean put(Gegenstand g) {
		if ( this.vorrat.size() < INVENTARSIZE ) {
			this.vorrat.add(g);
			return true;
		} else {
			return false;
		}
	}
	
	public List<Gegenstand> getVorrat() {
		return vorrat;
	}

}