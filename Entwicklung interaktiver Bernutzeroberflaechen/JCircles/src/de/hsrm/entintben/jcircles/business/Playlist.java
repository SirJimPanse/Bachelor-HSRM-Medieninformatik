package de.hsrm.entintben.jcircles.business;

import java.util.*;

/**
 *
 * @author Tino Landmann, Tobias Lehwalder, Dominik Schuhmann
 */
public class Playlist {

    private Date creationDate;
    private long id;
    private static long idCounter = 10000;
    private String title;
    private ArrayList<Track> playlist = new ArrayList<Track>();

    public Playlist() {
        this.title = "Default";
        this.id = idCounter;
        idCounter++;
    }

    public Playlist(String title) {

        this.id = idCounter;
        idCounter++;
        this.title = title;
    }

    public Playlist(Date creationDate, String title) {
        this.creationDate = creationDate;
        this.id = idCounter;
        idCounter++;
        this.title = title;
    }

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

    public void addTrack(Track track) {
        if (this.playlist.isEmpty()) {
            playlist = new ArrayList<Track>();
            playlist.add(track);
        } else {
            playlist.add(track);
        }
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
