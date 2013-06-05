package CamptureTheFlag.logik.inits;

import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import CamptureTheFlag.logik.entities.Feld;
import CamptureTheFlag.logik.entities.GameObject;
import CamptureTheFlag.logik.entities.Ingame;

/**
 * Stellt zuvor gespeicherte Spiele zum Laden bereit
 * 
 * @author dgens001
 */
public class GameRepository {
	
	public static String saveGameFolder = "./src/CamptureTheFlag/res/"; 
	public static Logger log = Logger.getLogger(GameRepository.class.getName());
	
	public static String FILE_EXT = ".ctfsav" + GameFactory.VERSION;
	
	public static Ingame deserialize(String dateiPfadUndName) {
		Ingame res = null;
		
		try {
		    FileInputStream file = new FileInputStream(GameRepository.saveGameFolder + dateiPfadUndName);
		    ObjectInputStream o = new ObjectInputStream(file);
		    res = (Ingame)o.readObject();
		    o.close();
		} catch (Exception e) { System.err.println(e); }
		
		for (GameObject go : res)
			go.setNotify(new PropertyChangeSupport(go));

		for (Feld f : res.getCampus())
			f.setNotify(new PropertyChangeSupport(f));
				
		res.getSpieler().setNotify(new PropertyChangeSupport(res));
		res.setNotify(new PropertyChangeSupport(res));

		return res;
	}
	
	/** Serialisiert das InGame-Objekt und schreibt es als Bytestrom auf die Festplatte (mit JSA), gibt den Speicherpfad zurück. **/
	public static String serialize(Ingame game) {
		Date dt = new Date();
		SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd_HH-mm-ss" );
		String filename = GameRepository.saveGameFolder + df.format(dt) + GameRepository.FILE_EXT;
		
		try {
			FileOutputStream file = new FileOutputStream(filename);
			ObjectOutputStream o = new ObjectOutputStream(file);
			o.writeObject(game); // schreibe komplettes Ingame-Objekt samt aller referenzierten Objekte
			o.close();
	    } catch (IOException e) {
	    	if (e instanceof NotSerializableException) {
	    		log.log(Level.WARNING, "Serialisierung fehlgeschlagen");
	    		log.log(Level.WARNING, e.getMessage());
	    		e.printStackTrace();
	    	} else {
	    		log.log(Level.WARNING, "Savegame schreiben fehlgeschlagen:");
	    		log.log(Level.WARNING, e.getMessage());
	    		e.printStackTrace();
	    	}
	    }
		return filename;
	}
}
