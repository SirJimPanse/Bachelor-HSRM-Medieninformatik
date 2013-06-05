package de.medieninf.webanw.sakila;

import javax.persistence.*;
import java.util.List;
import java.math.BigDecimal;

/**
 * Routines to interact with sakila database
 * @author peter
 */
public interface Sakila {

	/**
	 * Available actors
	 * @return List of all actors
	 */
	List<Actor> getActors();
	
	/**
	 * One Actor identified by actorId
	 * @param actorId 
	 * @return Actor if found, null otherwise
	 */
	Actor getActor(int actorId);

	/**
	 * Update changes to an actor or create new one
	 * @param actor
	 * @return updated actor
	 */
	Actor update(Actor actor);

	/**
	 * Searches actors. Searchable attributes as parameters.
	 * Boolean AND for all search parameters.
	 * Parameter is ignored if null.
	 * For all String parameters the search succeeds if the parameter
	 * is a substring of the searched Entity.
	 * @param firstName String 
	 * @param lastName String 
	 */
	List<Actor> searchActors(String firstName, String lastName);

	
	/**
	 * Available countries
	 * @return List of all countries
	 */
	List<Country> getCountries();

	/**
	 * One country identified by countryId
	 * @param countryId
	 * @return Country if found, null otherwise
	 */
	Country getCountry(int countryId);
	
	/**
	 * Update changes to a country or create new one
	 * @param country
	 * @return updated country
	 */
	Country update(Country country);

	/**
	 * Available cities
	 * @return List of all cities
	 */
	List<City> getCities();

	/**
	 * Available cities in a country
	 * @param Country
	 * @return List of all cities in that country
	 */
	List<City> getCities(Country country);

	/**
	 * One city identified by cityId
	 * @param cityId
	 * @return City if found, null otherwise
	 */
	City getCity(int cityId);
	
	/**
	 * Update changes to a city or create new one
	 * @param City
	 * @return updated city
	 */
	City update(City city);
	
	/**
	 * Removes a city from the underlying database
	 * @param City
	 */
	void remove(City city);

	/**
	 * One store identified by storeId
	 * @param storeId
	 * @return Store if found, null otherwise
	 */
	Store getStore(int storeId);

	/**
	 * Available stores
	 */
	List<Store> getStores();
	
	/**
	 * One staff member identified by staffId
	 * @param staffId
	 * @return Staff if found, null otherwise
	 */
	Staff getStaff(int staffId);

	/**
	 * Update changes to a Paddress or create new one
	 * @param Paddress
	 * @return updated paddress
	 */
	Paddress update(Paddress paddress);

	/**
	 * Available Customers
	 * @return List of all customers
	 */
	List<Customer> getCustomers();

	/**
	 * One customer identified by customerId
	 * @param customerId
	 * @return Customer if found, null otherwise
	 */
	Customer getCustomer(int customerId);

	/**
	 * Searches customers. Searchable attributes as parameters.
	 * Boolean AND for all search parameters.
	 * Parameter is ignored if null.
	 * For all String parameters the search succeeds if the parameter
	 * is a substring of the searched Entity.
	 * If parameter is not a String it must be equals. 
	 * @param firstName
	 * @param lastName
	 * @param active
	 * @param store
	 * @param paddress
	 * @param paddress2
	 * @param district
	 * @param city
	 * @param country
	 * @param postalCode
	 * @param phone
	 * @return
	 */
	List<Customer> searchCustomers(String firstName, String lastName, Boolean active,
			Store store,
			String paddress, String paddress2, String district, String city, 
			String country, String postalCode, String phone);

	/**
	 * Update changes to a customer or create new one
	 * @param Customer
	 * @return updated Customer
	 */	
	Customer update(Customer customer);

	
	/**
	 * Available categories
	 * @return List of all categories
	 */	
	List<Category> getCategories();
	
	/**
	 * One category identified by categoryId
	 * @param categoryId
	 * @return Category if found, null otherwise
	 */
	Category getCategory(int categoryId);
	
	
	/**
	 * All Films
	 * @return List of all films
	 */
	List<Film> getFilms();
	
	/**
	 * One customer identified by filmId
	 * @param filmId
	 * @return Film if found, null otherwise
	 */
	Film getFilm(int filmId);
	
	/**
	 * Update changes to a Film or create new one
	 * @param Film
	 * @return updated Film
	 */	
	Film update(Film film);
	
	/**
	 * Searches films. Searchable attributes as parameters.
	 * Boolean AND for all search parameters.
	 * Parameter is ignored if null.
	 * For all String parameters the search succeeds if the parameter
	 * is a substring of the searched Entity.
	 * If parameter comes in pairs starting with min/max, then value
	 * must be within min and max.
	 * If parameter is not a String it must be equals.  
	 * @param title String
	 * @param description String
	 * @param category Category
	 * @param minRentalDuration Integer
	 * @param maxRentalDuration Integer
	 * @param minRentalRate BigDecimal
	 * @param maxRentalRate BigDecimal
	 * @param minLength Integer
	 * @param maxLength Integer
	 * @param minReplacementCost BigDecimal
	 * @param maxReplacementCost BigDecimal
	 * @param rating String
	 * @return List of found films
	 */
	List<Film> searchFilms(String title, String description, Category category, 
			Integer minRentalDuration, Integer maxRentalDuration, 
			BigDecimal minRentalRate, BigDecimal maxRentalRate,
			Integer minLength, Integer maxLength,
			BigDecimal minReplacementCost, BigDecimal maxReplacementCost,
			String rating);
	
	
	/**
	 * Update changes to an Inventory or create new one
	 * @param Inventory
	 * @return updated Inventory
	 */	
	Inventory update(Inventory inventory);
	
	/**
	 * Update changes to a Payment or create new one
	 * @param Payment
	 * @return updated Payment
	 */	
	Payment update(Payment payment);
	
	/**
	 * Update changes to a Rental or create new one
	 * @param Payment
	 * @return updated Payment
	 */	
	Rental update(Rental rental);
	
	/**
	 * Rented film copies
	 * @param customer of that customer if non null
	 * @return currently rented films, all or by that customer
	 */
	List<Inventory> getRented(Customer customer);
	
	/**
	 * Remove rental
	 */
	void remove(Rental rental);
	
	/**
	 * Rent a film copy if available
	 * @param inventory The film copy to be rented
	 * @param customer rented by that customer
	 * @param staff managed by this staff member
	 * @return valid Rental iff the film copy is successfully rented, null otherwise
	 */
	Rental rent(Inventory inventory, Customer customer, Staff staff);
	
	/**
	 * Returns a film copy
	 * @param inventory the rented film copy
	 * @return true iff the film was rented and is successfully returned
	 */
	boolean giveBack(Inventory inventory);
	
	
	/**
	 * Reset and get entity manager. Should not be necessary.
	 */
	EntityManager getEntityManager();	
	
}
