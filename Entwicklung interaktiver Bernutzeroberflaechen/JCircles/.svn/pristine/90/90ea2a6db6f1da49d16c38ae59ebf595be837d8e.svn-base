package de.hsrm.entintben.jcircles.business;

import de.hsrm.entintben.jcircles.gui.MP3PlayerGUI;
import de.hsrm.entintben.jcircles.gui.TrackGUI;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.*;
import java.io.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.AudioFileIO;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.*;

/**
 *
 * @author Tobias Lehwalder, Tino Landmann, Dominik Schuhmann
 * 
 * Das Repository ist die zentrale Speicherverwaltung des Mp3-Players. 
 * Die Speicherung und der Abruf jeglicher Daten wird durch diese Klasse koordiniert.
 */
public class MP3Repository {

    private ArrayList<Track> mp3Collection;
    private Playlist aktPlaylist;
    private static long mp3Count = 100;
    private ArrayList<Playlist> playlistCollection;
    private long playlistCount = 1000000;
    private static final String SIGN = "#";
    private static final String COLLECTIONFILE = "mp3Collection.txt";
    private static int unknownTrackCounter = 0;

    public MP3Repository() throws JavaLayerException, FileNotFoundException, IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
        this.mp3Collection = new ArrayList<Track>();
        this.playlistCollection = new ArrayList<Playlist>();
        this.aktPlaylist = new Playlist();
    }

    /**
     * Alle Playlists werden abgerufen und in einer Liste gespeichert.
     * 
     * @return Liste aller Playlists.
     */
    public ArrayList<Playlist> findAll() {
        ArrayList<Playlist> b = new ArrayList<Playlist>();


        return b;
    }

    /**
     * Anhand ihrer ID wird die gewüschte Playlist im Speicher gesucht.
     * 
     * @param id Zur Identifikation der gewünschten Playlist.
     * @return Die angeforderte Playlist.
     */
    public Playlist findById(long id) {
        Playlist ret = null;

        for (Playlist aktEle : getPlaylists()) {

            if (aktEle.getId() == id) {
                ret = aktEle;
            }

        }
        return ret;
    }

    /**
     * Anhand des Namens wird die gewüschte Playlist im Speicher gesucht.
     * 
     * @param name Zur Identifikation der gewünschten Playlist.
     * @return Die angeforderte Playlist.
     */
    public List<Playlist> findByTitle(String name) {

        ArrayList<Playlist> ret = new ArrayList<Playlist>();


        return ret;

    }

    /**
     * Erstellt aus dem Dateipfad und den dort, in Form von ID3-Tags, 
     * vorhandenen Informationen ein neues Track Objekt.
     * 
     * @param mp3Path Der Dateipfad in dem nach der zugehörigen Sound Datei gesucht wird.
     * @return Das aus den vorhandenen Informationen zusammengesetzte Track Objekt.
     * @throws CannotReadException
     * @throws IOException
     * @throws TagException
     * @throws ReadOnlyFileException
     * @throws InvalidAudioFrameException 
     */
    public static Track createMP3File(String mp3Path) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
        Track newSong = null;

        File musicfile = new File(mp3Path);
        AudioFile f = AudioFileIO.read(musicfile);
        Tag tag = f.getTag();

        int trackLength = f.getAudioHeader().getTrackLength();

        String artist = tag.getFirst(FieldKey.ARTIST);
        String album = tag.getFirst(FieldKey.ALBUM);
        String titel = tag.getFirst(FieldKey.TITLE);
        String year = tag.getFirst(FieldKey.YEAR);
        String trackN = tag.getFirst(FieldKey.TRACK);
        String genre = tag.getFirst(FieldKey.GENRE);

        if (artist.length() == 0) {
            artist = "Unknown Artist";
        }
        if (titel.length() == 0) {
            unknownTrackCounter++;
            titel = "Unknown Track" + unknownTrackCounter;
        }

        newSong = new Track(artist, titel, trackLength, mp3Count, album, mp3Path, musicfile.getParent() + "/folder.jpg", mp3Path, genre);


        return newSong;
    }

    /**
     * @return Die PlaylistCollection.
     */
    public ArrayList<Playlist> getPlaylists() {
        return playlistCollection;
    }

    /**
     * Legt eine neue Playlist an.
     * 
     * @param dirName
     * @return Die neue Playlist.
     */
    public Playlist addPlaylist(String dirName) {
        Date dt = new Date();
        SimpleDateFormat myDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
        Playlist lis = new Playlist(dt, dirName);
        playlistCollection.add(lis);
        playlistCount += 1;


        return lis;
    }

    /**
     * Löscht eine Playlist.
     * 
     * @param i Index der zu löschenden Playlist. 
     */
    public void removePlaylist(int i) {
        playlistCollection.remove(i);

    }

    /**
     * @return the playlistCount
     */
    public long getPlaylistCount() {
        return playlistCount;
    }

    /**
     * Es wird eine m3u Datei erzeugt, um darin eine erstellte Playlist abzuspeichern.
     * 
     * @param pl Playlist, die gespeichert werden soll.
     * @throws IOException 
     */
    public void createM3U(Playlist pl) throws IOException {

        String m3u;
        pl.getPlaylist();


        FileWriter fw = new FileWriter(pl.getTitle() + ".m3u");
        BufferedWriter out = new BufferedWriter(fw);

        out.write("#EXTM3U\n");

        for (Track aktTrack : pl.getPlaylist()) {
            out.write(aktTrack.getSoundfile() + "\n");

        }

        out.close();
    }

    /**
     * Eine zuvor abgespeicherte Playlist wird aus dem Dateisystem abgerufen.
     * 
     * @param m3u Die zu ladende m3u Datei.
     * @return Eine Liste mit Strings zu den MP3-Pfäden.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public ArrayList<String> loadM3U(File m3u) throws FileNotFoundException, IOException {


        BufferedReader reader = new BufferedReader(new FileReader(m3u));
        String line = null;


        String pName = null;

        int index = m3u.getName().lastIndexOf('.');
        if (index > 0 && index <= m3u.getName().length() - 2) {
            pName = m3u.getName().substring(0, index);
        }
        ArrayList<String> ret = new ArrayList<String>();
        Playlist p = addPlaylist(pName);

        reader.readLine();

        while ((line = reader.readLine()) != null) {
            if (!"#EXTM3U\n".equals(line)) {
                ret.add(line);
            }
        }


        return ret;
    }

    /**
     * Die zum Zeitpunkt des Beendens vorhandenen Tracks werden in der Collection gespeichert.
     * Sie werden in Textform abgelegt und beim nächsten Start abgerufen.
     * 
     * @param trackCircles Liste aller vorhandenen Elemente.
     * @throws IOException 
     */
    public void saveCollection(CopyOnWriteArrayList<TrackGUI> trackCircles) throws IOException {

        FileWriter fw = new FileWriter(COLLECTIONFILE);
        BufferedWriter out = new BufferedWriter(fw);
        for (TrackGUI aktGUI : trackCircles) {


            out.write(aktGUI.getT().getMp3Path() + SIGN + aktGUI.getT().getDurchmesser() + SIGN + aktGUI.getT().getPlayCount() + SIGN + aktGUI.getRed() + SIGN + aktGUI.getGreen() + SIGN + aktGUI.getBlue() + SIGN + aktGUI.getX() + SIGN + aktGUI.getY() + "\n");

        }
        out.close();
    }

    /**
     * Wird beim Start aufgerufen und lädt die in Textform gespeicgerte Collection,
     * falls diese vorhanden ist.
     * 
     * @return Die Liste der zu ladenden Tracks.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public CopyOnWriteArrayList loadCollection() throws FileNotFoundException, IOException {
        if (new File(COLLECTIONFILE).exists()) {

            BufferedReader reader = new BufferedReader(new FileReader(COLLECTIONFILE));
            String line = null;
            CopyOnWriteArrayList ret = new CopyOnWriteArrayList<TrackGUI>();


            while ((line = reader.readLine()) != null) {
                String[] properties = new String[8];

                properties = line.split(SIGN);

                try {
                    if (new File(properties[0]).exists()) {
                        Track newTrack = createMP3File(properties[0]);
                        newTrack.setDurchmesser(Integer.parseInt(properties[1]));
                        Random ra = new Random();

                        TrackGUI newTrackGUI = new TrackGUI(newTrack, Integer.parseInt(properties[6]), Integer.parseInt(properties[7]), MP3PlayerGUI.randomDirection(), MP3PlayerGUI.randomDirection(), Integer.parseInt(properties[3]), Integer.parseInt(properties[4]), Integer.parseInt(properties[5]), 520, 1010, false);
                        ret.add(newTrackGUI);
                    }
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
            reader.close();
            return ret;
        } else {
            return new CopyOnWriteArrayList<TrackGUI>();
        }
    }

    /**
     * @return the mp3Collection
     */
    public ArrayList<Track> getMp3Collection() {
        return mp3Collection;
    }

    /**
     * @param mp3Collection the mp3Collection to set
     */
    public void setMp3Collection(ArrayList<Track> mp3Collection) {
        this.mp3Collection = mp3Collection;
    }

    /**
     * @return the aktPlaylist
     */
    public Playlist getAktPlaylist() {
        return aktPlaylist;
    }

    /**
     * @param aktPlaylist the aktPlaylist to set
     */
    public void setAktPlaylist(Playlist aktPlaylist) {
        this.aktPlaylist = aktPlaylist;
    }
}
