package CamptureTheFlag.logik.entities.gegenstaende;


/**
 * Mit Schlüsseln werden verschlossene Eingänge geöffnet.
 * 
 * Es können mehrere Schlüssel (mit gleicher Nummer) einen/mehere
 * Eingänge öffnen
 * 
 * @author shard001
 */
public class Schluessel extends Item {
	
	private static final long serialVersionUID = -103027833732358239L;
	
	private int nummer;

	public Schluessel(String name, String img, String beschreibung, int nummer) {
		super(name, img, beschreibung);
		this.nummer = nummer;
	}

	public int getNummer() {
		return this.nummer;
	}

	@Override
	public Object interagiere(Object o) {
		return null;
	}	

}