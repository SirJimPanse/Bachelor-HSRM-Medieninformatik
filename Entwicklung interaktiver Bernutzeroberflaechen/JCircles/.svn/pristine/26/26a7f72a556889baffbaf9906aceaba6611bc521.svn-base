package de.hsrm.entintben.jcircles.tutorial;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 *
 * @author Dominik Schuhmann, Tino Landmann, Tobias Lehwalder
 */
public class TutorialFrame extends javax.swing.JFrame {

    public TutorialFrame() {
        initComponents();

        setResizable(false);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (!e.isMetaDown() && e.getButton() == MouseEvent.BUTTON1) {
                    System.out.println(e);
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

        nextButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/next_pressed.gif")));
        nextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/next_over.gif")));



    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        nextButton = new javax.swing.JButton();
        tutorialImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/buttons/next_released.gif"))); // NOI18N
        nextButton.setBorder(null);
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        tutorialImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/tutorial/bedienung.gif"))); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(nextButton)
                    .add(tutorialImage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE))
                .add(29, 29, 29))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(tutorialImage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(nextButton)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Schaltet Tutorial-Bild weiter und schließt sobald kein Bild mehr vorhanden ist.
     * 
     * @param evt 
     */
    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed


        tutorialPage++;
        if (tutorialPage == 0) {
            tutorialImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/tutorial/bedienung.gif")));
        }
        if (tutorialPage == 1) {
            tutorialImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/tutorial/mausgesten.gif")));
        }
        if (tutorialPage == 2) {
            tutorialImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hsrm/entintben/jcircles/tutorial/playlisten.gif")));
        }
        if (tutorialPage == 3) {
            tutorialPage = -1;
            this.dispose();
        }

    }//GEN-LAST:event_nextButtonActionPerformed

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
            java.util.logging.Logger.getLogger(TutorialFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TutorialFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TutorialFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TutorialFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new TutorialFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton nextButton;
    private javax.swing.JLabel tutorialImage;
    // End of variables declaration//GEN-END:variables
    private int tutorialPage = 0;
    private static final int LEFTMOUSEBUTTON = 1024;
    private Point point = new Point();
}
