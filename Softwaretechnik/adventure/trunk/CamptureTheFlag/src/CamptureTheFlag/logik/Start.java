package CamptureTheFlag.logik;

import CamptureTheFlag.darstellung.TastaturHandler;

/**
 * Wird zum Starten des Programms verwendet
 * 
 * @author shard001
 */
public class Start {
	public static void main(String[] args) { new GameC(new TastaturHandler()).run(); }
}
