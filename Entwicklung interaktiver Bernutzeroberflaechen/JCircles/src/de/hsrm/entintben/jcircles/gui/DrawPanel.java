package de.hsrm.entintben.jcircles.gui;

import de.hsrm.entintben.jcircles.gui.TrackGUI;
import java.awt.Graphics;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;

/**
 *
 * @author Tobias Lehwalder, Tino Landmann, Dominik Schuhmann
 * 
 * Auf diesem Panel werden die Kreise und der Hintergrund gezeichnet.
 * Zusätzlich ist sie es, die den ausführbaren Thread für die Bewegung bereitstellt.
 */
public class DrawPanel extends JLayeredPane implements Runnable {

    private CopyOnWriteArrayList<TrackGUI> trackCircles;
    private int height;
    private int width;
    private int delay;
    private Thread t;
    private ImageIcon im;

    public DrawPanel(int w, int h, CopyOnWriteArrayList<TrackGUI> trackCircles, int delay) {
        this.width = w;
        this.height = h;
        this.delay = delay;
        this.trackCircles = trackCircles;
        t = new Thread(this);
        t.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        im = new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/background/player_bg.gif"));

        g.drawImage(im.getImage(), 0, 0, this);
    }

    @Override
    public void run() {

        while (true) {
            if (!trackCircles.isEmpty()) {
                for (TrackGUI aktElement : trackCircles) {

                    aktElement.calculatePos();
                    aktElement.setLocation(aktElement.getT().getX(), aktElement.getT().getY());


                }
                try {
                    t.sleep(delay);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DrawPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
}
