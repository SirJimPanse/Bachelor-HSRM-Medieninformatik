package CamptureTheFlag.darstellung;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

import CamptureTheFlag.darstellung.TastaturHandler.Events;
import CamptureTheFlag.logik.GameC;

/**
 * Fenster erhält den KeyListener und benachrichtigt TastaturHander An
 * TastaturHander können sich alle anmelden (mit event-typ/name)
 * 
 * @author shard001
 * 
 */
@SuppressWarnings("serial")
public class Fenster extends JFrame implements PropertyChangeListener, KeyListener {

	private static Logger log = Logger.getLogger(Fenster.class.getName());


	private JPanel hauptmenu;
	private JPanel optionsmenu;
	private JPanel pausemenu;
	private JPanel lademenu;
	private JPanel sichtBereich;
	private JPanel spielerV;
	public GameC gameC;

	private TastaturHandler tastaturHandler;

	public Fenster(GameC c, TastaturHandler t) {
		this.gameC = c;
		this.gameC.addPropertyChangeListener(Events.ZUSTAND.toString(), this);
		this.tastaturHandler = t;
		
		this.setHauptmenu(new Hauptmenu(this.gameC, this)); // erstellt direkt selbst das Hauptmenu
		this.setOptionsmenu(new Optionsmenu(this, this.tastaturHandler));
		this.setLademenu(new Lademenu(this, this.gameC));
		
		this.init();
	}

	/**
	 * Stellt das Fenster dar, setzt Titel etc.
	 * Erstellt KeyListener und setzt Fokus auf sich selbst
	 */
	private void init() {
		setLayout(null);
		setTitle("CamptureTheFlagTest");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setSize(1024, 700);
		setLocation(150, 10);
		setResizable(false);
		setVisible(true);
		
		// Hinzufügen des Listeners
		addKeyListener(this);
		requestFocus();
	}
	
	/**
	 * wird von allen anderen Klassen aufgerufen, wenn Buttons o.Ä. gedrückt werden
	 * um den Fokus danach wieder auf Fenster zu legen
	 */
	public void fireRequestFocus() {
		requestFocus();
	}

	/*
	 * legt alle anderen Panels auf sich selbst ab
	 * setzt abmessungen und plazierung
	 * holt sich den fokus für keylistener
	 * so können alle anderen JPanals angezeit / versteckt werden
	 */
	public void setHauptmenu(Hauptmenu hauptmenu) {
		this.hauptmenu = hauptmenu;
		this.add(this.hauptmenu);
		this.hauptmenu.setBounds(0, 0, 1024, 700);

		
		this.fireRequestFocus();
	}
	
	public void setOptionsmenu(Optionsmenu optionsmenu) {
		this.optionsmenu = optionsmenu;
		this.add(this.optionsmenu);
		this.optionsmenu.setBounds(0, 0, 1024, 700);
		this.optionsmenu.setVisible(false);
		
		this.fireRequestFocus();
	}

	public void setPausemenu(Pausemenu pausemenu) {
		this.pausemenu = pausemenu;
		this.add(this.pausemenu);
		this.pausemenu.setBounds(0, 0, 1024, 700);
		this.pausemenu.setVisible(false);

		this.fireRequestFocus();
	}

	public void setSichtBereich(Sichtbereich sichtBereich) {
		this.sichtBereich = sichtBereich;
		this.add(this.sichtBereich);
		this.sichtBereich.setBounds(0, 0, 1024, 500);
		this.sichtBereich.setVisible(false);
		
		this.fireRequestFocus();
	}
	
	public void setSpielerV(SpielerV spielerV) {
		this.spielerV = spielerV;
		this.add(this.spielerV);
		this.spielerV.setBounds(0, 500, 1024, 190);
		this.spielerV.setVisible(false);
		
		this.fireRequestFocus();
	}
	
	public void setLademenu(Lademenu lademenu) {
		this.lademenu = lademenu;
		this.add(this.lademenu);
		this.lademenu.setBounds(0, 0, 1024, 700);
		this.lademenu.setVisible(false);
		
		
		this.fireRequestFocus();
	}
	
	public JPanel getOptionsmenu() {
		return optionsmenu;
	}
	public JPanel getLademenu() {
		return lademenu;
	}

	// durch Anmeldung nur für Events.ZUSTAND
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		log.log(Level.INFO, "got event '" + arg0.getPropertyName() + "' old: '" + arg0.getOldValue() + "' new: '" + arg0.getNewValue() + "'");

		/*
		 * bei Statusänderung im GameC werden die passenden JPanels versteckt/angezeigt
		 * alles Zeichnen geschieht in den Bereichen (Klassen) selbst
		 * Fenster kümmert sich nur darum, ob es sichtbar ist
		 */
		switch ((GameC.Zustand) arg0.getNewValue()) {
			case HAUPTMENU:
				this.hauptmenu.setVisible(true);
				this.optionsmenu.setVisible(false);
				this.lademenu.setVisible(false);
				
				if ( this.gameC.isGameRunning()) {
					this.pausemenu.setVisible(false);
					this.sichtBereich.setVisible(false);
					this.spielerV.setVisible(false);

				}
				break;
			case PAUSEMENU:
				this.hauptmenu.setVisible(false);
				this.optionsmenu.setVisible(false);
				this.lademenu.setVisible(false);
				
				if ( this.gameC.isGameRunning()) {
					this.pausemenu.setVisible(true);
					this.sichtBereich.setVisible(false);
					this.spielerV.setVisible(false);

				}
				break;
			case OPTIONSMENU:
					this.hauptmenu.setVisible(false);
					this.optionsmenu.setVisible(true);
					this.lademenu.setVisible(false);
					
					if ( this.gameC.isGameRunning()) {
						this.pausemenu.setVisible(false);
						this.sichtBereich.setVisible(false);
						this.spielerV.setVisible(false);
					}
				break;
			case INGAME:
				this.hauptmenu.setVisible(false);
				this.optionsmenu.setVisible(false);
				this.lademenu.setVisible(false);
				
				if ( this.gameC.isGameRunning()) {
					this.pausemenu.setVisible(false);
					this.sichtBereich.setVisible(true);
					this.spielerV.setVisible(true);

				}
				break;
			case LADEMENU:
				this.hauptmenu.setVisible(false);
				this.optionsmenu.setVisible(false);
				this.lademenu.update(this.lademenu.getGraphics());
				this.lademenu.setVisible(true);
				
				if ( this.gameC.isGameRunning()) {
					this.pausemenu.setVisible(false);
					this.sichtBereich.setVisible(false);
					this.spielerV.setVisible(false);		
				}
				break;
		}

		this.validate(); // alles neu zeichnen

	}

	/**
	 * KeyListener benachrichtigt TastaturHandler mit KeyCode dieser feuert
	 * passendes Event
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {
		log.log(Level.INFO, "Taste gedrückt: Key = " + arg0.getKeyChar() + " Code = " + arg0.getKeyCode());
		tastaturHandler.getEventFromKeyCode(arg0.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

}