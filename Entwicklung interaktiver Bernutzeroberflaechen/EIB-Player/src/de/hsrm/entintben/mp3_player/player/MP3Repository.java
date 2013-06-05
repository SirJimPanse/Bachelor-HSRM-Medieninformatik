package de.hsrm.entintben.mp3_player.player;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.*;
import java.io.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.AudioFileIO;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.*;

/**
 *
 * @author Tobias Lehwalder, Dominik Schuhmann, Tino Landmann
 */
public class MP3Repository {

    private static long mp3Count = 100;
    private ArrayList<Playlist> playlistCollection;
    private long playlistCount = 1000000;
    private ArrayList<Track> mp3Collection;

    public MP3Repository() throws JavaLayerException, FileNotFoundException, IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
        this.mp3Collection = new ArrayList<Track>();
        this.playlistCollection = new ArrayList<Playlist>();
    }

    /**
     * 
     * @return 
     */
    public ArrayList<Playlist> findAll() {
        ArrayList<Playlist> list = new ArrayList<Playlist>();
        return list;
    }

    /**
     * Suche nach einem Track, anhand der ID.
     * @param id
     * @return 
     */
    public Playlist findById(long id) {
        Playlist list = null;

        for (Playlist aktPlaylist : getPlaylists()) {

            if (aktPlaylist.getId() == id) {
                list = aktPlaylist;
            }

        }
        return list;
    }

    /**
     * Suche nach einem Titel, anhand des Titel.
     * @param name
     * @return 
     */
    public List<Playlist> findByTitle(String name) {
        ArrayList<Playlist> list = new ArrayList<Playlist>();
        return list;

    }

    /*
     * erstellt den Track und speichert dort direkt die ID3-Tags
     * 
     */
    public static Track createMP3File(String mp3Path) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
        Track newSong = null;

        File musicfile = new File(mp3Path);
        AudioFile file = AudioFileIO.read(musicfile);
        Tag tag = file.getTag();

        int trackLength = file.getAudioHeader().getTrackLength();

        String artist = tag.getFirst(FieldKey.ARTIST);
        String album = tag.getFirst(FieldKey.ALBUM);
        String titel = tag.getFirst(FieldKey.TITLE);
        String year = tag.getFirst(FieldKey.YEAR);
        String trackN = tag.getFirst(FieldKey.TRACK);
        newSong = new Track(artist, titel, trackLength, mp3Count, album, mp3Path, musicfile.getParent() + "/folder.jpg");

        return newSong;
    }

    /**
     * @return the playlists
     */
    public ArrayList<Playlist> getPlaylists() {
        return getPlaylistCollection();
    }

    /**
     * Fügt eine Playlist der PlaylistCollection hinzu.
     * @param listName
     * @return 
     */
    public Playlist addPlaylist(String listName) {
        Date date = new Date();
        SimpleDateFormat myDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
        Playlist list = new Playlist(date, getPlaylistCount(), listName);
        getPlaylistCollection().add(list);
        playlistCount += 1;

        return list;
    }

    /**
     * Löscht eine Playlist aus der PlaylistCollection.
     * @param number 
     */
    public void removePlaylist(int number) {
        for (Track aktTrack : getPlaylistCollection().get(number).getPlaylist()) {
            aktTrack.setSoundfile(null);
            aktTrack = null;
        }
        getPlaylistCollection().remove(number);

    }

    /**
     * @return the playlistCount
     */
    public long getPlaylistCount() {
        return playlistCount;
    }

    /**
     * Erstellt eine M3U Datei aus der Playlist und speichert diese so.
     */
    public void createM3U(Playlist list, String dirPath) throws IOException {

        String m3u;
        list.getPlaylist();

        FileWriter fw = new FileWriter(dirPath + "/" + list.getTitle() + ".m3u");
        BufferedWriter out = new BufferedWriter(fw);

        out.write("#EXTM3U\n");

        for (Track aktTrack : list.getPlaylist()) {
            out.write(aktTrack.getSoundfile() + "\n");

        }

        out.close();
    }

    /**
     * Läd eine Playlist anhand einer M3U Datei.
     * @param m3u
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public Playlist loadM3U(File m3u) throws FileNotFoundException, IOException {


        BufferedReader reader = new BufferedReader(new FileReader(m3u));
        String line = null;


        String listName = null;

        int index = m3u.getName().lastIndexOf('.');
        if (index > 0 && index <= m3u.getName().length() - 2) {
            listName = m3u.getName().substring(0, index);
        }

        Playlist list = addPlaylist(listName);

        reader.readLine();

        while ((line = reader.readLine()) != null) {
            if (!"#EXTM3U\n".equals(line)) {
                try {
                    list.addTrack(createMP3File(line));
                } catch (CannotReadException ex) {
                    Logger.getLogger(MP3Repository.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TagException ex) {
                    Logger.getLogger(MP3Repository.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ReadOnlyFileException ex) {
                    Logger.getLogger(MP3Repository.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidAudioFrameException ex) {
                    Logger.getLogger(MP3Repository.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        return list;
    }

    /**
     * @return the playlistCollection
     */
    public ArrayList<Playlist> getPlaylistCollection() {
        return playlistCollection;
    }

    /**
     * @param playlistCollection the playlistCollection to set
     */
    public void setPlaylistCollection(ArrayList<Playlist> playlistCollection) {
        this.playlistCollection = playlistCollection;
    }
}
