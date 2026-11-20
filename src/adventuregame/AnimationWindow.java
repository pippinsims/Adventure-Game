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
    
    private int clickCellX = 0, clickCellY = 0, dragCellX = 0, dragCellY = 0;
    private boolean stretchTop, stretchBottom, stretchRight, stretchLeft;
    private Box toDrag;

    private final Timer animationTimer;
    private static final int FPS = 60; 
    private static final int DELAY = 1000 / FPS;
    public static final int cellPixels = 4;
    public static final int scaleFactor = 4;

    private Font customFont;
    private BufferedImage customIcon;

    public AnimationPanel() {
        setPreferredSize(new Dimension(1540, 820));

        backgroundImage = new ImageIcon(AnimationWindow.path+"background.png").getImage();

        MouseAdapter a = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clickCellX = e.getX() / scaleFactor / cellPixels;
                clickCellY = e.getY() / scaleFactor / cellPixels;

                toDrag = null;
                stretchTop = stretchBottom = stretchRight = stretchLeft = false;
                for(Box box : boxes.reversed()) if(box.draggable || box.stretchable)
                {
                    if(clickCellX == box.left()) stretchLeft = true;
                    else if(clickCellX == box.right()) stretchRight = true;
                    
                    if(clickCellY == box.top()) stretchTop = true;
                    else if(clickCellY == box.bottom()) stretchBottom = true;
                    
                    if(clickCellX == box.left() && box.top() <= clickCellY && clickCellY <= box.bottom() || 
                       clickCellX == box.right() && box.top() <= clickCellY && clickCellY <= box.bottom() ||
                       clickCellY == box.top() && box.left() <= clickCellX && clickCellX <= box.right() ||
                       clickCellY == box.bottom() && box.left() <= clickCellX && clickCellX <= box.right()) 
                    {
                        toDrag = box;
                        break;
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                dragCellX = e.getX() / scaleFactor / cellPixels;
                dragCellY = e.getY() / scaleFactor / cellPixels;

                if(toDrag != null) 
                {
                    if(stretchLeft && stretchTop && toDrag.draggable)
                    {
                        toDrag.x = dragCellX;
                        toDrag.y = dragCellY;
                    }
                    else if(toDrag.stretchable)
                    {
                        int oldx = toDrag.x, oldy = toDrag.y;
                        if(toDrag.draggable)
                        {
                            if(stretchLeft)
                            {
                                toDrag.width += toDrag.x - dragCellX; //TODO sus if you slam it closed from the left
                                toDrag.x = dragCellX;
                            }
                            
                            if(stretchTop)
                            {
                                toDrag.height += toDrag.y - dragCellY;
                                toDrag.y = dragCellY;
                            }    
                        }
                        if(stretchRight) toDrag.width += dragCellX - toDrag.right();
                        if(stretchBottom) toDrag.height += dragCellY - toDrag.bottom();  
                        
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
            for(Box b : boxes) if (b.contains(e.getX() / scaleFactor, e.getY() / scaleFactor)) {
                b.changeStartLine(e.getUnitsToScroll());
                repaint();
                break;
            }
        });
    
        //bounds 95x50
        boxes = new ArrayList<>(List.of(
                new Box(Box.Mode.TEXTOUT, 25, 5, 80, 25, Box.Border.NONE, true, false),
                new Box(Box.Mode.TEXTOUT, 5, 5, 80, 25, Box.Border.BLUE, true, false),
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
            File fontFile = new File("src/adventuregame/asciiart/assets/fonts/KiwiSoa.ttf"); 
            
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            customFont = customFont.deriveFont(Font.PLAIN, 28f);
            
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            customFont = new Font("Courier New", Font.BOLD, 28);
        }

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
        int w = (int) Math.ceil(getWidth() / (double) scaleFactor);
        int h = (int) Math.ceil(getHeight() / (double) scaleFactor);
        BufferedImage buf = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buf.createGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

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
                        g2d.setColor(Color.WHITE);
                        g2d.setFont(customFont.deriveFont((float)cellPixels));

                        int startX = b.left() + (noborder ? 0 : 1);
                        int currentY = b.top() + 1 + (noborder ? 0 : 1);

                    b.updateText(Utils.getOut());
                    String[] lines = b.getTextLines();
                    displaystring: for (int i = b.getStartLine(); i < lines.length; i++)
                    {
                        String line = lines[i];
                        int currentX = startX*2;
                        for (String c : line.split("")) 
                        {
                            if (currentY > b.bottom() + (noborder ? 1 : 0) - 1) break displaystring;

                            if(c.equals("@")) 
                            { 
                                g2d.drawImage(customIcon, currentX * cellPixels/2 + cellPixels/4, (currentY - 1)*cellPixels, cellPixels, cellPixels, null);
                                currentX++; 
                            }
                            else 
                            {
                                int gy = currentY * cellPixels;
                                g2d.setPaint(new LinearGradientPaint(0, gy - cellPixels, 0, gy,
                                    new float[]{0f, 0.5f, 1f},
                                    new Color[]{
                                        new Color(255, 240, 150), 
                                        new Color(218, 165, 32), 
                                        new Color(255, 240, 150)//new Color(139, 90, 0)
                                    }
                                ));
                                g2d.drawString(c, currentX * cellPixels/2 + (cellPixels - g2d.getFontMetrics().stringWidth(c)) / 2, gy);
                            }
                                // g2d.drawString(c, currentX * cellPixels/2 + (cellPixels - g2d.getFontMetrics().stringWidth(c)) / 2, currentY * cellPixels);
                            currentX++;
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
        } finally {
            g2d.dispose();
        }
        Graphics2D scaled = (Graphics2D) g;
        scaled.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        scaled.drawImage(buf, 0, 0, (int) (w * scaleFactor), (int) (h * scaleFactor), null);
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
    public final Mode mode;
    public int x, y, width, height;
    public Image[][] tiles;
    public Border border;
    public Image image = new ImageIcon(AnimationWindow.path+"peili.png").getImage();
    private String outText = "Heyo, buddy!eeeeeeeeeeee\nnice to@@sight@thee there.";
    private int startLine = -1;

    public int left() { return x; }
    public int top() { return y; }
    public int right() { return border == Border.NONE? x + width - 1 : x + width + 1; }
    public int bottom() { return border == Border.NONE? y + height - 1 : y + height + 1; }

    public void updateText(String newtext) { outText = newtext; }
    public String[] getTextLines() { return Utils.splitLines(outText.split("\n"), width*2-1); }
    public int getStartLine() { return startLine == -1 ? maxStartLine() : startLine; }
    public void changeStartLine(int toAdd) {
        startLine = getStartLine() + toAdd;
        if(startLine >= maxStartLine()) startLine = -1;
        else if(startLine < 0) startLine = 0;
    }
    private int maxStartLine() { return Math.max(getTextLines().length - height + 1, 0); } //plus 1 because of that extra - 1 for margin when printing
    
    public Box(Mode mode, int x, int y, int width, int height, Border border, boolean draggable, boolean stretchable)
    {
        ImageIcon c = new ImageIcon(AnimationWindow.path+"corner.png");
        tlcorner = c.getImage();
        trcorner = transformIcon(c, Dir.X).getImage();
        blcorner = transformIcon(c, Dir.Y).getImage();
        brcorner = transformIcon(transformIcon(c, Dir.X), Dir.Y).getImage();
        ImageIcon s = new ImageIcon(AnimationWindow.path+"side.png");
        tside = s.getImage();
        rside = rotateIconRight(s).getImage();
        bside = rotateIconRight(rotateIconRight(s)).getImage();
        lside = rotateIconRight(rotateIconRight(rotateIconRight(s))).getImage();
        center = new ImageIcon(AnimationWindow.path+"center.png").getImage();
        
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

    boolean contains(int x, int y) {
        int p = AnimationPanel.cellPixels;
        return x >= left()   * p && 
               x <= right()  * p && 
               y >= top()    * p && 
               y <= bottom() * p;
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
