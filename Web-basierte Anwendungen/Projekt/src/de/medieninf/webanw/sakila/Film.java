package de.medieninf.webanw.sakila;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name="film")
public class Film implements Serializable {
	private static final long serialVersionUID = -676235611904733273L;
	
	protected int filmId;
	protected int version;
	protected Category category;
	protected String title;
	protected String description;
	protected int rentalDuration;
	protected BigDecimal rentalRate;
	protected Integer length;
	protected BigDecimal replacementCost;
	protected String rating;
	protected List<Actor> actors;
	protected List<Inventory> inventories;

	final static private String[] ALLOWED_RATINGS = {
		"G", "PG", "PG-13", "R", "NC-17"
	};

    public static ArrayList<String> possibleRatings() {
    	ArrayList<String> ret = new ArrayList<String>();
    	for (String rating : ALLOWED_RATINGS) {
    		ret.add(rating);
    	}
    	return ret;
    }
	
	public Film() {
		this.actors = new ArrayList<Actor>();
		// this.inventories = new HashSet<Inventory>();		
	}
	
    @Id
    @SequenceGenerator(name="FilmIdGen", sequenceName="film_film_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FilmIdGen")
    @Column(name="film_id")
    public int getFilmId() {
    	return this.filmId;
    }    
    public void setFilmId(int filmId) {
    	this.filmId = filmId;
    }
    
    @Version
    @Column(name="version")
    public int getVersion() {
    	return this.version;
    }
    public void setVersion(int version) {
    	this.version = version;
    }
    
    @Column(name="title")
    public String getTitle() {
    	return this.title;
    }
    public void setTitle(String title) {
    	this.title = title;
    }

    @Column(name="description")
    public String getDescription() {
    	return this.description;
    }
    public void setDescription(String description) {
    	this.description = description;
    }
    
    @ManyToOne
    @JoinColumn(name="category_id")
    public Category getCategory() {
    	return this.category;
    }
    public void setCategory(Category category) {
    	this.category = category;
    }
    
    @Column(name="rental_duration")
    public int getRentalDuration() {
    	return this.rentalDuration;
    }
    public void setRentalDuration(int rentalDuration) {
    	this.rentalDuration = rentalDuration;
    }
    
    @Column(name="rental_rate")
    public BigDecimal getRentalRate() {
    	return this.rentalRate;
    }
    public void setRentalRate(BigDecimal rentalRate) {
        this.rentalRate = rentalRate;
    }
    
    @Column(name="length")
    public int getLength() {
    	return this.length;
    }    
    public void setLength(int length) {
    	this.length = length;
    }

    @Column(name="replacement_cost")
    public BigDecimal getReplacementCost() {
    	return this.replacementCost;
    }    
    public void setReplacementCost(BigDecimal replacementCost) {
    	this.replacementCost = replacementCost;
    }
    
    @Column(name="rating")
    public String getRating() {
    	return this.rating;
    }    
    public void setRating(String rating) {
    	for (String allowed : ALLOWED_RATINGS) {
    		if (rating.equals(allowed)) {
    			this.rating = rating;
    			return;
    		}
    	}
    	throw new RuntimeException("Film::setRating: rating " + rating + " not allowed.");
    }
    
    @ManyToMany
    @JoinTable(name="film_actor",
    	joinColumns = @JoinColumn(name="film_id", referencedColumnName="film_id"),
    	inverseJoinColumns = @JoinColumn(name="actor_id", referencedColumnName="actor_id")
    )
    public List<Actor> getActors() {
    	return this.actors;
    }
    public void setActors(List<Actor> actors) {
    	this.actors = actors;
    }
    
    @OneToMany(mappedBy="film")
    public List<Inventory> getInventories() {
    	return this.inventories;
    }
    public void setInventories(List<Inventory> inventories) {
    	this.inventories = inventories;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + filmId;
		result = prime * result + ((length == null) ? 0 : length.hashCode());
		result = prime * result + ((rating == null) ? 0 : rating.hashCode());
		result = prime * result + rentalDuration;
		result = prime * result
				+ ((rentalRate == null) ? 0 : rentalRate.hashCode());
		result = prime * result
				+ ((replacementCost == null) ? 0 : replacementCost.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Film other = (Film) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (filmId != other.filmId)
			return false;
		if (length == null) {
			if (other.length != null)
				return false;
		} else if (!length.equals(other.length))
			return false;
		if (rating == null) {
			if (other.rating != null)
				return false;
		} else if (!rating.equals(other.rating))
			return false;
		if (rentalDuration != other.rentalDuration)
			return false;
		if (rentalRate == null) {
			if (other.rentalRate != null)
				return false;
		} else if (!rentalRate.equals(other.rentalRate))
			return false;
		if (replacementCost == null) {
			if (other.replacementCost != null)
				return false;
		} else if (!replacementCost.equals(other.replacementCost))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Film[" + filmId + "] " + title;
	}
}
