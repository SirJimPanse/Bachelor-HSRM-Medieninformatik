package de.medieninf.webanw.sakila;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name="inventory")
public class Inventory implements Serializable {
	private static final long serialVersionUID = 2580491481649886846L;
	
	protected int inventoryId;
    protected int version;
    protected Film film;
    protected Store store;
    protected List<Rental> rentals;

    public Inventory() { 
    	rentals = new ArrayList<Rental>();
    }
    @Id
    @SequenceGenerator(name="InventoryIdGen", sequenceName="inventory_inventory_id_seq",
                    allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="InventoryIdGen")
    @Column(name = "inventory_id")
    public int getInventoryId() {
    	return this.inventoryId;
    }
    public void setInventoryId(int inventoryId) {
    	this.inventoryId = inventoryId;
    }
    
    @Version
    @Column(name = "version")
    public int getVersion() {
    	return this.version;
    }
    public void setVersion(int version) {
    	this.version = version;
    }

    @ManyToOne
    @JoinColumn(name="film_id")
    public Film getFilm() {
    	return this.film;
    }
    public void setFilm(Film film) {
    	this.film = film;
    }
    
    @ManyToOne
    @JoinColumn(name="store_id")
    public Store getStore() {
    	return this.store;
    }
    public void setStore(Store store) {
    	this.store = store;
    }
        
    @OneToMany(mappedBy="inventory")
    public List<Rental> getRentals() {
    	return this.rentals;
    }
    public void setRentals(List<Rental> rentals) {
    	this.rentals = rentals;
    }
    
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((film == null) ? 0 : film.hashCode());
		result = prime * result + inventoryId;
		result = prime * result + ((store == null) ? 0 : store.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Inventory other = (Inventory) obj;
		if (film == null) {
			if (other.film != null)
				return false;
		} else if (!film.equals(other.film))
			return false;
		if (inventoryId != other.inventoryId)
			return false;
		if (store == null) {
			if (other.store != null)
				return false;
		} else if (!store.equals(other.store))
			return false;
		return true;
	}
    
	@Override
	public String toString() {
		return "Inventory[" + inventoryId + "] of " + film.toString();
	}
}
