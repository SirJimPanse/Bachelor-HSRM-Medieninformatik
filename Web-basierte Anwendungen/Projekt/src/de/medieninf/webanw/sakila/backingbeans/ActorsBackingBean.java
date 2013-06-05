package de.medieninf.webanw.sakila.backingbeans;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import javax.faces.event.ActionEvent;

import de.medieninf.webanw.sakila.Actor;

public class ActorsBackingBean extends PageBackingBean<Actor> implements Serializable{
	
	private static final long serialVersionUID = 3191334673751973070L;
	private int first;
	private int noRows;
	private boolean idAscending;
	private boolean firstNameAscending;
	private boolean lastNameAscending;
	
	public ActorsBackingBean() {
		this.first = 0;
		this.noRows = 20;
		this.model = gsb().getActors();
	}
	
	/**
	 * Sortiert die actors-Liste nach der actorID, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByActorId() {
		if(idAscending) {
			Collections.sort(model, new Comparator<Actor>() {
				@Override
				public int compare(Actor a1, Actor a2) {
					return new Integer(a1.getActorId()).compareTo(new Integer(a2.getActorId()));
				}
			});
			idAscending = false;
		} else {
			Collections.sort(model, new Comparator<Actor>() {
				@Override
				public int compare(Actor a1, Actor a2) {
					return new Integer(a2.getActorId()).compareTo(new Integer(a1.getActorId()));
				}
			});
			idAscending = true;
		}
		return null;
	}
	
	/**
	 * Sortiert die actors-Liste nach dem firstName, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByFirstName() {
		
		  if(firstNameAscending){
				Collections.sort(model, new Comparator<Actor>() {
					@Override
					public int compare(Actor a1, Actor a2) {
						return a1.getFirstName().compareTo(a2.getFirstName());
					}
				});
				firstNameAscending = false;
			   }else{
				Collections.sort(model, new Comparator<Actor>() {
					@Override
					public int compare(Actor a1, Actor a2) {
						return a2.getFirstName().compareTo(a1.getFirstName());
					}
				});
				firstNameAscending = true;
			   }
		
		return null;
	}
	
	/**
	 * Sortiert die actors-Liste nach dem lastName, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortBylastName() {
		
		  if (lastNameAscending) {
					Collections.sort(model, new Comparator<Actor>() {
						@Override
						public int compare(Actor a1, Actor a2) {
							return a1.getLastName().compareTo(a2.getLastName());
						}
					});
					lastNameAscending = false;
			   } else {
				Collections.sort(model, new Comparator<Actor>() {
					@Override
					public int compare(Actor a1, Actor a2) {
						return a2.getLastName().compareTo(a1.getLastName());
					}
				});
				lastNameAscending = true;
			   }

		  return null;
	}

	/**
	 * Setz first Variable um noRows weiter
	 * Ist die Listengröße dabei überschritten worden wird first wieder zurückgesetzt
	 * 
	 * @return
	 */
	public String weiter() {
	    first = first + noRows;
	    int size = model.size();
	    if (first > size - noRows)
	        first = size - noRows;
	    return null;
	}
	
	/**
	 * Setz first Variable um noRows zurück
	 * Wird first dabei kleiner 0 wird first auf 0 gesetzt
	 * 
	 * @return
	 */
	public String zurueck() {
	    first = first - noRows;
	    if (first <= 0)
	        first = 0;
	    return null;
	}
	
	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getNoRows() {
		return noRows;
	}

	public void setNoRows(int noRows) {
		this.noRows = noRows;
	}

	@Override
	public void update(ActionEvent ae) {
		setModel(gsb().getActors());		
	}

}
