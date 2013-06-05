package CamptureTheFlag.config;

import java.util.ArrayList;
import java.util.List;

import CamptureTheFlag.logik.entities.NPC;

/**
 * 
 * @author shard001
 *
 */
public class NPCs {
	
	public static List<NPC> getNPCs() {
		
		List<NPC> l = new ArrayList<NPC>();
		
		l.add(new NPC("Homer", "homer.png", "Fettwanst"));
		l.add(new NPC("Lisa", "lisa.png", "kleiner Schlauberger"));
		l.add(new NPC("Meister Berdux", "berdux.png", "Er hat den Plan"));
		l.add(new NPC("Meister Barth", "barth.png", "Prüft in ADS"));
		l.add(new NPC("Meister Weitz", "weitz.png", "Prüft in Rechnernetze"));
		
		return l;
	}

}
