package de.medieninf.webanw.sakila.backingbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import de.medieninf.webanw.sakila.Customer;
import de.medieninf.webanw.sakila.Inventory;
import de.medieninf.webanw.sakila.Rental;

/** Wrapperklasse um Inventory mit Utilty Methoden **/
public class InventoryWrapper implements Serializable {

	private static final long serialVersionUID = 6109942751718731791L;
	
	public Inventory inv;
	
	public InventoryWrapper() { }
	
	public InventoryWrapper(Inventory i) {
		this.inv = i;
	}
	
	public void setInv(Inventory i) { this.inv = i; }
	public Inventory getInv() { return this.inv; }
	
    public Customer getCustomer(){
    	if(isRented()) {
        	for(Rental r : this.inv.getRentals()) {
        		if(r.getReturnDate()== null)
        			return 	r.getCustomer();
        	}
    	}
    	return null;
    }
    
    public boolean isRented(){
    	ArrayList<Date> returnDates = new ArrayList<Date>();
    	for(Rental r : this.inv.getRentals()) {
    		if(r.getReturnDate()!= null)
    			returnDates.add(r.getReturnDate());
    	}
    	return this.inv.getRentals().size() != returnDates.size();
    }
}