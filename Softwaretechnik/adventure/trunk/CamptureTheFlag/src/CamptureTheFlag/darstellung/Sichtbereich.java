package CamptureTheFlag.darstellung;

import java.awt.Color;
import java.awt.Font;
import java.awt.Polygon;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;

import CamptureTheFlag.darstellung.TastaturHandler.Events;
import CamptureTheFlag.logik.models.Drehrichtung;
import CamptureTheFlag.logik.models.Richtung;
import CamptureTheFlag.logik.entities.Feld;
import CamptureTheFlag.logik.entities.GameObject;
import CamptureTheFlag.logik.entities.Ingame;
import CamptureTheFlag.logik.entities.Spieler;

/**
 * Zeichnet das Spielfeld Aktualisiert sich selbst, wenn sich der Spieler bewegt
 * Die erstellten FeldV aktualisieren sich ebenfalls selbst, wenn sich auf dem
 * Feld etwas ändert
 * 
 * @author dom, shardt001
 */
public class Sichtbereich extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 5959021095503136986L;

	private static Logger log = Logger.getLogger(Sichtbereich.class.getName());

	private static final int SZENEN_BREITE = 1024;
	private static final int SZENEN_HOEHE = 500;

	private FeldV[] felder;
	private Spieler spieler;
	private Feld aktFeldSpieler;
	private Richtung aktBlickSpieler;
	private Richtung aktRechts;
	private Richtung aktLinks;
	@SuppressWarnings("unused") // wird nicht verwendet, nur erstellt
	private Richtung aktHinten;
	private Ingame ingame;
	private JPanel gewonnen;

	private int x0 = 0;
	private int x1 = SZENEN_BREITE;
	private int x2 = SZENEN_BREITE - 25 * (SZENEN_BREITE / 100);
	private int x3 = 25 * (SZENEN_BREITE / 100);
	private int x4 = SZENEN_BREITE - 38 * (SZENEN_BREITE / 100);
	private int x5 = 38 * (SZENEN_BREITE / 100);
	private int x6 = SZENEN_BREITE - 44 * (SZENEN_BREITE / 100);
	private int x7 = 44 * (SZENEN_BREITE / 100);

	private int y0 = SZENEN_HOEHE;
	private int y1 = SZENEN_HOEHE - 40 * (SZENEN_HOEHE / 100);
	private int y2 = SZENEN_HOEHE - 60 * (SZENEN_HOEHE / 100);
	private int y3 = SZENEN_HOEHE - 70 * (SZENEN_HOEHE / 100);
	private int y4 = 0;

	/*
	 * Polygone fuer die Waende der 9 Felder, die beim Erstellen mit uebergeben
	 * werden.
	 */
	int[] xPoints_wand1 = { x0, x3, x3, x0 };
	int[] yPoints_wand1 = { y0, y1, y4, y4 };
	Polygon poly_wand1 = new Polygon(xPoints_wand1, yPoints_wand1, 4);

	int[] xPoints_wand2 = { x3, x2, x2, x3 };
	int[] yPoints_wand2 = { y1, y1, y4, y4 };
	Polygon poly_wand2 = new Polygon(xPoints_wand2, yPoints_wand2, 4);

	int[] xPoints_wand3 = { x2, x1, x1, x2 };
	int[] yPoints_wand3 = { y1, y0, y4, y4 };
	Polygon poly_wand3 = new Polygon(xPoints_wand3, yPoints_wand3, 4);

	int[] xPoints_wand4 = { x3, x3, x5, x5 };
	int[] yPoints_wand4 = { y4, y1, y2, y4 };
	Polygon poly_wand4 = new Polygon(xPoints_wand4, yPoints_wand4, 4);

	int[] xPoints_wand5 = { x5, x4, x4, x5 };
	int[] yPoints_wand5 = { y2, y2, y4, y4 };
	Polygon poly_wand5 = new Polygon(xPoints_wand5, yPoints_wand5, 4);

	int[] xPoints_wand6 = { x4, x2, x2, x4 };
	int[] yPoints_wand6 = { y2, y1, y4, y4 };
	Polygon poly_wand6 = new Polygon(xPoints_wand6, yPoints_wand6, 4);

	int[] xPoints_wand7 = { x5, x5, x7, x7 };
	int[] yPoints_wand7 = { y4, y2, y3, y4 };
	Polygon poly_wand7 = new Polygon(xPoints_wand7, yPoints_wand7, 4);

	int[] xPoints_wand8 = { x7, x6, x6, x7 };
	int[] yPoints_wand8 = { y3, y3, y4, y4 };
	Polygon poly_wand8 = new Polygon(xPoints_wand8, yPoints_wand8, 4);

	int[] xPoints_wand9 = { x6, x4, x4, x6 };
	int[] yPoints_wand9 = { y3, y2, y4, y4 };
	Polygon poly_wand9 = new Polygon(xPoints_wand9, yPoints_wand9, 4);

	int[] xPoints_wand10 = { x0, x3, x3, x0 };
	int[] yPoints_wand10 = { y1, y1, y4, y4 };
	Polygon poly_wand10 = new Polygon(xPoints_wand10, yPoints_wand10, 4);

	int[] xPoints_wand11 = { x0, x5, x5, x0 };
	int[] yPoints_wand11 = { y2, y2, y4, y4 };
	Polygon poly_wand11 = new Polygon(xPoints_wand11, yPoints_wand11, 4);

	int[] xPoints_wand12 = { x0, x7, x7, x0 };
	int[] yPoints_wand12 = { y3, y3, y4, y4 };
	Polygon poly_wand12 = new Polygon(xPoints_wand12, yPoints_wand12, 4);

	int[] xPoints_wand13 = { x2, x1, x1, x2 };
	int[] yPoints_wand13 = { y1, y1, y4, y4 };
	Polygon poly_wand13 = new Polygon(xPoints_wand13, yPoints_wand13, 4);

	int[] xPoints_wand14 = { x4, x1, x1, x4 };
	int[] yPoints_wand14 = { y2, y2, y4, y4 };
	Polygon poly_wand14 = new Polygon(xPoints_wand14, yPoints_wand14, 4);

	int[] xPoints_wand15 = { x6, x1, x1, x6 };
	int[] yPoints_wand15 = { y3, y3, y4, y4 };
	Polygon poly_wand15 = new Polygon(xPoints_wand15, yPoints_wand15, 4);

	public Sichtbereich(Spieler spieler, Ingame ingame) {
		this.spieler = spieler;
		this.ingame = ingame;
		this.spieler.addPropertyChangeListener(this);
		this.ingame.addPropertyChangeListener(Events.GEWONNEN.toString(), this);
		
		this.felder = new FeldV[9]; // array für alle sichtbaren JPanels
		this.aktFeldSpieler = this.spieler.getAktFeld();
		this.aktBlickSpieler = this.spieler.getBlick();
		this.aktRechts = Richtung.values()[(aktBlickSpieler.ordinal() + 1)
				% Richtung.values().length];
		this.aktHinten = Richtung.values()[(aktBlickSpieler.ordinal()
				+ Richtung.values().length - 2)
				% Richtung.values().length];
		this.aktLinks = Richtung.values()[(aktBlickSpieler.ordinal()
				+ Richtung.values().length - 1)
				% Richtung.values().length];

		this.setLayout(null);

		// höhe und breite an FeldV (klassenvariable) weitergeben
		FeldV.setBreite(SZENEN_BREITE);
		FeldV.setHoehe(SZENEN_HOEHE);

		// gewonnen.banner (erst unsichtbar)
		this.gewonnen = new JPanel();
		this.gewonnen.setLayout(null);

		this.gewonnen.setLayout(null);		
		this.gewonnen.setBackground(Color.white);
		this.gewonnen.setBounds(0, 150, 1024, 200);

		this.gewonnen.setBounds(0, 150, 1024, 200);		
		JLabel txtGewonnen = new JLabel();
		txtGewonnen.setForeground(Color.orange);
		txtGewonnen.setOpaque(false);
		txtGewonnen.setFont(new Font("Serif", Font.BOLD, 50));
		txtGewonnen.setBounds(350, 10, 1024, 100);
		txtGewonnen.setText("Spielziel erreicht!");
		this.gewonnen.add(txtGewonnen);
		this.gewonnen.setVisible(false);
		this.add(this.gewonnen);

		// felder zeichnen
		this.draw();
	}

	/**
	 * zeichnet alle benötigten JPanels für Felder, Wände erstellt passende
	 * FeldV für die Felder
	 */
	private void draw() {
		/*
		 * Die 9 Felder, die mit ihrem Feld-Polygon und ihren jeweiligen
		 * Wand-Polygonen instanziiert werden.
		 */
		int[] xPoints_feld1 = { x0, x3, x0 };
		int[] yPoints_feld1 = { y0, y1, y1 };
		Polygon poly_feld1 = new Polygon(xPoints_feld1, yPoints_feld1, 3);
		FeldV feldVornLinks = new FeldV(poly_feld1, poly_wand10, null,
				this.aktFeldSpieler.getNachbar(Richtung.WEST), Position.VORN);

		int[] xPoints_feld2 = { x0, x1, x2, x3 };
		int[] yPoints_feld2 = { y0, y0, y1, y1 };
		Polygon poly_feld2 = new Polygon(xPoints_feld2, yPoints_feld2, 4);
		FeldV feldVorn = new FeldV(poly_feld2, poly_wand2, null, poly_wand1,
				poly_wand3, this.aktFeldSpieler, Position.VORN);

		int[] xPoints_feld3 = { x1, x1, x2 };
		int[] yPoints_feld3 = { y0, y1, y1 };
		Polygon poly_feld3 = new Polygon(xPoints_feld3, yPoints_feld3, 3);
		FeldV feldVornRechts = new FeldV(poly_feld3, poly_wand13, null,
				this.aktFeldSpieler.getNachbar(Richtung.OST), Position.VORN);

		int[] xPoints_feld4 = { x0, x3, x5, x0 };
		int[] yPoints_feld4 = { y1, y1, y2, y2 };
		Polygon poly_feld4 = new Polygon(xPoints_feld4, yPoints_feld4, 4);
		FeldV feldMitteLinks = new FeldV(poly_feld4, poly_wand11, poly_wand10,
				this.aktFeldSpieler.getNachbar(Richtung.NORD).getNachbar(
						Richtung.WEST), Position.MITTEL);

		int[] xPoints_feld5 = { x3, x2, x4, x5 };
		int[] yPoints_feld5 = { y1, y1, y2, y2 };
		Polygon poly_feld5 = new Polygon(xPoints_feld5, yPoints_feld5, 4);
		FeldV feldMitte = new FeldV(poly_feld5, poly_wand5, poly_wand2,
				poly_wand4, poly_wand6,
				this.aktFeldSpieler.getNachbar(Richtung.NORD), Position.MITTEL);

		int[] xPoints_feld6 = { x2, x1, x1, x4 };
		int[] yPoints_feld6 = { y1, y1, y2, y2 };
		Polygon poly_feld6 = new Polygon(xPoints_feld6, yPoints_feld6, 4);
		FeldV feldMitteRechts = new FeldV(poly_feld6, poly_wand14, poly_wand13,
				this.aktFeldSpieler.getNachbar(Richtung.NORD).getNachbar(
						Richtung.OST), Position.MITTEL);

		int[] xPoints_feld7 = { x0, x5, x7, x0 };
		int[] yPoints_feld7 = { y2, y2, y3, y3 };
		Polygon poly_feld7 = new Polygon(xPoints_feld7, yPoints_feld7, 4);
		FeldV feldHintenLinks = new FeldV(poly_feld7, poly_wand12, poly_wand11,
				this.aktFeldSpieler.getNachbar(Richtung.NORD)
						.getNachbar(Richtung.NORD).getNachbar(Richtung.WEST),
				Position.HINTEN);

		int[] xPoints_feld8 = { x5, x4, x6, x7 };
		int[] yPoints_feld8 = { y2, y2, y3, y3 };
		Polygon poly_feld8 = new Polygon(xPoints_feld8, yPoints_feld8, 4);
		FeldV feldHinten = new FeldV(poly_feld8, poly_wand8, poly_wand5,
				poly_wand7, poly_wand9, this.aktFeldSpieler.getNachbar(
						Richtung.NORD).getNachbar(Richtung.NORD),
				Position.HINTEN);

		int[] xPoints_feld9 = { x4, x1, x1, x6 };
		int[] yPoints_feld9 = { y2, y2, y3, y3 };
		Polygon poly_feld9 = new Polygon(xPoints_feld9, yPoints_feld9, 4);
		FeldV feldHintenRechts = new FeldV(poly_feld9, poly_wand15,
				poly_wand14, this.aktFeldSpieler.getNachbar(Richtung.NORD)
						.getNachbar(Richtung.NORD).getNachbar(Richtung.OST),
				Position.HINTEN);

		/*
		 * Hinzufuegen der 9 Felder zum Feld-Array, wobei die Reihenfolge fuer
		 * das Zeichnen beachtet werden muss.
		 */
		this.felder[0] = feldVorn;
		this.felder[1] = feldVornLinks;
		this.felder[2] = feldVornRechts;
		this.felder[3] = feldMitte;
		this.felder[4] = feldMitteLinks;
		this.felder[5] = feldMitteRechts;
		this.felder[6] = feldHinten;
		this.felder[7] = feldHintenLinks;
		this.felder[8] = feldHintenRechts;

		for (int i = 0; i < felder.length; i++) {
			this.add(felder[i]);
		}
		
		
		this.itemsAufFeldNeuMalen();
	}

	/**
	 * zeichnet einen Hinweis, dass das Spielziel erreicht wurde Der Spieler
	 * kann danach dennoch weiterspielen
	 */
	private void drawGewonnen() {
		log.log(Level.INFO, "Spielziel erreicht");
		this.gewonnen.setVisible(true);
		this.revalidate();
	}
	
	private void itemsAufFeldNeuMalen() {
		
		this.felder[0].removeAll();
		try{
			for(GameObject obj : felder[0].getFeld().getObjects()){
				JLabel label = new JLabel(DarstellungHelper.getImage(obj.getImg()));
				
				/*
				 *  eingänge auf Boden legen
				 *  klappt, wenn FeldV setLayout(null) hat
				 *  dann werden die NPCs aber abgeschnitten
				 */
//				if (obj instanceof Eingang) {
//					label.setBounds(200, 300, 128, 128);
//				} else {
//					label.setBounds(felder[0].getBoden().xpoints[3]+(felder[0].getBoden().xpoints[2]-felder[0].getBoden().xpoints[3])/2, felder[0].getBoden().ypoints[0]-(felder[0].getBoden().ypoints[3]-felder[0].getBoden().ypoints[0])/2, DarstellungHelper.getImage(obj.getImg()).getIconWidth(), DarstellungHelper.getImage(obj.getImg()).getIconHeight());
//				}
				
				// ohne setLayout(null)
				label.setBounds(felder[0].getBoden().xpoints[3]+(felder[0].getBoden().xpoints[2]-felder[0].getBoden().xpoints[3])/2, felder[0].getBoden().ypoints[0]-(felder[0].getBoden().ypoints[3]-felder[0].getBoden().ypoints[0])/2, DarstellungHelper.getImage(obj.getImg()).getIconWidth(), DarstellungHelper.getImage(obj.getImg()).getIconHeight());
				felder[0].add(label);
				felder[0].revalidate();
			}
		}catch(Exception e){}
	}

	// durch Anmeldung nur für Events.BEWEGT
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		log.log(Level.INFO, "got event '" + arg0.getPropertyName() + "' old: '"
				+ arg0.getOldValue() + "' new: '" + arg0.getNewValue() + "'");

		 this.felder[0].removeAll();
		 this.felder[3].removeAll();
		 this.felder[6].removeAll();
		 
		switch (Events.valueOf(arg0.getPropertyName())) {
		/*
		 * Der Spieler hat das Spielziel erreicht Es wird eine Meldung
		 * eingeblendet, dass das Spiel nun beendet wird Evtl. kann er noch
		 * weiterspielen
		 */
		case GEWONNEN:
			this.drawGewonnen();
			break;

		/*
		 * Der Spieler hat sich bewegt Felder, Wände usw. müssen neu Gezeichnet
		 * werden
		 */
		case BEWEGT:

			this.gewonnen.setVisible(false);

			for (FeldV f : felder) {
				f.removeAll();
			}

			this.aktFeldSpieler = this.spieler.getAktFeld(); // Feld hat sich
																// geändert
			this.aktBlickSpieler = this.spieler.getBlick();
			this.aktRechts = Richtung.values()[(aktBlickSpieler.ordinal() + 1)
					% Richtung.values().length];
			this.aktHinten = Richtung.values()[(aktBlickSpieler.ordinal()
					+ Richtung.values().length - 2)
					% Richtung.values().length];
			this.aktLinks = Richtung.values()[(aktBlickSpieler.ordinal()
					+ Richtung.values().length - 1)
					% Richtung.values().length];
			this.aktBlickSpieler = this.spieler.getBlick();
			this.aktRechts = Richtung.values()[(aktBlickSpieler.ordinal() + 1)
					% Richtung.values().length];
			this.aktHinten = Richtung.values()[(aktBlickSpieler.ordinal()
					+ Richtung.values().length - 2)
					% Richtung.values().length];
			this.aktLinks = Richtung.values()[(aktBlickSpieler.ordinal()
					+ Richtung.values().length - 1)
					% Richtung.values().length];

			Polygon temp;

			if (arg0.getNewValue() instanceof Drehrichtung) {

				if (arg0.getNewValue() == Drehrichtung.LINKS) {
					for (FeldV f : felder) {
						temp = f.getWandN();
						f.setWandN(f.getWandO());
						f.setWandO(f.getWandS());
						f.setWandS(f.getWandW());
						f.setWandW(temp);
						temp = null;
					}
				}

				else if (arg0.getNewValue() == Drehrichtung.RECHTS) {
					for (FeldV f : felder) {
						temp = f.getWandN();
						f.setWandN(f.getWandW());
						f.setWandW(f.getWandS());
						f.setWandS(f.getWandO());
						f.setWandO(temp);
						temp = null;
					}
				}

			}

			else if (arg0.getNewValue() instanceof Feld) {

			}

			this.aktFeldSpieler = this.spieler.getAktFeld(); // Feld hat sich
																// geändert

			try {
				this.felder[0].setFeld(this.aktFeldSpieler);
				this.felder[1].setFeld(this.aktFeldSpieler
						.getNachbar(this.aktLinks));
				this.felder[2].setFeld(this.aktFeldSpieler
						.getNachbar(this.aktRechts));
				this.felder[3].setFeld(this.aktFeldSpieler
						.getNachbar(this.aktBlickSpieler));
				this.felder[4].setFeld(this.aktFeldSpieler.getNachbar(
						this.aktBlickSpieler).getNachbar(this.aktLinks));
				this.felder[5].setFeld(this.aktFeldSpieler.getNachbar(
						this.aktBlickSpieler).getNachbar(this.aktRechts));
				this.felder[6].setFeld(this.aktFeldSpieler.getNachbar(
						this.aktBlickSpieler).getNachbar(this.aktBlickSpieler));
				this.felder[7].setFeld(this.aktFeldSpieler
						.getNachbar(this.aktBlickSpieler)
						.getNachbar(this.aktBlickSpieler)
						.getNachbar(this.aktLinks));
				this.felder[8].setFeld(this.aktFeldSpieler
						.getNachbar(this.aktBlickSpieler)
						.getNachbar(this.aktBlickSpieler)
						.getNachbar(this.aktRechts));
			} catch (NullPointerException e) {
				log.log(Level.INFO, "Keine angrenzenden Felder mehr");
			}

			try {
				this.felder[0].setFeld(this.aktFeldSpieler);
				this.felder[1].setFeld(this.aktFeldSpieler
						.getNachbar(this.aktLinks));
				this.felder[2].setFeld(this.aktFeldSpieler
						.getNachbar(this.aktRechts));
				this.felder[3].setFeld(this.aktFeldSpieler
						.getNachbar(this.aktBlickSpieler));
				this.felder[4].setFeld(this.aktFeldSpieler.getNachbar(
						this.aktBlickSpieler).getNachbar(this.aktLinks));
				this.felder[5].setFeld(this.aktFeldSpieler.getNachbar(
						this.aktBlickSpieler).getNachbar(this.aktRechts));
				this.felder[6].setFeld(this.aktFeldSpieler.getNachbar(
						this.aktBlickSpieler).getNachbar(this.aktBlickSpieler));
				this.felder[7].setFeld(this.aktFeldSpieler
						.getNachbar(this.aktBlickSpieler)
						.getNachbar(this.aktBlickSpieler)
						.getNachbar(this.aktLinks));
				this.felder[8].setFeld(this.aktFeldSpieler
						.getNachbar(this.aktBlickSpieler)
						.getNachbar(this.aktBlickSpieler)
						.getNachbar(this.aktRechts));
			
			
			} catch (NullPointerException e) {
				log.log(Level.INFO, "Keine angrenzenden Felder mehr");
			}

			break;
			
		case ABLEGEN:
		case AUFNEHMEN:
			// nichts zu tun, da this.itemsAufFeldNeuMalen(); unten nochmal kommt
			break;
		}
		
		this.itemsAufFeldNeuMalen();
		this.repaint();
	
	}

}
