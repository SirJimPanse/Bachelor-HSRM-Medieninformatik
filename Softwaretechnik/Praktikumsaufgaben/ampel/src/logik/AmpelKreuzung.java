package logik;

public class AmpelKreuzung {
	
	private Ampel nord;
	private Ampel ost;
	
	public AmpelKreuzung() {
		nord = new Ampel();
		ost = new Ampel();
	}
	public void tick() {
		nord.tick();
		ost.tick();
	}
	
	public boolean getNordRot() { return false; }
	public boolean getNordGelb() { return false; }
	public boolean getNordGruen() { return false; }

	public boolean getSuedRot() { return getNordRot(); }
	public boolean getSuedGelb() { return getNordGelb(); }
	public boolean getSuedGruen() { return getNordGruen(); }
	
	public boolean getOstRot() { return false; }
	public boolean getOstGelb() { return false; }
	public boolean getOstGruen() { return false; }
	
	public boolean getWestRot() { return getOstRot(); }
	public boolean getWestGelb() { return getOstGelb(); }
	public boolean getWestGruen() { return getOstGruen(); }
}
