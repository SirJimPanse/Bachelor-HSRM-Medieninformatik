package de.hsrm.entintben.mp3_player.player;

import java.util.*;

/**
 *
 * @author Tobias Lehwalder, Dominik Schuhmann, Tino Landmann
 */
public class Playlist {

    private Date creationDate;
    private long id;
    private String title;
    private ArrayList<Track> playlist = new ArrayList<Track>();

    /**
     * Erstellt eine Playlist
     * @param creationDate
     * @param id
     * @param title 
     */
    public Playlist(Date creationDate, long id, String title) {
        this.creationDate = creationDate;
        this.id = id;
        this.title = title;
    }

    /**
     * 
     * @return 
     */
    public int numberOfTracks() {
        int trackCount;
        trackCount = getPlaylist().size();

        return trackCount;
    }

    public Track getTrack(int no) {
        Track track;
        track = getPlaylist().get(no);

        return track;
    }

    /**
     * Fügt Track hinzu.
     * @param track 
     */
    public void addTrack(Track track) {
        this.getPlaylist().add(track);
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the playlist
     */
    public ArrayList<Track> getPlaylist() {
        return playlist;
    }

    /**
     * @param playlist the playlist to set
     */
    public void setPlaylist(ArrayList<Track> playlist) {
        this.playlist = playlist;
    }

    @Override
    public String toString() {
        return title;
    }
}
