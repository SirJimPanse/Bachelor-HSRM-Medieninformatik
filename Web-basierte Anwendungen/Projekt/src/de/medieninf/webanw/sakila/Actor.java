package de.medieninf.webanw.sakila;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name="actor")
public class Actor implements Serializable {
	private static final long serialVersionUID = 4466129408994314150L;
	
	protected int actorId;
	protected int version;
	protected String firstName;
	protected String lastName;
	protected List<Film> films;
	
	public Actor() {
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
    @SequenceGenerator(name="ActorIdGen", sequenceName="actor_actor_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ActorIdGen")
    @Column(name="actor_id")
    public int getActorId() {
		return this.actorId;
    }
    public void setActorId(int actorId) {
    	this.actorId = actorId;
    }
    
    @Column(name = "first_name")
    public String getFirstName() {
    	return this.firstName;
    }
    public void setFirstName(String firstName) {
    	this.firstName = firstName;
    }
    
    @Column(name = "last_name")
    public String getLastName() {
    	return this.lastName;
    }
    public void setLastName(String lastName) {
    	this.lastName = lastName;
    }
    
    @ManyToMany(mappedBy="actors")
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
		result = prime * result + actorId;
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
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
		Actor other = (Actor) obj;
		if (actorId != other.actorId)
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
		return true;
	}
    
	@Override
	public String toString() {
		return "Actor[" + actorId + "] " + firstName + " " + lastName;
	}
}
