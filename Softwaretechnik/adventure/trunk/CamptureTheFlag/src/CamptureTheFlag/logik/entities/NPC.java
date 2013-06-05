package CamptureTheFlag.logik.entities;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import CamptureTheFlag.logik.models.Dialog;
import CamptureTheFlag.logik.models.Erwiderung;

/**
 * Ein NPC erhält einen Dialog, welchen er beim Interagieren mit
 * dem Spieler fortführt.
 * 
 * Er kann via interagiere() Geschenke aus den Dialogen an den
 * Spieler weiterreichen.
 * 
 * @author shard001
 */
public class NPC extends Charakter implements Serializable {
	
	private static final long serialVersionUID = -2272100970669663520L;
	private static Logger log = Logger.getLogger(NPC.class.getName());
	
	private Dialog startDialog; // damit Dialog-Baum nicht gelöscht wird (GarbageCollection)
	private Dialog aktDialog;

	public NPC(String name, String img, String beschreibung) {
		super(name, img, beschreibung);
	}
	
	public void setStartDialog(Dialog startDialog) {
		this.startDialog = startDialog;
		this.aktDialog = this.startDialog;
	}

	/**
	 * Entscheidet, ob der Spieler die gewünschte Erwiderung (via Tastatureingabe) aufrufen darf.
	 * Die Erwiderung fordert einen Tribut - dieser Gegenstand muss sich in der Hand des Spielers befinden.
	 * 
	 * @return		Der folgende Dialog der gewählten Erwiderung, falls erlaubt
	 */
	public Dialog getAktDialog() {
		return this.aktDialog;
	}

	/**
	 * Spricht der Spieler eine NPC an, wird ihm der Dialog eröffnet
	 * NPC merkt sich den Spieler für Abfragen an dessen Hand
	 * 
	 * @return 		o, wenn OK / Gegenstand, falls Geschenk erhalten / null, falls Abbruch
	 */
	@Override
	public Object interagiere(Object o) {
		if ( o instanceof Spieler) { // Spieler ruft auf?
			log.log(Level.INFO, "Starte Dialog");
			this.interaktionMit = o; // Spieler merken
			return o;
		} else {
			// Mensch wählt Nummer aus
			if ((Integer)o == 0) {
				// abbruch
				return null;
			} else {
				Erwiderung e = null;
				Gegenstand tribut = null;
				try {
					e = this.aktDialog.getErwiderungen((Integer) o -1); // passende Erwiderung wählen
					tribut = e.getTribut();
				} catch (Exception ex) {
					log.log(Level.WARNING, "Keine Erwiderung mit dieser Nummer");
				}
				//TODO if ( tribut instanceof Schluessel) => auf Schluessel.getNummer() prüfen
				try {
					// Typ des Tributs muss den gleichen Typ des Objektes in der Hand haben
					if (tribut == null || ((Charakter) this.interaktionMit).getHand().getClass().equals(tribut.getClass())) { // kein Tribut erforderlich oder in Hand vorhanden
						this.aktDialog = e.getFolgeDialog();
						
						if (tribut != null) { // wurde tribut verlangt?
							((Charakter) this.interaktionMit).setHand(null); // Tribut in der Hand löschen
						}
						
						/* gibt Nummer wieder zurück - so weiß Spieler, dass es weiter geht
						 * alternativ bekommt man durch den neuen Dialog ein Geschenk
						 */
						return (e.getFolgeDialog().getGeschenk() != null) ? e.getFolgeDialog().getGeschenk() : o; 
					} else {
						return o; // Tribut nicht in Hand - neuer versuch
					}
				} catch (Exception ex) {
					log.log(Level.INFO, "Nichts in der Hand.");
					return o;
				}
			}
		}
	}
	
}