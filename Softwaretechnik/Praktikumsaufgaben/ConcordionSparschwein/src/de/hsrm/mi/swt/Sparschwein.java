package de.hsrm.mi.swt;

public interface Sparschwein {

	/**
	 * @return aktuelles Guthaben im Sparschwein
	 */
	public abstract int getGuthaben();

	/**
	 * Spender wirft Betrag ins Sparschwein
	 * @param spenderName - Name des edlen Spenders
	 * @param betrag - gespendeter Betrag
	 */
	public abstract void spare(String spenderName, int betrag);

	/**
	 * @param betrag - dem Sparschwein zu entnehmender Betrag
	 */
	public abstract void entspare(int betrag);

	/**
	 * @return Name des ersten Spenders des bisher höchsten Beitrags
	 */
	public abstract String getTopspender();

	/**
	 * @return Höhe des bisher höchsten Beitrags
	 */
	public abstract int getTopspende();

}