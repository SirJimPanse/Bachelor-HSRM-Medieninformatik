package tests;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import logik.Ampel;
import logik.AmpelKreuzung;

public class AmpelKreuzungTest {
	
	AmpelKreuzung test_kreuz = new AmpelKreuzung();
	
	private void tick(int i) {
		for (int j = 0; j<i; ++j)
			test_kreuz.tick();
	}
	
	
	/* Phase Null: ROT/GRÜN */
	@Test public void testPhaseNullRot() {
		assertEquals(true,test_kreuz.getNordRot());
		assertEquals(false,test_kreuz.getSuedRot());
	}
	
	@Test public void testPhaseNullGelb() {
		assertEquals(false,test_kreuz.getNordGelb());
		assertEquals(false,test_kreuz.getSuedGelb());
	}
	
	@Test public void testPhaseNullGruen() {
		assertEquals(false,test_kreuz.getNordGruen());
		assertEquals(true,test_kreuz.getSuedGruen());
	}
	
	
	/* Phase Eins: ROTGELB/GELB */
	@Test public void testPhaseEinsRot() {
		tick(1);
		assertEquals(true,test_kreuz.getNordRot());
		assertEquals(false,test_kreuz.getSuedRot());
	}
	
	@Test public void testPhaseEinsGelb() {
		tick(1);
		assertEquals(true,test_kreuz.getNordGelb());
		assertEquals(true,test_kreuz.getSuedGelb());
	}
	
	@Test public void testPhaseEinsGruen() {
		tick(1);
		assertEquals(false,test_kreuz.getNordGruen());
		assertEquals(false,test_kreuz.getSuedGruen());
	}
	
	//TODO hier fehlen noch einige Tests..
}
