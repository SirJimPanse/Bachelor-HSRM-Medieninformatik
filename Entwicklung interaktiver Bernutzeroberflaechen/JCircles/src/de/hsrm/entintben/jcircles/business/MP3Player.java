package de.hsrm.entintben.jcircles.business;

import de.hsrm.entintben.jcircles.gui.MP3PlayerGUI;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.io.FileNotFoundException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.farng.mp3.TagException;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import java.util.*;

/**
 *
 * @author Tobias Lehwalder, Tino Landmann, Dominik Schuhmann
 * 
 * Der zu Grunde liegende Player mit allen Standard-Funktionen. 
 */
public class MP3Player {

    public static final String ACTTIMECHANGE = "actTime";
    public static final String ACTTRACKCHANGE = "actTrack";
    public AdvancedPlayer player;
    private Playlist actPlaylist;
    private MP3Repository repository;
    private Track actTrack;
    private Timer timer;
    private int actTime;
    private int trackNo = 0;
    private boolean playing;
    private boolean stopped;
    private boolean shuffle;
    private boolean repeat;
    private PropertyChangeSupport changes;

    public MP3Player() throws JavaLayerException, IOException, TagException, CannotReadException, org.jaudiotagger.tag.TagException, InvalidAudioFrameException, ReadOnlyFileException {
        playing = false;
        stopped = false;
        shuffle = false;
        changes = new PropertyChangeSupport(this);

        try {
            repository = createRepository(repository);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Repository wird erstellt
     * 
     * @param repository 
     * @return repository
     * @throws FileNotFoundException
     * @throws JavaLayerException
     * @throws IOException
     * @throws TagException
     * @throws CannotReadException
     * @throws org.jaudiotagger.tag.TagException
     * @throws ReadOnlyFileException
     * @throws InvalidAudioFrameException 
     */
    public MP3Repository createRepository(MP3Repository repository) throws FileNotFoundException, JavaLayerException, IOException, TagException, CannotReadException, org.jaudiotagger.tag.TagException, ReadOnlyFileException, InvalidAudioFrameException {

        repository = new MP3Repository();

        return repository;
    }

    /**
     * spielt aktuellen Track ab
     * 
     * @throws JavaLayerException
     * @throws FileNotFoundException 
     */
    public void play() throws JavaLayerException, FileNotFoundException {
        if (!playing) {
            if (actTrack.getDurchmesser() >= 56) {
            } else {
                actTrack.setPlayCount(actTrack.getPlayCount() + 1);
                actTrack.setDurchmesser(actTrack.getDurchmesser() + 1);
            }
            InputStream in = new FileInputStream(actTrack.getSoundfile());
            player = new AdvancedPlayer(in);
            timer = new Timer();
            stopped = false;


            playing = true;

            new Thread() {

                @Override
                public void run() {

                    try {
                        startTimer();
                        player.play();

                    } catch (JavaLayerException ex) {
                        Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        if (isStopped()) {
                        } else {
                            if (repository.getAktPlaylist() == null) {
                                selectTrack(0);
                            } else {
                                skipForward();
                            }
                        }
                    } catch (JavaLayerException ex) {
                        Logger.getLogger(MP3Player.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MP3Player.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.start();

        }
    }

    /**
     * Wenn shuffle eingestellt ist, wird per zufall ein neuer Track ausgewählt. Ansonsten
     * wird der nachfolgende Track in der Playlist aufgerufen. Wird dabei gerade ein
     * Track abgespielt so wird der nachfolgende auch automatisch abgespielt, ansonsten
     * nicht.Befindet man sich beim Track in der Playlist und Skipt nach vorne, so
     * gelangt man wieder zum ersten Track.
     * 
     * @throws JavaLayerException
     * @throws FileNotFoundException 
     */
    public void skipForward() throws JavaLayerException, FileNotFoundException {



        if (actPlaylist != null) {
            if (isShuffle()) {
                if (playing) {
                    stopped = true;
                    this.stop();

                    setActTrack(repository.getMp3Collection().get(shuffleValue(repository.getMp3Collection().size())));
                    this.play();
                } else {
                    setActTrack(repository.getMp3Collection().get(shuffleValue(repository.getMp3Collection().size())));

                }


            } else {

                if (playing) {
                    stopped = true;
                    this.stop();
                    if (trackNo < actPlaylist.getPlaylist().size() - 1) {
                        setActTrack(actPlaylist.getTrack(++trackNo));
                    } else {
                        trackNo = 0;
                        setActTrack(actPlaylist.getTrack(trackNo));
                    }
                    this.play();
                } else {
                    if (trackNo < actPlaylist.getPlaylist().size() - 1) {
                        setActTrack(actPlaylist.getTrack(++trackNo));
                    } else {
                        trackNo = 0;
                        setActTrack(actPlaylist.getTrack(trackNo));
                    }
                }
            }
        } else {
            setActTrack(repository.getMp3Collection().get(shuffleValue(repository.getMp3Collection().size())));
            if (playing) {
                stopped = true;
                stop();
                play();
            }
        }

    }

    /**
     * Hält den Timer und den Player an.
     */
    public void stop() {
        this.stopTimer();
        playing = false;
        player.close();
    }

    /**
     * Wenn shuffle eingestellt ist, wird per zufall ein neuer ausgewählt. Ansonsten
     * wird der vorherige Track in der Playlist aufgerufen. Wird dabei gerade ein
     * Track abgespielt so wird der vorherige auch automatisch abgespielt, ansonsten
     * nicht. Befindet man sich beim ersten Track und skipt zurück, so gelangt man
     * zum letzten Track der Playlist.
     * 
     * @throws JavaLayerException
     * @throws FileNotFoundException 
     */
    public void skipBack() throws JavaLayerException, FileNotFoundException {


        if (actPlaylist != null) {
            if (isShuffle()) {
                if (playing) {
                    stopped = true;
                    this.stop();

                    setActTrack(repository.getMp3Collection().get(shuffleValue(repository.getMp3Collection().size())));
                    this.play();
                } else {
                    setActTrack(repository.getMp3Collection().get(shuffleValue(repository.getMp3Collection().size())));

                }


            } else {

                if (playing) {
                    stopped = true;
                    this.stop();
                    System.out.println(actPlaylist.getPlaylist().size());
                    System.out.println(trackNo);
                    if (trackNo <= actPlaylist.getPlaylist().size() - 1 && trackNo != 0) {
                        trackNo = trackNo - 1;
                        setActTrack(actPlaylist.getTrack(trackNo));
                    } else {
                        if (trackNo == 0) {
                            trackNo = actPlaylist.getPlaylist().size() - 1;
                            setActTrack(actPlaylist.getTrack(trackNo));
                        }
                    }
                    this.play();
                } else {
                    if (trackNo <= actPlaylist.getPlaylist().size() - 1 && trackNo != 0) {
                        trackNo = trackNo - 1;
                        setActTrack(actPlaylist.getTrack(trackNo));
                    } else {
                        if (trackNo == 0) {
                            trackNo = actPlaylist.getPlaylist().size() - 1;
                            setActTrack(actPlaylist.getTrack(trackNo));
                        }
                    }

                }
            }
            System.out.println(trackNo);

        } else {
            setActTrack(repository.getMp3Collection().get(shuffleValue(repository.getMp3Collection().size())));
            if (playing) {
                stopped = true;
                stop();
                play();
            }
        }

    }

    /**
     * Berechnet zufällig eine Zahl, welche dem nächsten Track entspricht.
     * 
     * @param high - Höchstwert der Zufallszahl
     * @return shuffleValue
     */
    public int shuffleValue(int high) {
        return (int) (Math.random() * (high));

    }

    /**
     * startet Timer, dieser zählt jede Sekunde hoch. Und setzt die aktuelle Zeit neu.
     */
    public void startTimer() {
        class Task extends TimerTask {

            public void run() {
                setActTime(actTime + 1);

            }
        }

        timer.schedule(new Task(), 1000, 1000);
    }

    /**
     * Stoppt den Timer und setzt die aktuelle Zeit auf 0; 
     */
    public void stopTimer() {
        timer.cancel();
        setActTime(0);
    }

    /**
     * wählt aktuelle Playlist aus
     * 
     * @param id
     * @return getActPlaylist 
     * @throws FileNotFoundException 
     */
    public Playlist selectPlaylist(long id) throws FileNotFoundException {

        setActPlaylist(getRepository().findById(id));

        if (!actPlaylist.getPlaylist().isEmpty()) {
            setActTrack(actPlaylist.getTrack(getTrackNo()));
        }

        return getActPlaylist();

    }

    /**
     * wählt aktuellen Track aus
     * 
     * @param number
     * @return actTrack
     * @throws JavaLayerException
     * @throws FileNotFoundException 
     */
    public Track selectTrack(int no) throws JavaLayerException, FileNotFoundException {
        if (playing) {
            this.stop();
            this.stopped = true;
        }

        if (repository.getAktPlaylist() == null) {

            setActTrack(repository.getMp3Collection().get(shuffleValue(repository.getMp3Collection().size())));
        } else {
            trackNo = no;
            setActTrack(actPlaylist.getTrack(no));
        }
        this.play();

        return actTrack;

    }

    /**
     * wählt aktuellen Track aus
     * 
     * @param track - Der aus der GUI durch einen Klick übergebene Track
     * @return actTrack
     * @throws JavaLayerException
     * @throws FileNotFoundException 
     */
    public Track selectTrackByTrack(Track track) throws JavaLayerException, FileNotFoundException {
        if (playing) {
            this.stop();
            this.stopped = true;
        }

        setActTrack(track);
        timer = new Timer();

        this.play();

        return actTrack;

    }

    /**
     * @return the actTrack
     */
    public Track getActTrack() {
        return actTrack;
    }

    /**
     * @param actTrack the actTrack to set
     */
    public void setActTrack(Track actTrack) {
        if (this.actTrack != null) {
            this.actTrack.setIsActTrack(false);
        }
        Track lastActTrack = this.actTrack;
        this.actTrack = actTrack;
        this.actTrack.setIsActTrack(true);
        changes.firePropertyChange(ACTTRACKCHANGE, lastActTrack, actTrack);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        changes.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changes.removePropertyChangeListener(listener);
    }

    /**
     * @param actTime the actTime to set
     */
    public void setActTime(int actTime) {
        int lastActTime = this.actTime;
        this.actTime = actTime;
        changes.firePropertyChange(ACTTIMECHANGE, lastActTime, actTime);
    }

    /**
     * @return the actPlaylist
     */
    public Playlist getActPlaylist() {
        return actPlaylist;
    }

    /**
     * @param actPlaylist the actPlaylist to set
     */
    public void setActPlaylist(Playlist actPlaylist) {
        this.actPlaylist = actPlaylist;

    }

    /**
     * @param trackNo the trackNo to set
     */
    public void setTrackNo(int trackNo) {
        this.trackNo = trackNo;
    }

    /**
     * @return the trackNo
     */
    public int getTrackNo() {
        return trackNo;
    }

    /**
     * @return the zeit
     */
    public int getActTime() {
        return actTime;
    }

    /**
     * @return the playing
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * @param playing the playing to set
     */
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    /**
     * @return the stopped
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * @param stopped the stopped to set
     */
    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    /**
     * @return the repository
     */
    public MP3Repository getRepository() {
        return repository;
    }

    /**
     * @return the shuffle
     */
    public boolean isShuffle() {
        return shuffle;
    }

    /**
     * @param shuffle the shuffle to set
     */
    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    /**
     * @return the repeat
     */
    public boolean isRepeat() {
        return repeat;
    }

    /**
     * @param repeat the repeat to set
     */
    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }
}
