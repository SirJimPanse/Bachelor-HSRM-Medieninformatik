package CamptureTheFlag.tests;

import CamptureTheFlag.darstellung.TastaturHandler;
import CamptureTheFlag.logik.GameC;

/**
 * 
 * @author shard001
 *
 */
public class LogikTests {
	
	public static void main(String[] args) {
		
		TastaturHandler t = new TastaturHandler();
		GameC controller = new GameC(t);
		
		controller.neuesSpiel();
		
		System.out.println( controller.game.getSpieler().getAktFeld() );
		
		//controller.spielSpeichern();
		
	}

}
