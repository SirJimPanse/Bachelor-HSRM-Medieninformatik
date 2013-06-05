package de.medieninf.webanw.sakila;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="country")
public class Country implements Serializable {
	private static final long serialVersionUID = -6873196627442530827L;
	
	protected int countryId;
	protected int version;
	protected String country;

	public Country() {	
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
    @SequenceGenerator(name="CountryIdGen", sequenceName="country_country_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CountryIdGen")
    @Column(name="country_id")
    public int getCountryId() {
    	return this.countryId;
    }
    public void setCountryId(int countryId) {
    	this.countryId = countryId;
    }
    
    @Column(name="country")
    public String getCountry() {
    	return this.country;
    }
    public void setCountry(String country) {
    	this.country = country;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + countryId;
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
		Country other = (Country) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (countryId != other.countryId)
			return false;
		return true;
	}
    
	@Override
	public String toString() {
		return "Country[" + countryId + "] " + this.getCountry();
	}

}
