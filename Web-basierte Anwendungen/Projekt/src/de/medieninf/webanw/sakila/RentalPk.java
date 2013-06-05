package de.medieninf.webanw.sakila;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class RentalPk implements Serializable {
	private static final long serialVersionUID = -2657315963194265769L;
	
	protected Date rentalDate;
	protected int inventory;
	protected int customer;
        
	public RentalPk() { 	
	}
        
	public int getInventory() {
		return this.inventory;
	}
	public void setInventory(final int pInventory) {
		this.inventory = pInventory;
    }

	public int getCustomer() {
		return this.customer;
    }
	public void setCustomer(final int pCustomer) {
		this.customer = pCustomer;
    }
        
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRentalDate() {
		// I really *DO* hate Date, Time, Timestamp and alike
		// Date is modifiable in Java, offers millisecond precision on read
		// but somehow only seconds on write. Postgresql using *double*
		// internally. What a complete mess....
		// For this is seems that constantly chopping off the last three digits 
		// works a little bit.
		// Note, that reading and writing existing entries without modification
		// from JPA still results in a modified entry on the database because
		// chopping of the last three digits is then in the database. It is just
		// not observable from Java through JPA
		long val = (rentalDate.getTime() / 1000) * 1000;
		Date choppedRentalDate = new Date(val);
		// yes I always modify on read - I just *really* want to make sure 
		// that there never ever is something observable without last three
		// digits chopped off.
		this.rentalDate = choppedRentalDate;
		return this.rentalDate;
    }
	public void setRentalDate(final Date rentalDate) {
		// see comment getRentalDate
		long val = (rentalDate.getTime() / 1000) * 1000;
		Date choppedRentalDate = new Date(val);
		this.rentalDate = choppedRentalDate;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + customer;
		result = prime * result + inventory;
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
		RentalPk other = (RentalPk) obj;
		if (customer != other.customer)
			return false;
		if (inventory != other.inventory)
			return false;
		if (rentalDate == null) {
			if (other.rentalDate != null)
				return false;
		} else if (!rentalDate.equals(other.rentalDate))
			return false;
		return true;
	}
	
}
