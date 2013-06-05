package de.medieninf.webanw.sakila.backingbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import a4.InventoryWrapper;

import de.medieninf.webanw.sakila.Actor;
import de.medieninf.webanw.sakila.Customer;
import de.medieninf.webanw.sakila.Film;
import de.medieninf.webanw.sakila.Inventory;
import de.medieninf.webanw.sakila.Rental;

public class FilmDetailsBackingBean extends PageBackingBean<Film> implements Serializable {

	private static final long serialVersionUID = 8293179224574865194L;

	private Film film;
	private InventoryWrapper inventory;
	
	public FilmDetailsBackingBean() {}

	
	/**
	 * Methoden zum setzen des Customers im CuostmerDetailsBackingBean
	 * 
	 * @param ae ActionEvent um zu erkennen welcher Customer geklickt wurde
	 */
	public void changeToCustomer(ActionEvent ae){
		CustomerDetailsBackingBean cdbb = gb("cdbb", CustomerDetailsBackingBean.class);
		Customer customer = (Customer)ae.getComponent().getAttributes().get("customer");
		cdbb.setCustomer(customer);
	}
	
	/**
	 * Mit dieser Methode kann ein Film zurückgegeben werden.
	 * 
	 * @param ae ActionEvent um zu erkennen welches Inventory geklickt wurde.
	 */
	public void giveBack(ActionEvent ae){
		Inventory inventory = (Inventory)ae.getComponent().getAttributes().get("inv");
		
		
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
	
	public Film getFilm() {	return film;}
	public void setFilm(Film film) {this.film = film;}
	
	public String getTitle(){return film.getTitle();}
	public String getDescription(){return film.getDescription();}
	public String getCategory(){return film.getCategory().getName();}
	public String getFilmId(){return new Integer(film.getFilmId()).toString();}
	public String getLength(){return new Integer(film.getLength()).toString();}
	public String getRentalDuration(){return new Integer(film.getRentalDuration()).toString();}
	public String getRentalRate(){return film.getRentalRate().toString();}
	public String getReplacementCost(){return film.getReplacementCost().toString();}
	public String getRating(){return film.getRating();}
	public List<InventoryWrapper> getInventories(){
		List<InventoryWrapper> result = new ArrayList<InventoryWrapper>();
		for (Inventory i : film.getInventories())
			result.add(new InventoryWrapper(i));
		return result;
	}
	

	public List<Actor> getActors(){return film.getActors();}


	public InventoryWrapper getInventory() {
		return inventory;
	}


	public void setInventory(Inventory inventory) {
		this.inventory = new InventoryWrapper(inventory);
	}


	@Override
	public void update(ActionEvent ae) {
		film = gsb().getFilm(film.getFilmId());
	}
}