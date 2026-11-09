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
    
    private int clickX = 0, clickY = 0, dragX = 0, dragY = 0;
    private boolean stretchTop, stretchBottom, stretchRight, stretchLeft;
    private Box toDrag;

    private final Timer animationTimer;
    private static final int FPS = 60; 
    private static final int DELAY = 1000 / FPS;
    private final int cellPixels = 16;

    private Font customFont;
    private BufferedImage customIcon;

    public AnimationPanel() {
        // setPreferredSize(new Dimension(1920, 1080));
        setPreferredSize(new Dimension(1540, 820));

        backgroundImage = scaleImageIcon(new ImageIcon(AnimationWindow.path+"background.png"), 4).getImage();
        Utils.println(new ImageIcon(AnimationWindow.path+"background.png").getIconHeight());

        MouseAdapter a = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clickX = e.getX() / cellPixels;
                clickY = e.getY() / cellPixels;

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
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                dragX = e.getX() / cellPixels;
                dragY = e.getY() / cellPixels;

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
                        if(toDrag.mode == Box.Mode.IMAGE)
                        {
                            int maxX = toDrag.image.getWidth(null);
                            int maxY = toDrag.image.getHeight(null);
                            if(toDrag.height * cellPixels > maxY) { toDrag.height = maxY / cellPixels; toDrag.y = oldy; }
                            if(toDrag.width * cellPixels > maxX) { toDrag.width = maxX / cellPixels; toDrag.x = oldx; }
                        }

                        toDrag.build();
                    }
                }
            }
        };
        addMouseListener(a);
        addMouseMotionListener(a);

        addMouseWheelListener(e -> {
            for (int i = boxes.size() - 1; i >= 0; i--) {
                Box b = boxes.get(i);
                if (b.contains(e.getX(), e.getY())) {
                    Utils.println(b.scrollHeight);
                    int visible = (b.bottom() - b.y + 1) * 16;
                    int max = Math.max(0, b.contentHeight - visible);
                    b.scrollHeight += e.getUnitsToScroll() * 16;
                    b.scrollHeight = Math.max(0, Math.min(max, b.scrollHeight));
                    repaint();
                    break;
                }
            }
        });
    
        boxes = new ArrayList<>(List.of(
                new Box(Box.Mode.TEXTOUT, 5, 5, 80, 25, Box.Border.BLUE, true, false),
                new Box(Box.Mode.EMPTY, 10, 10, 5, 13, Box.Border.NONE, true, true),
                new Box(Box.Mode.IMAGE, 50, 5, 20, 2, Box.Border.BLUE, true, true),
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
            File fontFile = new File("src/adventuregame/asciiart/assets/fonts/KiwiSoda.ttf"); 
            
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

    @Override public void actionPerformed(ActionEvent e) { repaint(); }

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
                    g2d.drawImage(b.tiles[c][r], (c + b.x)*cellPixels, (r + b.y)*cellPixels, this);

            switch(b.mode)
            {
                case TEXTOUT: 
                    b.outText = Utils.getOut();
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(customFont.deriveFont((float)cellPixels));

                    int startX = b.left() + (noborder ? 0 : 1);
                    int currentY = b.top() + 1 + (noborder ? 0 : 1);

                    displaystring: for (String line : b.outText.split("\n"))
                    {
                        int currentX = startX;
                        for (String c : line.split("")) 
                        {
                            if (currentY > b.bottom() + (noborder ? 1 : 0)) break displaystring;

                            if(c.equals("@")) g2d.drawImage(customIcon, currentX * cellPixels, (currentY - 1)*cellPixels, cellPixels, cellPixels, null);
                            else g2d.drawString(c, currentX * cellPixels + (cellPixels - g2d.getFontMetrics().stringWidth(c)) / 2, currentY * cellPixels);
                            currentX++;

                            if (currentX >= b.right() + (noborder ? 1 : 0))
                            {
                                currentX = startX;
                                currentY++;
                            }
                        }
                        currentY++;
                    }
                break;
                case IMAGE:
                    Image i = b.image;
                    int t = (b.top() + (noborder ? 0 : 1)) * cellPixels;
                    int l = (b.left() + (noborder ? 0 : 1)) * cellPixels;
                    int fromX2 = Math.min(b.width * cellPixels, i.getWidth(null));
                    int fromY2 = Math.min(b.height * cellPixels, i.getHeight(null));
                    g2d.drawImage(i, l, t, l + fromX2, t + fromY2, 0, 0, fromX2, fromY2, null);
                break;
                case INPUT: break;
                default: break;
            }
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
        INPUT,
        IMAGE
    }

    public Image[][] tiles;
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
    public Image image = AnimationPanel.scaleImageIcon(new ImageIcon(AnimationWindow.path+"peili.png"), 4).getImage();

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

        build();
    }
    
    int scrollHeight = 0;
    int contentHeight = 0;   // total text height, filled in during painting

    boolean contains(int px, int py) {
        return px >= x * 16 && px < (right() + 1) * 16
            && py >= y * 16 && py < (bottom() + 1) * 16;
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

    public void build()
    {
        if(border != Border.NONE)
        {
            tiles = new Image[width+2][height+2];
            tiles[ 0 ][ 0 ] = tlcorner;
            tiles[ 0 ][height+1] = blcorner;
            tiles[width+1][ 0 ] = trcorner;
            tiles[width+1][height+1] = brcorner;
            for (int i = 1; i < width+1; i++) tiles[i][0] = tside;
            for (int i = 1; i < height+1; i++) tiles[0][i] = lside;
            for (int i = 1; i < height+1; i++) tiles[width+1][i] = rside;
            for (int i = 1; i < width+1; i++) tiles[i][height+1] = bside;
            for (int i = 1; i < width+1; i++) for (int j = 1; j < height+1; j++) tiles[i][j] = center;
        }
        else
        {            
            tiles = new Image[width][height];
            for (int i = 0; i < width; i++) for (int j = 0; j < height; j++) tiles[i][j] = center;
        }
    }
}
