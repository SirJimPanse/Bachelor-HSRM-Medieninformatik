package CamptureTheFlag.logik.entities;

/**
 * Objekte können zwar auf Feldern platziert werden
 * aber nicht in ein Inventar aufgenommen werden
 * 
 * @author shard001
 */
public abstract class Objekt extends GameObject {
	
	private static final long serialVersionUID = 5412468490717638720L;
	private Feld aktFeld;

	public Objekt(String name, String img, String beschreibung, Feld aktFeld) {
		super(name, img, beschreibung);
		this.aktFeld = aktFeld;
	}

	public Feld getAktFeld() {
		return this.aktFeld;
	}

}