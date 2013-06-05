package de.hsrm.entintben.jcircles.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import de.hsrm.entintben.jcircles.business.Playlist;

/**
 *
 * @author Tobias Lehwalder, Tino Landmann, Dominik Schuhmann
 * 
 * Ein Panel auf dem die Verbindungslinien zwischen den Elementen einer Playlist gezeichnet werden.
 */
public class PlaylistGUI extends JPanel {

    private Playlist pl;
    private final int MAXDURCHMESSER = 66;

    public PlaylistGUI() {
    }

    public PlaylistGUI(Playlist pl) {
        this.pl = pl;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.setOpaque(false);
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (pl != null) {
            if (pl.getPlaylist().size() > 1 && !pl.getPlaylist().isEmpty()) {
                for (int i = 0; i < pl.getPlaylist().size() - 1; i++) {
                    g2d.setColor(Color.yellow);
                    g2d.drawLine(getPl().getPlaylist().get(i).getX() + MAXDURCHMESSER / 2, getPl().getPlaylist().get(i).getY() + MAXDURCHMESSER / 2, getPl().getPlaylist().get(i + 1).getX() + MAXDURCHMESSER / 2, getPl().getPlaylist().get(i + 1).getY() + MAXDURCHMESSER / 2);
                }

            }
        }
    }

    /**
     * @return the pl
     */
    public Playlist getPl() {
        return pl;
    }

    /**
     * @param pl the pl to set
     */
    public void setPl(Playlist pl) {
        this.pl = pl;
    }
}
