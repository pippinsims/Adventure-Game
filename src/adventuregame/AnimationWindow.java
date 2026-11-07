package adventuregame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.List;

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

    private ArrayList<Box> boxes;
    
    private final Image i1 = scaleImageIcon(new ImageIcon(AnimationWindow.path+"corner.png"), 4).getImage();
    private final Image i2 = scaleImageIcon(new ImageIcon(AnimationWindow.path+"side.png"), 4).getImage();

    private Image cur1 = i1;

    private int clickx = 0, clicky = 0, dragx = 0, dragy = 0;
    private boolean stretchTop,stretchBottom,stretchRight,stretchLeft;
    private Box toDrag;

    private final Timer animationTimer;
    private static final int FPS = 60; 
    private static final int DELAY = 1000 / FPS;

    public AnimationPanel() {
        setPreferredSize(new Dimension(4*400, 4*225));

        backgroundImage = scaleImageIcon(new ImageIcon(AnimationWindow.path+"background.png"), 4).getImage();

        MouseAdapter a = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clickx = e.getX() / 20;
                clicky = e.getY() / 20;

                toDrag = null;
                stretchTop = stretchBottom = stretchRight = stretchLeft = false;
                for(Box box : boxes.reversed()) if(box.draggable || box.stretchable)
                {
                    if(clickx == box.left()) stretchLeft = true;
                    else if(clickx == box.right()) stretchRight = true;
                    
                    if(clicky == box.top()) stretchTop = true;
                    else if(clicky == box.bottom()) stretchBottom = true;
                    
                    if(clickx == box.left() && box.top() <= clicky && clicky <= box.bottom() || 
                       clickx == box.right() && box.top() <= clicky && clicky <= box.bottom() ||
                       clicky == box.top() && box.left() <= clickx && clickx <= box.right() ||
                       clicky == box.bottom() && box.left() <= clickx && clickx <= box.right()) 
                    {
                        toDrag = box;
                        break;
                    }
                }
                
                cur1 = i2;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                dragx = e.getX() / 20;
                dragy = e.getY() / 20;
                System.err.println(dragx+","+dragy+":"+toDrag);

                if(toDrag != null) 
                {
                    if(stretchLeft && stretchTop && toDrag.draggable)
                    {
                        toDrag.x = dragx;
                        toDrag.y = dragy;
                    }
                    else if(toDrag.stretchable)
                    {
                        int oldx = toDrag.x, oldy = toDrag.y;
                        if(toDrag.draggable)
                        {
                            if(stretchLeft)
                            {
                                toDrag.width += toDrag.x - dragx;
                                toDrag.x = dragx;
                            }
                            
                            if(stretchTop)
                            {
                                toDrag.height += toDrag.y - dragy;
                                toDrag.y = dragy;
                            }    
                        }
                        if(stretchRight) toDrag.width += dragx - toDrag.right();
                        if(stretchBottom) toDrag.height += dragy - toDrag.bottom();  
                        
                        int min = toDrag.border == Box.Border.NONE ? 3 : 1;
                        if(toDrag.height < min) { toDrag.height = min; toDrag.y = oldy; }
                        if(toDrag.width < min) { toDrag.width = min; toDrag.x = oldx; }

                        toDrag.update();
                    }
                }
            }
        };
        addMouseListener(a);
        addMouseMotionListener(a);
    
        boxes = new ArrayList<>(List.of(
            new Box(Box.Mode.EMPTY, 5, 5, 7, 9, Box.Border.BLUE, true, true),
            new Box(Box.Mode.EMPTY, 10, 10, 5, 13, Box.Border.NONE, true, true),
            new Box(Box.Mode.EMPTY, 50, 5, 20, 2, Box.Border.BLUE, true, true),
            new Box(Box.Mode.EMPTY, 0, 0, 2, 20, Box.Border.BLUE, true, true)
        ));

        // printer = Box(mode=textout,x,y,w,h,border=blue)
        // input = Box(mode=textin,x,y,w,h,border=null)
        // charview = Box(mode=image,xywh)
        // inv = Box(mode=textout)
        // invbutton = Box(button)
        // mapb = box(button)
        // map=box(textout)

        animationTimer = new Timer(DELAY, this);
        animationTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 
        // printer.update
        // charview.update
        // inv.update
        // map.border=brown
        // map.update
        // 
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundImage != null) g2d.drawImage(backgroundImage, 0, 0, this);
        for (Box b : boxes)
            for (int c = 0; c < b.tiles.length; c++)
                for (int r = 0; r < b.tiles[0].length; r++)
                    g2d.drawImage(b.tiles[c][r].getImage(), (c + b.x)*20, (r + b.y)*20, this);
        if (cur1 != null)
        { 
            int w = cur1.getWidth(null);
            int h = cur1.getHeight(null);
            g2d.drawImage(cur1, clickx*20, clicky*20, w, h, this);
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

class Box {
    enum Border {
        NONE,
        BLUE
    }
    enum Mode {
        EMPTY
    }

    public ImageIcon[][] tiles;
    public final Image tside;
    public final Image bside;
    public final Image rside;
    public final Image lside;
    public final Image tlcorner;
    public final Image trcorner;
    public final Image brcorner;
    public final Image blcorner;
    public final Image center;
    public final boolean stretchable;
    public final boolean draggable;
    public int x, y, width, height;
    public Border border;
    private Mode m;

    public int left() { return x; }
    public int top() { return y; }
    public int right() { return border == Border.NONE? x + width - 1 : x + width + 1; }
    public int bottom() { return border == Border.NONE? y + height - 1 : y + height + 1; }

    public Box(Mode mode, int x, int y, int width, int height, Border border, boolean draggable, boolean stretchable)
    {
        ImageIcon c = AnimationPanel.scaleImageIcon(new ImageIcon(AnimationWindow.path+"corner.png"), 4);
        tlcorner = c.getImage();
        trcorner = transformIcon(c, Dir.X).getImage();
        blcorner = transformIcon(c, Dir.Y).getImage();
        brcorner = transformIcon(transformIcon(c, Dir.X), Dir.Y).getImage();
        ImageIcon s = AnimationPanel.scaleImageIcon(new ImageIcon(AnimationWindow.path+"side.png"), 4);
        tside = s.getImage();
        rside = rotateIconRight(s).getImage();
        bside = rotateIconRight(rotateIconRight(s)).getImage();
        lside = rotateIconRight(rotateIconRight(rotateIconRight(s))).getImage();
        center = AnimationPanel.scaleImageIcon(new ImageIcon(AnimationWindow.path+"center.png"), 4).getImage();
        
        this.draggable = draggable;
        this.stretchable = stretchable;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.border = border;
        this.m = mode;

        update();
    }
    
    public static BufferedImage imageIconToBufferedImage(ImageIcon i) {
        BufferedImage b = new BufferedImage(i.getIconWidth(), i.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = b.createGraphics();        
        g.drawImage(i.getImage(), 0, 0, null);
        g.dispose();
        return b;
    }

    public static ImageIcon scaleImage(ImageIcon i, double scalex, double scaley) {
        if (scalex == 0 || scaley == 0) throw new IllegalArgumentException("Scale factors must be non-zero");
        BufferedImage b = imageIconToBufferedImage(i);
        AffineTransform t = new AffineTransform();
        t.scale(scalex, scaley);
        if (scalex < 0) t.translate(-b.getWidth(), 0);
        if (scaley < 0) t.translate(0, -b.getHeight());
        return new ImageIcon(new AffineTransformOp(t, AffineTransformOp.TYPE_BILINEAR).filter(b, null));
    }

    private enum Dir{X,Y}
    public static ImageIcon transformIcon(ImageIcon sourceIcon, Dir d) {
        switch(d)
        {
            case X: return scaleImage(sourceIcon, -1, 1);
            case Y: return scaleImage(sourceIcon, 1, -1);
            default: return null;
        }
    }

    public static ImageIcon rotateIconRight(ImageIcon i) {
        BufferedImage b = imageIconToBufferedImage(i);
        AffineTransform t = AffineTransform.getTranslateInstance(b.getHeight(), 0);
        t.quadrantRotate(1);
        return new ImageIcon(new AffineTransformOp(t, AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(b, null));
    }

    public void update()
    {
        if(border != Border.NONE)
        {
            tiles = new ImageIcon[width+2][height+2];
            tiles[ 0 ][ 0 ] = new ImageIcon(tlcorner);
            tiles[ 0 ][height+1] = new ImageIcon(blcorner);
            tiles[width+1][ 0 ] = new ImageIcon(trcorner);
            tiles[width+1][height+1] = new ImageIcon(brcorner);
            for (int i = 1; i < width+1; i++) tiles[i][0] = new ImageIcon(tside);
            for (int i = 1; i < height+1; i++) tiles[0][i] = new ImageIcon(lside);
            for (int i = 1; i < height+1; i++) tiles[width+1][i] = new ImageIcon(rside);
            for (int i = 1; i < width+1; i++) tiles[i][height+1] = new ImageIcon(bside);
            for (int i = 1; i < width+1; i++) for (int j = 1; j < height+1; j++) tiles[i][j] = new ImageIcon(center);
        }
        else
        {            
            tiles = new ImageIcon[width][height];
            for (int i = 0; i < width; i++) for (int j = 0; j < height; j++) tiles[i][j] = new ImageIcon(center);
        }

        // for(int a = 0; a < w+2; a++){
        //     for(int d = 0; d < h+2; d++) System.out.print(tiles[a][d] == null ? "n" : ".");
        //         System.out.println();}
    }
}