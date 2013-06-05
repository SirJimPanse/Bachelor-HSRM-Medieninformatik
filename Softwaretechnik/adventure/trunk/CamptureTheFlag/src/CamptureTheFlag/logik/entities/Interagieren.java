package CamptureTheFlag.logik.entities;

/**
 * Zentrale Schnittstelle zur Interaktion zwischen GameObjects
 * Wird mit beliebigen Objects aufgerufen und kann ebenso beliebige Typen zurückgeben
 * 
 * @author shard001
 */
public interface Interagieren {

	Object interagiere(Object o);

}