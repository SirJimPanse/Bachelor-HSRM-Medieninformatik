package CamptureTheFlag.logik.entities;

import java.util.List;

import CamptureTheFlag.logik.models.Inventar;

/**
 * Charaktäre besitzen ein Inventar und eine Hand
 * 
 * @author shard001
 */
public abstract class Charakter extends GameObject {
	
	private static final long serialVersionUID = -7443328076876697101L;
	protected Feld aktFeld;
	protected Inventar inv;
	protected Object interaktionMit;
	protected Gegenstand hand;
	
	public Charakter(String name, String img, String beschreibung) {
		super(name, img, beschreibung);
		this.inv = new Inventar();
	}
	
	public void setAktFeld(Feld aktFeld) {
		this.aktFeld = aktFeld;
	}
	public Feld getAktFeld() {
		return this.aktFeld;
	}

	public Gegenstand getHand() {
		return this.hand;
	}

	public void setHand(Gegenstand g) {
		this.hand = g;
	}

	public List<Gegenstand> getInventoryVorrat() {
		return this.inv.getVorrat();
	}

	
	/**
	 * NUR für unittests
	 */
	public void setInteraktionMit(Object interaktionMit) {
		this.interaktionMit = interaktionMit;
	}
	
}