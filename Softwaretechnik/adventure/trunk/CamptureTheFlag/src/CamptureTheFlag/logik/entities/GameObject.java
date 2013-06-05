package CamptureTheFlag.logik.entities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * Alle auf Feldern ablegbaren Sachen sind GameObjects und sind interagierbar
 * Bei Statusänderungen können sie PropertyChangeEvents feuern
 * 
 * @author shard001
 */
public abstract class GameObject implements Interagieren, Serializable {

	private static final long serialVersionUID = -25808558826627269L;
	protected String name;
	protected String img;
	protected String beschreibung;
	protected transient PropertyChangeSupport notify; // wird von Unterklassen verwendet
	
	public GameObject(String name, String img, String beschreibung) {
		this.name = name;
		this.img = img;
		this.beschreibung = beschreibung;
		this.notify = new PropertyChangeSupport(this);
// 		Das hier muss für "komplette" ImageObjekte 8 zurück geben
//		System.out.println(this.img.getImageLoadStatus());
//		System.out.println(java.awt.MediaTracker.ABORTED); // -> 2
//		System.out.println(java.awt.MediaTracker.COMPLETE);// -> 8
//		System.out.println(java.awt.MediaTracker.ERRORED); // -> 4
//		System.out.println(java.awt.MediaTracker.LOADING); // -> 1
	}

	public String getName() {
		return this.name;
	}

	public String getImg() {
		return this.img;
	}
	
	public String getBeschreibung() {
		return this.beschreibung;
	}
	
	public void setNotify(PropertyChangeSupport pcs) {
		this.notify = pcs;
	}


	// PropertyChangeSupport
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.notify.addPropertyChangeListener(propertyName, listener);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.notify.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.notify.removePropertyChangeListener(propertyName, listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.notify.removePropertyChangeListener(listener);
	}

}