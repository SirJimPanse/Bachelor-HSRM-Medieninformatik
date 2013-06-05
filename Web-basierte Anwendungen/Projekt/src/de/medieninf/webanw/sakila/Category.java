package de.medieninf.webanw.sakila;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name="category")
public class Category implements Serializable {
	private static final long serialVersionUID = 4734167456482392691L;
	
	protected int categoryId;
	protected int version;
	protected String name;
	protected List<Film> films;
	
	public Category() {
		films = new ArrayList<Film>();
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
    @SequenceGenerator(name="CategoryIdGen", sequenceName="category_category_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CategoryIdGen")
    @Column(name="category_id")
    public int getCategoryId() {
		return this.categoryId;
    }

    public void setCategoryId(int categoryId) {
    	this.categoryId = categoryId;
    }
    
    @Column(name = "name")
    public String getName() {
    	return this.name;
    }

    public void setName(String name) {
    	this.name = name;
    }

    @OneToMany(mappedBy="category")
    public List<Film> getFilms() {
    	return this.films;
    }
    public void setFilms(List<Film> films) {
    	this.films = films;
    }

    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + categoryId;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Category other = (Category) obj;
		if (categoryId != other.categoryId)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
    
	@Override
	public String toString() {
		return "Category[" + categoryId + "] " + this.getName();
	}
	
}
