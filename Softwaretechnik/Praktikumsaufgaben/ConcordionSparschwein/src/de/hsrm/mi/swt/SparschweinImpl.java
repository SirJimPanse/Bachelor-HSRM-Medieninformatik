package de.hsrm.mi.swt;

public class SparschweinImpl implements Sparschwein {
	
	private String topSpender;
	private int topSpende;
	private int guthaben;

	@Override
	public int getGuthaben() {
		return guthaben;
	}

	@Override
	public void spare(String spenderName, int betrag) {
		if (betrag > topSpende) {
			topSpende = betrag;
			topSpender = spenderName;
		}
		guthaben += betrag;
	}

	@Override
	public void entspare(int betrag) {
		if (betrag <= 100 && betrag <= guthaben)
			guthaben -= betrag;
		else 
			throw new SchweinUeberzogenException();
	}

	@Override
	public String getTopspender() {
		return topSpender;
	}

	@Override
	public int getTopspende() {
		return topSpende;
	}
}
