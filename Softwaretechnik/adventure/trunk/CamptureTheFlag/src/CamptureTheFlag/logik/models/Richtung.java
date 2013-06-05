package CamptureTheFlag.logik.models;

import java.io.Serializable;

/**
 * Enum um die Richtung der Bewegung zu unterscheiden
 * 
 * @author shard001
 */
public enum Richtung implements Serializable {
	NORD, OST, SUED, WEST; // Reihenfolge ist wichtig (rechtsdrehend)
}