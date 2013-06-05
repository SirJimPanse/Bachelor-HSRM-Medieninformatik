package CamptureTheFlag.darstellung;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import CamptureTheFlag.darstellung.TastaturHandler.Events;
import CamptureTheFlag.logik.entities.Eingang;
import CamptureTheFlag.logik.entities.Feld;
import CamptureTheFlag.logik.entities.GameObject;
import CamptureTheFlag.logik.entities.NPC;
import CamptureTheFlag.logik.models.Richtung;

@SuppressWarnings("unused")
public class FeldV extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(FeldV.class.getName());

	// klassen variablen (für alle objekte gleich)
	private static int breite;
	private static int hoehe;
	private String imgWarp = "src/CamptureTheFlag/res/stargate.png";
	private String imgWand = "src/CamptureTheFlag/res/texture_wand_1.jpg";
	private String imgBoden = "src/CamptureTheFlag/res/texture_floor_1.jpg";

	// klasse, NICHT objekt
	public static void setBreite(int b) {
		breite = b;
	}

	// klasse, NICHT objekt
	public static void setHoehe(int h) {
		hoehe = h;
	}

	// ab jetzt wieder für objekte
	private Feld feld;
	private BufferedImage warpImage;
	private ImageIcon warpIcon;
	private BufferedImage bodenImage;
	private BufferedImage wandImage;
	private TexturePaint bodenTextur;
	private TexturePaint wandTextur;
	private Rectangle2D wandTexturRectangle;
	private Rectangle2D bodenTexturRectangle;
	private float scale;
	private Polygon boden;
	private Polygon wandW;
	private Polygon wandN;
	private Polygon wandO;
	private Polygon wandS;
	private Color color;
	private JPanel slots[];

	public FeldV(Polygon boden, Polygon wandN, Polygon wandS, Feld feld, Position position) {
		this(boden, wandN, wandS, null, null, feld, position);
	}
	
	public FeldV(Polygon boden, Polygon wandN, Polygon wandS, Polygon wandW, Polygon wandO, Feld feld, Position position) {
		// texturen für alle objekte -> klassen-variable!
		try {
			warpImage = ImageIO.read(new File(imgWarp));
			wandImage = ImageIO.read(new File(imgWand));
			bodenImage = ImageIO.read(new File(imgBoden));
		} catch (IOException ex) {
			log.log(Level.WARNING, "Textur nicht gefunden");
		}
		
		setSize(breite, hoehe);
		// wegen rechnung für NPC in Sichtbereich
//		this.setLayout(null);
		
		
		// jetzt wieder objekt-spezifisches
		
		switch(position){
			case VORN:
				this.color = new Color(30,200,30);
				this.scale = 0.4f;
			break;
			case MITTEL:
				this.color = new Color(30,180,30);
				this.scale = 0.3f;
			break;
			case HINTEN:
				this.color = new Color(30,190,30);
				this.scale = 0.2f;
			break;
		}
		
		this.feld = feld;
		this.boden = boden;
		
		this.wandW = wandW;
		this.wandN = wandN;
		this.wandO = wandO;
		this.wandS = wandS;
		
		this.wandTexturRectangle = new Rectangle2D.Float(0, 0, this.scale*400, this.scale*400);
		this.wandTextur = new TexturePaint(wandImage, this.wandTexturRectangle);

		this.bodenTexturRectangle = new Rectangle2D.Float(0, 0, this.scale*300, this.scale*300);
		this.bodenTextur = new TexturePaint(bodenImage, this.bodenTexturRectangle);
		
		// an Feld anmelden
		this.feld.addPropertyChangeListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		Graphics2D g2D = (Graphics2D) g;

		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		

		
		if (this.feld != null) {
			
			if (!this.feld.isNachbarBegehbar(Richtung.SUED) && this.wandS != null) {

				g2D.setPaint(this.color);
				g2D.fillPolygon(this.wandS);
				g2D.setPaint(Color.BLACK);
				g2D.drawPolygon(this.wandS);
			}
			
			if (!this.feld.isNachbarBegehbar(Richtung.OST) && this.wandO != null) {
				g2D.setPaint(this.color);
				g2D.fillPolygon(this.wandO);
				g2D.setPaint(Color.BLACK);
				g2D.drawPolygon(this.wandO);

			}
			if (!this.feld.isNachbarBegehbar(Richtung.WEST) && this.wandW != null) {
				g2D.setPaint(this.color);
				g2D.fillPolygon(this.wandW);
				g2D.setPaint(Color.BLACK);
				g2D.drawPolygon(this.wandW);
			}
			
			if (!this.feld.isNachbarBegehbar(Richtung.NORD) && this.wandN != null) {
//				for(GameObject obj : this.feld.getObjects()){
//					if(obj instanceof Eingang){
//						g2D.setPaint(this.color = new Color(30,200,30,200));
//					}
				g2D.setPaint(this.color);
				g2D.fillPolygon(this.wandN);
				g2D.setPaint(Color.BLACK);
				g2D.drawPolygon(this.wandN);
			}
			
			g2D.setPaint(Color.BLACK);
			g2D.fillPolygon(this.boden);
			g2D.setPaint(Color.GREEN);
			g2D.drawPolygon(this.boden);
		}
	}
		
//		try{
//			for(GameObject obj : this.feld.getObjects()){
//				if(obj instanceof Eingang){
//					if(((Eingang) obj).getDahinter() == this.feld.getNachbar(Richtung.NORD) && this.wandN != null){
//					g2D.drawImage(this.warpImage, null, this.wandN.xpoints[3]+(this.wandN.xpoints[2]-this.wandN.xpoints[3])/2-warpImage.getWidth()/2, this.wandN.ypoints[3]+(this.wandN.ypoints[0]-this.wandN.ypoints[3])/2-warpImage.getHeight()/2);
//					}	
//					else if(((Eingang) obj).getDahinter() == this.feld.getNachbar(Richtung.SUED) && this.wandS != null){
//						g2D.drawImage(this.warpImage, null, this.wandS.xpoints[3]+(this.wandS.xpoints[2]-this.wandS.xpoints[3])/2-warpImage.getWidth()/2, this.wandS.ypoints[3]+(this.wandS.ypoints[0]-this.wandS.ypoints[3])/2-warpImage.getHeight()/2);
//					}
//					else if(((Eingang) obj).getDahinter() == this.feld.getNachbar(Richtung.WEST) && this.wandW != null){
//						g2D.drawImage(this.warpImage, null, this.wandN.xpoints[3]+(this.wandW.xpoints[2]-this.wandW.xpoints[3])/2-warpImage.getWidth()/2, this.wandW.ypoints[3]+(this.wandW.ypoints[0]-this.wandW.ypoints[3])/2-warpImage.getHeight()/2);
//					}
//					else if(((Eingang) obj).getDahinter() == this.feld.getNachbar(Richtung.OST) && this.wandO != null){
//						g2D.drawImage(this.warpImage, null, this.wandO.xpoints[3]+(this.wandO.xpoints[2]-this.wandO.xpoints[3])/2-warpImage.getWidth()/2, this.wandO.ypoints[3]+(this.wandO.ypoints[0]-this.wandO.ypoints[3])/2-warpImage.getHeight()/2);
//					}
//				}
//			
//			}
//			
//		}
//		catch(Exception e){}

	
	public Feld getFeld() {
		return feld;
	}

	public void setFeld(Feld feld) {
		this.feld = feld;
	}
	
	public Polygon getWandW() {
		return wandW;
	}

	public void setWandW(Polygon wandW) {
		this.wandW = wandW;
	}

	public Polygon getWandN() {
		return wandN;
	}

	public void setWandN(Polygon wandN) {
		this.wandN = wandN;
	}

	public Polygon getWandO() {
		return wandO;
	}

	public void setWandO(Polygon wandO) {
		this.wandO = wandO;
	}
	
	public Polygon getWandS() {
		return wandS;
	}

	public void setWandS(Polygon wandS) {
		this.wandS = wandS;
	}
	
	public Polygon getBoden() {
		return boden;
	}

	public void setBoden(Polygon boden) {
		this.boden = boden;
	}

	// hört nur auf ABLEGEN
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		log.log(Level.INFO, "got event '" + arg0.getPropertyName() + "' old: '"	+ arg0.getOldValue() + "' new: '" + arg0.getNewValue() + "'");
		//TODO GameObjects auf Feld neu zeichnen -> this.feld.getObjects()
	}

}