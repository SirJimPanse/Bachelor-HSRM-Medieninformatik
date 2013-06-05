package a4;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import de.medieninf.webanw.sakila.Customer;
import de.medieninf.webanw.sakila.Inventory;
import de.medieninf.webanw.sakila.Rental;
import de.medieninf.webanw.sakila.SakilaBean;

/**
 * Bean für Kunden
 * passender Kunde wird beim Aufruf anhand der Sessiondaten gesetzt
 * 
 * @author shard001
 */
@ManagedBean(name="CustomerBean")
@SessionScoped
public class CustomerBean {

	public SakilaBean sb = new SakilaBean();
	private Customer c;

	public void setCustomer(int id) {
		c = sb.getCustomer(id);
	}

	public String getFirstName() {
		return c.getFirstName();
	}

	public String getLastName() {
		return c.getLastName();
	}
	
	public int getCustomerId() {
    	return c.getCustomerId();
    }
	
	/**
	 * @return		Liste aller ausgeliehenen und nicht zurückgegebenen Kopien
	 */
	public List<Inventory> getNotReturnedInventories(){ 
		ArrayList<Inventory> notReturned = new ArrayList<Inventory>();
		
		for (Rental r : c.getRentals()){
			if (r.getReturnDate()== null){
				notReturned.add(r.getInventory());
			}
		}
		
		return (notReturned.size() > 0) ? notReturned : null;  // wenn einträge vorhanden, liste zurück ansonsten null zwecks erkennen leerer liste
	}
	
	
}
