package CamptureTheFlag.darstellung;

import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 * stellt verschiedene Helper-Methoden bereit
 * die in der Darstellung mehrmals verwendet werden
 * 
 * @author shard001
 */
public class DarstellungHelper {
	
	private static Logger log = Logger.getLogger(Fenster.class.getName());
	private static final String IMGPATH = "src/CamptureTheFlag/res/";
	
	/**
	 * Helper, um von einem String (meist in GameObject), das ImageIcon zu
	 * bekommen
	 * 
	 * @param url		String mit Pfad zum Bild
	 * @return 			das geladene ImageIcon
	 */
	public static ImageIcon getImage(String url) {
		try {
			ImageIcon image = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IMGPATH + url));
			return image;
		} catch (NullPointerException e) {
			log.log(Level.WARNING, "Bild nicht gefunden");
			return null;
		}
	}
	
	/**
	 * skaliert ein Bild auf die vorgebenen Abmessungen
	 * oder auf Standard-Abmessungen von 40x40
	 * 
	 * @param img		Das Bild
	 * @param width		gewünschte Max-Breite
	 * @param height	gewünschte Max-Höhe
	 * @return			das skalierte ImageIcon
	 */
	public static ImageIcon imageScale(ImageIcon img, int width, int height) {
		try {
			Image imgage = img.getImage();
			Image scaledImage = imgage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
			return scaledImageIcon;
		} catch (NullPointerException e) {
			log.log(Level.WARNING, "Bild konnte nicht skaliert werden");
			return null;
		}
	}
	public static ImageIcon imageScale(ImageIcon img) {
		return imageScale(img, 40, 40);
	}

}
