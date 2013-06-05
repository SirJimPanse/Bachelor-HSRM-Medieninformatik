package de.medieninf.webanw.sakila;

import javax.persistence.*;
import java.util.List;
import java.math.BigDecimal;
import org.junit.*;

public class TestSakila {

	private SakilaBean sb;
	
	
	@Before
	public void setUp() throws Exception {
		sb = new SakilaBean();
	}

	@Test
	public void testActor() {
		List<Actor> ls = sb.getActors();
		Assert.assertEquals(200, ls.size());
		int actorId = 1;
		Actor a = sb.getActor(actorId);
		Assert.assertEquals(actorId, a.getActorId());
		Assert.assertEquals("PENELOPE", a.getFirstName());
		Assert.assertEquals("GUINESS", a.getLastName());
		actorId = 42;
		a = sb.getActor(actorId);
		Assert.assertEquals(actorId, a.getActorId());
		Assert.assertEquals("TOM", a.getFirstName());
		Assert.assertEquals("MIRANDA", a.getLastName());
		String firstName = a.getFirstName();
		String lastName = a.getLastName();
		String firstName_new = "Hrun";
		String lastName_new = "The Barbarian";
		a.setFirstName(firstName_new);
		a.setLastName(lastName_new);
		Assert.assertEquals(firstName_new, a.getFirstName());
		Assert.assertEquals(lastName_new, a.getLastName());
		sb.update(a);
		Actor a_new = sb.getActor(actorId);
		Assert.assertEquals(firstName_new, a_new.getFirstName());
		Assert.assertEquals(lastName_new, a_new.getLastName());
		a_new.setFirstName(firstName);
		a_new.setLastName(lastName);
		sb.update(a_new);
		a = sb.getActor(actorId);
		Assert.assertEquals(firstName, a.getFirstName());
		Assert.assertEquals(lastName, a.getLastName());
		// search Actor
		List<Actor> actors = sb.searchActors("ARY", null);
		Assert.assertEquals(7, actors.size());
		actors = sb.searchActors("PENELOPE", "GUINESS");
		Assert.assertEquals(1, actors.size());		
		Assert.assertEquals(1, actors.get(0).getActorId());
	}
	
	@Test
	public void changeAllActors() {
		boolean doIt = false; // expensive, takes long
		if (!doIt) 
			return;
		for (int i=1; i<=200; i++) {
			Actor a = sb.getActor(i);
			String firstName = a.getFirstName();
			String lastName = a.getLastName();
			String firstName_new = "Majikthise";
			String lastName_new = "Vroomfondel";
			a.setFirstName(firstName_new);
			a.setLastName(lastName_new);
			sb.update(a); // change
			Actor a_new = sb.getActor(i);
			Assert.assertEquals(firstName_new, a_new.getFirstName());
			Assert.assertEquals(lastName_new, a_new.getLastName());
			a_new.setFirstName(firstName);
			a_new.setLastName(lastName);
			sb.update(a_new); // change back
		}
	}
	
	@Test
	public void testCountry() {
		List<Country> lc = sb.getCountries();
		Assert.assertEquals(109, lc.size());
		int countryId = 1;
		Country c = sb.getCountry(countryId);
		Assert.assertEquals(countryId, c.getCountryId());
		Assert.assertEquals("Afghanistan", c.getCountry());
		countryId = 38;
		c = sb.getCountry(countryId);
		Assert.assertEquals(countryId, c.getCountryId());
		Assert.assertEquals("Germany", c.getCountry());
		String old = c.getCountry();
		String neu = "Deutschland";
		c.setCountry(neu);
		sb.update(c);
		Country c2 = sb.getCountry(countryId);
		Assert.assertEquals(neu, c2.getCountry());		
		c2.setCountry(old);
		sb.update(c2);
		c = sb.getCountry(countryId);
		Assert.assertEquals(old, c.getCountry());
	}
	
	@Test
	public void testCity() {
		List<City> ls = sb.getCities();
		Assert.assertEquals(600, ls.size());
		int cityId = 1;
		City c = sb.getCity(cityId);
		Assert.assertEquals(cityId, c.getCityId());
		Assert.assertEquals("A Corua (La Corua)", c.getCity());
		cityId = 575;
		c = sb.getCity(cityId);
		Assert.assertEquals(cityId, c.getCityId());
		Assert.assertEquals("Witten", c.getCity());
		Country germany = sb.getCountry(38);
		Assert.assertEquals(germany, c.getCountry());
		String city = c.getCity();
		String city_new = "Wiesbaden";
		c.setCity(city_new);
		Country france = sb.getCountry(34);
		c.setCountry(france);
		Assert.assertEquals(city_new, c.getCity());
		Assert.assertEquals(france, c.getCountry());
		sb.update(c);
		City c_new = sb.getCity(cityId);
		Assert.assertEquals(city_new, c_new.getCity());
		Assert.assertEquals(france, c_new.getCountry());
		c_new.setCity(city);
		c_new.setCountry(germany);
		sb.update(c_new);
		c = sb.getCity(cityId);
		Assert.assertEquals(city, c.getCity());
		Assert.assertEquals(germany, c.getCountry());
		// new cities
		City c1 = new City();
		c1.setCity("Wiesbaden");
		c1.setCountry(germany);
		c1 = sb.update(c1);
		c1.getCityId();
		City c2 = new City();
		c2.setCity("Mainz");
		c2.setCountry(germany);
		c2 = sb.update(c2);
		c2.getCityId();
		// remove new cities
		sb.remove(c1);
		sb.remove(c2);
		// Attention; sequence has advanced!; modifying test
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testPaddress() {
		EntityManager em = sb.getEntityManager();
		Query query = em.createQuery("SELECT p FROM Paddress p");		
		List<Paddress> lp = (List<Paddress>) query.getResultList();
		Assert.assertEquals(603, lp.size());
		int paddressId = 1;
		Paddress pa = em.find(Paddress.class, paddressId);
		Assert.assertEquals(paddressId, pa.getPaddressId());
		Assert.assertEquals("47 MySakila Drive", pa.getPaddress());
		City city = pa.getCity();
		City witten = sb.getCity(575);
		pa.setCity(witten);
		sb.update(pa);
		em = sb.getEntityManager();
		Paddress pa2 = em.find(Paddress.class, paddressId);
		Assert.assertEquals(witten, pa2.getCity());
		pa2.setCity(city);
		sb.update(pa2);
		em = sb.getEntityManager();
		pa = em.find(Paddress.class, paddressId);
		Assert.assertEquals(city, pa.getCity());
	}
	
	@Test
	public void testStaffStore() {
		EntityManager em = sb.getEntityManager();
		Store store1 = em.find(Store.class, 1);
		Staff manager1 = store1.getManager();		
		Assert.assertEquals(1, manager1.getStaffId());
		Paddress paddress = store1.getPaddress();
		Paddress paddress2 = em.find(Paddress.class, paddress.getPaddressId());
		Assert.assertEquals(paddress, paddress2);		
	}
	
	@Test
	public void testCustomer() {
		List<Customer> lc = sb.getCustomers();
		Assert.assertEquals(599, lc.size());
		int customerId = 1;
		Customer c = sb.getCustomer(customerId);
		Assert.assertEquals(customerId, c.getCustomerId());
		Assert.assertEquals("MARY", c.getFirstName());
		String firstName = "ARY";
		lc = sb.searchCustomers(
				firstName, null, null, null, 
				null, null, null, null, null, null, null);
		Assert.assertEquals(5, lc.size());
		String paddress = "Parkway";
		lc = sb.searchCustomers(
				firstName, null, null, null, 
				paddress, null, null, null, null, null, null);
		Assert.assertEquals(1, lc.size());
		c = lc.get(0);
		Assert.assertEquals("ROSEMARY", c.getFirstName());
		Store store1 = sb.getStore(1); 
		lc = sb.searchCustomers(
				"MARY", "SMITH", true, store1, 
				"1913 Hanoi Way", "", "Nagasaki", "Sasebo", "Japan", 
				"35200", "28303384290");		
		Assert.assertEquals(1, lc.size());
		c = lc.get(0);
		Assert.assertEquals(1, c.getCustomerId());
		c = sb.getCustomer(1);
		firstName = c.getFirstName();
		String newFirstName = "Hululu";
		c.setFirstName(newFirstName);
		c = sb.update(c);
		Customer c2 = sb.getCustomer(1);
		Assert.assertEquals(newFirstName, c2.getFirstName());
		c2.setFirstName(firstName);
		c2 = sb.update(c2);
		c = sb.getCustomer(1);
		Assert.assertEquals(firstName, c.getFirstName());		
	}
	
	@Test
	public void testFilm() {
		List<Film> lf = sb.getFilms();
		Assert.assertEquals(1000, lf.size());
		int filmId = 1;
		Film f = sb.getFilm(filmId);
		Assert.assertEquals(filmId, f.getFilmId());
		Assert.assertEquals("ACADEMY DINOSAUR", f.getTitle());
		List<Actor> actors = f.getActors();
		Assert.assertEquals(10, actors.size());
		String title = f.getTitle();
		String title_new = "Fachhochschule Eichhörnchen";
		f.setTitle(title_new);
		sb.update(f);
		Film f_new = sb.getFilm(filmId);
		Assert.assertEquals(title_new, f_new.getTitle());
		f_new.setTitle(title);
		sb.update(f_new);
		f = sb.getFilm(filmId);
		Assert.assertEquals(title, f.getTitle());
		// Actors
		Actor actor = sb.getActor(1);
		List<Film> films = actor.getFilms();
		Assert.assertEquals(19, films.size());
		// search Film
		films = sb.searchFilms("ACADEMY", null, null, null, null, null, null, null, null, null, null, null);
		Assert.assertEquals(2, films.size());
		Category documentary = sb.getCategory(6);
		films = sb.searchFilms(
				"ACADEMY DINOSAUR",
				"A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies",
				documentary, 
				6, 6, 
				new BigDecimal("0.99"), new BigDecimal("0.99"), 
				86, 86, 
				new BigDecimal("20.99"), new BigDecimal("20.99"), 
				"PG");
		Assert.assertEquals(1, films.size());
		Assert.assertEquals(1, films.get(0).getFilmId());
	}
	
	@Test
	public void testInventory() {
		int filmId = 1;
		Film f = sb.getFilm(filmId);
		List<Inventory> inventories = f.getInventories();
		Assert.assertEquals(8, inventories.size());
	}
	
	@Test
	public void testPayment() {
		Customer c = sb.getCustomer(1);
		List<Payment> payments = c.getPayments();
		Assert.assertEquals(32, payments.size());		
	}
	
	@Test
	public void testRental() {
		Customer c = sb.getCustomer(1);
		List<Rental> rentals = c.getRentals();
		Assert.assertEquals(32, rentals.size());
		Film f = sb.getFilm(1);
		List<Inventory> inventories = f.getInventories();
		Assert.assertEquals(8, inventories.size());
		int[] inventoryIds =        {1,2,3,4,5,6,7,8};
		int[] rentalsPerInventory = {3,5,2,2,5,5,4,2};
		for (Inventory inventory : inventories) {
			int size = 0;
			for (int i=0; i < inventoryIds.length; i++) {
				if (inventoryIds[i] == inventory.getInventoryId())
					size = rentalsPerInventory[i];
			}
			Assert.assertTrue(size != 0);
			Assert.assertEquals(size, inventory.getRentals().size());		
		}
	}

	@Test
	public void testRented() {
		List<Inventory> inventories = sb.getRented(null);
		Assert.assertEquals(184, inventories.size());	
	}
	
	@Test
	public void testRentandGiveBack() {
		Customer customer = sb.getCustomer(1);
		Film film = sb.getFilm(1);
		Staff staff = sb.getStaff(1);
		Inventory inventory = film.getInventories().get(0);
		// Note, that no copies of film 1 are currently rented
		Rental rental = sb.rent(inventory, customer, staff);
		doSleep(1); // wait a second - to get a different timestamp
		boolean ok = sb.giveBack(rental.getInventory());
		Assert.assertEquals(true, ok);
		rental = null;
		inventory = sb.getFilm(1).getInventories().get(0); // refresh
		for (Rental r : inventory.getRentals()) {
			if (r.getCustomer().equals(customer)) { // rented only once!
				rental = r;
				break;
			}
		}
		Assert.assertEquals(film, rental.getInventory().getFilm());
		Assert.assertEquals(staff, rental.getStaff());
		sb.remove(rental);
	}
	
	
	private void doSleep(int secs) {
		try {
			Thread.sleep(secs*1000);
		} catch (InterruptedException e) {
			// ignore
		}
	}
}
