package CamptureTheFlag.logik.entities.gegenstaende;


import java.io.Serializable;

/**
 * Ein Doughnut
 * wird für Dialoge verwendet
 * 
 * @author shard001
 */
public class Doughnut extends Item implements Serializable {
	
	private static final long serialVersionUID = 5159795763238205474L;

	public Doughnut(String name, String img, String beschreibung) {
		super(name, img, beschreibung);
	}

	@Override
	public Object interagiere(Object o) {
		return null;
	}

}