package de.hsrm.entintben.mp3_player.gui;

import de.hsrm.entintben.mp3_player.player.MP3Player;
import de.hsrm.entintben.mp3_player.player.MP3Repository;
import de.hsrm.entintben.mp3_player.player.Playlist;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javazoom.jl.decoder.JavaLayerException;
import org.farng.mp3.TagException;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import java.util.*;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import java.beans.PropertyChangeListener;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MP3PlayGUI.java
 *
 * Created on 08.12.2011, 14:23:51
 */
/**
 *
 * @author Tobias Lehwalder, Dominik Schuhmann, Tino Landmann
 */
public class MP3PlayerGUI extends javax.swing.JFrame implements Observer {

    ResourceBundle bundle;

    /** Creates new form MP3PlayGUI */
    public MP3PlayerGUI() throws JavaLayerException, IOException, TagException, CannotReadException, org.jaudiotagger.tag.TagException, InvalidAudioFrameException, ReadOnlyFileException {


        player = new MP3Player();
        player.addObserver(this);
        /**
         * Listener welcher überprüft ob player spielt oder gestoppt ist und
         * demnacht den Playbutton ändert.
         */
        player.addPropertyChangeListener(MP3Player.PLAYINGCHANGE, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (player.isPlaying()) {


                    playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/mp3_player/buttons/stop.gif")));
                    playButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/mp3_player/buttons/stoppressed.gif")));


                } else {

                    playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/mp3_player/buttons/play.gif")));
                    playButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/mp3_player/buttons/playpressed.gif")));

                }


            }
        });
        /**
         * Listener der überprüft Tracks in der Liste sind und wenn ja, entsprechende
         * die GUI aktualisiert.
         */
        player.addPropertyChangeListener(MP3Player.ACTTRACKCHANGE, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (event.getNewValue() == null) {
                    coverLabel.setIcon(null);
                    jLabel3.setText("");
                    jLabel5.setText("");
                    jLabel6.setText("");
                    jLabel4.setVisible(false);
                    playButton.setEnabled(false);
                    skipForward.setEnabled(false);
                    skipBack.setEnabled(false);


                } else {

                    jLabel3.setText(player.getActTrack().getInterpret());
                    jLabel5.setText(player.getActTrack().getTitel());
                    jLabel6.setText(player.getActTrack().getAlbumtitle());
                    setTime(player);
                    coverLabel.setIcon(new ImageIcon(player.getActTrack().getCover()));
                    playButton.setEnabled(true);
                    skipForward.setEnabled(true);
                    skipBack.setEnabled(true);
                    if (!player.isPlaying()) {
                        jProgressBar1.setMaximum(player.getActTrack().getLength());
                        jLabel4.setVisible(true);

                    }
                }

            }
        });
        /**
         * Listener der während des Trackabspielens, die die Zeit und Progressbar aktualisiert.
         */
        player.addPropertyChangeListener(MP3Player.ACTTIMECHANGE, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent event) {
                setTime(player);
                jProgressBar1.setValue(player.getActTime());

            }
        });
        initComponents();

        playlistList.setSize(60, 100);
        playlistChooserList.setSize(60, 20);

        skipForward.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/mp3_player/buttons/skippressed.gif")));
        skipBack.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/mp3_player/buttons/skipBackpressed.gif")));
        playButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/mp3_player/buttons/playpressed.gif")));
        exit.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/mp3_player/buttons/exitpressed.gif")));


        bundle = ResourceBundle.getBundle("de/hsrm/entintben/mp3_player/language/lang", Locale.GERMAN);

        openFCButton.setText(bundle.getString("lang.addTrack"));
        directoryChooser.setText(bundle.getString("lang.addDir"));
        newPlaylist.setText(bundle.getString("lang.newPlaylist"));
        removePlaylist.setText(bundle.getString("lang.remPlaylist"));
        savePlaylist.setText(bundle.getString("lang.savPlaylist"));
        loadPlaylist.setText(bundle.getString("lang.loaPlaylist"));
        removeTrack.setText(bundle.getString("lang.remTrack"));

        jLabel4.setVisible(false);


        DropTarget target = new DropTarget(playlistList, new DropTargetAdapter() {

            /**
             * Vollführt die Dropfunktion und aktualisiert nach dem Drop
             * die Playlist.
             */
            @Override
            public void drop(DropTargetDropEvent dropEvent) {
                if (dropEvent.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dropEvent.acceptDrop(dropEvent.getDropAction());

                    List<File> list;
                    try {
                        list = (List<File>) dropEvent.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        for (File file : list) {

                            if (player.getRepository().getPlaylists().isEmpty()) {
                                player.setActPlaylist(player.getRepository().addPlaylist("Default"));

                                try {

                                    if (file.isDirectory()) {
                                        walkin(file, player.getActPlaylist());

                                    }
                                    if (file.getName().endsWith(".mp3")) {
                                        player.getActPlaylist().addTrack(MP3Repository.createMP3File(file.getPath()));
                                        player.getChanges().firePropertyChange("actPlaylist", null, player.getActPlaylist());
                                    }


                                } catch (CannotReadException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (org.jaudiotagger.tag.TagException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (ReadOnlyFileException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (InvalidAudioFrameException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            } else {
                                try {

                                    if (file.isDirectory()) {
                                        walkin(file, player.getActPlaylist());

                                    }

                                    if (file.getName().endsWith(".mp3")) {
                                        player.getActPlaylist().addTrack(MP3Repository.createMP3File(file.getPath()));

                                    }
                                } catch (CannotReadException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (org.jaudiotagger.tag.TagException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (ReadOnlyFileException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (InvalidAudioFrameException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    } catch (UnsupportedFlavorException ex) {
                        Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }



                }
                player.tell();
            }

            /**
             * Geht rekursiv in alle Ordner und schaut nach Mp3s.
             */
            public void walkin(File directory, Playlist list) {
                String pattern = ".mp3";

                File listFile[] = directory.listFiles();
                if (listFile != null) {
                    for (int i = 0; i < listFile.length; i++) {
                        if (listFile[i].isDirectory()) {
                            walkin(listFile[i], list);
                        } else {
                            if (listFile[i].getName().endsWith(pattern)) {
                                try {
                                    list.addTrack(MP3Repository.createMP3File(listFile[i].getPath()));
                                } catch (CannotReadException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (org.jaudiotagger.tag.TagException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (ReadOnlyFileException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (InvalidAudioFrameException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                }
            }
        });


        DropTarget playlistChooserDropTarget = new DropTarget(playlistChooserList, new DropTargetAdapter() {

            /**
             *  Vollführt die Dropfunktion und aktualisiert nach dem Drop
             * die Jlist.
             */
            @Override
            public void drop(DropTargetDropEvent dropEvent) {
                if (dropEvent.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dropEvent.acceptDrop(dropEvent.getDropAction());

                    List<File> list;
                    try {
                        list = (List<File>) dropEvent.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        for (File file : list) {

                            if (file.isDirectory()) {
                                Playlist lis = player.getRepository().addPlaylist(file.getName());

                                walkin(file, lis);

                            }

                        }
                    } catch (UnsupportedFlavorException ex) {
                        Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }



                }
                player.tell();
            }

            /**
             * Geht rekursiv in alle Ordner und schaut nach Mp3s.
             */
            public void walkin(File directory, Playlist list) {
                String pattern = ".mp3";

                File listFile[] = directory.listFiles();
                if (listFile != null) {
                    for (int i = 0; i < listFile.length; i++) {
                        if (listFile[i].isDirectory()) {
                            walkin(listFile[i], list);
                        } else {
                            if (listFile[i].getName().endsWith(pattern)) {
                                try {
                                    list.addTrack(MP3Repository.createMP3File(listFile[i].getPath()));
                                } catch (CannotReadException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (org.jaudiotagger.tag.TagException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (ReadOnlyFileException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (InvalidAudioFrameException ex) {
                                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                }
            }
        });

        playlistList.addMouseListener(new MouseAdapter() {

            /**
             * Überprüft ob Maustaste gedrückt wurde und unterscheidest zwischen 
             * doppel und einzel Klick.
             */
            @Override
            public void mouseClicked(MouseEvent event) {

                if (event.getClickCount() == 1) {

                    actTrackIndex = playlistList.getSelectedIndex();
                    player.setActTrack(player.getActPlaylist().getTrack(((JList) event.getSource()).getSelectedIndex()));
                }

                if (event.getClickCount() == 2) {
                    try {
                        player.selectTrack(((JList) event.getSource()).getSelectedIndex());
                    } catch (JavaLayerException ex) {
                        Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    player.setPlaying(true);

                }

            }
        });


        playlistChooserList.addMouseListener(new MouseAdapter() {

            /**
             * Überprüft ob Maustaste gedrückt wurde und unterscheidet
             * zwischen nicht oder einmal gedrückt.
             */
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 1) {
                    if (playlistChooserList.getSelectedIndex() >= 0) {
                        actPlaylistIndex = playlistChooserList.getSelectedIndex();

                        try {
                            player.selectPlaylist(((Playlist) playlistChooserList.getSelectedValue()).getId());
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });


        addMouseListener(new MouseAdapter() {

            /**
             * überprüft ob Maustaste gerade gedrückt wird und merkt sich Position.
             * Und führt dort event aus.
             */
            @Override
            public void mousePressed(MouseEvent event) {
                if (!event.isMetaDown()) {
                    point.x = event.getX();
                    point.y = event.getY();
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            /**
             * Merkt sich aktuelle Mouseposition und setzt
             * dann neue mit Hilfe des Events.
             */
            @Override
            public void mouseDragged(MouseEvent event) {
                if (!event.isMetaDown()) {
                    Point point_ = getLocation();
                    setLocation(point_.x + event.getX() - point.x,
                            point_.y + event.getY() - point.y);
                }
            }
        });
        playButton.setEnabled(false);
        skipForward.setEnabled(false);
        skipBack.setEnabled(false);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        hauptPanel = new javax.swing.JPanel();
        trackPanel = new javax.swing.JPanel();
        interpretL = new javax.swing.JLabel();
        titelL = new javax.swing.JLabel();
        albumL = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        controlPanel = new javax.swing.JPanel();
        skipBack = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        skipForward = new javax.swing.JButton();
        shuffleButton = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        listPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        playlistList = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        playlistChooserList = new javax.swing.JList();
        directoryChooser = new javax.swing.JButton();
        openFCButton = new javax.swing.JButton();
        newPlaylist = new javax.swing.JButton();
        removePlaylist = new javax.swing.JButton();
        savePlaylist = new javax.swing.JButton();
        loadPlaylist = new javax.swing.JButton();
        removeTrack = new javax.swing.JButton();
        coverLabel = new javax.swing.JLabel();
        exit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        minimize = new javax.swing.JButton();

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(212, 204, 195));
        setResizable(false);
        setUndecorated(true);

        hauptPanel.setBackground(new java.awt.Color(56, 56, 56));
        hauptPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        trackPanel.setBackground(new java.awt.Color(56, 56, 56));

        interpretL.setFont(new java.awt.Font("Lucida Grande", 1, 11));
        interpretL.setForeground(new java.awt.Color(255, 255, 255));
        interpretL.setText("Interpret");

        titelL.setFont(new java.awt.Font("Lucida Grande", 1, 11));
        titelL.setForeground(new java.awt.Color(255, 255, 255));
        titelL.setText("Titel");

        albumL.setFont(new java.awt.Font("Lucida Grande", 1, 11));
        albumL.setForeground(new java.awt.Color(255, 255, 255));
        albumL.setText("Album");

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 0, 11));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText(" ");

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 0, 11));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText(" ");

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 0, 11));
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText(" ");

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 0, 15));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("00:00 / 00:00");

        controlPanel.setBackground(new java.awt.Color(56, 56, 56));
        controlPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        skipBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/mp3_player/buttons/skipBack.gif"))); // NOI18N
        skipBack.setBorder(null);
        skipBack.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        skipBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skipBackActionPerformed(evt);
            }
        });
        controlPanel.add(skipBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/mp3_player/buttons/play.gif"))); // NOI18N
        playButton.setBorder(null);
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        controlPanel.add(playButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, -1, -1));

        skipForward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/mp3_player/buttons/skip.gif"))); // NOI18N
        skipForward.setBorder(null);
        skipForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skipForwardActionPerformed(evt);
            }
        });
        controlPanel.add(skipForward, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 0, -1, -1));

        shuffleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/mp3_player/buttons/shuffle.gif"))); // NOI18N
        shuffleButton.setBorder(null);
        shuffleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shuffleButtonActionPerformed(evt);
            }
        });
        controlPanel.add(shuffleButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, 27, -1));

        org.jdesktop.layout.GroupLayout trackPanelLayout = new org.jdesktop.layout.GroupLayout(trackPanel);
        trackPanel.setLayout(trackPanelLayout);
        trackPanelLayout.setHorizontalGroup(
            trackPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(trackPanelLayout.createSequentialGroup()
                .add(trackPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(interpretL)
                    .add(titelL)
                    .add(albumL))
                .add(18, 18, 18)
                .add(trackPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .add(jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .add(jLabel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 154, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(controlPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 173, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(jProgressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        trackPanelLayout.setVerticalGroup(
            trackPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(trackPanelLayout.createSequentialGroup()
                .add(trackPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(interpretL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(trackPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(titelL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel5))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(trackPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(albumL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel6))
                .add(18, 18, 18)
                .add(controlPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(14, 14, 14)
                .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jProgressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        hauptPanel.add(trackPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, 250, 158));

        listPanel.setBackground(new java.awt.Color(56, 56, 56));

        playlistList.setBackground(new java.awt.Color(70, 70, 70));
        playlistList.setFont(new java.awt.Font("Lucida Grande", 1, 11));
        playlistList.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(playlistList);

        playlistChooserList.setBackground(new java.awt.Color(70, 70, 70));
        playlistChooserList.setFont(new java.awt.Font("Lucida Grande", 0, 11));
        playlistChooserList.setForeground(new java.awt.Color(255, 255, 255));
        playlistChooserList.setMaximumSize(new java.awt.Dimension(150, 0));
        jScrollPane2.setViewportView(playlistChooserList);

        directoryChooser.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        directoryChooser.setText("add Dir");
        directoryChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directoryChooserActionPerformed(evt);
            }
        });

        openFCButton.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        openFCButton.setText("add Track");
        openFCButton.setActionCommand("");
        openFCButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFCButtonActionPerformed(evt);
            }
        });

        newPlaylist.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        newPlaylist.setText("new Playlist");
        newPlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newPlaylistActionPerformed(evt);
            }
        });

        removePlaylist.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        removePlaylist.setText("remove Playlist\n");
        removePlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removePlaylistActionPerformed(evt);
            }
        });

        savePlaylist.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        savePlaylist.setText("save Playlist");
        savePlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePlaylistActionPerformed(evt);
            }
        });

        loadPlaylist.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        loadPlaylist.setText("load Playlist");
        loadPlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadPlaylistActionPerformed(evt);
            }
        });

        removeTrack.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        removeTrack.setText("remove Track");
        removeTrack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeTrackActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout listPanelLayout = new org.jdesktop.layout.GroupLayout(listPanel);
        listPanel.setLayout(listPanelLayout);
        listPanelLayout.setHorizontalGroup(
            listPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, listPanelLayout.createSequentialGroup()
                .add(listPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(listPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, removeTrack, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 156, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, directoryChooser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 156, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, openFCButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 156, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(newPlaylist, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                    .add(removePlaylist, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 149, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(savePlaylist, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 149, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(loadPlaylist, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 149, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 254, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        listPanelLayout.linkSize(new java.awt.Component[] {directoryChooser, loadPlaylist, newPlaylist, openFCButton, removePlaylist, removeTrack, savePlaylist}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        listPanelLayout.setVerticalGroup(
            listPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, listPanelLayout.createSequentialGroup()
                .add(listPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
                    .add(listPanelLayout.createSequentialGroup()
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 199, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(openFCButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(directoryChooser)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(removeTrack)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 31, Short.MAX_VALUE)
                        .add(newPlaylist)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(removePlaylist)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(savePlaylist)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(loadPlaylist)))
                .addContainerGap())
        );

        hauptPanel.add(listPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 420, -1));

        coverLabel.setBackground(new java.awt.Color(255, 255, 255));
        coverLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        coverLabel.setMaximumSize(new java.awt.Dimension(150, 150));
        hauptPanel.add(coverLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 150, 150));

        exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/mp3_player/buttons/exit.gif"))); // NOI18N
        exit.setBorder(null);
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        hauptPanel.add(exit, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, -1, -1));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("EntIntBen_MP3Player ");
        hauptPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 230, -1));

        minimize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/mp3_player/buttons/minimize.gif"))); // NOI18N
        minimize.setBorder(null);
        minimize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minimizeActionPerformed(evt);
            }
        });
        hauptPanel.add(minimize, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, -1, -1));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(hauptPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 447, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(hauptPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Button für SkipBack Methode.
     * @param evt 
     */
    private void skipBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skipBackActionPerformed

        try {
            player.setStopped(true);
            player.skipBack();
        } catch (JavaLayerException ex) {
            Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_skipBackActionPerformed
    /**
     * Button für Play Methode
     * @param evt 
     */
    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed

        if (!player.isPlaying()) {
            player.setPlaying(true);

            try {
                player.play();
            } catch (JavaLayerException ex) {
                Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }


        } else {
            player.setStopped(true);
            player.setPlaying(false);
            player.stop();

        }

    }//GEN-LAST:event_playButtonActionPerformed
    /*
     * Button für SkipForward Methode
     */
    private void skipForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skipForwardActionPerformed

        try {
            player.setStopped(true);
            player.skipForward();
        } catch (JavaLayerException ex) {
            Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_skipForwardActionPerformed
    /**
     * Button um Ordner mit Mp3s hinzuzufügen.
     * 
     * @param evt 
     */
    private void directoryChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_directoryChooserActionPerformed
        JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        int state = fileChooser.showOpenDialog(this);

        File file;

        if (state == JFileChooser.APPROVE_OPTION) {

            file = fileChooser.getSelectedFile();
            Playlist list = player.getRepository().addPlaylist(file.getName());
            String[] listName = file.list();

            for (int i = 0; i < listName.length; i++) {
                if (listName[i].endsWith(".mp3")) {
                    try {
                        list.addTrack(MP3Repository.createMP3File(file.getPath() + "/" + listName[i]));
                    } catch (CannotReadException ex) {
                        Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (org.jaudiotagger.tag.TagException ex) {
                        Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ReadOnlyFileException ex) {
                        Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvalidAudioFrameException ex) {
                        Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
            player.tell();

        }/*
            p.tell();    }//GEN-LAST:event_directoryChooserActionPerformed
         */
    }

    /**
     * Button zum Öffnen eines Ordners um Track auszuwählen und Liste hinzuzufügen.
     * 
     * @param evt 
     */
    private void openFCButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFCButtonActionPerformed

        JFileChooser fileChooser = new javax.swing.JFileChooser();
        int state = fileChooser.showOpenDialog(this);
        fileChooser.setMultiSelectionEnabled(true);

        if (state == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();
            if (player.getRepository().getPlaylists().isEmpty()) {
                player.setActPlaylist(player.getRepository().addPlaylist("Default"));
            }

            player.tell();

            try {

                player.getActPlaylist().addTrack(MP3Repository.createMP3File(file.getPath()));
            } catch (CannotReadException ex) {
                Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (org.jaudiotagger.tag.TagException ex) {
                Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ReadOnlyFileException ex) {
                Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidAudioFrameException ex) {
                Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }

            player.tell();
        }    }//GEN-LAST:event_openFCButtonActionPerformed
    /**
     * Button um Shuffle funktion zu aktvieren/ deaktivieren.
     * 
     * @param evt 
     */
    private void shuffleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shuffleButtonActionPerformed

        if (player.isShuffle()) {
            shuffleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/mp3_player/buttons/shuffle.gif")));
            player.setShuffle(false);

        } else {
            shuffleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/mp3_player/buttons/shufflepressed.gif")));
            player.setShuffle(true);

        }
    }//GEN-LAST:event_shuffleButtonActionPerformed
    /**
     * Erstellt neue Playlist
     * @param evt 
     */
    private void newPlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newPlaylistActionPerformed

        String string = javax.swing.JOptionPane.showInputDialog("Wie soll die Playlist heißen?");
        player.getRepository().addPlaylist(string);
        player.tell();
    }//GEN-LAST:event_newPlaylistActionPerformed
    /**
     * Löscht Playlist.
     * @param evt 
     */
    private void removePlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removePlaylistActionPerformed
        if (actPlaylistIndex != -1) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.getRepository().removePlaylist(actPlaylistIndex);

            if (!player.getRepository().getPlaylists().isEmpty()) {
                player.setActPlaylist(player.getRepository().getPlaylists().get(0));
                if (player.getActPlaylist().getPlaylist().isEmpty()) {
                } else {
                    player.setActTrack(player.getActPlaylist().getTrack(0));
                }
            }
            player.tell();
        }
    }//GEN-LAST:event_removePlaylistActionPerformed
    /**
     * Schließt den Player.
     * @param evt 
     */
    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed

        System.exit(0);
    }//GEN-LAST:event_exitActionPerformed
    /**
     * Minimiert den Player
     * @param evt 
     */
    private void minimizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minimizeActionPerformed

        setState(JFrame.ICONIFIED);

    }//GEN-LAST:event_minimizeActionPerformed
    /**
     * Speichert aktuelle Playlist.
     * @param evt 
     */
    private void savePlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savePlaylistActionPerformed
        JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setApproveButtonText("Hier speichern");
        int state = fileChooser.showOpenDialog(this);



        if (state == JFileChooser.APPROVE_OPTION) {
            System.out.println(fileChooser.getSelectedFile().getAbsolutePath());
            try {
                player.getRepository().createM3U(player.getActPlaylist(), fileChooser.getSelectedFile().getAbsolutePath());
            } catch (IOException ex) {
                Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }



        }




    }//GEN-LAST:event_savePlaylistActionPerformed
    /**
     * Läd eine Playlist.
     * @param evt 
     */
    private void loadPlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadPlaylistActionPerformed

        JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        int state = fileChooser.showOpenDialog(this);

        File file;

        if (state == JFileChooser.APPROVE_OPTION) {

            file = fileChooser.getSelectedFile();
            file.getName();

            if (file.getName().endsWith(".m3u")) {
                try {
                    player.getRepository().loadM3U(file);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }

            }


        }
        player.tell();
    }//GEN-LAST:event_loadPlaylistActionPerformed
    /**
     * Löscht ausgewählten Track
     * @param evt 
     */
    private void removeTrackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeTrackActionPerformed
        if (actTrackIndex != -1) {
            player.getActPlaylist().getPlaylist().remove(actTrackIndex);
            player.setActTrack(null);
            player.tell();
        }
    }//GEN-LAST:event_removeTrackActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MP3PlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MP3PlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MP3PlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MP3PlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

                try {
                    MP3PlayerGUI frame = new MP3PlayerGUI();
                } catch (JavaLayerException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CannotReadException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (org.jaudiotagger.tag.TagException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidAudioFrameException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ReadOnlyFileException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TagException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    new MP3PlayerGUI().setVisible(true);
                } catch (JavaLayerException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TagException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CannotReadException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (org.jaudiotagger.tag.TagException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidAudioFrameException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ReadOnlyFileException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel albumL;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JLabel coverLabel;
    public javax.swing.JButton directoryChooser;
    private javax.swing.JButton exit;
    private javax.swing.JPanel hauptPanel;
    private javax.swing.JLabel interpretL;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel listPanel;
    private javax.swing.JButton loadPlaylist;
    private javax.swing.JButton minimize;
    private javax.swing.JButton newPlaylist;
    private javax.swing.JButton openFCButton;
    private javax.swing.JButton playButton;
    private javax.swing.JList playlistChooserList;
    private javax.swing.JList playlistList;
    private javax.swing.JButton removePlaylist;
    private javax.swing.JButton removeTrack;
    private javax.swing.JButton savePlaylist;
    private javax.swing.JButton shuffleButton;
    private javax.swing.JButton skipBack;
    private javax.swing.JButton skipForward;
    private javax.swing.JLabel titelL;
    private javax.swing.JPanel trackPanel;
    // End of variables declaration//GEN-END:variables
    // Own variables
    private MP3Player player;
    private String idEingabe = null;
    private int time;
    private DefaultComboBoxModel model = new DefaultComboBoxModel();
    private int actPlaylistIndex = -1;
    private int actTrackIndex = -1;
    private Point point = new Point();

    /**
     * Setzt abgespielte Zeit des aktuell laufenden Tracks.
     * @param r 
     */
    public void setTime(MP3Player r) {
        int trackLengthV = (r.getActTrack().getLength() / 60);
        int trackLengthN = (r.getActTrack().getLength() % 60);
        int actTimeV = (r.getActTime() / 60);
        int actTimeN = (r.getActTime() % 60);
        String trackLength;
        String actTime;

        if (trackLengthV < 10) {
            if (trackLengthN < 10) {
                trackLength = "0" + trackLengthV + ":" + "0" + trackLengthN;
            } else {
                trackLength = "0" + trackLengthV + ":" + trackLengthN;
            }

        } else {
            if (trackLengthN < 10) {
                trackLength = trackLengthV + ":" + "0" + trackLengthN;
            } else {
                trackLength = trackLengthV + ":" + trackLengthN;
            }
        }

        if (actTimeV < 10) {
            if (actTimeN < 10) {
                actTime = "0" + actTimeV + ":" + "0" + actTimeN;
            } else {
                actTime = "0" + actTimeV + ":" + actTimeN;
            }

        } else {
            if (actTimeN < 10) {
                actTime = actTimeV + ":" + "0" + actTimeN;
            } else {
                actTime = actTimeV + ":" + actTimeN;
            }
        }


        jLabel4.setText(actTime + " / " + trackLength);

    }

    /**
     * Aktualisiert Playlist wenn Observer meldet das sich etwas geändert hat.
     * @param observ
     * @param object 
     */
    @Override
    public void update(Observable observ, Object object) {
        MP3Player player_locale = (MP3Player) object;
        model.removeAllElements();
        for (Playlist aktPlayer : player_locale.getRepository().getPlaylists()) {
            model.addElement(aktPlayer);
        }


        if (player_locale.getRepository().getPlaylists().isEmpty()) {
            player_locale.setActTrack(null);
            //player.setActPlaylist(null);


            coverLabel.setText("");
            playlistList.setModel(new DefaultListModel());
            if (player_locale.getRepository().getPlaylistCollection().isEmpty()) {
                playlistChooserList.setModel(new DefaultListModel());

            }

        } else {
            playlistChooserList.setListData(new Vector(player_locale.getRepository().getPlaylists()));
            playlistList.setListData(new Vector(player_locale.getActPlaylist().getPlaylist()));

        }





    }
}