package adventuregame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AnimationWindow extends JFrame {

    public static final String path = "src/adventuregame/asciiart/assets/";

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

    private int clickX = 0, clickY = 0, dragX = 0, dragY = 0;
    private boolean stretchTop, stretchBottom, stretchRight, stretchLeft;
    private Box toDrag;

    private final Timer animationTimer;
    private static final int FPS = 60; 
    private static final int DELAY = 1000 / FPS;

    private Font customFont;
    private BufferedImage customIcon;

    public AnimationPanel() {
        // setPreferredSize(new Dimension(1920, 1080));
        setPreferredSize(new Dimension(1540, 820));

        backgroundImage = scaleImageIcon(new ImageIcon(AnimationWindow.path+"background.png"), 4).getImage();
        System.out.println(new ImageIcon(AnimationWindow.path+"background.png").getIconHeight());

        MouseAdapter a = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clickX = e.getX() / 20;
                clickY = e.getY() / 20;

                toDrag = null;
                stretchTop = stretchBottom = stretchRight = stretchLeft = false;
                for(Box box : boxes.reversed()) if(box.draggable || box.stretchable)
                {
                    if(clickX == box.left()) stretchLeft = true;
                    else if(clickX == box.right()) stretchRight = true;
                    
                    if(clickY == box.top()) stretchTop = true;
                    else if(clickY == box.bottom()) stretchBottom = true;
                    
                    if(clickX == box.left() && box.top() <= clickY && clickY <= box.bottom() || 
                       clickX == box.right() && box.top() <= clickY && clickY <= box.bottom() ||
                       clickY == box.top() && box.left() <= clickX && clickX <= box.right() ||
                       clickY == box.bottom() && box.left() <= clickX && clickX <= box.right()) 
                    {
                        toDrag = box;
                        break;
                    }
                }
                
                cur1 = i2;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                dragX = e.getX() / 20;
                dragY = e.getY() / 20;

                if(toDrag != null) 
                {
                    if(stretchLeft && stretchTop && toDrag.draggable)
                    {
                        toDrag.x = dragX;
                        toDrag.y = dragY;
                    }
                    else if(toDrag.stretchable)
                    {
                        int oldx = toDrag.x, oldy = toDrag.y;
                        if(toDrag.draggable)
                        {
                            if(stretchLeft)
                            {
                                toDrag.width += toDrag.x - dragX;
                                toDrag.x = dragX;
                            }
                            
                            if(stretchTop)
                            {
                                toDrag.height += toDrag.y - dragY;
                                toDrag.y = dragY;
                            }    
                        }
                        if(stretchRight) toDrag.width += dragX - toDrag.right();
                        if(stretchBottom) toDrag.height += dragY - toDrag.bottom();  
                        
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
                new Box(Box.Mode.TEXTOUT, 5, 5, 7, 9, Box.Border.BLUE, true, true),
                new Box(Box.Mode.EMPTY, 10, 10, 5, 13, Box.Border.NONE, true, true),
                new Box(Box.Mode.TEXTOUT, 10, 10, 5, 13, Box.Border.NONE, true, true),
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
        
        try {
            File fontFile = new File("src/resources/my-custom-font.ttf"); 
            
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            customFont = customFont.deriveFont(Font.PLAIN, 28f);
            
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            customFont = new Font("Serif", Font.PLAIN, 28);
        }

        // try { customIcon = ImageIO.read(new File(AnimationWindow.path+"peili.png")); } 
        try { customIcon = ImageIO.read(new File("src/resources/icon.png")); } 
        catch (IOException e) {
            customIcon = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = customIcon.createGraphics();
            g.setColor(Color.YELLOW);
            g.fillOval(0, 0, 24, 24);
            g.dispose();
        }

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
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (backgroundImage != null) g2d.drawImage(backgroundImage, 0, 0, this);
        for (Box b : boxes)
        {
            boolean noborder = b.border == Box.Border.NONE;
            
            for (int c = 0; c < b.tiles.length; c++)
                for (int r = 0; r < b.tiles[0].length; r++)
                    g2d.drawImage(b.tiles[c][r].getImage(), (c + b.x)*20, (r + b.y)*20, this);

            if(b.mode == Box.Mode.TEXTOUT)
            {
                g2d.setColor(Color.WHITE);
                g2d.setFont(customFont);
                FontMetrics metrics = g2d.getFontMetrics(customFont);

                //int lineHeight = metrics.getHeight();
                //int iconSize = metrics.getAscent();
                int charSpace = 20;
                int startX = (b.x)*20;
                int currentY = (b.y+1)*20;
                if(!noborder)
                {
                    startX += 20;
                    currentY += 20;
                }

                for (String line : b.outText.split("\n")) 
                {
                    int currentX = startX;
                    for(char c : line.toCharArray()) 
                    {
                        if(currentY > (b.bottom() + 1 - (noborder ? 0 : 1)) * 20) break;
                        if (c == '@') g2d.drawImage(customIcon, currentX, currentY - charSpace, charSpace, charSpace, null);
                        else {
                            String s = String.valueOf(c);
                            g2d.drawString(s, currentX + (charSpace - metrics.stringWidth(s)) / 2, currentY);
                        }
                        
                        currentX += charSpace;
                        if(currentX > (b.right() - (noborder ? 0 : 1)) * 20)
                        {
                            currentY += charSpace *2;
                            currentX = startX;
                        }
                    }
                    currentY += charSpace * 2;
                }
            }
        }
        
        if (cur1 != null)
        { 
            int w = cur1.getWidth(null);
            int h = cur1.getHeight(null);
            g2d.drawImage(cur1, clickX*20, clickY*20, w, h, this);
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
        EMPTY,
        TEXTOUT,
        TEXTIN,
        IMAGE,
        BUTTON
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
    public final Mode mode;
    public String outText = "Heyo, buddy!eeeeeeeeeeee\nnice to@@sight@thee there.";

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
        this.mode = mode;

        update();
    }
    
    public static BufferedImage imageIconToBufferedImage(ImageIcon i) {
        BufferedImage b = new BufferedImage(i.getIconWidth(), i.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = b.createGraphics();        
        g.drawImage(i.getImage(), 0, 0, null);
        g.dispose();
        return b;
    }

    public static ImageIcon scaleImage(ImageIcon i, double scaleX, double scaleY) {
        if (scaleX == 0 || scaleY == 0) throw new IllegalArgumentException("Scale factors must be non-zero");
        BufferedImage b = imageIconToBufferedImage(i);
        AffineTransform t = new AffineTransform();
        t.scale(scaleX, scaleY);
        if (scaleX < 0) t.translate(-b.getWidth(), 0);
        if (scaleY < 0) t.translate(0, -b.getHeight());
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
