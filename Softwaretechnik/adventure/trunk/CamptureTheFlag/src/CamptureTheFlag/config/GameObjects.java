package CamptureTheFlag.config;

import java.util.ArrayList;
import java.util.List;
import CamptureTheFlag.logik.entities.GameObject;
import CamptureTheFlag.logik.entities.gegenstaende.Doughnut;
import CamptureTheFlag.logik.entities.gegenstaende.GluecksKeks;
import CamptureTheFlag.logik.entities.gegenstaende.Spicker;

/**
 * 
 * @author shard001
 *
 */
public class GameObjects {
	
	public static List<GameObject> getGameObjects() {
		
		List<GameObject> l = new ArrayList<GameObject>();
		
		l.add(new GluecksKeks("Keks", "glueckskeks.png", "Weise, du musst sein"));
		l.add(new Doughnut("Doughnut", "donut.png", "Nicht sofort essen"));
		l.add(new Doughnut("Doughnut", "donut.png", "Nicht sofort essen"));
		l.add(new Spicker("Spicker", "spicker.png", "Damit besteht man manche Prüfungen")); // falls vom Dialog angeboten
		
		return l;
	}
}
