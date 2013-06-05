package CamptureTheFlag.tests;

import CamptureTheFlag.darstellung.TastaturHandler;
import CamptureTheFlag.logik.inits.CampusFactory;

public class DatenTests implements Runnable {
	
	public static TastaturHandler t;
	
	public static void main(String args[]) { new DatenTests().run(); }

	@Override
	public void run() {
		CampusFactory cf = new CampusFactory();
		cf.parseMap("./CamptureTheFlag/src/CamptureTheFlag/config/maps/room_door_fancy.ppm");
	}
}