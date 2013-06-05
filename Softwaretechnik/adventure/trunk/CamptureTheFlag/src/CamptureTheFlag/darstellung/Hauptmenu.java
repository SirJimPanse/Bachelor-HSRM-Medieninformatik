package CamptureTheFlag.darstellung;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import CamptureTheFlag.logik.GameC;
import CamptureTheFlag.logik.GameC.Zustand;

/**
 * Zeigt das Hauptmenu mit den passenden Buttons an
 * 
 * @author shard001, Toni
 */
public class Hauptmenu extends JPanel {

	private static final long serialVersionUID = 7637009978624738722L;
	private GameC gameC;
	private Fenster fenster;
	
	public Hauptmenu(GameC c, Fenster f) {
		this.gameC = c;
		this.fenster = f;
		
		setLayout(null);
		this.draw();
	}

	/**
	 * zeichnet alle Elemente des Hauptmenus
	 * und legt sie auf sich selbst ab
	 */
	public void draw() {
		final JPanel main = new JPanel();
		main.setLayout(null);
		main.setBounds(0, 0, 1024, 700);
		
		JLabel label = new JLabel(DarstellungHelper.getImage("Fatbeard.jpg"));
		label.setBounds(0, 0, 1024, 700);
		
		JLabel header = new JLabel();
		header.setForeground(Color.orange);
		header.setOpaque(false);
		header.setFont(new Font("Serif", Font.BOLD, 50));
		header.setBounds(350, 10, 300, 100);
		header.setText("Hauptmenü");
		
		JButton neuesSpiel = new JButton("Neues Spiel");
		neuesSpiel.setBounds(10, 450, 200, 40);
		ActionListener ln = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameC.neuesSpiel();
				fenster.fireRequestFocus();
			}
		};
		neuesSpiel.addActionListener(ln);

		JButton laden = new JButton("Spiel Laden");
		laden.setBounds(10, 500, 200, 40);
		ActionListener ld = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((Lademenu) fenster.getLademenu()).setFrom(Zustand.HAUPTMENU); // setzte from-zustand
				fenster.gameC.setZustand(Zustand.LADEMENU);
				revalidate();
				fenster.fireRequestFocus();
			}
		};
		laden.addActionListener(ld);

		JButton option = new JButton("Optionen");
		option.setBounds(10, 550, 200, 40);
		ActionListener opt = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((Optionsmenu) fenster.getOptionsmenu()).setFrom(Zustand.HAUPTMENU); // setzte from-zustand
				fenster.gameC.setZustand(Zustand.OPTIONSMENU);
				fenster.fireRequestFocus();
			}
		};
		option.addActionListener(opt);

		JButton spielBeenden = new JButton("Spiel beenden");
		spielBeenden.setBounds(10, 600, 200, 40);
		ActionListener end = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Fenster schließen
				fenster.dispose();
				fenster.fireRequestFocus();
			}
		};
		spielBeenden.addActionListener(end);
		
		main.add(option);
		main.add(neuesSpiel);
		main.add(laden);
		main.add(spielBeenden);
		main.add(header);
		main.add(label);

		this.add(main);
	}
	
}
