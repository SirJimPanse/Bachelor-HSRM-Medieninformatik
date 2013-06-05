package de.medieninf.webanw.sakila;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;


public class SakilaBean implements Sakila, Serializable {
	private static final long serialVersionUID = 3572741847177964351L;
	
	private transient EntityManagerFactory emf;
    private transient ThreadLocal<EntityManager> em;
    private transient ThreadLocal<EntityTransaction> tx;
    
    public SakilaBean() {
    	init();
    }
    
    private void init() {
        emf = Persistence.createEntityManagerFactory("sakila");
        em = new ThreadLocal<EntityManager>();
        tx = new ThreadLocal<EntityTransaction>();    	
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
    	// just close before serialization
    	if (em.get() != null) 
    		em.get().close();
    	emf.close();
    	out.defaultWriteObject();
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    	in.defaultReadObject();
    	// new initialize after deserialization
    	init();
    }
    
    private void cEm() {
    	if (em.get() != null) 
    		em.get().close();
    	em.set(emf.createEntityManager());
    	tx.set(em.get().getTransaction());
    }
    
	@Override
	@SuppressWarnings("unchecked")
	public List<Actor> getActors() {
		cEm();
        String queryString = "SELECT a FROM Actor a";
        Query query = em.get().createQuery(queryString);
        List<Actor> actors = query.getResultList();
        return actors;
	}
	
	@Override
	public Actor getActor(int actorId) {
		cEm();
		String queryString = "SELECT a FROM Actor a WHERE a.actorId=:actorId";
        Query query = em.get().createQuery(queryString);
		query.setParameter("actorId", actorId);
		try {
			Actor actor = (Actor) query.getSingleResult();
			return actor;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public Actor update(Actor actor) {
		cEm();
		tx.get().begin();
		actor = em.get().merge(actor);
	    tx.get().commit();
	    return actor;
	}
	
	private Query genQuerySearchActor (String firstName, String lastName) {
		StringBuffer queryString = new StringBuffer("SELECT a FROM Actor a");
		String joinString = " WHERE ";
		if (firstName != null) {
			queryString.append(joinString + "a.firstName LIKE :firstName");
			joinString = " AND ";
		}
		if (lastName != null) {
			queryString.append(joinString + "a.lastName LIKE :lastName");
			joinString = " AND ";
		}
		Query query = em.get().createQuery(queryString.toString());
		if (firstName != null)
			query.setParameter("firstName", "%" + firstName + "%");
		if (lastName != null)
			query.setParameter("lastName", "%" + lastName + "%");
		return query;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Actor> searchActors(String firstName, String lastName) {
		cEm();
        Query query = genQuerySearchActor(firstName, lastName);
        List<Actor> actors = query.getResultList();
        return actors;	
	}
	
	@Override 
	@SuppressWarnings("unchecked")
	public List<Country> getCountries() {
		cEm();
        String queryString = "SELECT c FROM Country c";
        Query query = em.get().createQuery(queryString);
        List<Country> countries = query.getResultList();
        return countries;		
	}

	@Override
	public Country getCountry(int countryId) {
		cEm();
		String queryString = "SELECT c FROM Country c WHERE c.countryId=:countryId";
        Query query = em.get().createQuery(queryString);
		query.setParameter("countryId", countryId);
		try {
			Country country = (Country) query.getSingleResult();
			return country;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public Country update(Country country) {
		cEm();
		tx.get().begin();
		country = em.get().merge(country);
	    tx.get().commit();
	    return country;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<City> getCities() {
		cEm();
        String queryString = "SELECT c FROM City c";
        Query query = em.get().createQuery(queryString);
        List<City> cities = query.getResultList();
        return cities;		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<City> getCities(Country country) {
		cEm();
        String queryString = "SELECT c FROM City c WHERE c.country=:country";
        Query query = em.get().createQuery(queryString);
		query.setParameter("country", country);
        List<City> cities = query.getResultList();
        return cities;				
	}

	@Override
	public City getCity(int cityId) {
		cEm();
		String queryString = "SELECT c FROM City c WHERE c.cityId=:cityId";
        Query query = em.get().createQuery(queryString);
		query.setParameter("cityId", cityId);
		try {
			City city = (City) query.getSingleResult();
			return city;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public City update(City city) {
		cEm();
		tx.get().begin();
	    city = em.get().merge(city);
	    tx.get().commit();
	    return city;
	}
	
	@Override
	public void remove(City city) {
		cEm();
		tx.get().begin();
		city = em.get().merge(city);
		em.get().remove(city);
		tx.get().commit();
	}
	
	@Override
	public Store getStore(int storeId) {
		cEm();
		String queryString = "SELECT s FROM Store s WHERE s.storeId=:storeId";
        Query query = em.get().createQuery(queryString);
		query.setParameter("storeId", storeId);
		try {
			Store store = (Store) query.getSingleResult();
			return store;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Store> getStores() {
		cEm();
        String queryString = "SELECT s FROM Store s";
        Query query = em.get().createQuery(queryString);
        List<Store> stores = query.getResultList();
        return stores;					
	}
	
	@Override
	public Staff getStaff(int staffId) {
		cEm();
		String queryString = "SELECT s FROM Staff s WHERE s.staffId=:staffId";
        Query query = em.get().createQuery(queryString);
		query.setParameter("staffId", staffId);
		try {
			Staff staff = (Staff) query.getSingleResult();
			return staff;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public Paddress update(Paddress paddress) {
		cEm();
		tx.get().begin();
		paddress = em.get().merge(paddress);
	    tx.get().commit();
	    return paddress;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Customer> getCustomers() {
		cEm();
        String queryString = "SELECT c FROM Customer c";
        Query query = em.get().createQuery(queryString);
        List<Customer> customers = query.getResultList();
        return customers;			
	}

	@Override
	public Customer getCustomer(int customerId) {
		cEm();
		String queryString = "SELECT c FROM Customer c WHERE c.customerId=:customerId";
        Query query = em.get().createQuery(queryString);
		query.setParameter("customerId", customerId);
		try {
			Customer customer = (Customer) query.getSingleResult();
			return customer;
		} catch (NoResultException e) {
			return null;
		}		
	}
		
	private Query genQuerySearchCustomer (
			String firstName, String lastName, 
			Boolean active, Store store,
			String paddress, String paddress2, String district, String city, 
			String country, String postalCode, String phone) {	
		StringBuffer queryString = new StringBuffer("SELECT c FROM Customer c");
		String joinString = " WHERE ";
		if (firstName != null) {
			queryString.append(joinString + "c.firstName LIKE :firstName");
			joinString = " AND ";
		}
		if (lastName != null) {
			queryString.append(joinString + "c.lastName LIKE :lastName");
			joinString = " AND ";
		}
		if (active != null) {
			queryString.append(joinString + "c.active = :active");
			joinString = " AND ";
		}
		if (store != null){
			queryString.append(joinString + "c.store = :store");
			joinString = " AND ";
		}
		if (paddress != null){
			queryString.append(joinString + "c.paddress.paddress LIKE :paddress");
			joinString = " AND ";
		}		
		if (paddress2 != null){
			queryString.append(joinString + "c.paddress.paddress2 LIKE :paddress2");
			joinString = " AND ";
		}	
		if (district != null){
			queryString.append(joinString + "c.paddress.district LIKE :district");
			joinString = " AND ";
		}
		if (city != null){
			queryString.append(joinString + "c.paddress.city.city LIKE :city");
			joinString = " AND ";
		}
		if (country != null){
			queryString.append(joinString + "c.paddress.city.country.country LIKE :country");
			joinString = " AND ";
		}
		if (postalCode != null){
			queryString.append(joinString + "c.paddress.postalCode LIKE :postalCode");
			joinString = " AND ";
		}
		if (phone != null){
			queryString.append(joinString + "c.paddress.phone LIKE :phone");
			joinString = " AND ";
		}
		Query query = em.get().createQuery(queryString.toString());
		if (firstName != null)
			query.setParameter("firstName", "%" + firstName + "%");
		if (lastName != null)
			query.setParameter("lastName", "%" + lastName + "%");
		if (active != null)
			query.setParameter("active", active);
		if (store != null)
			query.setParameter("store", store);
		if (paddress != null)
			query.setParameter("paddress", "%" + paddress + "%");
		if (paddress2 != null)
			query.setParameter("paddress2", "%" + paddress2 + "%");
		if (district != null)
			query.setParameter("district", "%" + district + "%");
		if (city != null)
			query.setParameter("city", "%" + city + "%");
		if (country != null)
			query.setParameter("country", "%" + country + "%");
		if (postalCode != null)
			query.setParameter("postalCode", "%" + postalCode + "%");
		if (phone != null)
			query.setParameter("phone", "%" + phone + "%");
		return query;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Customer> searchCustomers(
			String firstName, String lastName, 
			Boolean active, Store store,
			String paddress, String paddress2, String district, String city, 
			String country, String postalCode, String phone) {
		cEm();
		// ToDo - Query String
        Query query = genQuerySearchCustomer(
        		firstName, lastName, active, store, 
        		paddress, paddress2, district, city, country, 
        		postalCode, phone);
        List<Customer> customers = query.getResultList();
        return customers;	
	}
	
	@Override
	public Customer update(Customer customer) {
		cEm();
		tx.get().begin();
		customer = em.get().merge(customer);
	    tx.get().commit();
	    return customer;		
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Category> getCategories() {
		cEm();
        String queryString = "SELECT c FROM Category c";
        Query query = em.get().createQuery(queryString);
        List<Category> categories = query.getResultList();
        return categories;	
	}

	@Override
	public Category getCategory(int categoryId) {
		cEm();
		String queryString = "SELECT c FROM Category c WHERE c.categoryId=:categoryId";
        Query query = em.get().createQuery(queryString);
		query.setParameter("categoryId", categoryId);
		try {
			Category category = (Category) query.getSingleResult();
			return category;
		} catch (NoResultException e) {
			return null;
		}		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Film> getFilms() {
		cEm();
        String queryString = "SELECT f FROM Film f";
        Query query = em.get().createQuery(queryString);
        List<Film> films = query.getResultList();
        return films;		
	}
	
	@Override
	public Film getFilm(int filmId) {
		cEm();
		String queryString = "SELECT f FROM Film f WHERE f.filmId=:filmId";
        Query query = em.get().createQuery(queryString);
		query.setParameter("filmId", filmId);
		try {
			Film film = (Film) query.getSingleResult();
			return film;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public Film update(Film film) {
		cEm();
		tx.get().begin();
		film = em.get().merge(film);
	    tx.get().commit();
	    return film;
	}		
	
	private Query genQuerySearchFilm(String title, String description, Category category, 
			Integer minRentalDuration, Integer maxRentalDuration, 
			BigDecimal minRentalRate, BigDecimal maxRentalRate,
			Integer minLength, Integer maxLength,
			BigDecimal minReplacementCost, BigDecimal maxReplacementCost,
			String rating) {
		StringBuffer queryString = new StringBuffer("SELECT f FROM Film f");
		String joinString = " WHERE ";
		if (title != null) {
			queryString.append(joinString + "f.title LIKE :title");
			joinString = " AND ";
		}
		if (description != null) {
			queryString.append(joinString + "f.description LIKE :description");
			joinString = " AND ";
		}
		if (category != null) {
			queryString.append(joinString + "f.category = :category");
			joinString = " AND ";
		}
		if (minRentalDuration != null) {
			queryString.append(joinString + "f.rentalDuration >= :minRentalDuration");
			joinString = " AND ";			
		}
		if (maxRentalDuration != null) {
			queryString.append(joinString + "f.rentalDuration <= :maxRentalDuration");
			joinString = " AND ";			
		}
		if (minLength != null) {
			queryString.append(joinString + "f.length >= :minLength");
			joinString = " AND ";			
		}
		if (maxLength != null) {
			queryString.append(joinString + "f.length <= :maxLength");
			joinString = " AND ";			
		}
		if (minRentalRate != null) {
			queryString.append(joinString + "f.rentalRate >= :minRentalRate");
			joinString = " AND ";			
		}
		if (maxRentalRate != null) {
			queryString.append(joinString + "f.rentalRate <= :maxRentalRate");
			joinString = " AND ";			
		}
		if (minReplacementCost != null) {
			queryString.append(joinString + "f.replacementCost >= :minReplacementCost");
			joinString = " AND ";			
		}
		if (maxReplacementCost != null) {
			queryString.append(joinString + "f.replacementCost <= :maxReplacementCost");
			joinString = " AND ";			
		}
		if (rating != null) {
			queryString.append(joinString + "f.rating LIKE :rating");
			joinString = " AND ";
		}		
		Query query = em.get().createQuery(queryString.toString());
		if (title != null)
			query.setParameter("title", "%" + title + "%");
		if (description != null)
			query.setParameter("description", "%" + description + "%");
		if (category != null)
			query.setParameter("category", category);
		if (minRentalDuration != null)
			query.setParameter("minRentalDuration", minRentalDuration);
		if (maxRentalDuration != null)
			query.setParameter("maxRentalDuration", maxRentalDuration);
		if (minLength != null)
			query.setParameter("minLength", minLength);
		if (maxLength != null)
			query.setParameter("maxLength", maxLength);
		if (minRentalRate != null)
			query.setParameter("minRentalRate", minRentalRate);
		if (maxRentalRate != null)
			query.setParameter("maxRentalRate", maxRentalRate);
		if (minReplacementCost != null)
			query.setParameter("minReplacementCost", minReplacementCost);
		if (maxReplacementCost != null)
			query.setParameter("maxReplacementCost", maxReplacementCost);
		if (rating != null)
			query.setParameter("rating", "%" + rating + "%");
		return query;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Film> searchFilms(String title, String description, Category category, 
			Integer minRentalDuration, Integer maxRentalDuration, 
			BigDecimal minRentalRate, BigDecimal maxRentalRate,
			Integer minLength, Integer maxLength,
			BigDecimal minReplacementCost, BigDecimal maxReplacementCost,
			String rating) {
		cEm();
        Query query = genQuerySearchFilm(title, description, category, 
    			minRentalDuration, maxRentalDuration, 
    			minRentalRate, maxRentalRate,
    			minLength, maxLength,
    			minReplacementCost, maxReplacementCost,
    			rating);
        List<Film> films = query.getResultList();
        return films;	
	}

	@Override
	public Inventory update(Inventory inventory) {
		cEm();
		tx.get().begin();
		inventory = em.get().merge(inventory);
	    tx.get().commit();
	    return inventory;
	}		
	
	@Override
	public Payment update(Payment payment) {
		cEm();
		tx.get().begin();
		payment = em.get().merge(payment);
	    tx.get().commit();
	    return payment;
	}	

	@Override
	public Rental update(Rental rental) {
		cEm();
		tx.get().begin();
		rental.setRentalDate(rental.getRentalDate()); // see comments in Rental, chop off
		rental = em.get().merge(rental);
	    tx.get().commit();
	    System.out.println("UUUU" + rental.version);
	    return rental;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Inventory> getRented(Customer customer) {
		cEm();
		Query query;
		if (customer == null) {
			String queryString = "SELECT r.inventory FROM Rental r WHERE r.returnDate IS NULL";
			query = em.get().createQuery(queryString);
		} else {
			String queryString = "SELECT r.inventory FROM Rental r WHERE r.customer=:customer AND r.returnDate IS NULL";
	        query = em.get().createQuery(queryString);
			query.setParameter("customer", customer);			
		}
		List<Inventory> inventories = query.getResultList();
		return inventories;		
	}
	
	@Override
	public void remove(Rental rental) {
		EntityManager lem = emf.createEntityManager();
		EntityTransaction ltx = lem.getTransaction();
		rental = lem.merge(rental);
		Customer customer = rental.getCustomer();
		List<Rental> rentals = customer.getRentals();
		rentals.remove(rental);
		customer.setRentals(rentals);
		Inventory inventory = rental.getInventory();
		rentals = inventory.getRentals();
		rentals.remove(rental);
		inventory.setRentals(rentals);
		ltx.begin();
		customer = lem.merge(customer);
		inventory = lem.merge(inventory);
		lem.remove(rental);
	    ltx.commit();
	}
	
	@Override
	public Rental rent(Inventory inventory, Customer customer, Staff staff) {
		EntityManager lem = emf.createEntityManager();
		EntityTransaction ltx = lem.getTransaction();
		if (!customer.isActive())
			return null;
		Rental rental = new Rental();
		rental.setCustomer(customer);
		rental.setStaff(staff);
		rental.setInventory(inventory);
		java.util.Date rentalDate = new java.util.Date();
		rental.setRentalDate(rentalDate); // rentalDate gets millisecs chopped off
		List<Rental> rentals= customer.getRentals();
		rentals.add(rental);
		customer.setRentals(rentals);
		rentals = inventory.getRentals();
		rentals.add(rental);
		inventory.setRentals(rentals);
		try {
			ltx.begin();
			rental = lem.merge(rental);
			customer = lem.merge(customer);
			inventory = lem.merge(inventory);
			ltx.commit();
			lem.close();
			return rental;
		} catch (OptimisticLockException e) {
			lem.close();
			return null;
		}
	}
	
	@Override
	public boolean giveBack(Inventory inventory) {
		EntityManager lem = emf.createEntityManager();
		EntityTransaction ltx = lem.getTransaction();
		String queryString = "SELECT r FROM Rental r WHERE r.inventory=:inventory AND r.returnDate IS NULL";
        Query query = lem.createQuery(queryString);
		query.setParameter("inventory", inventory);
		Rental rental = null;
		try {
			rental = (Rental) query.getSingleResult();
		} catch (NoResultException e) {
			// ignore;
		}
		if (rental == null)
			return false;
		rental.setReturnDate(new java.util.Date());
		try {
			ltx.begin();
			rental = lem.merge(rental);
			ltx.commit();
			lem.refresh(rental);
			lem.close();
			return true;
		} catch (OptimisticLockException e) { 
			// that one is not thrown but packaged ...
			lem.close();
			return false;
		} catch (RollbackException e) {
			// ... in that one
			lem.close();
			return false;
		}
     }
	
	@Override
	public EntityManager getEntityManager() {
		cEm();
		return em.get();
	}
	
}
