package de.medieninf.webanw.sakila;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "rental")
public class Rental implements Serializable {
	private static final long serialVersionUID = -5014421450044524297L;
	
	protected RentalPk rentalId;
	protected int version;
	protected Date rentalDate;
	protected Inventory inventory;
	protected Customer customer;
	protected Date returnDate;
	protected Staff staff;
	
    public Rental() {
        this.rentalId = new RentalPk();
    }

    @EmbeddedId
    @AttributeOverrides({
    	@AttributeOverride(name="rentalDate", column = @Column(name = "rent_date")),
    	@AttributeOverride(name="inventory", column = @Column(name = "inventory_id")),
    	@AttributeOverride(name="customer", column = @Column(name = "customer_id"))
    })
    public RentalPk getRentalId() {
    	return this.rentalId;
    }
    public void setRentalId(final RentalPk rentalId) {
    	this.rentalId = rentalId;
    }
    
    @Version
    @Column(name="version")
    public int getVersion() {
    	return this.version;
    }
    public void setVersion(int version) {
    	this.version = version;
    }
        
    @ManyToOne
    @JoinColumn(name="inventory_id", updatable=false, insertable=false)
    public Inventory getInventory() {
    	return this.inventory;
    }
    public void setInventory(final Inventory inventory) {
    	this.inventory = inventory;
    	this.rentalId.setInventory(inventory.getInventoryId());
    }

    @ManyToOne
    @JoinColumn(name="customer_id", updatable=false, insertable=false)
    public Customer getCustomer() {
    	return this.customer;
    }
    public void setCustomer(final Customer customer) {
    	this.customer = customer;
    	this.rentalId.setCustomer(customer.getCustomerId());
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="rent_date", updatable=false, insertable=false)
    public Date getRentalDate() {
    	this.rentalDate = this.rentalId.getRentalDate(); // ensure chop off
    	return this.rentalDate;
    }
    public void setRentalDate(final Date rentalDate) {
    	this.rentalId.setRentalDate(rentalDate);
    	this.rentalDate = this.rentalId.getRentalDate(); // ensure chop off
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="return_date")
    public Date getReturnDate() {
    	return this.returnDate;
    }
    public void setReturnDate(final Date returnDate) {
    	this.returnDate = returnDate;
    }
    
    @ManyToOne
    @JoinColumn(name="staff_id")
    public Staff getStaff() {
    	return this.staff;
    }
    public void setStaff(final Staff staff) {
    	this.staff = staff;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customer == null) ? 0 : customer.hashCode());
		result = prime * result
				+ ((inventory == null) ? 0 : inventory.hashCode());
		result = prime * result
				+ ((rentalDate == null) ? 0 : rentalDate.hashCode());
		result = prime * result
				+ ((rentalId == null) ? 0 : rentalId.hashCode());
		result = prime * result
				+ ((returnDate == null) ? 0 : returnDate.hashCode());
		result = prime * result + ((staff == null) ? 0 : staff.hashCode());
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
		Rental other = (Rental) obj;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (inventory == null) {
			if (other.inventory != null)
				return false;
		} else if (!inventory.equals(other.inventory))
			return false;
		if (rentalDate == null) {
			if (other.rentalDate != null)
				return false;
		} else if (!rentalDate.equals(other.rentalDate))
			return false;
		if (rentalId == null) {
			if (other.rentalId != null)
				return false;
		} else if (!rentalId.equals(other.rentalId))
			return false;
		if (returnDate == null) {
			if (other.returnDate != null)
				return false;
		} else if (!returnDate.equals(other.returnDate))
			return false;
		if (staff == null) {
			if (other.staff != null)
				return false;
		} else if (!staff.equals(other.staff))
			return false;
		return true;
	}     
	
	@Override
	public String toString() {
		String ret = "Rental[" + version;
		if (customer != null)
			ret += ",c:" + customer.getCustomerId();
		if (inventory != null) 
			ret += ",i:" + inventory.getInventoryId();
		if (rentalDate != null)
			ret += ",s:" + rentalDate + "(" + rentalDate.getTime() + ")";
		if (returnDate != null)
			ret += ",e:" + returnDate  + "(" + returnDate.getTime() + ")";
		return ret;
	}

}
