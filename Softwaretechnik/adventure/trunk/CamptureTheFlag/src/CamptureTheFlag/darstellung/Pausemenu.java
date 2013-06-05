package CamptureTheFlag.darstellung;

import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import CamptureTheFlag.logik.GameC;
import CamptureTheFlag.logik.GameC.Zustand;
import CamptureTheFlag.logik.inits.GameRepository;

/**
 * Zeigt das Pausemenu mit den passenden Buttons an
 * 
 * @author shard001, Toni
 */
public class Pausemenu extends JPanel {

	private static final long serialVersionUID = -205580832531040212L;
	private Fenster fenster;
	private GameC gameC;
	private static Logger log = Logger.getLogger(GameC.class.getName());
	
	public Pausemenu(GameC c, Fenster f) {
		this.fenster = f;
		this.gameC = c;
		
		setLayout(null);
		this.draw();
	}
	
	/**
	 * zeichnet alle Elemente des Pausemenus
	 * und legt sie auf sich selbst ab
	 */
	public void draw() {
		final JPanel pause = new JPanel();
		pause.setLayout(null);
		pause.setBounds(0, 0, 1024, 700);
		
		JLabel label = new JLabel(DarstellungHelper.getImage("Fatbeard.jpg"));
		label.setBounds(0, 0, 1024, 700);
		
		JLabel header = new JLabel("Pause");
		header.setForeground(Color.BLACK);
		header.setFont(new Font("Serif", Font.BOLD, 50));
		header.setBounds(400, 10, 300, 100);
		header.setOpaque(false);

		JButton spielFortsetzen = new JButton("Spiel fortsetzen");
		spielFortsetzen.setBounds(10, 400, 200, 40);
		ActionListener cont = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fenster.gameC.setZustand(Zustand.INGAME);
				fenster.fireRequestFocus();
			}
		};
		spielFortsetzen.addActionListener(cont);

		JButton spielSpeichern = new JButton("Spiel speichern");
		spielSpeichern.setBounds(10, 450, 200, 40);
		ActionListener sav = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pause.setVisible(false);
				drawSave();
				fenster.fireRequestFocus();
			}
		};
		spielSpeichern.addActionListener(sav);

		JButton laden = new JButton("Spiel Laden");
		laden.setBounds(10, 500, 200, 40);
		ActionListener ld = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((Lademenu) fenster.getLademenu()).setFrom(Zustand.PAUSEMENU); // setzte from-zustand
				fenster.gameC.setZustand(Zustand.LADEMENU);
				fenster.fireRequestFocus();
			}
		};
		laden.addActionListener(ld);

		JButton option = new JButton("Optionen");
		option.setBounds(10, 550, 200, 40);
		ActionListener opt = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((Optionsmenu) fenster.getOptionsmenu()).setFrom(Zustand.PAUSEMENU); // setzte from-zustand
				fenster.gameC.setZustand(Zustand.OPTIONSMENU);
				fenster.fireRequestFocus();
			}
		};
		option.addActionListener(opt);

		JButton spielAbbrechen = new JButton("Spiel Abbrechen");
		spielAbbrechen.setBounds(10, 600, 200, 40);
		ActionListener abb = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pause.setVisible(false);
				fenster.gameC.setZustand(Zustand.HAUPTMENU);
				fenster.fireRequestFocus();
			}
		};
		spielAbbrechen.addActionListener(abb);

		pause.add(spielFortsetzen);
		pause.add(spielSpeichern);
		pause.add(laden);
		pause.add(spielAbbrechen);
		pause.add(option);
		pause.add(header);
		pause.add(label);
		pause.revalidate();
		pause.setVisible(true);

		this.add(pause);
	}

	/**
	 * Zeichnet das Speichern-Menu
	 */
	public void drawSave() {
		final JPanel save = new JPanel();
		save.setLayout(null);
		save.setBounds(0, 0, 1024, 700);
		final ScrollPane scroll = new ScrollPane();

		Label header = new Label("Spiel speichern");
		header.setForeground(Color.BLACK);
		header.setFont(new Font("Serif", Font.BOLD, 50));
		header.setBounds(305, 10, 400, 80);
		
		
		final JTable list = new JTable();
		final AbstractTableModel listModel = new FileTableModel("./src/CamptureTheFlag/res");
		list.setModel(listModel);
		scroll.add(list);

		JButton speichern = new JButton("Spielstand speichern");
		speichern.setBounds(525, 560, 170, 25);
		speichern.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { 
			gameC.spielSpeichern();
			save.setVisible(false);
			save.revalidate();
			drawSave();
			}
		});

		JButton zurueck = new JButton("Zurück");
		zurueck.setBounds(10, 625, 200, 25);
		ActionListener zur = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save.setVisible(false);
				draw();
				fenster.fireRequestFocus();
			}
		};
		zurueck.addActionListener(zur);
		
		JButton loeschen = new JButton("Spielstand löschen");
		loeschen.setBounds(255, 555, 170, 25);
		ActionListener del = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dateiName = (String)listModel.getValueAt(list.getSelectedRow(),1);
				File file = new File(GameRepository.saveGameFolder + dateiName);
				if ( ! file.delete() )
					  log.log(Level.WARNING,file + " could not be deleted!");
				
				save.setVisible(false);
				save.revalidate();
				drawSave();
						
			}
		};
		loeschen.addActionListener(del);
		scroll.setBounds(260, 100, 450, 450);
		
		save.add(header);
		save.add(zurueck);
		save.add(speichern);
		save.add(scroll);
		save.add(loeschen);
		save.revalidate();
		save.setVisible(true);

		this.add(save);
	}
	
}
