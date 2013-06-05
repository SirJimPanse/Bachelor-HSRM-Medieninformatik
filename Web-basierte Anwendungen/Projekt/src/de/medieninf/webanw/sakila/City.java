package de.medieninf.webanw.sakila;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="city")
public class City implements Serializable {
	private static final long serialVersionUID = -2732965361975281971L;
	
	protected int cityId;
	protected int version;
	protected String city;
	protected Country country;
	
	public City() {	
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
    @SequenceGenerator(name="CityIdGen", sequenceName="city_city_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CityIdGen")
    @Column(name="city_id")
    public int getCityId() {
    	return this.cityId;
    }
    public void setCityId(int cityId) {
    	this.cityId = cityId;
    }
    
    @Column(name="city")
    public String getCity() {
    	return this.city;
    }
    public void setCity(String city) {
    	this.city = city;
    }
    
    @ManyToOne
    @JoinColumn(name="country_id") 
    public Country getCountry() {
    	return this.country;
    }
    public void setCountry(Country country) {
    	this.country = country;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + cityId;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
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
		City other = (City) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (cityId != other.cityId)
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		return true;
	}    
    
	@Override
	public String toString() {
		return "City[" + cityId + "] " + this.getCity();
	}
}
