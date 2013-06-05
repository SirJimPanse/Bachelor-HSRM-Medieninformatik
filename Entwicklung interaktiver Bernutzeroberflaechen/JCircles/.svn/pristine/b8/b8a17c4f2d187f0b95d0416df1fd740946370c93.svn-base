package de.hsrm.entintben.jcircles.gui;

import de.hsrm.entintben.jcircles.tutorial.TutorialFrame;
import com.smardec.mousegestures.MouseGestures;
import com.smardec.mousegestures.MouseGesturesListener;
import java.awt.Color;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javazoom.jl.decoder.JavaLayerException;
import org.farng.mp3.TagException;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import de.hsrm.entintben.jcircles.business.MP3Player;
import de.hsrm.entintben.jcircles.business.MP3Repository;
import de.hsrm.entintben.jcircles.business.Playlist;
import de.hsrm.entintben.jcircles.business.Track;

/**
 *  Die GUI-Klasse des MP3-Players
 * 
 * 
 * @author Dominik Schuhmann, Tino Landmann, Tobias Lehwalder
 */
public class MP3PlayerGUI extends javax.swing.JFrame {

    ResourceBundle bundle;
    private MP3Player player;

    public MP3PlayerGUI() {
        try {
            player = new MP3Player();
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
        initComponents();

        mouseGestures.start();
        ImageIcon im = new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/background/player_bg.gif"));

        try {
            trackCircles = new CopyOnWriteArrayList<TrackGUI>();

            trackCircles = player.getRepository().loadCollection();
            for (TrackGUI aktGUI : trackCircles) {
                player.getRepository().getMp3Collection().add(aktGUI.getT());
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }



        if (trackCircles == null) {
            trackCircles = new CopyOnWriteArrayList<TrackGUI>();
        }
        
        setSize(1010, 563);


        playlistLines = new PlaylistGUI();
        drawPanel = new DrawPanel(this.getWidth(), DRAWPANELHEIGHT, trackCircles, SLEEPTIME);

        for (TrackGUI aktE : trackCircles) {
            drawPanel.setLayer(aktE, 2, 2);
        }

        drawPanel.setBackground(null);
        coverLabel = new JLabel();

        coverLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        coverLabel.setPreferredSize(new java.awt.Dimension(COVERSIZE, COVERSIZE));
        coverLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/background/jcircles.gif")));

        drawPanel.setVisible(true);

        mainPanel.add(drawPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, this.getWidth(), DRAWPANELHEIGHT));
        drawPanel.add(coverLabel);

        javax.swing.GroupLayout drawPanel1Layout = new javax.swing.GroupLayout(drawPanel);
        drawPanel1Layout.setHorizontalGroup(
                drawPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(drawPanel1Layout.createParallelGroup().addComponent(coverLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        drawPanel1Layout.setVerticalGroup(
                drawPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, drawPanel1Layout.createSequentialGroup().addContainerGap(250, Short.MAX_VALUE).addComponent(coverLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));

        drawPanel.setLayout(drawPanel1Layout);

        bundle = ResourceBundle.getBundle("de/hsrm/entintben/jcircles/language/lang", Locale.ENGLISH);


        playlistModeButton.setToolTipText(bundle.getString("lang.playlistModeButton"));
        playlistPopUp.setToolTipText(bundle.getString("lang.playlistPopUp"));
        showSongInfosButton.setToolTipText(bundle.getString("lang.showSongInfosButton"));
        stopCircles.setToolTipText(bundle.getString("lang.stopCircles"));
        searchTextField.setToolTipText(bundle.getString("lang.searchTextField"));
        newPlaylistButton.setToolTipText(bundle.getString("lang.newPlaylistButton"));
        toggleCoverLabel.setToolTipText(bundle.getString("lang.toggleCoverLabel"));
        tutorialButton.setToolTipText(bundle.getString("lang.tutorialButton"));
        minimizeButton.setToolTipText(bundle.getString("lang.minimizeButton"));
        exitButton.setToolTipText(bundle.getString("lang.exitButton"));



        DropTarget target = new DropTarget(drawPanel, new DropTargetAdapter() {

            @Override
            public void drop(DropTargetDropEvent dtde) {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrop(dtde.getDropAction());

                    List<File> list;
                    int red = (int) (Math.random() * 256);
                    int green = (int) (Math.random() * 256);
                    int blue = (int) (Math.random() * 256);
                    try {
                        list = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        for (File file : list) {
                            try {
                                if (file.isDirectory()) {
                                    walkin(file, player.getActPlaylist(), dtde, red, green, blue);
                                }
                                if (file.getName().toLowerCase().endsWith(".mp3")) {
                                    Track newTrack = MP3Repository.createMP3File(file.getPath());

                                    if (!player.getRepository().getMp3Collection().contains(newTrack)) {
                                        player.getRepository().getMp3Collection().add(newTrack);

                                        final TrackGUI newCircle = new TrackGUI(newTrack, dtde.getLocation().x, dtde.getLocation().y, randomDirection(), randomDirection(), red, green, blue, drawPanel.getHeight(), drawPanel.getWidth(), circlesStopped);
                                        newCircle.getCircle().add(new MyMouseListener(newCircle));
                                        trackCircles.add(newCircle);
                                        newCircle.setX(dtde.getLocation().x);
                                        newCircle.setY(dtde.getLocation().y);
                                        newCircle.setDoubleBuffered(true);
                                        drawPanel.add(newCircle);
                                        drawPanel.setLayer(coverLabel, 1, -1);
                                        for (TrackGUI aktE : trackCircles) {
                                            drawPanel.setLayer(aktE, 2, 3);
                                        }
                                    }
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
                    } catch (UnsupportedFlavorException ex) {
                        Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }

            public void walkin(File dir, Playlist lis, DropTargetDropEvent dtde, int r, int g, int b) throws CannotReadException, IOException, org.jaudiotagger.tag.TagException, ReadOnlyFileException, InvalidAudioFrameException {
                String pattern = ".mp3";

                File listFile[] = dir.listFiles();
                if (listFile != null) {
                    for (int i = 0; i < listFile.length; i++) {
                        if (listFile[i].isDirectory()) {
                            walkin(listFile[i], lis, dtde, r, g, b);
                        } else {
                            if (listFile[i].getName().toLowerCase().endsWith(pattern)) {
                                Track newTrack = MP3Repository.createMP3File(listFile[i].getPath());

                                if (!player.getRepository().getMp3Collection().contains(newTrack)) {
                                    player.getRepository().getMp3Collection().add(newTrack);

                                    TrackGUI newCircle = new TrackGUI(newTrack, dtde.getLocation().x, dtde.getLocation().y, randomDirection(), randomDirection(), r, g, b, drawPanel.getHeight(), drawPanel.getWidth(), circlesStopped);
                                    newCircle.getCircle().add(new MyMouseListener(newCircle)); 
                                    trackCircles.add(newCircle);
                                    drawPanel.add(newCircle);
                                    newCircle.setDoubleBuffered(true);
                                    drawPanel.setLayer(coverLabel, 1, -1);
                                    for (TrackGUI aktE : trackCircles) {
                                        drawPanel.setLayer(aktE, 2, 3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        for (TrackGUI aktE : trackCircles) {
            drawPanel.add(aktE);
            aktE.add(new MyMouseListener(aktE));
        }

        /**
         * Mausgesten-Listener
         * Zur Steuerung der Playerfunktion über BUTTON3(Rechtsklick)
         * und Mausbewegung. 
         * Die Gesten returnen einen String der D,R,U oder L enthalten kann,
         * dieser wird vom Listener verarbeitet.
         * 
         * D - Down
         * U - Up
         * R - Right
         * L - Left
         * 
         */
        mouseGestures.setMouseButton(MouseEvent.BUTTON3_MASK);
        mouseGestures.addMouseGesturesListener(new MouseGesturesListener() {

            @Override
            public void processGesture(String string) {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                }
            }

            @Override
            public void gestureMovementRecognized(String string) {
                if (!trackCircles.isEmpty()) {
                    if ("DRUL".equals(string)) {
                        player.setPlaying(false);
                        player.setStopped(true);
                        player.stop();
                    }
                    if ("R".equals(string)) {

                        try {
                            player.skipForward();
                        } catch (JavaLayerException ex) {
                            Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    if ("L".equals(string)) {
                        try {
                            player.skipBack();
                        } catch (JavaLayerException ex) {
                            Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                    if ("UR".equals(string) && player.getActTrack() != null) {
                        try {
                            player.play();
                        } catch (JavaLayerException ex) {
                            Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    drawPanel.repaint();
                }
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    player.getRepository().saveCollection(trackCircles);
                } catch (IOException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }));


        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    player.getRepository().saveCollection(trackCircles);
                } catch (IOException ex) {
                    Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        newPlaylistButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/clearPlaylist_pressed.gif")));
        playlistPopUp.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/playlistPopup_pressed.gif")));
        playlistModeButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/playlistMode_pressed.gif")));
        showSongInfosButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/showInfoButton_pressed.gif")));
        stopCircles.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/stopCircles_pressed.gif")));
        toggleCoverLabel.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/coverDisable_pressed.gif")));
        exitButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/exit_pressed.gif")));
        minimizeButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/minimize_pressed.gif")));
        tutorialButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/tutorial_pressed.gif")));


        newPlaylistButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/clearPlaylist_over.gif")));
        playlistPopUp.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/playlistPopup_over.gif")));
        playlistModeButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/playlistMode_over.gif")));
        showSongInfosButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/showInfoButton_over.gif")));
        stopCircles.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/stopCircles_over.gif")));
        toggleCoverLabel.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/coverDisable_over.gif")));
        exitButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/exit_over.gif")));
        minimizeButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/minimize_over.gif")));
        tutorialButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/tutorial_over.gif")));

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (!e.isMetaDown() && e.getButton() == MouseEvent.BUTTON1) {
                    point.x = e.getX();
                    point.y = e.getY();
                }

            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {

                if (e.getModifiersEx() == LEFTMOUSEBUTTON) {
                    Point p = getLocation();
                    setLocation(p.x + e.getX() - point.x,
                            p.y + e.getY() - point.y);
                }

            }
        });




        player.addPropertyChangeListener(MP3Player.ACTTIMECHANGE, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                setTime(player);
            }
        });

        player.addPropertyChangeListener(MP3Player.ACTTRACKCHANGE, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if (pce.getNewValue() != null) {
                    coverImage = new ImageIcon(player.getActTrack().getCover());
                    if (coverImage.getIconHeight() > 1) {
                        coverLabel.setIcon(coverImage);
                    } else {
                        coverLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/background/jcircles.gif")));
                    }
                    songAnzeige.setText(player.getActTrack().getSongInfo());

                } else {
                    coverLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/background/jcircles.gif")));

                }


            }
        });


    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        showSongInfosButton = new javax.swing.JButton();
        playlistModeButton = new javax.swing.JButton();
        stopCircles = new javax.swing.JButton();
        searchTextField = new javax.swing.JTextField();
        songAnzeige = new javax.swing.JLabel();
        playlistPopUp = new javax.swing.JButton();
        newPlaylistButton = new javax.swing.JButton();
        timeLabel = new javax.swing.JLabel();
        toggleCoverLabel = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        minimizeButton = new javax.swing.JButton();
        tutorialButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(1010, 562));
        setUndecorated(true);

        mainPanel.setBackground(new java.awt.Color(51, 51, 51));
        mainPanel.setPreferredSize(new java.awt.Dimension(1010, 600));
        mainPanel.setSize(new java.awt.Dimension(1010, 600));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        showSongInfosButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/showInfoButton_released.gif"))); // NOI18N
        showSongInfosButton.setBorder(null);
        showSongInfosButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showSongInfosButtonActionPerformed(evt);
            }
        });
        mainPanel.add(showSongInfosButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 530, -1, -1));

        playlistModeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/playlistMode_released.gif"))); // NOI18N
        playlistModeButton.setBorder(null);
        playlistModeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playlistModeButtonActionPerformed(evt);
            }
        });
        mainPanel.add(playlistModeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 530, -1, -1));

        stopCircles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/stopCircles_released.gif"))); // NOI18N
        stopCircles.setBorder(null);
        stopCircles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopCirclesActionPerformed(evt);
            }
        });
        mainPanel.add(stopCircles, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 530, -1, -1));

        searchTextField.setBackground(new java.awt.Color(102, 102, 102));
        searchTextField.setForeground(new java.awt.Color(255, 255, 255));
        searchTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        searchTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTextFieldActionPerformed(evt);
            }
        });
        searchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchTextFieldKeyReleased(evt);
            }
        });
        mainPanel.add(searchTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(554, 530, 120, -1));

        songAnzeige.setFont(new java.awt.Font("Tahoma", 0, 12));
        songAnzeige.setForeground(new java.awt.Color(255, 255, 255));
        mainPanel.add(songAnzeige, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 530, 340, 20));

        playlistPopUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/playlistPopup_released.gif"))); // NOI18N
        playlistPopUp.setBorder(null);
        playlistPopUp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                playlistPopUpMouseClicked(evt);
            }
        });
        playlistPopUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playlistPopUpActionPerformed(evt);
            }
        });
        mainPanel.add(playlistPopUp, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 530, -1, -1));

        newPlaylistButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/clearPlaylist_released.gif"))); // NOI18N
        newPlaylistButton.setBorder(null);
        newPlaylistButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newPlaylistButtonActionPerformed(evt);
            }
        });
        mainPanel.add(newPlaylistButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 530, -1, -1));

        timeLabel.setFont(new java.awt.Font("Lucida Grande", 0, 12));
        timeLabel.setForeground(new java.awt.Color(255, 255, 255));
        mainPanel.add(timeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 530, 130, 20));

        toggleCoverLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/coverDisable_released.gif"))); // NOI18N
        toggleCoverLabel.setBorder(null);
        toggleCoverLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleCoverLabelActionPerformed(evt);
            }
        });
        mainPanel.add(toggleCoverLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, -1, -1));

        exitButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/exit_released.gif"))); // NOI18N
        exitButton.setBorder(null);
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        mainPanel.add(exitButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 530, -1, -1));

        minimizeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/minimize_released.gif"))); // NOI18N
        minimizeButton.setBorder(null);
        minimizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minimizeButtonActionPerformed(evt);
            }
        });
        mainPanel.add(minimizeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 530, -1, -1));

        tutorialButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/tutorial_released.gif"))); // NOI18N
        tutorialButton.setBorder(null);
        tutorialButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tutorialButtonActionPerformed(evt);
            }
        });
        mainPanel.add(tutorialButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 530, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 563, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * 
     * Die Methode stoppt alle Circles. Die boolesche Variable circlesStopped wird true wenn sie false ist und umgekehrt.
     * Von allen TrackGUI Objekten wird standStill gesetzt.
     * 
     * @param evt 
     */
    private void stopCirclesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopCirclesActionPerformed
        if (!playlistMode) {
            if (circlesStopped) {
                circlesStopped = false;
                stopCircles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/stopCircles_released.gif")));
                stopCircles.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/stopCircles_over.gif")));

            } else {
                circlesStopped = true;
                stopCircles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/stopCircles_pressed.gif")));
                stopCircles.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/stopCircles_pressed.gif")));

            }
            for (TrackGUI aktTrack : trackCircles) {

                aktTrack.setStandStill(circlesStopped);
            }
        }
    }//GEN-LAST:event_stopCirclesActionPerformed

    private void searchTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTextFieldActionPerformed
    }//GEN-LAST:event_searchTextFieldActionPerformed

    /**
     * Die Methode aktiviert die Songinfos unter jedem Circle anhand der booleschen Variable showSongInfos, diese
     * wird true gesetzt wenn false und umgekehrt.
     * Die JLabels songInfo ändern ihre Visibility.
     * 
     * 
     * @param evt 
     */
    private void showSongInfosButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showSongInfosButtonActionPerformed
        for (TrackGUI aktTrack : trackCircles) {
            if (!showSongInfos) {
                aktTrack.getSongInfo().setText(aktTrack.getT().getSongInfo());
                aktTrack.getSongInfo().setVisible(true);
            } else {
                aktTrack.getSongInfo().setVisible(false);
            }
        }
        if (!showSongInfos) {
            showSongInfosButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/showInfoButton_pressed.gif")));
            showSongInfosButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/showInfoButton_pressed.gif")));

            showSongInfos = true;
        } else {
            showSongInfosButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/showInfoButton_released.gif")));
            showSongInfosButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/showInfoButton_over.gif")));

            showSongInfos = false;
        }
    }//GEN-LAST:event_showSongInfosButtonActionPerformed
    /**
     * Der Playlistmodus wird aktiviert, dadurch können Playlisten erstellt und bearbeitet werden.
     * Songs werden der Playlist hinzugefügt durch einen Linksklick auf den Circle des jeweiligen Songs.
     * Nach Beenden des Playlistmodes wird ein JOptionPane geöffnet. Dort kann man den Namen der Playlist angeben,
     * wenn man diese Speichern möchte.
     * Alle Circle werden angehalten.
     * 
     * @param evt 
     */
    private void playlistModeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playlistModeButtonActionPerformed
        if (playlistMode) {

            if (player.getActPlaylist() != null && player.getActPlaylist().getPlaylist().size() > 1) {
                String s = javax.swing.JOptionPane.showInputDialog("Wie soll die Playlist heißen?");


                if (s != null) {

                    player.getActPlaylist().setTitle(s);

                    try {
                        player.getRepository().createM3U(player.getActPlaylist());
                    } catch (IOException ex) {
                        Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {

                    clearPlaylist();
                }
            }else {
                    for (TrackGUI aktE : trackCircles) {
                        aktE.setStandStill(circlesStopped);
                        aktE.setInPlaylist(false);
                    }
                    playlistLines.setPl(null);
                    player.setActPlaylist(null);
                
            }
            playlistModeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/playlistMode_released.gif")));
            playlistModeButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/playlistMode_over.gif")));
            playlistMode = false;
        } else {
            playlistModeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/playlistMode_pressed.gif")));
            playlistModeButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/playlistMode_pressed.gif")));

            playlistMode = true;
        }

        for (TrackGUI aktTrack : trackCircles) {
            aktTrack.setStandStill(true);
            if (playlistMode) {
                aktTrack.setStandStill(true);
            } else {
                aktTrack.setStandStill(circlesStopped);
            }
        }

    }//GEN-LAST:event_playlistModeButtonActionPerformed

    private void searchTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyPressed
    }//GEN-LAST:event_searchTextFieldKeyPressed
    /**
     * Bei jedem Tastendruck wird diese Methode aufgerufen.
     * Es werden nur noch Songs angezeigt die im Interpret oder Titel den Eingegebenen String oder Char enthalten.
     * 
     * @param evt 
     */
    private void searchTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyReleased
        String text = searchTextField.getText();

        if (trackCircles.size() > 0) {
            for (TrackGUI aktTrack : trackCircles) {
                if (!aktTrack.getT().toString().toLowerCase().contains(text.toLowerCase())) {
                    aktTrack.setVisible(false);
                } else {
                    aktTrack.setVisible(true);
                }

            }
        }    }//GEN-LAST:event_searchTextFieldKeyReleased

    private void playlistPopUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playlistPopUpActionPerformed
    }//GEN-LAST:event_playlistPopUpActionPerformed

    /**
     * Über den newPlaylistButton wird clearPlaylist aufgerufen, welche die Playlist aufhebt.
     * 
     * @see clearPlaylist 
     * @param evt 
     */
    private void newPlaylistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newPlaylistButtonActionPerformed
        clearPlaylist();
    }//GEN-LAST:event_newPlaylistButtonActionPerformed
    /**
     * Ein JPopUpMenu wird geöffnet, welches die gespeicherten Playlisten anzeigt.
     * 
     * @param evt 
     */
    private void playlistPopUpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playlistPopUpMouseClicked
        PlaylistPopUp menu = new PlaylistPopUp();
        menu.show(evt.getComponent(), evt.getX() - 10, evt.getY() - 120);
    }//GEN-LAST:event_playlistPopUpMouseClicked
    /**
     * Die Visibility des Covers wird true gesetzt wenn sie false ist und umgekehrt.
     * 
     * @param evt 
     */
    private void toggleCoverLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleCoverLabelActionPerformed
        if (coverDisabled) {
            toggleCoverLabel.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/coverDisable_over.gif")));
            toggleCoverLabel.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/coverDisable_pressed.gif")));
            toggleCoverLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/coverDisable_released.gif")));

            coverLabel.setVisible(true);
            coverDisabled = false;
        } else {
            toggleCoverLabel.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/coverEnable_over.gif")));
            toggleCoverLabel.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/coverEnable_pressed.gif")));
            toggleCoverLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/coverEnable_released.gif")));

            coverLabel.setVisible(false);

            coverDisabled = true;
        }
    }//GEN-LAST:event_toggleCoverLabelActionPerformed

    /**
     * Das Programm wird beendet
     * 
     * @param evt 
     */
    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed
    /**
     * Das Programm wird minimiert
     * 
     * @param evt 
     */
    private void minimizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minimizeButtonActionPerformed
        setState(MP3PlayerGUI.ICONIFIED);
    }//GEN-LAST:event_minimizeButtonActionPerformed
    /**
     * Das TutorialFrame wird geöffnet
     * 
     * @param evt 
     */
    private void tutorialButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tutorialButtonActionPerformed
        TutorialFrame tutorialframe = new TutorialFrame();
        tutorialframe.setVisible(true);
    }//GEN-LAST:event_tutorialButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

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

        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MP3PlayerGUI().setVisible(true);

            }
        });

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exitButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton minimizeButton;
    private javax.swing.JButton newPlaylistButton;
    private javax.swing.JButton playlistModeButton;
    private javax.swing.JButton playlistPopUp;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JButton showSongInfosButton;
    private javax.swing.JLabel songAnzeige;
    private javax.swing.JButton stopCircles;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JButton toggleCoverLabel;
    private javax.swing.JButton tutorialButton;
    // End of variables declaration//GEN-END:variables
    private static final int LEFTMOUSEBUTTON = 1024;
    private static final int MAXSPEED = 5;
    private static final int DRAWPANELHEIGHT = 520;
    private static final int SLEEPTIME = 60;
    private static final int COVERSIZE = 250;
    private CopyOnWriteArrayList<TrackGUI> trackCircles;
    private boolean playing = false;
    private boolean pressed = false;
    private boolean popDown = false;
    private boolean coverDisabled = false;
    private boolean circlesStopped = false;
    private boolean showSongInfos = false;
    private boolean playlistMode = false;
    private boolean rightClickMenu = false;
    private DrawPanel drawPanel;
    private JLabel coverLabel;
    private ImageIcon coverImage;
    private PlaylistGUI playlistLines;
    private Point point = new Point();
    private Timer timer = new Timer();
    private MouseGestures mouseGestures = new MouseGestures();
    private int actTrackIndex = -1;

    /**
     * @return the playlistMode
     */
    public boolean isPlaylistMode() {
        return playlistMode;
    }

    /**
     * @param playlistMode the playlistMode to set
     */
    public void setPlaylistMode(boolean playlistMode) {
        this.playlistMode = playlistMode;
    }

    /**
     * MouseListener Klasse
     * 
     * 
     */
    class MyMouseListener extends JPanel implements MouseMotionListener, MouseListener {

        boolean entered = false;
        TrackGUI newCircle;

        public MyMouseListener(TrackGUI newCircle) {
            this.newCircle = newCircle;
            newCircle.addMouseListener(this);
            newCircle.addMouseMotionListener(this);
        }

        public void releasedOnPoint(Point pIn, Point pRel) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            releasedOnPoint(e.getPoint(), null);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (playlistMode) {
                newCircle.getSongInfo().setVisible(true);

            } else {
                newCircle.getSongInfo().setVisible(true);
                newCircle.getSongInfo().setText(newCircle.getT().getSongInfo());
                newCircle.setStandStill(true);
                entered = true;

            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!showSongInfos) {
                newCircle.getSongInfo().setVisible(false);
            }
            if (entered && !circlesStopped && !rightClickMenu) {
                newCircle.setStandStill(false);
            } else {
            }
            entered = false;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (playlistMode) {
                if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
                    if (playlistLines.getPl() == null) {
                        Playlist newPlaylist = new Playlist();
                        player.setActPlaylist(newPlaylist);
                        playlistLines.setPl(newPlaylist);
                        playlistLines.setLayout(null);
                        drawPanel.add(playlistLines, 1, 1);  
                        playlistLines.setLocation(0, 0);
                        playlistLines.setSize(1010, DRAWPANELHEIGHT);    
                        playlistLines.setVisible(true);                        
                        playlistLines.paintComponent(drawPanel.getGraphics());

                    }
                    playlistLines.getPl().addTrack(newCircle.getT());
                    System.out.println(newCircle.getT() + " zur Playlist hinzugefügt");
                    newCircle.setInPlaylist(true);
                    playlistLines.repaint();
                    drawPanel.repaint();
                }
                if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3) {
                    boolean removed = playlistLines.getPl().getPlaylist().remove(newCircle.getT());
                    System.out.println(newCircle.getT() + " aus Playlist entfernt");
                    playlistLines.repaint();
                    drawPanel.repaint();
                }

            } else {
                if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3) {
                    newCircle.setStandStill(true);
                    PopUpDemo menu = new PopUpDemo(newCircle);
                    menu.addPopupMenuListener(new PopupMenuListener() {

                        @Override
                        public void popupMenuWillBecomeVisible(PopupMenuEvent pme) {
                            rightClickMenu = true;

                        }

                        @Override
                        public void popupMenuWillBecomeInvisible(PopupMenuEvent pme) {
                            rightClickMenu = false;

                        }

                        @Override
                        public void popupMenuCanceled(PopupMenuEvent pme) {
                            newCircle.setStandStill(circlesStopped);
                            rightClickMenu = false;

                        }
                    });
                    menu.show(e.getComponent(), e.getX(), e.getY());

                }

                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {

                    if (newCircle.isInPlaylist()) {
                        int i = 0;
                        for (Track actTrack : player.getActPlaylist().getPlaylist()) {
                            if (newCircle.getT().equals(actTrack)) {
                                actTrackIndex = i;
                            }
                            i++;

                        }
                        player.setTrackNo(actTrackIndex);
                        player.setShuffle(false);

                    } else {
                        player.setShuffle(true);
                    }

                    try {
                        player.selectTrackByTrack(newCircle.getT());
                    } catch (JavaLayerException ex) {
                        Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MP3PlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    playing = true;

                }
            }
        }
    }

    /**
     * PopUpMenu für die Playlisten
     * für jede gefundene M3U-Datei wird JMenuItem erstellt
     * und eine Listener hinzugefügt
     *  
     */
    class PlaylistPopUp extends JPopupMenu {

        public PlaylistPopUp() {
            ArrayList<File> lists = searchM3U();
            JMenuItem[] menuItem = new JMenuItem[lists.size()];
            for (int i = 0; i < lists.size(); i++) {
                menuItem[i] = new JMenuItem(lists.get(i).getName().replaceFirst(".m3u", ""));
                menuItem[i].addActionListener(new LoadPlaylistAction(lists.get(i).getAbsolutePath()));
                add(menuItem[i]);
            }

        }
    }

    /**
     * Dieser ActionListener registriert zB. einen Klick auf einen Menueinträg der diesen Listener hinzugefügt hat
     * Der Listener bekommt den String der M3U Datei übergeben und lädt diese über das Repository.
     * Unter allen Tracks werden die gesucht die in der Playlist sind und daraus eine Playlist erstellt.
     * Diese Playlist wird in der PlaylistGUI referenziert.
     * 
     * 
     */
    class LoadPlaylistAction implements ActionListener {

        File m3u;

        public LoadPlaylistAction(String path) {
            this.m3u = new File(path);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            clearPlaylist();

            try {
                ArrayList<String> paths = player.getRepository().loadM3U(m3u);
                Playlist playlist = new Playlist();
                for (TrackGUI aktE : trackCircles) {
                    if (paths.contains(aktE.getT().getMp3Path())) {
                        playlist.addTrack(aktE.getT());
                        aktE.setInPlaylist(true);
                    }
                }
                player.setActPlaylist(playlist);
                playlistLines.setVisible(true);
                playlistLines.setPl(playlist);
                playlistLines.setLayout(null);
                playlistLines.setLocation(0, 0);
                playlistLines.setSize(getWidth(), DRAWPANELHEIGHT);
                drawPanel.add(playlistLines, 1, 0);
                playlistLines.paintComponent(drawPanel.getGraphics());


            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }


        }
    }

    /**
     * Rechtsklick PopUpMenu
     * für Farbänderungen und Lösch-Aktionen
     */
    class PopUpDemo extends JPopupMenu {

        JMenuItem colorChangeItem;
        JMenuItem albumColorChangeItem;
        JMenuItem interpretColorChangeItem;
        JMenuItem genreColorChangeItem;
        JMenuItem deleteTrackItem;
        JMenuItem deleteAlbumItem;
        JMenuItem deleteAllItem;
        TrackGUI newCircle;

        /**
         * Der Konstruktor bekommt die jeweilige TrackGUI des angeklickten Circles übergeben
         * da von dieser die Farbe geändert wird oder sie gelöscht wird.
         * 
         * @param newCircle 
         */
        public PopUpDemo(TrackGUI newCircle) {
            this.newCircle = newCircle;
            colorChangeItem = new JMenuItem(bundle.getString("lang.colorChangeItem"));
            deleteTrackItem = new JMenuItem(bundle.getString("lang.deleteTrackItem"));
            albumColorChangeItem = new JMenuItem(bundle.getString("lang.albumColorChangeItem"));
            interpretColorChangeItem = new JMenuItem(bundle.getString("lang.interpretColorChangeItem"));
            genreColorChangeItem = new JMenuItem(bundle.getString("lang.genreColorChangeItem"));
            deleteAlbumItem = new JMenuItem(bundle.getString("lang.deleteAlbumItem"));
            deleteAllItem = new JMenuItem(bundle.getString("lang.deleteAllItem"));

            colorChangeItem.addActionListener(new OpenColorChooserAction(newCircle));
            deleteTrackItem.addActionListener(new DeleteTrackAction(newCircle));
            albumColorChangeItem.addActionListener(new OpenAlbumColorChooserAction(newCircle));
            interpretColorChangeItem.addActionListener(new OpenInterpretColorChooserAction(newCircle));
            genreColorChangeItem.addActionListener(new OpenGenreColorChooserAction(newCircle));
            deleteAlbumItem.addActionListener(new DeleteAlbumAction(newCircle));
            deleteAllItem.addActionListener(new DeleteAllAction());

            add(colorChangeItem);
            add(albumColorChangeItem);
            add(interpretColorChangeItem);
            add(genreColorChangeItem);
            add(deleteTrackItem);
            add(deleteAlbumItem);
            add(deleteAllItem);

        }
    }

    /**
     * Von allen TrackCircles mit gleichem Interpreten wird die Farbe geändert
     * 
     */
    class OpenInterpretColorChooserAction implements ActionListener {

        TrackGUI newCircle;

        public OpenInterpretColorChooserAction(TrackGUI newCircle) {
            this.newCircle = newCircle;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            Color color = JColorChooser.showDialog(newCircle, "Farbe", null);
            for (TrackGUI aktCircle : trackCircles) {
                if (newCircle.getT().getInterpret().equalsIgnoreCase(aktCircle.getT().getInterpret())) {
                    aktCircle.setRed(color.getRed());
                    aktCircle.setBlue(color.getBlue());
                    aktCircle.setGreen(color.getGreen());
                    aktCircle.repaint();

                }
            }
            newCircle.setStandStill(circlesStopped);

        }
    }

    /**
     * Von allen TrackCircles mit gleichem Genre wird die Farbe geändert
     */
    class OpenGenreColorChooserAction implements ActionListener {

        TrackGUI newCircle;

        public OpenGenreColorChooserAction(TrackGUI newCircle) {
            this.newCircle = newCircle;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            Color color = JColorChooser.showDialog(newCircle, "Farbe", null);
            for (TrackGUI aktCircle : trackCircles) {
                if (newCircle.getT().getGenre().equalsIgnoreCase(aktCircle.getT().getGenre())) {
                    aktCircle.setRed(color.getRed());
                    aktCircle.setBlue(color.getBlue());
                    aktCircle.setGreen(color.getGreen());
                    aktCircle.repaint();
                }
            }
            newCircle.setStandStill(circlesStopped);

        }
    }

    /**
     * Von allen TrackCircles mit gleichem Album wird die Farbe geändert
     * 
     */
    class OpenAlbumColorChooserAction implements ActionListener {

        TrackGUI newCircle;

        public OpenAlbumColorChooserAction(TrackGUI newCircle) {
            this.newCircle = newCircle;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            Color color = JColorChooser.showDialog(newCircle, "Farbe", null);
            for (TrackGUI aktCircle : trackCircles) {
                if (newCircle.getT().getAlbumtitle().equalsIgnoreCase(aktCircle.getT().getAlbumtitle())) {
                    aktCircle.setRed(color.getRed());
                    aktCircle.setBlue(color.getBlue());
                    aktCircle.setGreen(color.getGreen());
                    aktCircle.repaint();
                }
            }
            newCircle.setStandStill(circlesStopped);

        }
    }

    /**
     * Von einem einzelnen Circle kann die Farbe geändert werden
     * 
     */
    class OpenColorChooserAction implements ActionListener {

        TrackGUI newCircle;

        public OpenColorChooserAction(TrackGUI newCircle) {
            this.newCircle = newCircle;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            Color color = JColorChooser.showDialog(newCircle, "Farbe", null);
            if (color != null) {
                newCircle.setRed(color.getRed());
                newCircle.setBlue(color.getBlue());
                newCircle.setGreen(color.getGreen());
            }
            newCircle.repaint();
            newCircle.setStandStill(circlesStopped);

        }
    }

    /**
     * Alle Circles mit gleichem Album werden gelöscht
     * 
     */
    class DeleteAlbumAction implements ActionListener {

        TrackGUI newCircle;

        public DeleteAlbumAction(TrackGUI newCircle) {
            this.newCircle = newCircle;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            for (TrackGUI aktEle : trackCircles) {
                if (newCircle.getT().getAlbumtitle().equals(aktEle.getT().getAlbumtitle())) {
                    player.getRepository().getMp3Collection().remove(aktEle.getT());
                    trackCircles.remove(aktEle);
                    mainPanel.remove(aktEle);
                    aktEle.setVisible(false);
                    aktEle.setCircle(null);
                    aktEle = null;
                }
            }
        }
    }

    /**
     * Alle Tracks werden gelöscht
     * 
     */
    class DeleteAllAction implements ActionListener {

        public DeleteAllAction() {
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            for (TrackGUI aktEle : trackCircles) {
                player.getRepository().getMp3Collection().remove(aktEle.getT());
                trackCircles.remove(aktEle);
                mainPanel.remove(aktEle);
                aktEle.setVisible(false);
                aktEle.setCircle(null);
                aktEle = null;
            }

        }
    }

    /**
     * Der angeklickte Track wird gelöscht
     * 
     */
    class DeleteTrackAction implements ActionListener {

        TrackGUI newCircle;

        public DeleteTrackAction(TrackGUI newCircle) {
            this.newCircle = newCircle;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            player.getRepository().getMp3Collection().remove(newCircle.getT());
            trackCircles.remove(newCircle);
            mainPanel.remove(newCircle);
            newCircle.setVisible(false);
            newCircle.setCircle(null);
            newCircle = null;
        }
    }

    /**
     * Die aktuelle Playlist wird aufgelöst
     * 
     */
    public void clearPlaylist() {
        for (TrackGUI aktE : trackCircles) {
            aktE.setInPlaylist(false);
        }

        player.setActPlaylist(null);
        playlistLines.setPl(null);
        playlistLines.repaint();
    }

    /**
     * Die Methode liefert einen Integer Wert der nicht Null ist und entweder positiv oder negativ ist.
     * 
     * @return Eine zufällige Zahl zwischen der negativen Maximalgeschwindigkeit
     * und der positiven Maximalgeschwindigkeit
     */
    public static int randomDirection() {
        Random random = new Random();
        int ret = random.nextInt(MAXSPEED * 2) - MAXSPEED;

        do {
            ret = random.nextInt(MAXSPEED * 2) - MAXSPEED;
        } while (ret == 0);

        return ret;
    }

    /**
     * Diese Methode sucht rekursiv nach m3u Dateien in Ordnern und Unterordnern
     * 
     * @param files 
     */
    public static void showFiles(File[] files) {
        for (File file : files) {
            if (file.getAbsolutePath().endsWith(".m3u")) {
                if (file.isDirectory()) {
                    showFiles(file.listFiles());


                }
            }
        }
    }

    /**
     * Gibt Pfäde der M3U Dateien zurück aus dem aktuellen Ordner und dessen Unterordnern
     * 
     * @return  Eine Arraylist mit den Strings der Pfäde zu den M3U-Dateien
     */
    public ArrayList searchM3U() {
        ArrayList<File> ret = new ArrayList<File>();
        File[] files = new File(".").listFiles();
        showFiles(files);

        for (File file : files) {
            if (file.getAbsolutePath().endsWith(".m3u")) {
                if (file.isDirectory()) {
                    showFiles(file.listFiles());

                } else {
                    ret.add(file);
                }
            }
        }
        return ret;
    }

    /**
     * Die Methode formatiert den Integerwert der aktuellen Abspielzeit
     * und der Tracklänge in eine Minutenanzeige
     * 
     * @param r Playerobjekt mit der Zeit und Trackinfos
     */
    public void setTime(MP3Player r) {
        int trackLengthV = (r.getActTrack().getLength() / 60);
        int trackLengthN = (r.getActTrack().getLength() % 60);
        int aktZeitV = (r.getActTime() / 60);
        int aktZeitN = (r.getActTime() % 60);
        String trackLength;
        String aktZeit;

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

        if (aktZeitV < 10) {
            if (aktZeitN < 10) {
                aktZeit = "0" + aktZeitV + ":" + "0" + aktZeitN;
            } else {
                aktZeit = "0" + aktZeitV + ":" + aktZeitN;
            }

        } else {
            if (aktZeitN < 10) {
                aktZeit = aktZeitV + ":" + "0" + aktZeitN;
            } else {
                aktZeit = aktZeitV + ":" + aktZeitN;
            }
        }


        timeLabel.setText(aktZeit + " / " + trackLength);

    }
}
