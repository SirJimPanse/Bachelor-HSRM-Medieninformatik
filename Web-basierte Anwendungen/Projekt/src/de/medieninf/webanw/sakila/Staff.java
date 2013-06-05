package de.medieninf.webanw.sakila;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="staff")
public class Staff implements Serializable {
	private static final long serialVersionUID = -5710503155465586255L;
	
	protected int staffId;
    protected int version;
    protected String firstName;
    protected String lastName;
    protected Paddress paddress;
    protected byte[] picture;
    protected Store store;
    protected boolean active;

    public Staff() { 
    	
    }
    
    @Version
    @Column(name = "version")
    public int getVersion() {
    	return this.version;
    }
    public void setVersion(int version) {
    	this.version = version;
    }

    @Id
    @SequenceGenerator(name="StaffIdGen", sequenceName="staff_staff_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="StaffIdGen")
    @Column(name = "staff_id")
    public int getStaffId() {
    	return this.staffId;
    }
    public void setStaffId(int staffId) {
    	this.staffId = staffId;
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
    @JoinColumn(name="p_address_id")
    public Paddress getPaddress() {
    	return this.paddress;
    }
    public void setPaddress(Paddress paddress) {
    	this.paddress = paddress;
    }

    @Column(name="picture")
    public byte[] getPicture() {
    	return this.picture;
    }
    public void setPicture(byte[] picture) {
    	this.picture = picture;
    }

    @ManyToOne
    @JoinColumn(name="store_id")
    public Store getStore() {
    	return this.store;
    }
    public void setStore(Store store) {
    	this.store = store;
    }

    @Column(name="active")
    public boolean isActive() {
    	return this.active;
    }
    public void setActive(boolean active) {
    	this.active = active;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((paddress == null) ? 0 : paddress.hashCode());
		result = prime * result + staffId;
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
		Staff other = (Staff) obj;
		if (active != other.active)
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
		if (staffId != other.staffId)
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
		return "Staff[" + staffId + "]";
	}
}
