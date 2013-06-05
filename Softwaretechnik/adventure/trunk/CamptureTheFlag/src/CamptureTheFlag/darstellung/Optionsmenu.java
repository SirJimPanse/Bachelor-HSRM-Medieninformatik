package CamptureTheFlag.darstellung;

import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import CamptureTheFlag.logik.GameC.Zustand;

/**
 * Zeigt das Hauptmenu mit den passenden Buttons an
 * hier kann z.B. die Tastaturbelegung geändert werden
 * 
 * @author Toni
 */
public class Optionsmenu extends JPanel {

	private static final long serialVersionUID = -5767743905666535408L;
	private Fenster fenster;
	private Zustand from;
	private List<JTextField> textFieldList;
	private TastaturHandler tastaturHandler;

	public Optionsmenu(Fenster f, TastaturHandler t) {
		this.fenster = f;
		this.tastaturHandler = t;

		this.setLayout(null);
		this.draw();
	}

	public void setFrom(Zustand from) {
		this.from = from;
	}

	/**
	 * zeichnet alle Elemente des Optionsmenus
	 * und legt sie auf sich selbst ab
	 */
	private void draw() {
		final JPanel options = new JPanel();
		options.setLayout(null);
		options.setBounds(0, 0, 1024, 700);

		Label header = new Label();
		header.setForeground(Color.BLACK);
		header.setFont(new Font("Serif", Font.BOLD, 50));
		header.setBounds(350, 10, 300, 100);
		header.setText("Optionen");

		JButton tastenBelegung = new JButton("Tastenbelegung");
		tastenBelegung.setBounds(10, 540, 200, 40);
		ActionListener tb = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				options.setVisible(false);
				drawTastenBelegung();
				fenster.fireRequestFocus();
			}
		};
		tastenBelegung.addActionListener(tb);

		JButton zurueck = new JButton("Zurück");
		zurueck.setBounds(10, 600, 200, 40);
		ActionListener zur = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// geht zu aufrufer-zustand zurück
				fenster.gameC.setZustand(Optionsmenu.this.from);
				fenster.fireRequestFocus();
			}
		};
		zurueck.addActionListener(zur);

		options.add(tastenBelegung);
		options.add(zurueck);
		options.add(header);
		options.revalidate();

		this.add(options);
	}

	// TODO Tastaturbelegung ändern geht noch nicht
	/**
	 * Zeichnet einen Dialog um die Tastaturbelegung anzusehen und zu bearbeiten
	 */
	private void drawTastenBelegung() {
		/*
		 * In die Textfelder wird der KeyCode für das gewünschte TastaturEvent
		 * geschrieben alle TastaturEvent sind via tastaturHandler.KEY_MENU usw.
		 * erreichbar (es gibt NUR diese)
		 * 
		 * Nun alle TastaturEvent durchgehen und mit
		 * tastaturHandler.setKeyForEvent(KeyCode, tastaturHandler.KEY_MENU)
		 * setzten
		 * 
		 * Anzeigen der aktuellen KeyCodes mit
		 * tastaturHandler.getKeyCodeFromEvent(tastaturHandler.KEY_MENU) usw.
		 */

		final JPanel tBelegung = new JPanel();
		tBelegung.setLayout(null);
		tBelegung.setBounds(0, 0, 1024, 700);

		JLabel header = new JLabel("Tastenbelegung");
		header.setForeground(Color.BLACK);
		header.setFont(new Font("Serif", Font.BOLD, 50));
		header.setBounds(300, 10, 400, 100);
		header.setOpaque(false);

		/*
		JLabel aktBelegung = new JLabel("aktuelle Belegung");
		aktBelegung.setFont(new Font("Serif", Font.BOLD, 18));
		aktBelegung.setBounds(700, 100, 400, 100);
		 */
		this.textFieldList = new ArrayList<JTextField>();

		for (int i = 0; i <= 10; i++) {
			JTextField p = new JTextField();
			p.setBackground(Color.white);
			this.textFieldList.add(new JTextField());

		}

		JLabel esc = new JLabel("Spiel unterbrechen/fortsetzen");// Esc
		esc.setBounds(200, 180, 200, 25);
		this.textFieldList.get(0).setBounds(400, 180, 200, 25);

		JLabel tLeft = new JLabel("Links drehen");// q
		tLeft.setBounds(200, 210, 200, 25);
		this.textFieldList.get(1).setBounds(400, 210, 200, 25);

		JLabel tRight = new JLabel("Rechts drehen");// e
		tRight.setBounds(200, 240, 200, 25);
		this.textFieldList.get(2).setBounds(400, 240, 200, 25);

		JLabel goNorth = new JLabel("Vorwärts gehen");// w
		goNorth.setBounds(200, 270, 200, 25);
		this.textFieldList.get(3).setBounds(400, 270, 200, 25);

		JLabel goWest = new JLabel("Westwärts gehen");// a
		goWest.setBounds(200, 300, 200, 25);
		this.textFieldList.get(4).setBounds(400, 300, 200, 25);

		JLabel goSouth = new JLabel("Südwärts gehen");// s
		goSouth.setBounds(200, 330, 200, 25);
		this.textFieldList.get(5).setBounds(400, 330, 200, 25);

		JLabel goEast = new JLabel("Ostwärts gehen");// d
		goEast.setBounds(200, 360, 200, 25);
		this.textFieldList.get(6).setBounds(400, 360, 200, 25);

		JLabel interaction = new JLabel("Interagieren");// i
		interaction.setBounds(200, 390, 200, 25);
		this.textFieldList.get(7).setBounds(400, 390, 200, 25);

		JLabel drop = new JLabel("Item ablegen");// space
		drop.setBounds(200, 420, 200, 25);
		this.textFieldList.get(8).setBounds(400, 420, 200, 25);

		JLabel take = new JLabel("Item aufnehmen");// n
		take.setBounds(200, 450, 200, 25);
		this.textFieldList.get(9).setBounds(400, 450, 200, 25);

		for (JTextField field : this.textFieldList) {
			tBelegung.add(field);
		}

		JButton aendern = new JButton("Belegung ändern");
		aendern.setBounds(415, 500, 170, 30);
		ActionListener edit = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Alle geschrieben Tastenänderungen werden realisiert, wenn
				// null wird alte Tastebeibehalten
				int i = 0;
				//int []keyCodes = {27,32,65,68,69,73,78,81,83,87};	

				for (JTextField field : textFieldList) {
					if (field.getText().length() != 0) {
						switch(i){
							case 0:
								tastaturHandler.setKeyForEvent(Integer.parseInt(field.getText()),tastaturHandler.KEY_MENU);
								break;
							case 1:
								tastaturHandler.setKeyForEvent(Integer.parseInt(field.getText()),tastaturHandler.KEY_BEWEGEN_LINKS);
								break;
							case 2:
								tastaturHandler.setKeyForEvent(Integer.parseInt(field.getText()),tastaturHandler.KEY_BEWEGEN_RECHTS);
								break;
							case 3:
								tastaturHandler.setKeyForEvent(Integer.parseInt(field.getText()),tastaturHandler.KEY_BEWEGEN_NORD);
								break;
							case 4:
								tastaturHandler.setKeyForEvent(Integer.parseInt(field.getText()),tastaturHandler.KEY_BEWEGEN_WEST);
								break;
							case 5:
								tastaturHandler.setKeyForEvent(Integer.parseInt(field.getText()),tastaturHandler.KEY_BEWEGEN_SUED);
								break;
							case 6:
								tastaturHandler.setKeyForEvent(Integer.parseInt(field.getText()),tastaturHandler.KEY_BEWEGEN_OST);
								break;
							case 7:
								tastaturHandler.setKeyForEvent(Integer.parseInt(field.getText()),tastaturHandler.KEY_INTERAGIERE);
								break;
							case 8:
								tastaturHandler.setKeyForEvent(Integer.parseInt(field.getText()),tastaturHandler.KEY_ABLEGEN);
								break;
							case 9:
								tastaturHandler.setKeyForEvent(Integer.parseInt(field.getText()),tastaturHandler.KEY_AUFNEHMEN);
								break;
						}
						field.setText("");
					}
					i++;
				}

				fenster.fireRequestFocus();
			}
		};
		aendern.addActionListener(edit);

		JButton zurueck = new JButton("Zurück");
		zurueck.setBounds(10, 600, 200, 40);
		ActionListener zur = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tBelegung.setVisible(false);
				draw();
				fenster.fireRequestFocus();
			}
		};
		zurueck.addActionListener(zur);

		tBelegung.add(take);
		tBelegung.add(drop);
		tBelegung.add(interaction);
		tBelegung.add(goSouth);
		tBelegung.add(goEast);
		tBelegung.add(goWest);
		tBelegung.add(goNorth);
		tBelegung.add(tRight);
		tBelegung.add(tLeft);
		tBelegung.add(esc);
		tBelegung.add(aendern);
		tBelegung.add(zurueck);
		tBelegung.add(header);
		tBelegung.revalidate();

		this.add(tBelegung);
	}
}
