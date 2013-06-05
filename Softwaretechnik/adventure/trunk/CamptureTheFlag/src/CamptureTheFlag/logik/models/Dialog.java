package CamptureTheFlag.logik.models;

import java.io.Serializable;
import java.util.List;
import CamptureTheFlag.logik.entities.Gegenstand;

/**
 * Dialoge werden beim Interagieren mit NPC benötigt
 * Sie können zu mehrere Erwiderungen verweisen und
 * dem Spieler beim Erreichen des Dialogs ein Geschenk geben
 * 
 * @author shard001
 */
public class Dialog implements Serializable {
	
	private static final long serialVersionUID = -3624932063236562517L;
	private String text;
	private Gegenstand geschenk;
	private List<Erwiderung> erwiderungen;
	
	public Dialog(String text) { this.text = text; }
	
	public Dialog(String text, Gegenstand geschenk) {
		this(text);
		this.geschenk = geschenk;
	}
	
	public Dialog(String text, List<Erwiderung> erwiderungen) {
		this(text);
		this.setErwiderungen(erwiderungen);
	}
	
	public Dialog(String text, Gegenstand geschenk, List<Erwiderung> erwiderungen) {
		this(text,geschenk);
		this.setErwiderungen(erwiderungen);
	}
	
	/** @param erwiderungen	Liste der mögichen Erwiderungen für diesen Dialog **/
	public void setErwiderungen(List<Erwiderung> erwiderungen) { this.erwiderungen = erwiderungen; }

	public String getText() { return text; }
	public Gegenstand getGeschenk() { return geschenk; }
	public Erwiderung getErwiderungen(int i) {return erwiderungen.get(i); }
	public List<Erwiderung> getErwiderungen() { return erwiderungen; }
}