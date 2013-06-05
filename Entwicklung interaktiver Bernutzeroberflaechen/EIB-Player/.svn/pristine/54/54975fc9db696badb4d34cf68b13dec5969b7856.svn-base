package de.hsrm.entintben.mp3_player.player;

import java.awt.Image;
import java.awt.Toolkit;

/**
 *
 * @author Tobias Lehwalder, Dominik Schuhmann, Tino Landmann
 */
public class Track {

    private String artist;
    private String title;
    private int length;
    private long id;
    private String albumTitle;
    private String soundfile;
    private Image albumCover;

    public Track(String artist, String title, int length, long id, String albumname, String soundfile, String coverpath) {
        this.artist = artist;
        this.title = title;
        this.length = length;
        this.id = id;
        this.albumTitle = albumname;
        this.soundfile = soundfile;

        this.albumCover = Toolkit.getDefaultToolkit().getImage(coverpath);
        this.albumCover = this.albumCover.getScaledInstance(150, 150, 150);
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
     * 
     * @return 
     */
    @Override
    public String toString() {
        String trackname = artist + " - " + title;

        return trackname;
    }
}