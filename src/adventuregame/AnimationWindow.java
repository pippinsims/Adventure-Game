package adventuregame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AnimationWindow extends JFrame {

    public static final String path = "C:/Users/kuhns/Documents/gimp/export/laur/";

    public AnimationWindow() {
        setTitle("Moving Image Animation");
        setUndecorated(true);
        setResizable(false);
        
        AnimationPanel animationPanel = new AnimationPanel();
        add(animationPanel);
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class AnimationPanel extends JPanel implements ActionListener {
    
    private final Image backgroundImage;
    private final Image tside;
    private final Image bside;
    private final Image rside;
    private final Image lside;
    private final Image tlcorner;
    private final Image trcorner;
    private final Image brcorner;
    private final Image blcorner;

    private Image cur1;

    private int clickX = 0;
    private int clickY = 0;

    private final Timer animationTimer;
    private static final int FPS = 60; 
    private static final int DELAY = 1000 / FPS;

    public AnimationPanel() {
        setPreferredSize(new Dimension(4*400, 4*225));

        backgroundImage = scaleImageIcon(new ImageIcon(AnimationWindow.path+"background.png"), 4).getImage();
        tlcorner = scaleImageIcon(new ImageIcon(AnimationWindow.path+"corner.png"), 4).getImage();
        trcorner = scaleImageIcon(new ImageIcon(AnimationWindow.path+"corner.png"), 4).getImage();
        blcorner = scaleImageIcon(new ImageIcon(AnimationWindow.path+"corner.png"), 4).getImage();
        brcorner = scaleImageIcon(new ImageIcon(AnimationWindow.path+"corner.png"), 4).getImage();
        tside = scaleImageIcon(new ImageIcon(AnimationWindow.path+"side.png"), 4).getImage();
        bside = scaleImageIcon(new ImageIcon(AnimationWindow.path+"side.png"), 4).getImage();
        lside = scaleImageIcon(new ImageIcon(AnimationWindow.path+"side.png"), 4).getImage();
        rside = scaleImageIcon(new ImageIcon(AnimationWindow.path+"side.png"), 4).getImage();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickX = (int)Math.round(e.getX() / 4.0) * 4;
                clickY = (int)Math.round(e.getY() / 4.0) * 4;
                cur1 = tside;
            }
        });

        animationTimer = new Timer(DELAY, this);
        animationTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundImage != null) g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        if (cur1 != null)
        { 
            int w = cur1.getWidth(null);
            int h = cur1.getHeight(null);
            int x = (int)Math.round((clickX-w/2) / 4.0) * 4;
            int y = (int)Math.round((clickY-h/2) / 4.0) * 4;
            g2d.drawImage(cur1, x, y, w, h, this);
        }
    }

    public static ImageIcon scaleImageIcon(ImageIcon originalIcon, int scaleFactor) {
        return new ImageIcon(originalIcon.getImage().getScaledInstance(
            originalIcon.getIconWidth() * scaleFactor, 
            originalIcon.getIconHeight() * scaleFactor, 
            Image.SCALE_SMOOTH
        ));
    }
}

