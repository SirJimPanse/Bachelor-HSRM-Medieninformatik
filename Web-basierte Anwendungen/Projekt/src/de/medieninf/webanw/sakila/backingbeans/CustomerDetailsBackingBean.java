package de.medieninf.webanw.sakila.backingbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.OptimisticLockException;

import de.medieninf.webanw.sakila.City;
import de.medieninf.webanw.sakila.Country;
import de.medieninf.webanw.sakila.Customer;
import de.medieninf.webanw.sakila.Inventory;
import de.medieninf.webanw.sakila.Rental;
import de.medieninf.webanw.sakila.Store;

public class CustomerDetailsBackingBean extends PageBackingBean<Customer> implements Serializable {

	private static final long serialVersionUID = -7073413711905311096L;
	private Customer customer;

	public CustomerDetailsBackingBean() 	{ }

	public void setCustomer(Customer value) { this.customer = value; }
	public void setFirstName(String value)  { this.customer.setFirstName(value); }
	public void setLastName(String value)   { this.customer.setLastName(value); }
	public void setPhone(String value)      { this.customer.getPaddress().setPhone(value); }
	public void setStore(String value) 		{ this.customer.setStore(gsb().getStore(Integer.valueOf(value))); }
	public void setStreet(String value) 	{ this.customer.getPaddress().setPaddress(value); }
	public void setPostal(String value)     { this.customer.getPaddress().setPostalCode(value); }
	public void setCity(String value) 		{ this.customer.getPaddress().setCity(gsb().getCity(Integer.valueOf(value))); }
	public void setCountry(String value) 	{ this.customer.getPaddress().getCity().setCountry(gsb().getCountry(Integer.valueOf(value))); }

	public Customer      getCustomer()  	{ return this.customer; }
	public String        getFirstName() 	{ return this.customer.getFirstName(); }
	public String        getLastName()  	{ return this.customer.getLastName(); }
	public String        getPhone()     	{ return this.customer.getPaddress().getPhone(); }
	public String        getStore()     	{ return Integer.toString(this.customer.getStore().getStoreId()); }
	public String        getStreet()    	{ return this.customer.getPaddress().getPaddress(); }
	public String        getPostal()    	{ return this.customer.getPaddress().getPostalCode(); }
	public String 		 getCity()      	{ return Integer.toString(this.customer.getPaddress().getCity().getCityId()); }
	public String        getCountry()   	{ return Integer.toString(this.customer.getPaddress().getCity().getCountry().getCountryId()); }

	public List<City>    getCities()  		{ return gsb().getCities(); }
	public List<Store>   getStores() 		{ return gsb().getStores(); }
	public List<Country> getCountries() 	{ return gsb().getCountries(); }

	public List<InventoryWrapper> getNotReturnedInventories(){ 
		ArrayList<InventoryWrapper> notReturned = new ArrayList<InventoryWrapper>();
		for(Rental r : this.customer.getRentals()){
			if(r.getReturnDate()== null)
				notReturned.add(new InventoryWrapper(r.getInventory()));
		}
		return notReturned;
	}

	/**
	 * Ruft giveBack der SakilaBean aus mit dem vom ActionEvent mitgegebenen Inventory
	 * und setzt darauf dass returnDate
	 * 
	 * @param ae
	 */
	public void giveBack(ActionEvent ae){
		Inventory inventory = (Inventory)ae.getComponent().getAttributes().get("invent");
		
		boolean successfullyReturned = gsb().giveBack(inventory);
		if(successfullyReturned){
			for (Rental r : inventory.getRentals()){
				if(r.getReturnDate() == null){
					java.util.Date date = new java.util.Date();
					r.setReturnDate(date);
				}
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Film has already been returned!"));
			update(null);
		}
	}

	public String submit() {
		try {
			gsb().update(customer);
			gsb().update(customer.getPaddress());
		} catch (OptimisticLockException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Data changed during update, updated failed!"));
			cancel();
		}
		this.customer = gsb().getCustomer(customer.getCustomerId());
		return "update";
	}
	
	public String cancel() {
		this.customer = gsb().getCustomer(customer.getCustomerId());
		gb("cbb",CustomersBackingBean.class).setCustomers(gsb().getCustomers());
		return "cancel";
	}

	@Override
	public void update(ActionEvent ae) {
		customer = gsb().getCustomer(customer.getCustomerId());		
	}
}