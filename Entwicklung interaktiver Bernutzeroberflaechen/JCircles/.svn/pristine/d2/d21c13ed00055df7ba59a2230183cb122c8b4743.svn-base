package de.hsrm.entintben.jcircles.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import de.hsrm.entintben.jcircles.business.Track;

/**
 *
 * @author Tobias Lehwalder, Tino Landmann, Dominik Schuhmann
 * 
 * Diese Klasse enthält alle Eigenschaften und Funktionen,
 * die zur grafischen Darstellung der einzelnen Tracks von Nöten sind.
 */
public class TrackGUI extends JComponent {

    protected final int MAXDURCHMESSER = 66;
    private final int CIRCLEPANELSIZE = MAXDURCHMESSER + 5;
    private Track t;
    private JPanel circle;
    private JLabel songInfo;
    private int x;
    private int y;
    private int direction_x, direction_y;
    private int durchmesser;
    private int drawPanelWidth;
    private int drawPanelHeight;
    private int red;
    private int green;
    private int blue;
    private int circleX = 0;
    private int circleY = 0;
    private boolean inPlaylist;
    private boolean standStill = false;
    private Shape containsShape;

    public TrackGUI(Track t, int xx, int yy, int d_xx, int d_yy, int r, int g, int b, int h, int w, boolean standStill) {
        this.x = xx;
        this.y = yy;
        this.direction_x = d_xx;
        this.direction_y = d_yy;
        this.drawPanelHeight = h;
        this.drawPanelWidth = w;

        this.setDoubleBuffered(true);
        this.t = t;
        this.inPlaylist = false;
        this.red = r;
        this.blue = b;
        this.green = g;
        this.setOpaque(false);
        this.standStill = standStill;
        this.containsShape = new Ellipse2D.Float(MAXDURCHMESSER / 2 - ((getT().getDurchmesser() + 6) / 2), MAXDURCHMESSER / 2 - ((getT().getDurchmesser() + 6) / 2), (getT().getDurchmesser() + 6), (getT().getDurchmesser() + 6));
        this.circle = new JPanel() {

            @Override
            /**
             * Zeichnet die einzelnen Elemente aus denen jeder Kreis zusammengesetzt ist.
             */
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getT().isIsActTrack()) {
                    g2d.setColor(new Color(255, 255, 255));
                    drawCircle(g2d, MAXDURCHMESSER / 2, MAXDURCHMESSER / 2, (getT().getDurchmesser() + 10) / 2);

                }
                if (new Color(getRed(), getGreen(), getBlue()).equals(new Color(getRed(), getGreen(), getBlue()).brighter())) {

                    g2d.setColor(new Color(getRed(), getGreen(), getBlue()));
                    drawCircle(g2d, MAXDURCHMESSER / 2, MAXDURCHMESSER / 2, (getT().getDurchmesser() + 6) / 2);

                    g2d.setColor(new Color(getRed(), getGreen(), getBlue()).darker());
                    drawCircle(g2d, MAXDURCHMESSER / 2, MAXDURCHMESSER / 2, getT().getDurchmesser() / 2);

                } else {

                    g2d.setColor(new Color(getRed(), getGreen(), getBlue()).brighter());
                    drawCircle(g2d, MAXDURCHMESSER / 2, MAXDURCHMESSER / 2, (getT().getDurchmesser() + 6) / 2);

                    g2d.setColor(new Color(getRed(), getGreen(), getBlue()));
                    drawCircle(g2d, MAXDURCHMESSER / 2, MAXDURCHMESSER / 2, getT().getDurchmesser() / 2);
                }
            }
        };

        circle.setBackground(null);
        circle.setLayout(null);
        circle.setLocation(circleX, circleY);
        circle.setSize(CIRCLEPANELSIZE, CIRCLEPANELSIZE);
        circle.setDoubleBuffered(true);

        this.add(circle);

        this.songInfo = new JLabel();
        songInfo.setLayout(null);
        songInfo.setLocation(0, 0);
        songInfo.setSize(200, 100);
        songInfo.setForeground(Color.white);
        songInfo.setFont(new Font("Arial", Font.CENTER_BASELINE, 9));

        this.add(songInfo);
        this.setSize(150, 150);

    }

    /**
     * Methode zur Berechnung der aktuellen Position jedes Kreises.
     */
    private void drawCircle(Graphics2D g, int x, int y, int radius) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    public void calculatePos() {

        songInfo.setLocation(0, t.getDurchmesser() / 2);


        if (!inPlaylist) {
            if (!isStandStill()) {
                x = x + direction_x;
                y = y + direction_y;
            }
                t.setX(x);
                t.setY(y);
        }
        if (x > drawPanelWidth - CIRCLEPANELSIZE / 2 - t.getDurchmesser() / 2 && direction_x > 0) {
            direction_x *= -1;

        }
        if (y > drawPanelHeight - CIRCLEPANELSIZE / 2 - t.getDurchmesser() / 2 && direction_y > 0) {
            direction_y *= -1;

        }
        if (x < 0 - MAXDURCHMESSER / 2 + t.getDurchmesser() / 2 && direction_x < 0) {
            direction_x *= -1;

        }
        if (y < 0 - MAXDURCHMESSER / 2 + t.getDurchmesser() / 2 && direction_y < 0) {
            direction_y *= -1;

        }

    }

    @Override
    public boolean contains(int x, int y) {
        if (this.containsShape.contains(x, y)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the t
     */
    public Track getT() {
        return t;
    }

    /**
     * @param t the t to set
     */
    public void setT(Track t) {
        this.t = t;
    }

    /**
     * @return the circle
     */
    public JPanel getCircle() {
        return circle;
    }

    /**
     * @param circle the circle to set
     */
    public void setCircle(JPanel circle) {
        this.circle = circle;
    }

    /**
     * @return the songInfo
     */
    public JLabel getSongInfo() {
        return songInfo;
    }

    /**
     * @param songInfo the songInfo to set
     */
    public void setSongInfo(JLabel songInfo) {
        this.songInfo = songInfo;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the standStil
     */
    public boolean isStandStill() {
        return standStill;
    }

    /**
     * @param standStil the standStil to set
     */
    public void setStandStill(boolean standStil) {
        this.standStill = standStil;
    }

    /**
     * @return the inPlaylist
     */
    public boolean isInPlaylist() {
        return inPlaylist;
    }

    /**
     * @param inPlaylist the inPlaylist to set
     */
    public void setInPlaylist(boolean inPlaylist) {
        this.inPlaylist = inPlaylist;
    }

    /**
     * @return the direction_x
     */
    public int getDirection_x() {
        return direction_x;
    }

    /**
     * @param direction_x the direction_x to set
     */
    public void setDirection_x(int direction_x) {
        this.direction_x = direction_x;
    }

    /**
     * @return the direction_y
     */
    public int getDirection_y() {
        return direction_y;
    }

    /**
     * @param direction_y the direction_y to set
     */
    public void setDirection_y(int direction_y) {
        this.direction_y = direction_y;
    }

    /**
     * @return the red
     */
    public int getRed() {
        return red;
    }

    /**
     * @param red the red to set
     */
    public void setRed(int red) {
        this.red = red;
    }

    /**
     * @return the green
     */
    public int getGreen() {
        return green;
    }

    /**
     * @param green the green to set
     */
    public void setGreen(int green) {
        this.green = green;
    }

    /**
     * @return the blue
     */
    public int getBlue() {
        return blue;
    }

    /**
     * @param blue the blue to set
     */
    public void setBlue(int blue) {
        this.blue = blue;
    }

    /**
     * @return the drawPanelWidth
     */
    public int getDrawPanelWidth() {
        return drawPanelWidth;
    }

    /**
     * @param drawPanelWidth the drawPanelWidth to set
     */
    public void setDrawPanelWidth(int drawPanelWidth) {
        this.drawPanelWidth = drawPanelWidth;
    }
}
