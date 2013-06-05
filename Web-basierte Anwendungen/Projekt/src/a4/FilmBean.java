package a4;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import de.medieninf.webanw.sakila.Actor;
import de.medieninf.webanw.sakila.Film;
import de.medieninf.webanw.sakila.Inventory;
import de.medieninf.webanw.sakila.SakilaBean;

/**
 * Bean für Filme
 * passender Kunde wird beim Aufruf anhand der Sessiondaten gesetzt
 * 
 * @author shard001
 */
@ManagedBean(name="FilmBean")
@SessionScoped
public class FilmBean {

	public SakilaBean sb = new SakilaBean();
	private Film f;

	public void setFilm(int id) {
		f = sb.getFilm(id);
	}
	
	public String getTitle() { return f.getTitle(); }
	public int getFilmId() { return f.getFilmId(); }
	public String getDescription(){return f.getDescription();}
	public String getCategory(){return f.getCategory().getName();}
	public String getLength(){return new Integer(f.getLength()).toString();}
	public String getRentalDuration(){return new Integer(f.getRentalDuration()).toString();}
	public String getRentalRate(){return f.getRentalRate().toString();}
	public String getReplacementCost(){return f.getReplacementCost().toString();}
	public String getRating(){return f.getRating();}
	public List<InventoryWrapper> getInventories(){
		List<InventoryWrapper> result = new ArrayList<InventoryWrapper>();
		for (Inventory i : f.getInventories())
			result.add(new InventoryWrapper(i));
		return result;
	}
	public List<Actor> getActors(){return f.getActors();}
	
	
	/**
	 * @return		liste von nicht nicht ausgeliehenen kopien
	 */
	public List<InventoryWrapper> getFreeInventories() {
		List<InventoryWrapper> ret = new ArrayList<InventoryWrapper>();
		
		for (Inventory i : f.getInventories()) {
			InventoryWrapper inv = new InventoryWrapper(i);
			if (!inv.isRented()) {
				ret.add(inv);
			}
		}
		
		return (ret.size() > 0) ? ret : null; // wenn einträge vorhanden, liste zurück ansonsten null zwecks erkennen leerer liste
	}
	
}
