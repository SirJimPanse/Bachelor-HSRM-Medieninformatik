package CamptureTheFlag.logik.inits;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import CamptureTheFlag.logik.P;
import static CamptureTheFlag.logik.P.p;

/**
 * Diese Klasse kapselt den Einlesevorgang einer *.ctfmapXX Datei, wobei XX die dazu
 * gehörende Versionsnummer ist. Näheres Zum Format findet sich im Quellcode oder in
 * Abschnitt 5 der Spezifikation.
 * 
 * @author dgens
 */
public class PPMReader {
	
	 /* Jede ctmap Datei baut sich wie folgt zusammen (vergleiche 5.3 in spec.pdf):
	 * 
	 * - Einer "magic number" um das PPM-Format festzulegen. Diese besteht aus zwei
	 *   Zeichen
	 *   
	 * - Whitespace (blank, TAB, CR, oder LF)
	 * 
	 * - die Breite des Bildes in ASCII-Zeichen (als Dezimalzahl)
	 * 
	 * - Whitespace
	 * 
	 * - die Höhe des Bildes (wieder dezimal ASCII)
	 * 
	 * - der maximale Farbwert (wieder dezimal ASCII)
	 * 
	 * - ein Whitespace
	 * 
	 * - die Bildinformationen zeilenweise von oben Links nach unten Rechts, wobei
	 *   jedes Pixel durch ein RGB Tripel dargestellt wird und jede Zeile width
	 *   Pixel enthält. Jede Komponente des Tripels besteht dabei aus jeweils einem
	 *   Byte, falls der Maximalwert kleiner als 255 ist, ansonsten aus zwei und ist
	 *   binär abgelegt.
	 **/
	
	private int width;
	private int height;
	private byte[][] img;
	private String file;
	private Logger log;
	
	public PPMReader() {
		this.img = null;
		this.file = null;
		this.log = 	Logger.getLogger(PPMReader.class.getName());
		this.log.setLevel(Level.OFF);
	}
	
	public PPMReader(String fileName) {
		this();
		this.file = fileName;
		this.img  = this.readByteImage(this.file);
	}
	
	public int getImgWidth() { return this.width; }
	public int getImgHeight() { return this.height; }
	
	public Map<P<Integer,Integer>,Integer> readPPM() { return this.byteImageToCoordinateMap(this.img); }
	
	public Map<P<Integer,Integer>,Integer> readPPM(String file) { 
		this.file = file;
		this.img  = this.readByteImage(this.file);
		this.width  = this.img[0].length / 3; // RGB Tripel -> jedes Pixel ist dreimal so groß
		this.height = this.img.length;
		return this.byteImageToCoordinateMap(this.img);
	}
	
	/** Liest ein Bild im PPM Format (nur P6) als Bytearray ein.
	 * @param filename Dateipfad (inklusive Dateiname)
	 * @return zweidimensionales Bytearray mit height * (width * 3) Elementen
	 */
	private byte[][] readByteImage(String filename) {

		byte[][] bytes = null;

		try {
			
			byte buffer;               // Zeichen im PPM Header
			String id = new String();  // PPM magic number ("P6")
			String dim = new String(); // Bilddimension als String
			FileInputStream fis = new FileInputStream(filename);
			
			do {
				buffer = (byte)fis.read();
				id = id + (char)buffer;
			} while ((char)buffer != '\n' && (char)buffer != ' ');
			
			if (id.charAt(0) == 'P' && id.charAt(1) == '6') {
				
				log.log(Level.INFO,"Reading header data");
				buffer = (byte)fis.read();
				
				do { // zweite Headerzeile ist "width height\n"
					dim = dim + (char)buffer;
					buffer = (byte)fis.read();
				} while ((char)buffer != ' ' && (char)buffer != '\n');

				int width = Integer.parseInt(dim);
				dim = new String();
				buffer = (byte) fis.read();
				
				do {
					dim = dim + (char)buffer;
					buffer = (byte) fis.read();
				} while ((char)buffer != ' ' && (char)buffer != '\n');
				
				int height = Integer.parseInt(dim);
				
				do { // dritte Headerzeile ist der maximale RGB Wert, z.B., "255\n"
					buffer = (byte) fis.read();
				} while (buffer != ' ' && buffer != '\n');
				
				log.log(Level.INFO,"Image is " + width + " X " + height + " pixels.");
				log.log(Level.INFO, "Reading image data");

				// der Rest der Datei ist width*height*3 groß (wegen den RGB Tripeln)

				bytes = new byte[height][width * 3];

				for (int y = 0; y < height; ++y)
					fis.read(bytes[y]);

				log.log(Level.INFO, "Done reading image");
				fis.close();
				
			} else {
				log.log(Level.WARNING, "Unsupported file format, no image read.");
				bytes = null;
			}
			
		} catch (FileNotFoundException e) {
			log.log(Level.WARNING, "Error opening PPM file, no image read.");
			bytes = null;
		} catch (IOException e) {
			log.log(Level.WARNING, "Error reading PPM file, no image read.");
			bytes = null;
		} catch (NumberFormatException e) {
			log.log(Level.WARNING, "Something's wrong with the PPM header?  No image read.");
		}
		
		return bytes;
	}
	
	/** Gibt eine Repräsentation des Bytearrays zurück, die in Entities geparsed werden kann - die Koordinatenmap **/
	private Map<P<Integer,Integer>,Integer> byteImageToCoordinateMap(byte img[][]) {
		Map<P<Integer,Integer>,Integer> result = new java.util.HashMap<P<Integer,Integer>,Integer>();
		StringBuffer sb;
		int width  = img[0].length;
		int height = img.length;
		for (int j = 0; j < height; ++j) {
			for (int i = 0; i < width; i = i+3) {
				sb = new StringBuffer();
				sb.append(String.format("%02x", img[j][i]));   // rot
				sb.append(String.format("%02x", img[j][i+1])); // grün
				sb.append(String.format("%02x", img[j][i+2])); // blau
				result.put(p(i / 3 + i%3,j),Integer.valueOf(sb.toString(), 16));
			}
		}
//		this.log.log(Level.INFO,"\n" + result);
		this.log.log(Level.INFO,"\n" + this.imgToString(result, width/3, height));
		return result;
	}
	
	/** Gibt eine Stringrepräsentation eines Campusbildes mit Farbinformationen in Hex zurück (nur zum testen). **/
	private String imgToString(Map<P<Integer,Integer>,Integer> img,int width, int height) {
		StringBuffer result = new StringBuffer();
		for (int y = 0; y<height; ++y) {
			for (int x = 0; x<width; ++x) {
				result.append(String.format("%06x",img.get(p(x,y))));
				result.append(" ");
			}
			result.append("\n");
		}		
		return result.toString();
	}
	
//	/** Gibt eine Stringrepräsentation des Bytearrays zurück (nur zum testen). **/
//	private String imgToString(byte img[][]) {
//		StringBuffer result = new StringBuffer();
//		int width = img[0].length;
//		for (byte line[] : img) {
//			for (int i = 0; i < width; i = i+3) {
//				result.append(String.format("%02x", line[i]));   // rot
//				result.append(String.format("%02x", line[i+1])); // grün
//				result.append(String.format("%02x", line[i+2])); // blau
//				result.append(" ");
//			}
//			result.append("\n");
//		}
//		return result.toString();
//	}
//
//	/** Gibt eine noch viel schönere Stringrepräsentation zurück (nur zum Testen). **/
//	private String imgToString2(Map<P<Integer,Integer>,Integer> img,int width, int height) {
//		StringBuffer result = new StringBuffer();
//		for (int y = 0; y<height; ++y) {
//			for (int x = 0; x<width; ++x) {
//				int color = img.get(p(x,y));
//				if (color == CampusFactory.WAND)
//					result.append("#");
//				else if (color == CampusFactory.FREI)
//					result.append(" ");
//				else if (color == CampusFactory.SPIELER)
//					result.append("O");
//				else if (color == CampusFactory.FELD)
//					result.append("X");
//				else if (color == CampusFactory.OFFEN)
//					result.append("+");
//				else if (color == CampusFactory.VERSCHLOSSEN)
//					result.append("+");
//			}
//			result.append("\n");
//		}		
//		return result.toString();
//	}
//
//	/** Konvertiert ein Bytearray (wie von readByteImage zurückgegeben) in eine Koordinatenmap mit
//	 *  P(width,height) als Schlüssel und 0xRRGGBB als Werten. */
//	private Map<P<Integer,Integer>,Integer> getColoredImage() {
//		Map<P<Integer,Integer>,Integer> result = new java.util.HashMap<P<Integer,Integer>,Integer>();
//		for (int i = 0; i<img.length; ++i) {
//			for (int j = 0; j<img[i].length; j=j+3) {
//				int r = img[i][j] << 8, g = img[i][j+1] << 4, b = img[i][j+2];
//				result.put(p(j,i), r|g|b);
//			}
//		}
//		return result;
//	}
}