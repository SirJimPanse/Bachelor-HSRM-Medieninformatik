package tests;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import logik.Ampel;

public class AmpelTest {
	
	Ampel test_amp = new Ampel();
	
	private void tick(int i) {
		for (int j = 0; j<i; ++j)
			test_amp.tick();
	}
	
	
	/* Phase Null: ROT  -   -   */
	@Test public void testPhaseNullRot() {
		assertEquals(true,test_amp.getROT());
	}
	
	@Test public void testPhaseNullGelb() {
		assertEquals(false,test_amp.getGELB());
	}
	
	@Test public void testPhaseNullGruen() {
		assertEquals(false,test_amp.getGRUEN());
	}
	
	
	/* Phase Eins: ROT  GELB   -   */
	@Test public void testPhaseEinsRot() {
		tick(1);
		assertEquals(true,test_amp.getROT());
	}
	
	@Test public void testPhaseEinsGelb() {
		tick(1);
		assertEquals(true,test_amp.getGELB());
	}
	
	@Test public void testPhaseEinsGruen() {
		tick(1);
		assertEquals(false,test_amp.getGRUEN());
	}
	
	
	/* Phase Zwei:  -    -    GRÜN   */
	@Test public void testPhaseZweiRot() {
		tick(2);
		assertEquals(false,test_amp.getROT());
	}
	
	@Test public void testPhaseZweiGelb() {
		tick(2);
		assertEquals(false,test_amp.getGELB());
	}
	
	@Test public void testPhaseZweiGruen() {
		tick(2);
		assertEquals(true,test_amp.getGRUEN());
	}
	
	
	/* Phase Drei:  -   GELB   -    */
	@Test public void testPhaseDreiRot() {
		tick(3);
		assertEquals(false,test_amp.getROT());
	}
	
	@Test public void testPhaseDreiGelb() {
		tick(3);
		assertEquals(true,test_amp.getGELB());
	}
	
	@Test public void testPhaseDreiGruen() {
		tick(3);
		assertEquals(false,test_amp.getGRUEN());
	}
	
	/* Genau 1000 Phasenwechsel:   ROT   -   -   */
	@Test public void test1000PhasenRot() {
		tick(1000);
		assertEquals(true,test_amp.getROT());
	}
	
	/* Genau 1000 Phasenwechsel:   ROT   -   -   */
	@Test public void test1000PhasenGelb() {
		tick(1000);
		assertEquals(false,test_amp.getGELB());
	}
	
	/* Genau 1000 Phasenwechsel:   ROT   -   -   */
	@Test public void test1000PhasenGruen() {
		tick(1000);
		assertEquals(false,test_amp.getGRUEN());
	}
	
	/* Mehr als 1000 Phasenwechsel:   -   -   -   */
	@Test public void test1001PhasenRot() {
		tick(1001);
		assertEquals(false,test_amp.getROT());
	}
	
	/* Mehr als 1000 Phasenwechsel:   -   -   -   */
	@Test public void test1001PhasenGelb() {
		tick(1001);
		assertEquals(false,test_amp.getGELB());
	}
	
	/* Mehr als 1000 Phasenwechsel:   -   -   -   */
	@Test public void test1001PhasenGruen() {
		tick(1001);
		assertEquals(false,test_amp.getGRUEN());
	}
	
	/* Mehr als 1000 Phasenwechsel, dann reset:   ROT   -   -   */
	@Test public void test1001PhasenResetRot() {
		tick(1001);
		assertEquals(false,test_amp.getROT());
		test_amp.reset();
		assertEquals(true,test_amp.getROT());
	}
	
	/* Mehr als 1000 Phasenwechsel, dann reset:   ROT   -   -   */
	@Test public void test1001PhasenResetGelb() {
		tick(1001);
		assertEquals(false,test_amp.getGELB());
		test_amp.reset();
		assertEquals(false,test_amp.getGELB());
	}
	
	/* Mehr als 1000 Phasenwechsel, dann reset:   ROT   -   -   */
	@Test public void test1001PhasenResetGruen() {
		tick(1001);
		assertEquals(false,test_amp.getGRUEN());
		test_amp.reset();
		assertEquals(false,test_amp.getGRUEN());
	}	
}
