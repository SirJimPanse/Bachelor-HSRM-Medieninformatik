package logik;

/**
 * Diese Klasse modelliert eine Ampel mit vier Phasen (Rot, Rot-Gelb, Grün, Gelb).
 * @author $Author: dgens001 $
 * @version $Date: 2012-04-03 22:46:28 +0200 (Di, 03. Apr 2012) $
 * @see $Id: Ampel.java 18 2012-04-03 20:46:28Z dgens001 $
 *
 */
public class Ampel {
	
	private int zaehler;
	
	public boolean getROT() { return zaehler%4 == 0 || zaehler%4 == 1; }
	public boolean getGELB() { return zaehler%4 == 1 || zaehler%4 == 3; }
	public boolean getGRUEN() { return zaehler%4 == 2 ; }
	public void reset() { zaehler = 0; }
	public void tick() {
		if (zaehler < 0 || zaehler++ > 999)
			zaehler = -1;		
	}
	
	
//	/** Rote Leuchte **/
//	private boolean rot;
//	
//	/** Gelbe Leuchte **/
//	private boolean gelb;
//	
//	/** Grüne Leuchte **/
//	private boolean gruen;
//	
//	/** Phasenzaehler **/
//	private int counter;
	
	
//	/** Default-Konstruktor (initale Phase ist "ROT") **/
//	public Ampel() {
//		counter = 0;
//		reset();
//	}	
//	
//	/** Gibt Status der roten Leuchte zurück **/
//	public boolean getRot() {
//		return rot;
//	}
//
//	/** Gibt Status der gelben Leuchte zurück **/
//	public boolean getGelb() {
//		return gelb;
//	}
//
//	/** Gibt Status der grünen Leuchte zurück **/
//	public boolean getGruen() {
//		return gruen;
//	}
//
//	/** Schaltet die Ampel eine Phase weiter (es gibt vier Phasen) **/
//	public void tick() {
//		
//		if (counter++ > 1000) {
//			return;
//		}
//		
//		if (rot && !gelb && !gruen) {
//			gelb = true;
//		} else if (rot && gelb && !gruen) {
//			rot = false;
//			gelb = false;
//			gruen = true;
//		} else if (!rot && !gelb && gruen) {
//			gruen = false;
//			gelb = true;
//		} else if (!rot && gelb && !gruen) {
//			gelb = false;
//			rot = true;
//		}
//	}
//
//	/** Wechselt alle Birnen aus **/
//	public void birnenWechsel() {
//		counter = 0;
//	}
//	
//	/** Setzt die Ampel auf die erste Phase ("ROT") **/
//	public void reset() {
//		rot = true;
//		gelb = false;
//		gruen = false;
//	}
}