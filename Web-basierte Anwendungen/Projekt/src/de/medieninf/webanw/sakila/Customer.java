package de.medieninf.webanw.sakila;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name="customer")
public class Customer implements Serializable {
	private static final long serialVersionUID = -2491306558840838872L;

	protected int customerId;
    protected int version;
    protected String firstName;
    protected String lastName;
    protected Store store;
    protected Paddress paddress;
    protected boolean active;
    protected List<Payment> payments;
    protected List<Rental> rentals;
    
    public Customer() { 
    	payments = new ArrayList<Payment>();
    	rentals = new ArrayList<Rental>();
    }
        
    @Id
    @SequenceGenerator(name="CustomerIdGen", sequenceName="customers_customerid_seq",
                    allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CustomerIdGen")
    @Column(name="customer_id")
    public int getCustomerId() {
    	return this.customerId;
    }
      
    public void setCustomerId(int customerId) {
    	this.customerId = customerId;
    }
    
    @Version
    @Column(name="version")
    public int getVersion() {
    	return this.version;
    }
    
    public void setVersion(int version) {
    	this.version = version;
    }
    
    @Column(name="first_name")
    public String getFirstName() {
    	return this.firstName;
    }

    public void setFirstName(String firstName) {
    	this.firstName = firstName;
    }
    
    @Column(name="last_name")
    public String getLastName() {
    	return this.lastName;
    }
    public void setLastName(String lastName) {
    	this.lastName = lastName;
    }

    @ManyToOne
    @JoinColumn(name="store_id")
    public Store getStore() {
    	return this.store;
    }
    
    public void setStore(final Store pStore) {
    	this.store = pStore;
    }
    
    @ManyToOne
    @JoinColumn(name="p_address_id")
    public Paddress getPaddress() {
    	return this.paddress;
    }
        
    public void setPaddress(Paddress paddress) {
    	this.paddress = paddress;
    }
    
    @Column(name="active")
    public boolean isActive() {
    	return this.active;
    }
    public void setActive(boolean active) {
    	this.active = active;
    }
    
    @OneToMany(mappedBy="customer")
    public List<Payment> getPayments() {
    	return this.payments;
    }
    public void setPayments(List<Payment> payments) {
    	this.payments = payments;
    }
    
    @OneToMany(mappedBy="customer")
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
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + customerId;
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((paddress == null) ? 0 : paddress.hashCode());
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
		Customer other = (Customer) obj;
		if (active != other.active)
			return false;
		if (customerId != other.customerId)
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (paddress == null) {
			if (other.paddress != null)
				return false;
		} else if (!paddress.equals(other.paddress))
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
		return "Customer[" + customerId + "] " + firstName + " " + lastName;
	}
}
