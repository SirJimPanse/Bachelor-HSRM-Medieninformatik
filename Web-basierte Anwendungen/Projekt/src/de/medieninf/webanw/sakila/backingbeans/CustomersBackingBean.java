package de.medieninf.webanw.sakila.backingbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.faces.event.ActionEvent;

import de.medieninf.webanw.sakila.Customer;
import de.medieninf.webanw.sakila.Store;

public class CustomersBackingBean extends PageBackingBean<Customer> implements Serializable {

	private static final long serialVersionUID = -3741830740160287106L;
	private FilmsBackingBean search;
	private int first;
	private int noRows;

	private String firstName;
	private String lastName;
	private String paddress;
	private String paddress2;
	private String district;
	private String city;
	private	String country;
	private String postalCode;
	private String phone;
	private	Boolean active;
	private String store;
	
	private boolean idAscending;
	private boolean firstNameAscending;
	private boolean lastNameAscending;
	private boolean storeAscending;
	private boolean activeAscending;
	private boolean paddressAscending;
	private boolean districtAscending;
	private boolean cityAscending;
	private boolean countryAscending;
	private boolean postalCodeAscending;
	private boolean phoneAscending;
	private boolean searchField;
	private String searchLink; 
	private Customer customer;
	
	public Customer getCustomer() { return this.customer; }
	public void setCustomer(Customer value) { this.customer = value; }
	
	
	public List<String> getStores() {
		List<String> stores = new ArrayList<String>();
		stores.add("");
		for (Store st : gsb().getStores()) {
			stores.add(st.getStoreId() + "");
		}
		return stores;
	}
	public String getStore() { return store; }

	public CustomersBackingBean() {
		this.first = 0;
		this.noRows = 20;
	
		this.model = new ArrayList<Customer>(gsb().getCustomers());
		this.searchField = false;
		this.searchLink = "Suche einblenden";
	}
	
	
	/**
	 * Toggle für den boolean searchField
	 * setzt je nach  Zustand auch den searchLink 
	 * 
	 * @return
	 */
	public String toggleSearch(){
		if(searchField){
			searchField = false;
			searchLink = "Suche einblenden";
		}else{
			searchField = true;
			searchLink = "Suche ausblenden";
		}
		return null;
	}
	
	
	/**
	 * Holt Daten neu aus SakilaBean
	 * 
	 * @return
	 */
	public String resetCustomers(){
		model = gsb().getCustomers();
		return null;
	}
	
	public void setStore(String store) {
		this.store = store;
	}
	
	/**
	 * Setzt Felder wenn Werte eingetragen wurden, ansonsten werden diese null gesetzt
	 * und setzt dann die customers-Liste neu, mit dem return der in der SakilaBean vorhandenen Suche
	 * 
	 * @return
	 */
	public String searchCustomers() {
		String result 	  = "";
		this.firstName 	  = firstName == "" ? null : firstName;
		this.lastName 	  = lastName == "" ? null : lastName;
		this.paddress 	  = paddress == "" ? null : paddress;
		this.district 	  = district == "" ? null : district;
		this.city 	   	  = city == "" ? null : city;
		this.country   	  = country == "" ? null : country;
		this.postalCode   = postalCode == "" ? null : postalCode;
		this.phone   	  = phone == "" ? null : phone;
		Boolean active    = this.active == null ? null : new Boolean (this.active);
		this.store 		  = store == "" ? null : store;
		
		for(Store st : gsb().getStores()){
			if (store != null && st.getStoreId() == Integer.parseInt(store)){
				// Wenn StoreID in Stores gefunden dann mit diesem Store suchen und Liste returnen
				model =  gsb().searchCustomers(firstName, lastName,  active, st,  paddress,  paddress2,  district,  city, 
						 country,  postalCode,  phone);		
				return result;
			}
		}
		// Stores durchlaufen, ID nicht gefunden, daher null Wert für Store
		model =  gsb().searchCustomers(firstName, lastName,  active, null,  paddress,  paddress2,  district,  city, 
				 country,  postalCode,  phone);
		return result;
	}
	
	/**
	 * Sortiert die customers-Liste nach der customerId, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByCustomerId() {
		 if(idAscending){
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return new Integer(c1.getCustomerId()).compareTo(new Integer(c2.getCustomerId()));
					}
				});
				idAscending = false;
		   }else{
			Collections.sort(model, new Comparator<Customer>() {
				@Override
				public int compare(Customer c1, Customer c2) {
					return new Integer(c2.getCustomerId()).compareTo(new Integer(c1.getCustomerId()));
				}
			});
			idAscending = true;
		   }

		   return null;
		}
	
	/**
	 * Sortiert die customers-Liste nach dem firstName, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByFirstName() {
		
		  if(firstNameAscending){
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return c1.getFirstName().compareTo(c2.getFirstName());
					}
				});
				firstNameAscending = false;
			   }else{
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return c2.getFirstName().compareTo(c1.getFirstName());
					}
				});
				firstNameAscending = true;
			   }
		
		return null;
	}
	
	/**
	 * Sortiert die customers-Liste nach dem lastName, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortBylastName() {
		
		  if(lastNameAscending){
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return c1.getLastName().compareTo(c2.getLastName());
					}
				});
				lastNameAscending = false;
			   }else{
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return c2.getLastName().compareTo(c1.getLastName());
					}
				});
				lastNameAscending = true;
			   }
		
	
		  
		  return null;
	}
	
	/**
	 * Sortiert die customers-Liste nach dem store, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByStore() {
		
		  if(storeAscending){
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return (c1.getStore().getPaddress().getPaddress()).compareTo(c2.getStore().getPaddress().getPaddress());
					}
				});
				storeAscending = false;
			   }else{
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return c2.getStore().getPaddress().getPaddress().compareTo(c1.getStore().getPaddress().getPaddress());
					}
				});
				storeAscending = true;
			   }
		
	
		  
		  return null;
	}
	
	/**
	 * Sortiert die customers-Liste nach der Aktivität ( true oder false = aktiv oder nicht aktiv), ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByActive() {
		
		  if(activeAscending){
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return new Boolean(c1.isActive()).compareTo(new Boolean(c2.isActive()));
					}
				});
				activeAscending = false;
			   }else{
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return new Boolean(c2.isActive()).compareTo(new Boolean(c1.isActive()));
					}
				});
				activeAscending = true;
			   }
		
	
		  
		  return null;
	}
	
	/**
	 * Sortiert die customers-Liste nach der Adresse, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByPaddress() {
		
		  if(paddressAscending){
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return (c1.getPaddress().getPaddress()).compareTo(c2.getPaddress().getPaddress());
					}
				});
				paddressAscending = false;
			   }else{
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return (c2.getPaddress().getPaddress()).compareTo(c1.getPaddress().getPaddress());
					}
				});
				paddressAscending = true;
			   }
			  
		  return null;
	}
	
	
	/**
	 * Sortiert die customers-Liste nach dem district, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByDistrict() {
		
		  if(districtAscending){
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return (c1.getPaddress().getDistrict()).compareTo(c2.getPaddress().getDistrict());
					}
				});
				districtAscending = false;
			   }else{
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return (c2.getPaddress().getDistrict()).compareTo(c1.getPaddress().getDistrict());
					}
				});
				districtAscending = true;
			   }
			  
		  return null;
	}
	
	
	/**
	 * Sortiert die customers-Liste nach der Stadt, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByCity() {
		
		  if(cityAscending){
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return (c1.getPaddress().getCity().getCity()).compareTo(c2.getPaddress().getCity().getCity());
					}
				});
				cityAscending = false;
			   }else{
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return (c2.getPaddress().getCity().getCity()).compareTo(c1.getPaddress().getCity().getCity());
					}
				});
				cityAscending = true;
			   }
			  
		  return null;
	}
	
	
	/**
	 * Sortiert die customers-Liste nach dem Land, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByCountry() {
		
		  if(countryAscending){
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return (c1.getPaddress().getCity().getCountry().getCountry()).compareTo(c2.getPaddress().getCity().getCountry().getCountry());
					}
				});
				countryAscending = false;
			   }else{
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return (c2.getPaddress().getCity().getCountry().getCountry()).compareTo(c1.getPaddress().getCity().getCountry().getCountry());
					}
				});
				countryAscending = true;
			   }
			  
		  return null;
	}
	
	/**
	 * Sortiert die customers-Liste nach dem postalCode, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByPostalCode() {
		
		  if(postalCodeAscending){
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return (c1.getPaddress().getPostalCode().compareTo(c2.getPaddress().getPostalCode()));
					}
				});
				postalCodeAscending = false;
			   }else{
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return (c2.getPaddress().getPostalCode().compareTo(c1.getPaddress().getPostalCode()));
					}
				});
				postalCodeAscending = true;
			   }
			  
		  return null;
	}

	/**
	 * Sortiert die customers-Liste nach der Telefonnummer, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByPhone() {
		
		  if(phoneAscending){
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return (c1.getPaddress().getPhone().compareTo(c2.getPaddress().getPhone()));
					}
				});
				phoneAscending = false;
			   }else{
				Collections.sort(model, new Comparator<Customer>() {
					@Override
					public int compare(Customer c1, Customer c2) {
						return (c2.getPaddress().getPhone().compareTo(c1.getPaddress().getPhone()));
					}
				});
				phoneAscending = true;
			   }
			  
		  return null;
	}
	/**
	 * Setz first Variable um noRows weiter
	 * Ist die Listengröße dabei überschritten worden wird first wieder zurückgesetzt
	 * 
	 * @return
	 */
	public String weiter() {
	    int size = model.size();
	    first = first + noRows;
	    if (first > size - noRows)
	        first = size - noRows;
	    return null;
	}
	
	/**
	 * Setz first Variable um noRows zurück
	 * Wird first dabei kleiner 0 wird first auf 0 gesetzt
	 * 
	 * @return
	 */
	public String zurueck() {
	    first = first - noRows;
	    if (first <= 0)
	        first = 0;
	    return null;
	}
	
	/**
	 * Setzt den Customer in der CustomerDetailsBackignBean 
	 *
	 * @param ae ActionEvent aus de, der customer geholt wird
	 */
	public void editCustomer(ActionEvent ae) {
		CustomerDetailsBackingBean cdbb = gb("cdbb", CustomerDetailsBackingBean.class);
		Customer customer = (Customer)ae.getComponent().getAttributes().get("customer");
		cdbb.setCustomer(customer);
	}
	
	public FilmsBackingBean getSearch() {
		return search;
	}

	public void setSearch(FilmsBackingBean search) {
		this.search = search;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getNoRows() {
		return noRows;
	}

	public void setNoRows(int noRows) {
		this.noRows = noRows;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPaddress() {
		return paddress;
	}

	public String getPaddress2() {
		return paddress2;
	}

	public String getDistrict() {
		return district;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getPhone() {
		return phone;
	}

	public Boolean getActive() {
		return active;
	}

	public boolean isIdAscending() {
		return idAscending;
	}

	public boolean isFirstNameAscending() {
		return firstNameAscending;
	}

	public boolean isLastNameAscending() {
		return lastNameAscending;
	}

	public boolean isStoreAscending() {
		return storeAscending;
	}

	public boolean isActiveAscending() {
		return activeAscending;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPaddress(String paddress) {
		this.paddress = paddress;
	}

	public void setPaddress2(String paddress2) {
		this.paddress2 = paddress2;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}


	public void setIdAscending(boolean idAscending) {
		this.idAscending = idAscending;
	}

	public void setFirstNameAscending(boolean firstNameAscending) {
		this.firstNameAscending = firstNameAscending;
	}

	public void setLastNameAscending(boolean lastNameAscending) {
		this.lastNameAscending = lastNameAscending;
	}

	public void setStoreAscending(boolean storeAscending) {
		this.storeAscending = storeAscending;
	}

	public void setActiveAscending(boolean activeAscending) {
		this.activeAscending = activeAscending;
	}

	public void setCustomers(List<Customer> customers) {
		this.model = customers;
	}

	public boolean isSearchField() {
		return searchField;
	}

	public void setSearchField(boolean searchField) {
		this.searchField = searchField;
	}

	public String getSearchLink() {
		return searchLink;
	}

	public void setSearchLink(String searchLink) {
		this.searchLink = searchLink;
	}

	@Override
	public void update(ActionEvent ae) {
		resetCustomers();		
	}
}