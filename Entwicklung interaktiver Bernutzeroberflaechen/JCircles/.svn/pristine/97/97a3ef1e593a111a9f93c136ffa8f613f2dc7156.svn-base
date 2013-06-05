package de.hsrm.entintben.jcircles.business;

import java.awt.Image;
import java.awt.Toolkit;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Tobias Lehwalder, Tino Landmann, Dominik Schuhmann
 */
public class Track {

    private String songInfo;
    private String genre;
    private String albumTitle;
    private String soundfile;
    private String mp3Path;
    private String artist;
    private String title;
    private int playCount = 0;
    private int durchmesser;
    private int length;
    private int x;
    private int y;
    private long id;
    private Image albumCover;
    private boolean isActTrack = false;
    private PropertyChangeSupport propertyChangeSupport;

    public Track(String artist, String title, int length, long id, String albumname, String soundfile, String coverpath, String mp3Path, String genre) {
        this.artist = artist;
        this.title = title;
        this.length = length;
        this.id = id;
        this.albumTitle = albumname;
        this.soundfile = soundfile;
        this.mp3Path = mp3Path;
        this.genre = genre;
        this.durchmesser = 6;
        this.songInfo = artist + " - " + title;
        this.albumCover = Toolkit.getDefaultToolkit().getImage(coverpath);
        this.albumCover = this.albumCover.getScaledInstance(250, 250, 250);
        this.propertyChangeSupport = new PropertyChangeSupport(this);

    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        String trackname = artist + " - " + title;

        return trackname;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.songInfo.equals(((Track) obj).getSongInfo())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the interpret
     */
    public String getInterpret() {
        return artist;
    }

    /**
     * @param interpret the interpret to set
     */
    public void setInterpret(String interpret) {
        this.artist = interpret;
    }

    /**
     * @return the titel
     */
    public String getTitel() {
        return title;
    }

    /**
     * @param titel the titel to set
     */
    public void setTitel(String titel) {
        this.title = titel;

        String alterTitel = this.title;
        this.title = this.title;
        propertyChangeSupport.firePropertyChange("titel", alterTitel, titel);
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the albumtitle
     */
    public String getAlbumtitle() {
        return albumTitle;
    }

    /**
     * @param albumtitle the albumtitle to set
     */
    public void setAlbumtitle(String albumtitle) {
        this.albumTitle = albumtitle;
    }

    /**
     * @return the soundfile
     */
    public String getSoundfile() {

        return soundfile;
    }

    /**
     * @param soundfile the soundfile to set
     */
    public void setSoundfile(String soundfile) {

        this.soundfile = soundfile;
    }

    /**
     * @return the cover
     */
    public Image getCover() {
        return albumCover;
    }

    /**
     * @param cover the cover to set
     */
    public void setCover(Image cover) {
        this.albumCover = cover;
    }

    /**
     * @return the durchmesser
     */
    public int getDurchmesser() {
        return durchmesser;
    }

    /**
     * @param durchmesser the durchmesser to set
     */
    public void setDurchmesser(int d) {
        this.durchmesser = d;
    }

    /**
     * @return the songInfo
     */
    public String getSongInfo() {
        return songInfo;
    }

    /**
     * @param songInfo the songInfo to set
     */
    public void setSongInfo(String songInfo) {
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
     * @return the playCount
     */
    public int getPlayCount() {
        return playCount;
    }

    /**
     * @param playCount the playCount to set
     */
    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    /**
     * @return the mp3Path
     */
    public String getMp3Path() {
        return mp3Path;
    }

    /**
     * @param mp3Path the mp3Path to set
     */
    public void setMp3Path(String mp3Path) {
        this.mp3Path = mp3Path;
    }

    /**
     * @return the isActTrack
     */
    public boolean isIsActTrack() {
        return isActTrack;
    }

    /**
     * @param isActTrack the isActTrack to set
     */
    public void setIsActTrack(boolean isActTrack) {
        this.isActTrack = isActTrack;
    }

    /**
     * @return the genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * @param genre the genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }
}
