package CamptureTheFlag.logik.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Ein Raum nimmt mehrere Felder auf
 * Er ist durch Wände von anderen Räumen getrennt und daher 
 * nur über Eingänge erreichbar.
 * 
 * @author shard001
 */
public class Raum implements Serializable {

	private static final long serialVersionUID = 7624676099206094385L;
	
	private Integer nummer;
	private String name;
	private List<Feld> felder;
	
	public Integer getNummer() {
		return this.nummer;
	}

	public String getName() {
		return this.name;
	}

	public Raum(Integer nummer, String name) {
		this.nummer = nummer;
		this.name = name;
	}
	
	public Raum(Integer nummer, String name, List<Feld> felder) {
		this(nummer,name);
		this.setFelder(felder);
	}
	
	public Raum(Integer nummer, List<Feld> felder) {
		this(nummer,Integer.toString(nummer),felder);
	}

	public void setFelder(List<Feld> felder) { this.felder = felder; }
	public List<Feld> getFelder() { return this.felder; }
	public boolean contains(Object o) {
		if (o instanceof GameObject) {
			for (Feld f : this.felder)
				if (f.contains(o))
					return true;
		} else if (o instanceof Feld) {
			return this.felder.contains(o);
		}
		return false;
	}
}