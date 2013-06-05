package de.medieninf.webanw.sakila;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="p_address")
public class Paddress implements Serializable {
	private static final long serialVersionUID = -2866465061941355664L;
	
	protected int paddressId;
	protected int version;
	protected String paddress;
	protected String paddress2;
	protected String district;
	protected City city;
	protected String postalCode;
	protected String phone;

	public Paddress() {	
	}
	
    @Version
    @Column(name="version")
    public int getVersion() {
    	return this.version;
    }
    public void setVersion(int version) {
    	this.version = version;
    }
    
    @Id
    @SequenceGenerator(name="PaddressIdGen", sequenceName="p_address_p_address_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PaddressIdGen")
    @Column(name="p_address_id")
    public int getPaddressId() {
    	return this.paddressId;
    }
    public void setPaddressId(int paddressId) {
    	this.paddressId = paddressId;
    }

    @Column(name="p_address")
    public String getPaddress() {
		return paddress;
	}
	public void setPaddress(String paddress) {
		this.paddress = paddress;
	}

	@Column(name="p_address2")
	public String getPaddress2() {
		return paddress2;
	}
	public void setPaddress2(String paddress2) {
		this.paddress2 = paddress2;
	}

	@Column(name="district")
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}

	@ManyToOne
    @JoinColumn(name="city_id")
    public City getCity() {
    	return this.city;
    }
    public void setCity(City city) {
    	this.city = city;
    }

	@Column(name="postal_code")
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@Column(name="phone")
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result
				+ ((district == null) ? 0 : district.hashCode());
		result = prime * result
				+ ((paddress == null) ? 0 : paddress.hashCode());
		result = prime * result
				+ ((paddress2 == null) ? 0 : paddress2.hashCode());
		result = prime * result + paddressId;
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result
				+ ((postalCode == null) ? 0 : postalCode.hashCode());
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
		Paddress other = (Paddress) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (district == null) {
			if (other.district != null)
				return false;
		} else if (!district.equals(other.district))
			return false;
		if (paddress == null) {
			if (other.paddress != null)
				return false;
		} else if (!paddress.equals(other.paddress))
			return false;
		if (paddress2 == null) {
			if (other.paddress2 != null)
				return false;
		} else if (!paddress2.equals(other.paddress2))
			return false;
		if (paddressId != other.paddressId)
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (postalCode == null) {
			if (other.postalCode != null)
				return false;
		} else if (!postalCode.equals(other.postalCode))
			return false;
		return true;
	}

}
