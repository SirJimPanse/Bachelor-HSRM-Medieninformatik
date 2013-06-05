package de.medieninf.webanw.sakila.backingbeans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.event.ActionEvent;

import de.medieninf.webanw.sakila.Category;
import de.medieninf.webanw.sakila.Film;

public class FilmsBackingBean extends PageBackingBean<Film> implements Serializable {

	private static final long serialVersionUID = 586459049290019527L;
	private String title;
	private String description;
	private String category; 
	private String maxRentalDuration;
	private String minRentalRate;
	private String maxRentalRate;
	private String minLength;
	private String maxLength;
	private String minReplacementCost;
	private String rating;
	private String maxReplacementCost;
	private String minRentalDuration;
	
	private String selectedCategory;
	private String selectedRating;
	
	private boolean idAscending;
	private boolean titleAscending;
	private boolean categoryAscending;
	private boolean descriptionAscending;
	private boolean rentalDurationAscending;
	private boolean rentalRateAscending;
	private boolean lengthAscending;
	private boolean replacementCostAscending;
	private boolean ratingAscending;
	
	private boolean searchField;
	
	private String searchLink;
	private int first;
	private int noRows;

	public FilmsBackingBean(){
		this.first  = 0;
		this.noRows = 20;
		this.searchField = false;
		this.searchLink  = "Suche einblenden";
		this.model       = gsb().getFilms();
	}

	/**
	 * Holt die neue FilmListe aus dem SakilaBean
	 * 
	 * @return
	 */
	public String reset() {
		setModel(gsb().getFilms());
		title = null;
		description = null;
		category = null;
		maxRentalDuration = null;
		minRentalRate = null;
		maxRentalRate = null;
		minLength = null;
		maxLength = null;
		minReplacementCost = null;
		rating = null;
		maxReplacementCost = null;
		minRentalDuration = null;

		return null;
	}
	
	/**
	 * Toggle für das searchField
	 * (setzt searchField true wenn false und umgekehrt)
	 * 
	 * @return
	 */
	public String toggleSearch(){
		if(searchField){
			searchField = false;
			searchLink  = "Suche einblenden";
		} else {
			searchField = true;
			searchLink  = "Suche ausblenden";
		}
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

	/**
	 * Setzt Felder wenn Werte eingetragen wurden, ansonsten werden diese null gesetzt
	 * und setzt dann die films-Liste neu, mit dem return der in der SakilaBean vorhandenen Suche
	 * 
	 * @return
	 */
	public String searchFilms() {
		
		Category c = null;
		
		String title                  = this.title;
		String description            = this.description;
		Integer minRentalDuration     = this.minRentalDuration == "" || this.minRentalDuration == null ? null : new Integer(this.minRentalDuration);
		Integer maxRentalDuration     = this.maxRentalDuration == "" || this.maxRentalDuration == null ? null : new Integer(this.maxRentalDuration);
		BigDecimal minRentalRate      = this.minRentalRate == "" || this.minRentalRate == null ? null : new BigDecimal(this.minRentalRate);
		BigDecimal maxRentalRate      = this.maxRentalRate == "" || this.maxRentalRate == null ? null : new BigDecimal(this.maxRentalRate);
		Integer minLength             = this.minLength == "" || this.minLength == null ? null : new Integer(this.minLength);
		Integer maxLength             = this.maxLength == "" || this.maxLength == null ? null : new Integer(this.maxLength);
		BigDecimal minReplacementCost = this.minReplacementCost == "" || this.minReplacementCost == null ? null : new BigDecimal(this.minReplacementCost);
		BigDecimal maxReplacementCost = this.maxReplacementCost == "" || this.maxReplacementCost == null ? null : new BigDecimal(this.maxReplacementCost);
		
		for (Category category : gsb().getCategories()){
			if(category.getName().equalsIgnoreCase(selectedCategory))
				c = category;
		}
			
		this.model = gsb().searchFilms(title, description, c, minRentalDuration, maxRentalDuration, minRentalRate, maxRentalRate, minLength, maxLength, minReplacementCost, maxReplacementCost, selectedRating);
		return "";
	}

	public String getTitle() { return title; }

	public void setTitle(String title) { this.title = title.toUpperCase(); }

	public String getDescription() { return description; }

	public void setDescription(String description) { this.description = description; }
	
	public String getrentalDuration() { return maxRentalDuration; }

	public String getCategory() {
		return category;
	}
	
	public List<String> getCategories(){
		List<String> categoryNames = new ArrayList<String>();
		categoryNames.add("");

		for(Category cat : gsb().getCategories()) {
			categoryNames.add(cat.getName());
		}
		return categoryNames;
	}
	
	public List<String> getRatings(){
		List<String> ratings = new ArrayList<String>();
		ratings.add("");
		for(String rating : Film.possibleRatings()){
			ratings.add(rating);
		}
		return ratings;
	}
	
	/**
	 * Sortiert die films-Liste nach der filmId, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByFilmId() {
		 
		 if(idAscending){
				Collections.sort(model, new Comparator<Film>() {
					@Override
					public int compare(Film f1, Film f2) {
						return new Integer(f1.getFilmId()).compareTo(new Integer(f2.getFilmId()));
					}
				});
				idAscending = false;
		   }else{
			Collections.sort(model, new Comparator<Film>() {
				@Override
				public int compare(Film f1, Film f2) {
					return new Integer(f2.getFilmId()).compareTo(new Integer(f1.getFilmId()));
				}
			});
			idAscending = true;
		   }
	 
		   return null;
	}
	
	/**
	 * Sortiert die films-Liste nach dem Titel, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByTitle() {
		
		   if(titleAscending){
			Collections.sort(model, new Comparator<Film>() {
				@Override
				public int compare(Film f1, Film f2) {
					return f1.getTitle().compareTo(f2.getTitle());
				}
			});
			titleAscending = false;
		   }else{
			Collections.sort(model, new Comparator<Film>() {
				@Override
				public int compare(Film f1, Film f2) {
					return f2.getTitle().compareTo(f1.getTitle());
				}
			});
			titleAscending = true;
		   }
	 
		   return null;
	}
	
	
	/**
	 * Sortiert die films-Liste nach der Beschreibung, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByDescription() {
		
		   if(descriptionAscending){
			Collections.sort(model, new Comparator<Film>() {
				@Override
				public int compare(Film f1, Film f2) {
					return f1.getDescription().compareTo(f2.getDescription());
				}
			});
			descriptionAscending = false;
		   }else{
			Collections.sort(model, new Comparator<Film>() {
				@Override
				public int compare(Film f1, Film f2) {
					return f2.getDescription().compareTo(f1.getDescription());
				}
			});
			descriptionAscending = true;
		   }
	 
		   return null;
	}
	
	/**
	 * Sortiert die films-Liste nach der Kategorie, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByCategory() {
		
		   if(categoryAscending){
			Collections.sort(model, new Comparator<Film>() {
				@Override
				public int compare(Film f1, Film f2) {
					return f1.getCategory().getName().compareTo(f2.getCategory().getName());
				}
			});
			categoryAscending = false;
		   }else{
			Collections.sort(model, new Comparator<Film>() {
				@Override
				public int compare(Film f1, Film f2) {
					return f2.getCategory().getName().compareTo(f1.getCategory().getName());
				}
			});
			categoryAscending = true;
		   }
	 
		   return null;
	}
	
	/**
	 * Sortiert die films-Liste nach der rentalDuration, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByRentalDuration() {
		 
		 if(rentalDurationAscending){
				Collections.sort(model, new Comparator<Film>() {
					@Override
					public int compare(Film f1, Film f2) {
						return new Integer(f1.getRentalDuration()).compareTo(new Integer(f2.getRentalDuration()));
					}
				});
				rentalDurationAscending = false;
		   }else{
			Collections.sort(model, new Comparator<Film>() {
				@Override
				public int compare(Film f1, Film f2) {
					return new Integer(f2.getRentalDuration()).compareTo(new Integer(f1.getRentalDuration()));
				}
			});
			rentalDurationAscending = true;
		   }
	 
		   return null;
	}
	
	/**
	 * Sortiert die films-Liste nach der rentalRate, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByRentalRate() {
		 
		 if(rentalRateAscending){
				Collections.sort(model, new Comparator<Film>() {
					@Override
					public int compare(Film f1, Film f2) {
						return (f1.getRentalRate()).compareTo((f2.getRentalRate()));
					}
				});
				rentalRateAscending = false;
		   }else{
			Collections.sort(model, new Comparator<Film>() {
				@Override
				public int compare(Film f1, Film f2) {
					return (f2.getRentalRate()).compareTo(f1.getRentalRate());
				}
			});
			rentalRateAscending = true;
		   }
	 
		   return null;
	}
	
	/**
	 * Sortiert die films-Liste nach der Länge, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByLength() {
		 
		 if(lengthAscending){
				Collections.sort(model, new Comparator<Film>() {
					@Override
					public int compare(Film f1, Film f2) {
						return new Integer(f1.getLength()).compareTo(new Integer(f2.getLength()));
					}
				});
				lengthAscending = false;
		   }else{
			Collections.sort(model, new Comparator<Film>() {
				@Override
				public int compare(Film f1, Film f2) {
					return new Integer(f2.getLength()).compareTo(new Integer(f1.getLength()));
				}
			});
			lengthAscending = true;
		   }
	 
		   return null;
	}
	

	/**
	 * Sortiert die films-Liste nach dem Rating, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByRating() {
		
		   if(ratingAscending){
			Collections.sort(model, new Comparator<Film>() {
				@Override
				public int compare(Film f1, Film f2) {
					return f1.getRating().compareTo(f2.getRating());
				}
			});
			ratingAscending = false;
		   }else{
			Collections.sort(model, new Comparator<Film>() {
				@Override
				public int compare(Film f1, Film f2) {
					return f2.getRating().compareTo(f1.getRating());
				}
			});
			ratingAscending = true;
		   }
	 
		   return null;
	}
	
	
	/**
	 * Sortiert die films-Liste nach den Pfandkosten, ist diese absteigend sortiert und die Methode
	 * wird aufgerufen so wird die Liste aufsteigend sortiert
	 * 
	 * @return
	 */
	public String sortByReplacementCost() {
		
		   if(replacementCostAscending){
			Collections.sort(model, new Comparator<Film>() {
				@Override
				public int compare(Film f1, Film f2) {
					return f1.getReplacementCost().compareTo(f2.getReplacementCost());
				}
			});
			replacementCostAscending = false;
		   }else{
			Collections.sort(model, new Comparator<Film>() {
				@Override
				public int compare(Film f1, Film f2) {
					return f2.getReplacementCost().compareTo(f1.getReplacementCost());
				}
			});
			replacementCostAscending = true;
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
		int size = getFilms().size();
		
		if ( size < noRows)
			return null;
	    
		first +=  noRows;
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
		
		int size = getFilms().size();
		
		if ( size < noRows)
			return null;
		
	    first = first - noRows;

	    if (first <= 0)
	        first = 0;
	 
	    return null;
	}
	
	/**
	 * Setzt den Film in der FilmDetailBackingbean
	 * 
	 * @param e ActionEvent aus dem der Film geholt wird
	 */
	public void filmDetail(ActionEvent e){
		FilmDetailsBackingBean fdbb = gb("fdbb", FilmDetailsBackingBean.class);
		Film film= (Film)e.getComponent().getAttributes().get("film");
		fdbb.setFilm(film);
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMaxRentalDuration() {
		return maxRentalDuration;
	}

	public void setMaxRentalDuration(String maxRentalDuration) {
		this.maxRentalDuration = maxRentalDuration;
	}

	public String getMinRentalRate() {
		return minRentalRate;
	}

	public void setMinRentalRate(String minRentalRate) {
		this.minRentalRate = minRentalRate;
	}

	public String getMaxRentalRate() {
		return maxRentalRate;
	}

	public void setMaxRentalRate(String maxRentalRate) {
		this.maxRentalRate = maxRentalRate;
	}

	public String getMinLength() {
		return minLength;
	}

	public void setMinLength(String minLength) {
		this.minLength = minLength;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public String getMinReplacementCost() {
		return minReplacementCost;
	}

	public void setMinReplacementCost(String minReplacementCost) {
		this.minReplacementCost = minReplacementCost;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getMaxReplacementCost() {
		return maxReplacementCost;
	}

	public void setMaxReplacementCost(String maxReplacementCost) {
		this.maxReplacementCost = maxReplacementCost;
	}

	public String getMinRentalDuration() {
		return minRentalDuration;
	}

	public void setMinRentalDuration(String minRentalDuration) {

		this.minRentalDuration = minRentalDuration;
	}
	public List<Film> getFilms() {
		return model;
	}
	public void setFilms(List<Film> films) {
		this.model = films;
	}
	
	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;	
	}

	public String getSelectedRating() {
		return selectedRating;
	}

	public void setSelectedRating(String selectedRating) {
		this.selectedRating = selectedRating;
	}

	public boolean isSearchField() {
		return searchField;
	}

	public void setSearchField(boolean searchField) {
		this.searchField = searchField;
	}

	public String getSearchLink() {
		return searchLink;
	}
	
	public void setSearchLink(String searchLink) {
		this.searchLink = searchLink;
	}

	@Override
	public void update(ActionEvent ae) {
		reset();		
	}
}