package CamptureTheFlag.logik.models;

import java.io.Serializable;

import CamptureTheFlag.logik.entities.Gegenstand;

/**
 * Erwiderungen werden im Zusammenspiel mit Dialogen
 * bei der Interaktion mit NPCs verwendet.
 * 
 * Kann nur ausgewählt werden, wenn sich der geforderte Tribut
 * in der Hand des Spielers befindet
 * 
 * @author shard001
 */
public class Erwiderung implements Serializable {

	private static final long serialVersionUID = 7601440986346019513L;
	private String text;
	private Gegenstand tribut;
	private Dialog folgeDialog;

	public Erwiderung(String text) { this.text = text; }

	public Erwiderung(String text, Dialog folgeDialog) {
		this(text);
		this.folgeDialog = folgeDialog;
	}

	public Erwiderung(String text, Gegenstand tribut, Dialog folgeDialog) {
		this(text,folgeDialog);
		this.tribut = tribut;
	}

	public void setFolgeDialog(Dialog folgeDialog) { this.folgeDialog = folgeDialog; }
	public void setTribut(Gegenstand tribut) { this.tribut = tribut; }

	public String getText() {
		return this.text;
	}

	public Gegenstand getTribut() {
		return this.tribut;
	}

	public Dialog getFolgeDialog() {
		return folgeDialog;
	}
}