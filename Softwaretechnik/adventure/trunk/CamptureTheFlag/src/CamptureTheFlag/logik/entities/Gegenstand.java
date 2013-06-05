package CamptureTheFlag.logik.entities;

/**
 * Ein Gegenstand kann im Inventar eines Charakters abgelegt werden
 * 
 * @author shard001
 */
public abstract class Gegenstand extends GameObject {
	
	private static final long serialVersionUID = 6656632965922965728L;

	public Gegenstand(String name, String img, String beschreibung) {
		super(name, img, beschreibung);
	}
}