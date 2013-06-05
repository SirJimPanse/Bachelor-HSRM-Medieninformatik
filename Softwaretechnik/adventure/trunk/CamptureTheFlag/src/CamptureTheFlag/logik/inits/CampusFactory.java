package CamptureTheFlag.logik.inits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import CamptureTheFlag.logik.entities.Campus;
import CamptureTheFlag.logik.entities.Eingang;
import CamptureTheFlag.logik.entities.Feld;
import CamptureTheFlag.logik.entities.GameObject;
import CamptureTheFlag.logik.entities.Raum;

import CamptureTheFlag.logik.P;
import CamptureTheFlag.logik.models.Richtung;
import static CamptureTheFlag.logik.P.p;

/**
 * Diese Klasse kapselt den Erstellungsprozess eines Campusobjekts. Grundlage des
 * Campusobjekts ist immer eine *.ctfmapXX Datei. Die dazughörige Versionsnummer 
 * XX gibt die Version des Spiels an, für die diese gültig ist. Die CampusFactory
 * unterstützt zur Zeit Version 0.2 (abwärtskompatibel zu Version 0.1).
 * 
 * @author dgens
 * @version 0.2
 */
public class CampusFactory {
	
	/* Konstanten für das Parsen (später xml?) wie in spec.pdf (Abschnitt 5 - UI) spezifiziert */
	public static final String ENDUNG    = ".ppm"; //TODO: Endung zum Schluss auf .ctfmap ändern
	public static final int FELD_UNIT    = 3;  
	public static final int FREI_UNIT    = 1;
	
	public static final int DOUGHNUT     = 0xc08000;   // braun
	public static final int GLUECKSKEKS  = 0xddccbb;   // graubraun
	public static final int SPICKER      = 0x808080;   // dunkelgrau
	
	public static final int BERDUX       = 0x00ffff;   // cyan
	public static final int WEITZ        = 0x008080;   // dunkelcyan
	public static final int BARTH        = 0xff8000;   // orange
	public static final int LISA         = 0xff00ff;   // magenta
	public static final int HOMER        = 0xffff00;   // gelb
	public static final int SPIELER      = 0x0000ff;   // blau
	
	public static final int OFFEN        = 0x00ff00;   // gruen
	public static final int VERSCHLOSSEN = 0xff0000;   // rot
	public static final int FELD         = 0xc0c0c0;   // hellgrau
	public static final int WAND         = 0x000000;   // schwarz
	public static final int FREI         = 0xffffff;   // weiss

	/** Wir delegieren den Dateieinleseprozess (erste Phase) an PPMReader **/
	private PPMReader ppmreader;
	
	/** Pixelkoordinaten => Farbinformation als RGB Integer (nach erster Phase gesetzt) **/
	private Map<P<Integer,Integer>,Integer> img;
	
	/** Mapping von Farbcodes auf Feldobjekte **/
	private Map<Integer,Feld> positionenNPCs;
	
	/** Mapping von Farbcodes von Gegenständen auf eine Liste von Feldobjekten **/
	private Map<Integer,List<Feld>> positionenGegenstaende;
	
	/** Feldkoordinaten => Feldobjekt (nach zweiter Phase gesetzt, nach dritter Phase vollständig) **/
	private Map<P<Integer,Integer>,Feld> felder;
	
	/** Feld,Feld => Eingang der diese verbindet **/
	private Map<P<Feld,Feld>,Eingang> eingaenge;
	
	/** Raum,Raum => Eingang der diese verbindet **/
	private Map<P<Raum,Raum>,Eingang> verbindungen;
	
	/** Raumnummer => Raumobjekt (nach vierter Phase gesetzt) **/
	private List<Raum> raeume;
	
	/** Logger fürs Logging **/
	private Logger log;
	
	/** Einfacherer Zugriff von aussen über die Klassenmethode getCampus **/
	public static Campus getCampusFromCTFMAP(String pfad) { return new CampusFactory().parseMap(pfad); }
	
	/** Default Konstructor **/
	public CampusFactory() {
		this.ppmreader = new PPMReader();
		this.log = Logger.getLogger(CampusFactory.class.getName());
		this.log.setLevel(Level.WARNING);
		this.positionenNPCs  = new HashMap<Integer,Feld>();
		this.positionenGegenstaende = new HashMap<Integer,List<Feld>>();
		this.eingaenge = new HashMap<P<Feld,Feld>,Eingang>();
		this.verbindungen = new HashMap<P<Raum,Raum>,Eingang>();
		this.positionenGegenstaende.put(CampusFactory.DOUGHNUT, new ArrayList<Feld>());
		this.positionenGegenstaende.put(CampusFactory.GLUECKSKEKS, new ArrayList<Feld>());
		this.positionenGegenstaende.put(CampusFactory.SPICKER, new ArrayList<Feld>());
	}
	
	/** Bekommt den Pfad zu einem PPM Bild und gibt ein Campusobjekt mit all den Objekten auf der Map zurück. **/
	public Campus parseMap(String mapFilePath) {
		if (mapFilePath.indexOf(CampusFactory.ENDUNG) != -1) { // eigentlich nicht ausreichend, bei Gelegenheit verfeinern
			String campusName   = mapFilePath.substring(mapFilePath.lastIndexOf(java.io.File.separatorChar)+1).replaceFirst(CampusFactory.ENDUNG, "");
			this.img = this.ppmreader.readPPM(mapFilePath); // Phase 1 (loggt PPMReader selbst)
			this.felder = parseFelder();                    // Phase 2
			this.log.log(Level.INFO,"nach Phase 2:\n" + this.felder2String());
			this.felder = parseNachbarn();				    // Phase 3
			this.log.log(Level.INFO,"nach Phase 3:\n" + this.felder2String());
			this.raeume = parseRaeume();	                // Phase 4
			this.log.log(Level.INFO,"nach Phase 4:\n" + this.felder2String());
			Campus result = new Campus(campusName);
			result.setRaeume(this.raeume);
			result.setStartFeld(this.positionenNPCs.get(CampusFactory.SPIELER));
			result.setPosNPCs(this.positionenNPCs);
			result.setPosGegenstaende(this.positionenGegenstaende);
			result.setEingaenge(this.eingaenge);
			result.setVerbindungen(this.verbindungen);
			return result;
		}
		this.log.log(Level.WARNING, "Mapdatei hat nicht die richtige Endung, *." + CampusFactory.ENDUNG + " erwartet, " + mapFilePath.split(".")[1] + " bekommen.");
		return null;
	}
	
	/** Erstellt aus Feldern mit Objekten und Beziehungen zueinander abgekapselte Raumobjekte (vierte und letzte Phase) und setzt deren Verbindungen **/
	private List<Raum> parseRaeume() {
		this.raeume  = new ArrayList<Raum>();
		int i = 0;
		
		for (Feld f : this.felder.values()) {
			if (!inRaeume(f))
				this.raeume.add(new Raum(i++,findeRaum(f, new ArrayList<Feld>())));
		}
		
		for (P<Feld,Feld> ff : this.eingaenge.keySet()) {
			Raum rx = null, ry = null;
			for (Raum r : this.raeume) {
				if (r.contains(ff.x))
					rx = r;
				if (r.contains(ff.y))
					ry = r;				
			}
			this.verbindungen.put(p(rx,ry), this.eingaenge.get(ff));
		}
		return this.raeume;
	}
	
	/** Findet rekursiv alle Felder, die sich mit Feld f in einem Raum befinden. **/
	private List<Feld> findeRaum(Feld f, List<Feld> raum) {
		
		raum.add(f);
		
		if (f.getNachbar(Richtung.NORD) != null)
			if (f.isNachbarBegehbar(Richtung.NORD) && !raum.contains(f.getNachbar(Richtung.NORD)))
				findeRaum(f.getNachbar(Richtung.NORD), raum);
		
		if (f.getNachbar(Richtung.SUED) != null)
			if (f.isNachbarBegehbar(Richtung.SUED) && !raum.contains(f.getNachbar(Richtung.SUED)))
				findeRaum(f.getNachbar(Richtung.SUED), raum);
		
		if (f.getNachbar(Richtung.OST) != null)
			if (f.isNachbarBegehbar(Richtung.OST) && !raum.contains(f.getNachbar(Richtung.OST)))
				findeRaum(f.getNachbar(Richtung.OST), raum);
		
		if (f.getNachbar(Richtung.WEST) != null)
			if (f.isNachbarBegehbar(Richtung.WEST) && !raum.contains(f.getNachbar(Richtung.WEST)))
				findeRaum(f.getNachbar(Richtung.WEST), raum);
		
		return raum;
	}
	
	/** Gibt true zurück, falls das Feld bereits Teil eines Raums ist, sonst false. **/
	private boolean inRaeume(Feld f) {
		for (Raum r : this.raeume)
			if (r.getFelder().contains(f))
				return true;
		return false;
	}
	
	/** Füllt die Beziehungen zwischen den Feldern wie z.B. Eingänge, Nachbarn, etc (dritte Phase) **/
	private Map<P<Integer,Integer>,Feld> parseNachbarn() {
		
		int height = this.ppmreader.getImgHeight();
		int width  = this.ppmreader.getImgWidth();
		
		for (int y = CampusFactory.FREI_UNIT; y < height-CampusFactory.FREI_UNIT; y += CampusFactory.FELD_UNIT+1) {
			for (int x = CampusFactory.FREI_UNIT; x < width-CampusFactory.FREI_UNIT; x += CampusFactory.FELD_UNIT+1) {
				
				P<Integer,Integer> feldKoord = pixel2Feld(p(x,y)); // wir konvertieren vom Pixelspace (jedes Feld UNIT*UNIT groß) 
				Feld aktFeld = this.felder.get(feldKoord);              // in den Feldspace (jedes Feld ist exakt 1 groß)
				if (aktFeld != null) {
					Map<Richtung, Feld> nachbarn = new HashMap<Richtung, Feld>();
					
					/* Nachbarkoordinaten (von linker oberer Ecke des Feldes ausgehend) */
					P<Integer,Integer> nord = feld2Pixel(p(feldKoord.x,feldKoord.y - 1));
					P<Integer,Integer> sued = feld2Pixel(p(feldKoord.x,feldKoord.y + 1));
					P<Integer,Integer> ost  = feld2Pixel(p(feldKoord.x + 1,feldKoord.y));
					P<Integer,Integer> west = feld2Pixel(p(feldKoord.x - 1,feldKoord.y));
					
					/* Nachbarn parsen */
					if (this.img.containsKey(nord) && isFeld(this.img.get(nord)))      // Pixel "nördlich" vom Feld
						nachbarn.put(Richtung.NORD,this.felder.get(pixel2Feld(nord)));
					
					if (this.img.containsKey(sued) && isFeld(this.img.get(sued))) // Pixel "südlich"  vom Feld
						nachbarn.put(Richtung.SUED,this.felder.get(pixel2Feld(sued)));
					
					if (this.img.containsKey(ost) && isFeld(this.img.get(ost))) // Pixel "östlich"  vom Feld
						nachbarn.put(Richtung.OST,this.felder.get(pixel2Feld(ost)));
					
					if (this.img.containsKey(west) && isFeld(this.img.get(west))) // Pixel "westlich" vom Feld
						nachbarn.put(Richtung.WEST,this.felder.get(pixel2Feld(west)));
					aktFeld.setNachbarn(nachbarn);
					
					/* Eingänge parsen (das ganze geht auch hier natürlich wieder schief wenn die Map falsch gezeichnet ist..) */
					List<Eingang> eingaenge = null;
					while ((eingaenge = getEingaenge(aktFeld)) != null) {
						
						Feld fNord = this.felder.get(pixel2Feld(nord));
						Feld fSued = this.felder.get(pixel2Feld(sued));
						Feld fOst  = this.felder.get(pixel2Feld(ost));
						Feld fWest = this.felder.get(pixel2Feld(west));
						
						Eingang aktTuer = eingaenge.get(0);
						
						List<Eingang> eingaengeNord = fNord != null ? getEingaenge(fNord) : null;
						List<Eingang> eingaengeSued = fSued != null ? getEingaenge(fSued) : null;
						List<Eingang> eingaengeOst  = fOst  != null ? getEingaenge(fOst)  : null;
						List<Eingang> eingaengeWest = fWest != null ? getEingaenge(fWest) : null;

						if (eingaengeNord != null) {
							aktTuer.setDahinter(fNord);
							eingaengeNord.get(0).setDahinter(aktFeld);
							this.eingaenge.put(p(aktFeld,fNord), eingaengeNord.get(0));
						}
						
						if (eingaengeSued != null) {
							aktTuer.setDahinter(fSued);
							eingaengeSued.get(0).setDahinter(aktFeld);
							this.eingaenge.put(p(aktFeld,fSued), eingaengeSued.get(0));
						}
						
						if (eingaengeOst  != null) {
							aktTuer.setDahinter(fOst);
							eingaengeOst.get(0).setDahinter(aktFeld);
							this.eingaenge.put(p(aktFeld,fOst), eingaengeOst.get(0));
						}
						
						if (eingaengeWest != null) {
							aktTuer.setDahinter(fWest);
							eingaengeWest.get(0).setDahinter(aktFeld);
							this.eingaenge.put(p(aktFeld,fWest), eingaengeWest.get(0));
						}
					}
				}
			}
		}
		return this.felder;
	}
	
	/** Felder mit Objekten darauf und Wänden erstellen (zweite Phase)**/
	private Map<P<Integer,Integer>,Feld> parseFelder() {
		Map<P<Integer,Integer>,Feld> felder = new java.util.HashMap<P<Integer,Integer>,Feld>();
		
		int height = this.ppmreader.getImgHeight();
		int width  = this.ppmreader.getImgWidth();
		
		for (int y = CampusFactory.FREI_UNIT; y < height-CampusFactory.FREI_UNIT; y += CampusFactory.FELD_UNIT+1) {
			for (int x = CampusFactory.FREI_UNIT; x < width-CampusFactory.FREI_UNIT; x += CampusFactory.FELD_UNIT+1) {

				/* px ist die linke obere Ecke eines UNIT*UNIT großen Pixelquadrats */
				int px = img.get(p(x,y));
				if (isFeld(px)) {

					Feld res = new Feld(this.pixel2Feld(p(x,y)));
					List<GameObject> slots = new ArrayList<GameObject>();
					Map<Richtung,Boolean> waende = new HashMap<Richtung,Boolean>();

					/* Parsen der Feldumrandung */
					waende.put(Richtung.NORD, img.get(p(x,y-1)) != CampusFactory.FREI);
					waende.put(Richtung.SUED, img.get(p(x,y+CampusFactory.FELD_UNIT)) != CampusFactory.FREI);
					waende.put(Richtung.WEST, img.get(p(x-1,y)) != CampusFactory.FREI);
					waende.put(Richtung.OST,  img.get(p(x+CampusFactory.FELD_UNIT,y)) != CampusFactory.FREI);
					
					/* Parsen der Elemente auf dem Feld (pixelweise) - geht bei falsch gezeichneter Map natürlich immens schief */
					int size = CampusFactory.FELD_UNIT*CampusFactory.FELD_UNIT;
					for (int i = 0; i < size; ++i) {
						px = img.get(p(x+i%CampusFactory.FELD_UNIT,y+i/CampusFactory.FELD_UNIT));
						if (px == CampusFactory.FELD)
							continue;
						else if (px == CampusFactory.OFFEN)
							slots.add(new Eingang(null, null, null, res, null, true));  // Eingänge werden hier erstellt (macht Sinn!)
						else if (px == CampusFactory.VERSCHLOSSEN)
							slots.add(new Eingang(null, null, null, res, null, false)); // Eingänge werden hier erstellt (macht Sinn!)
						else if (px == CampusFactory.LISA)
								this.positionenNPCs.put(CampusFactory.LISA, res);
						else if (px == CampusFactory.HOMER) 
							this.positionenNPCs.put(CampusFactory.HOMER, res);
						else if (px == CampusFactory.WEITZ) 
							this.positionenNPCs.put(CampusFactory.WEITZ, res);
						else if (px == CampusFactory.BERDUX) 
							this.positionenNPCs.put(CampusFactory.BERDUX, res);
						else if (px == CampusFactory.BARTH)
							this.positionenNPCs.put(CampusFactory.BARTH, res);
						else if (px == CampusFactory.SPIELER)
							this.positionenNPCs.put(CampusFactory.SPIELER, res);
						else if (px == CampusFactory.DOUGHNUT)
							this.positionenGegenstaende.get(CampusFactory.DOUGHNUT).add(res);
						else if (px == CampusFactory.GLUECKSKEKS)
							this.positionenGegenstaende.get(CampusFactory.GLUECKSKEKS).add(res);
						else if (px == CampusFactory.SPICKER)
							this.positionenGegenstaende.get(CampusFactory.SPICKER).add(res);
					}

					res.setWaende(waende);
					res.addGameObjects(slots);
					felder.put(p(x/(CampusFactory.FELD_UNIT+1),y/(CampusFactory.FELD_UNIT+1)), res);
				}
			}
		}
		return felder;
	}
	
	/** Gibt die Breite zurück (maximal mögliche Feldanzahl auf der x-Achse) **/
	public int getWidth()  { return (this.ppmreader.getImgWidth()  - CampusFactory.FREI_UNIT) / (CampusFactory.FELD_UNIT+1); }
	
	/** Gibt die Höhe zurück (maximal mögliche Feldanzahl auf der y-Achse) **/
	public int getHeight() { return (this.ppmreader.getImgHeight() - CampusFactory.FREI_UNIT) / (CampusFactory.FELD_UNIT+1); }
	
	/** Gibt alle Eingänge auf dem Feld zurück, die noch keine Verbindung haben (bei f = null -> null) **/
	private List<Eingang> getEingaenge(Feld f) {
		if (f != null) {
			List<Eingang> res = new ArrayList<Eingang>();
			List<GameObject> slots = null;
			if ((slots = f.getObjects()) != null) {
				for (GameObject go : slots) {
					if (go instanceof Eingang) {
						Eingang tuer = (Eingang)go;
						if (tuer.getDahinter() == null)
							res.add(tuer);
					}
				}
			}
			return res.size() == 0 ? null : res;
		}
		return null;
	}
	
	/** Gibt die bereits geparsten Felder als String formatiert zurück (nur zum testen) **/
	private String felder2String() {
		StringBuilder sb = new StringBuilder();
		Feld aktFeld = null;
		int width  = getWidth();
		int height = getHeight();
		
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				aktFeld = this.felder.get(p(x,y));
				sb.append(aktFeld != null ? aktFeld.getPos() : "XXXXX");
			}
			sb.append('\n');
		}
		
		return sb.toString();
	}
	
	/** Konvertiert Feldraumkoordinaten (jedes Feld ist exakt 1 groß) in Pixelraumkoordinaten (linke obere Ecke eines UNIT*UNIT großen Feldes) **/
	private P<Integer,Integer> feld2Pixel(P<Integer,Integer> xy) { return p(xy.x*(CampusFactory.FELD_UNIT+1) + 1,xy.y*(CampusFactory.FELD_UNIT+1) + 1); }
	/** Konvertiert Pixelraumkoordinaten (jedes Feld ist UNIT*UNIT groß) in Feldraumkoordinaten (jedes Feld ist exakt 1 groß) **/
	private P<Integer,Integer> pixel2Feld(P<Integer,Integer> xy) { return p(xy.x/(CampusFactory.FELD_UNIT+1),xy.y/(CampusFactory.FELD_UNIT+1)); }
	/** Überprüft ob das Pixel Zwischenraum darstellen soll **/
	private boolean isZwischenraum(int px) { return px == CampusFactory.FREI || px == CampusFactory.WAND; }
	/** Überprüft ob das Pixel zu einem Feld gehören soll **/
	private boolean isFeld(int px) { return !isZwischenraum(px); }
}