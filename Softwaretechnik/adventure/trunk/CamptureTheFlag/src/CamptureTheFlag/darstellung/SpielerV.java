package CamptureTheFlag.darstellung;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JTextArea;
import CamptureTheFlag.darstellung.TastaturHandler.Events;
import CamptureTheFlag.logik.entities.Charakter;
import CamptureTheFlag.logik.entities.Feld;
import CamptureTheFlag.logik.entities.GameObject;
import CamptureTheFlag.logik.entities.Gegenstand;
import CamptureTheFlag.logik.entities.NPC;
import CamptureTheFlag.logik.entities.Spieler;
import CamptureTheFlag.logik.models.Dialog;
import CamptureTheFlag.logik.models.Drehrichtung;
import CamptureTheFlag.logik.models.Erwiderung;
import CamptureTheFlag.logik.models.Richtung;

import javax.swing.JLabel;

/**
 * Zeichnet die untere Statusleiste
 * Mit Avatar, Hand, Dialog/Interaktionsmenu, Kompass, Inventar
 * 
 * @author shard001, Toni
 */
public class SpielerV extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = -2350402726252574464L;
	private static Logger log = Logger.getLogger(SpielerV.class.getName());

	private Spieler spieler;
	private Fenster fenster;
	private JPanel statusLeiste;
	private JScrollPane dialogScroll;
	private JTextArea txtrDialoganzeige;
	private JPanel inventar;
	private JPanel hand;
	private List<JPanel> slots;
	private List<JLabel> slotList;

	public SpielerV(Spieler s, Fenster f) {
		this.spieler = s;
		this.fenster = f;
		this.spieler.addPropertyChangeListener(this); // hört auf alle Events vom Spieler

		setLayout(null);
		this.drawStatusleiste();
	}

	/**
	 * Malt die untere Statusleiste Samt Avatar, Inventar, Panels für Dialoge
	 * etc.
	 */
	public void drawStatusleiste() {
		this.statusLeiste = new JPanel();
		this.statusLeiste.setLayout(null);
		this.statusLeiste.setBounds(0, 0, 1024, 250);
		this.statusLeiste.setBackground(Color.orange);

		this.drawAvatar(this.spieler);
		this.drawKompass();

		// erstelle ScrollPane und TextArea
		this.txtrDialoganzeige = new JTextArea(4, 20);
		this.txtrDialoganzeige.setEditable(false);
		this.dialogScroll = new JScrollPane(this.txtrDialoganzeige);
		this.dialogScroll.getVerticalScrollBar().setPreferredSize(
				new Dimension(0, 0));
		this.dialogScroll.setBounds(290, 11, 419, 144);
		this.statusLeiste.add(this.dialogScroll);
		this.dialogScroll.setVisible(false);

		// hand
		this.hand = new JPanel();
		this.hand.setBackground(Color.white);
		this.hand.setBounds(150, 15, 130, 140);
		this.drawHand();
		this.statusLeiste.add(hand);

		// Inventory
		this.inventar = new JPanel();
		this.inventar.setLayout(null);
		this.inventar.setBounds(830, 0, 300, 200);
		this.inventar.setBackground(Color.gray);

		this.slots = new ArrayList<JPanel>();
		// hintergründe aller slots weiß machen und auf inventar-panel ablegen
		for (int i = 0; i < 9; i++) {
			JPanel p = new JPanel();
			p.setBackground(Color.white);
			this.slots.add(new JPanel());

		}

		// inventar slots platzieren
		this.slots.get(0).setBounds(10, 10, 45, 45);
		this.slots.get(1).setBounds(70, 10, 45, 45);
		this.slots.get(2).setBounds(130, 10, 45, 45);
		this.slots.get(3).setBounds(10, 65, 45, 45);
		this.slots.get(4).setBounds(70, 65, 45, 45);
		this.slots.get(5).setBounds(130, 65, 45, 45);
		this.slots.get(6).setBounds(10, 120, 45, 45);
		this.slots.get(7).setBounds(70, 120, 45, 45);
		this.slots.get(8).setBounds(130, 120, 45, 45);

		// slots auf inventar ablegen
		for (JPanel p : this.slots) {
			this.inventar.add(p);
		}

		// JLbalen mit SlotNummern erzeugen und in SlotListe ablegen
		this.slotList = new ArrayList<JLabel>();
		this.slotList = new ArrayList<JLabel>();
		for (int i = 1; i <= 9; i++) {
			JLabel slotNr = new JLabel(Integer.toString(i));
			this.slotList.add(slotNr);
		}

		// SlotNummern platzieren
		this.slotList.get(0).setBounds(59, -15, 100, 100);
		this.slotList.get(1).setBounds(119, -15, 100, 100);
		this.slotList.get(2).setBounds(178, -15, 100, 100);
		this.slotList.get(3).setBounds(59, 40, 100, 100);
		this.slotList.get(4).setBounds(119, 40, 100, 100);
		this.slotList.get(5).setBounds(178, 40, 100, 100);
		this.slotList.get(6).setBounds(59, 90, 100, 100);
		this.slotList.get(7).setBounds(119, 90, 100, 100);
		this.slotList.get(8).setBounds(178, 90, 100, 100);

		// SlotNummern auf Inventar ablegen
		for (JLabel l : this.slotList) {
			this.inventar.add(l);
		}

		this.statusLeiste.add(this.inventar);
		this.drawInventar();

		// am ende alles adden
		this.statusLeiste.revalidate();
		this.add(this.statusLeiste);

		// listener um focus wieder ans fenster zu geben, wenn in das textfeld
		// geklickt wurde
		this.txtrDialoganzeige.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				fenster.requestFocus();
			}
		});

	}

	/** 
	 * Malt den Kompass mit aktueller Blickrichtung des Spielers 
	 */
	private void drawKompass() {
		JPanel kompass = new JPanel();
		kompass.setBounds(710, 35, 90, 90);
		kompass.setBackground(Color.black);
		Richtung blickrichtung = spieler.getBlick();
		switch (blickrichtung) {
			case NORD:
				kompass.add(new JLabel(DarstellungHelper.imageScale(DarstellungHelper.getImage("nord.png"),80,80)));
				break;
			case OST:
				kompass.add(new JLabel(DarstellungHelper.imageScale(DarstellungHelper.getImage("ost.png"),80,80)));
				break;
			case SUED:
				kompass.add(new JLabel(DarstellungHelper.imageScale(DarstellungHelper.getImage("sued.png"),80,80)));
				break;
			case WEST:
				kompass.add(new JLabel(DarstellungHelper.imageScale(DarstellungHelper.getImage("west.png"),80,80)));
				break;
		}
		this.statusLeiste.add(kompass);
		this.statusLeiste.revalidate();
	}


	/**
	 * Malt das Inventar Zeichnet die Gegenstände im Inventar des Spielers
	 */
	private void drawInventar() {
		int i = 0;

		// slots löschen
		try {
			for (JPanel slot : this.slots) {
				slot.removeAll();
				slot.repaint();
			}
		} catch (NullPointerException e) {
			// nichts zum löschen, macht ja nix
		}

		// gegenstände auf slots ablegen
		for (Gegenstand g : this.spieler.getInventoryVorrat()) {
			JPanel slot = slots.get(i);

			ImageIcon imageIcon = DarstellungHelper.getImage(g.getImg());

			slot.add(new JLabel(DarstellungHelper.imageScale(imageIcon)));
			slot.repaint();
			i++;
		}

		this.inventar.revalidate();
		this.statusLeiste.revalidate();
	}

	/**
	 * Stellt den Dialog mit einem NPC dar
	 * 
	 * @param npc
	 *            Der NPC, mit dem Interagiert wird
	 */
	private void drawInteraktion(NPC npc) {
		log.log(Level.INFO, "Zeige Dialog an");

		/*
		 * Baue Text aus Dialog und Erwiderungen zusammen
		 */
		Dialog d = npc.getAktDialog();
		String ret = npc.getName() + " sagt:\n" + d.getText() + "\n\n";
		ret += "0 abbrechen\n";

		if (d.getErwiderungen() != null) {
			int i = 1;
			for (Erwiderung e : d.getErwiderungen()) {
				ret += i++ + " " + e.getText();

				if (e.getTribut() != null) { // Tribut-Typ anzeigen
					String klasse = e.getTribut().getClass().toString();
					ret += " (" + klasse.substring(klasse.lastIndexOf('.') + 1)
							+ ")";
				}

				ret += "\n";
			}
		}

		this.txtrDialoganzeige.setText(ret);

		// Zeigt die erste Zeile an, indem dort der Caret positioniert wird
		this.txtrDialoganzeige.setCaretPosition(0);

		this.dialogScroll.setVisible(true);
		this.statusLeiste.revalidate();
	}

	/**
	 * Stellt den Gegenstand in der Hand des Spielers dar oder eine leere Hand,
	 * wenn die Hand == null ist
	 */
	private void drawHand() {
		try {
			this.hand.removeAll();
		} catch (NullPointerException e) {
			// nichts zum löschen, macht ja nix
		}

		JLabel image;
		if (this.spieler.getHand() == null) {
			image = new JLabel(DarstellungHelper.getImage("hand.png"));
		} else {
			image = new JLabel(DarstellungHelper.getImage(this.spieler.getHand().getImg()));
		}

		this.hand.add(image);
		this.hand.repaint();
		this.statusLeiste.revalidate();
	}

	/**
	 * Stellt das Bild des Spielers dar Falls gerade mit einem NPC interagiert
	 * wird, wird dessen Bild gezeigt
	 * 
	 * @param c
	 */
	private void drawAvatar(Charakter c) {
		JPanel portrait = new JPanel();

		JLabel image = new JLabel(DarstellungHelper.imageScale(DarstellungHelper.getImage(c.getImg()),200,200));
		portrait.setBackground(Color.white);
		portrait.setBounds(10, 15, 130, 140);

		portrait.add(image);

		this.statusLeiste.add(portrait);
		this.statusLeiste.revalidate();
	}
	

	// hört auf verschiedene Events
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		log.log(Level.INFO, "got event '" + arg0.getPropertyName() + "' old: '"
				+ arg0.getOldValue() + "' new: '" + arg0.getNewValue() + "'");

		switch (Events.valueOf(arg0.getPropertyName())) {
			/*
			 * Bei Interaktion muss unterschieden werden, mit was interagiert wird
			 * oder ob abgebrochen wurde
			 */
			case INTERAGIERE:
				// unterscheide, mit was interagiert wird (feld, npc, item usw.)
				if (arg0.getNewValue() instanceof Feld) {
	
					// TODO Liste von this.spieler.getAktFeld().getObjects()
					// anzeigen
					String res = ""; //maximal 9 Elemente.. Stringkonkatenation passt
					
					res += ("0 abbrechen\n");
					int i = 1;
					for (GameObject o : this.spieler.getAktFeld().getObjects()) {
						res += (i++ + " " + o.getName());
						if (o.getBeschreibung() != null) {
							res += (" (" + o.getBeschreibung() + ")");
						}
	
						res += ("\n");
					}
					
					this.txtrDialoganzeige.setText(res);
	
					// Zeigt die erste Zeile an, indem dort der Caret positioniert wird
					this.txtrDialoganzeige.setCaretPosition(0);
	
					this.dialogScroll.setVisible(true);
					//revalidate() zu repaint() geändert
					this.statusLeiste.revalidate();
	
				} else if (arg0.getNewValue() instanceof NPC) {
	
					NPC npc = ((NPC) arg0.getNewValue());
					this.drawInteraktion(npc);
					this.drawAvatar(npc);
	
				} else if (arg0.getNewValue() == null) {
	
					log.log(Level.INFO, "Interaktion abbrechen");
	
					this.dialogScroll.setVisible(false);
					//revalidate() zu repaint() geändert
					this.statusLeiste.revalidate();
					this.drawAvatar(this.spieler); // wieder Avatar des Spielers
													// darstellen
	
				}
	
				break;
	
			/*
			 * Es wurd ein Gegenstand von der Hand des Spielers auf das aktFeld
			 * abgelegt Beide Ansichten müssen aktualisiert werden
			 */
			case ABLEGEN:
				this.drawHand();
				this.drawInventar();
	
				break;
	
			/*
			 * Es wurd ein Gegenstand von der Hand des Spielers in seinem Inventar
			 * abgelegt Beide Ansichten müssen aktualisiert werden
			 */
			case AUFNEHMEN:
				this.drawHand();
				this.drawInventar();
	
				break;
	
			/*
			 * Es wurde ein Gegenstand in die Hand genommen
			 */
			case INHAND:
				this.drawHand();
				this.drawInventar();
	
				break;

			/*
			 * es wurde die Blickrichtung geändert
			 */
			case BEWEGT:
				if (arg0.getNewValue() instanceof Drehrichtung) {
					this.drawKompass();
				}
		}
	}

}