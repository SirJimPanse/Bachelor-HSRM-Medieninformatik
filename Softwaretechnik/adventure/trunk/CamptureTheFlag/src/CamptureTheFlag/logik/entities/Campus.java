package CamptureTheFlag.logik.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import CamptureTheFlag.logik.P;

/**
 * Der Campus beinhaltet alle Räume der eingelesenen Map
 * 
 * @author shard001
 */
public class Campus implements Serializable, Iterable<Feld> {

	private static final long serialVersionUID = 8694113647489359647L;
	private String bezeichnung;
	private List<Raum> raeume;
	private Feld startFeld;
	private Map<Integer,Feld> positionenNPCs;
	private Map<Integer,List<Feld>> positionenGegenstaende;
	private Map<P<Feld,Feld>,Eingang> eingaenge;
	private Map<P<Raum,Raum>,Eingang> verbindungen;

	public Campus(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public String getBezeichnung() { return bezeichnung; }
	public Feld getStartFeld() { return this.startFeld; }
	public List<Raum> getRaeume() { return this.raeume; }
	public Map<Integer,Feld> getPosNPCs() { return this.positionenNPCs; }
	public Map<Integer,List<Feld>> getPosGegenstaende() { return this.positionenGegenstaende; }
	public Map<P<Feld,Feld>,Eingang> getEingaenge() { return this.eingaenge; }
	public Map<P<Raum,Raum>,Eingang> getVerbindungen() { return this.verbindungen; }
	
	public void setStartFeld(Feld f) { this.startFeld = f; }
	public void setRaeume(List<Raum> raeume) { this.raeume = raeume; }
	public void setPosNPCs(Map<Integer,Feld> posnpcs) { this.positionenNPCs = posnpcs; }
	public void setPosGegenstaende(Map<Integer,List<Feld>> posgstnde) { this.positionenGegenstaende = posgstnde; }
	public void setEingaenge(Map<P<Feld,Feld>,Eingang> eingaenge) { this.eingaenge = eingaenge; }
	public void setVerbindungen(Map<P<Raum,Raum>,Eingang> verb) { this.verbindungen = verb; }

	@Override
	public Iterator<Feld> iterator() {
		List<Feld> felder = new ArrayList<Feld>();
		for (Raum r : this.raeume)
			felder.addAll(r.getFelder());
		return felder.iterator();
	}
}