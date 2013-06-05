package CamptureTheFlag.tests;

import CamptureTheFlag.logik.entities.gegenstaende.Schluessel;

public class GoTest {
	public static void main(String[] args) {
		Schluessel s1 = new Schluessel("foo", null, "bar", 12);
		Schluessel s2 = new Schluessel("bar", null, "fiep", 2);
		
		if ( s1.getClass().equals(s2.getClass()) ) {
			System.out.println("gleich");
		} else {
			System.out.println("ungleich");
		}
	}
}
