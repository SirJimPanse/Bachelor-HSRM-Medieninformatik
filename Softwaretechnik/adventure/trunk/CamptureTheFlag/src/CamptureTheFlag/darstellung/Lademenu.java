package CamptureTheFlag.darstellung;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import CamptureTheFlag.logik.GameC;
import CamptureTheFlag.logik.GameC.Zustand;
import CamptureTheFlag.logik.inits.GameRepository;

public class Lademenu extends JPanel {
	private static final long serialVersionUID = 3172823791776367889L;
	private GameC gameC;
	private Fenster fenster;
	private Zustand from;
	private JTable list;
	private AbstractTableModel listModel;
	
	private static Logger log = Logger.getLogger(GameC.class.getName());
	
	String IMGPATH = "./CamptureTheFlag/res/";
	
	public Lademenu(Fenster f, GameC c) {
		this.fenster = f;
		this.gameC = c;

		this.setLayout(null);
		final JPanel load = new JPanel();
		load.setLayout(null);
		load.setBounds(0, 0, 1024, 700);
		final ScrollPane scroll = new ScrollPane();
		scroll.setBounds(250, 100, 450, 450);
		
		this.list = new JTable();
		this.listModel = new FileTableModel(GameRepository.saveGameFolder);
		list.setModel(listModel);
		scroll.add(list);

		Label header = new Label();
		header.setForeground(Color.BLACK);
		header.setFont(new Font("Serif", Font.BOLD, 50));
		header.setBounds(350, 10, 300, 100);
		header.setText("Spiel laden");
		

		JButton laden = new JButton("Spielstand laden");
		laden.setBounds(525,555, 170, 25);
		laden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dateiName = (String)listModel.getValueAt(list.getSelectedRow(),1);
				gameC.spielLaden(dateiName);
				fenster.fireRequestFocus();
			}
		});

		JButton zurueck = new JButton("Zurück");
		zurueck.setBounds(10, 625, 200, 40);
		ActionListener zur = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fenster.gameC.setZustand(Lademenu.this.from); // geht zu
																// aufrufer-zustand
																	// zurück
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
				update(getGraphics());
			}
		};
		loeschen.addActionListener(del);

		load.add(laden);
		load.add(zurueck);
		load.add(loeschen);
		load.add(scroll);
		load.add(header);
		load.revalidate();
		load.setVisible(true);

		this.add(load);
	}
	
	public void setFrom(Zustand from) {
		this.from = from;
		
	}
	
	@Override
	public void update(Graphics g) {
		super.update(g);
		this.listModel = new FileTableModel(GameRepository.saveGameFolder);
		this.list.setModel(this.listModel);
	}
}
