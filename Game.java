
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.awt.MouseInfo;
import java.awt.Image;

/**
 * The client class
 */
public class Game extends JComponent implements KeyListener, MouseListener {
    public enum State {
        CONNECTING, CONNECT_FAILURE, WAITING, PLAYING, DEAD
    }
    public enum Scope {
        X1(1), X2(0.9), X4(0.6), X8(0.4), X15(0.2), X31(0.1);

        private double scale;

        private Scope(double scale) {
            this.scale = scale;
        }

        public double getScale() {
            return scale;
        }
        
    }

    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 720;
    public static double GAME_SCALE = Scope.X1.getScale();
    public static final int FRAME_RATE = 30;

    public static final int PLAYER_SIZE = (int)(50);
    public static final int HAND_SIZE = (int)(18);

    private static Map<String, Image> images;

    private static int screenLocationX;
    private static int screenLocationY;
    
    private static final int MIN_X = 0;
    private static final int MIN_Y = 0;
    private static final int MAX_X = 4000;
    private static final int MAX_Y = 4000;

    private JFrame frame;
    private Map<Integer, Player> players;
    private Player thisPlayer;
    
    private Map<Integer, Obstacle> obstacles;
    private Map<Integer, Bullet> bullets;
    
    private Map<Integer, Item> items;

    
    private Network network;
    private ReadWriteLock lock;
    private long ping;

    public State gameState;

    public Game() {
        final Game self = this;
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.getContentPane().add(self);
                    frame.addKeyListener(self);

                    //Display the window.
                    self.setPreferredSize(new Dimension(
                            (int)(GAME_WIDTH), (int)(GAME_HEIGHT)
                        ));
                    frame.setFocusable(true);
                    frame.pack();
                    frame.setVisible(true);
                    frame.setTitle("Battle Royale");
                }
            });

        lock = new ReentrantReadWriteLock();
        this.addMouseListener(this);
        
        obstacles = new HashMap<Integer, Obstacle>();
        players = new HashMap<Integer, Player>();
        bullets = new HashMap<Integer, Bullet>();
        items = new HashMap<Integer, Item>();
        images = new HashMap<String, Image>();

        gameState = State.CONNECTING;
        
        //network = new Network("http://localhost:5000", this, lock);
        //network = new Network("http://apcs-survivio.herokuapp.com/", this, lock);
        network = new Network("https://chiragzq-survivio.dev.harker.org", this, lock);
       
        
        new Timer().scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run() {
                    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                    if(thisPlayer != null) {
                        thisPlayer.faceCursor();
                        if(thisPlayer.getEquippedGun() != null) {
                            thisPlayer.getEquippedGun().faceCursor();
                        }
                    }
                    network.mouseLocation(Game.getMouseX(), Game.getMouseY());
                    updateScreenLocation();
                    repaint();
                    network.ping();
                }
            }, 100, 1000/FRAME_RATE);
    }

    public void setPing(long millis) {
        this.ping = millis;
    }

    public void setPlayer(Player player, int id) {
        players.put(id, player);
        thisPlayer = player;
    }

    public Player getPlayer() {
        return thisPlayer;
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public Map<Integer, Bullet> getBullets() {
        return bullets;
    }

    public Map<Integer, Obstacle> getObstacles() {
        return obstacles;
    }

    public Map<Integer, Item> getItems() {
        return items;
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {} 
    public void mouseExited(MouseEvent e) {} 
    public void mouseReleased(MouseEvent e){}

    @Override
    public void mousePressed(MouseEvent e) {
        int y = e.getY();
        int x = e.getX();
        int thickness  =60;
        int between = 20;
        int height = 30;
        int start = Game.GAME_HEIGHT/4;
        if(x <= Game.GAME_WIDTH && x >= Game.GAME_WIDTH - thickness)
        {
            if(y >= start + 2 * height /3&& y <= start + height + between) {
                //thisPlayer.setBandages(0);
                network.useBandages();
            }
            else if(y >= start + 2 * height& y <= start + 3 * height)
            {
                network.useMedkit();
            }
        }
        else
            network.click();    
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W)
            network.wReleased();
        else if (code == KeyEvent.VK_A)
            network.aReleased();
        else if (code == KeyEvent.VK_S)
            network.sReleased();
        else if (code == KeyEvent.VK_D)
            network.dReleased();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W)
            network.wPressed();
        else if (code == KeyEvent.VK_A)
            network.aPressed();
        else if (code == KeyEvent.VK_S)
            network.sPressed();
        else if (code == KeyEvent.VK_D)
            network.dPressed();
        else if (code == KeyEvent.VK_1)
            network.num1();
        else if (code == KeyEvent.VK_2)
            network.num2();
        else if (code == KeyEvent.VK_3)
            network.num3();
        else if (code == KeyEvent.VK_R)
            network.rPressed();
        else if (code == KeyEvent.VK_F)
            network.fPressed();
        else if (code == KeyEvent.VK_Z) 
            network.click();
    }
    

    @Override
    public void paintComponent(Graphics g) {
        long startTime = System.currentTimeMillis();
        if(gameState == State.CONNECTING) {
            FontMetrics metrics = g.getFontMetrics();
            g.setColor(new Color(168,168, 168, 100));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            String text = "Connecting to server...";
            g.drawString(text, Game.GAME_WIDTH/2 - metrics.stringWidth(text), Game.GAME_HEIGHT/2 - Game.GAME_HEIGHT/20);
        } else if(gameState == State.CONNECT_FAILURE) {
            FontMetrics metrics = g.getFontMetrics();
            g.setColor(new Color(168,168, 168, 100));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            String text = "Failed to connect to server";
            g.drawString(text, Game.GAME_WIDTH/2 - metrics.stringWidth(text), Game.GAME_HEIGHT/2 - Game.GAME_HEIGHT/20);        
        } else if(gameState == State.WAITING) {
            FontMetrics metrics = g.getFontMetrics();
            g.setColor(new Color(168,168, 168, 100));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            String text = "Waiting for game to start...";
            g.drawString(text, Game.GAME_WIDTH/2 - metrics.stringWidth(text), Game.GAME_HEIGHT/2 - Game.GAME_HEIGHT/20); 
        } else if(gameState == State.PLAYING) {
            lock.readLock().lock();

            g.setColor(new Color(0x7DAE58));
            g.fillRect(0, 0, getWidth(), getHeight());
            int xShift = (int)(Game.GAME_WIDTH/2 / Game.GAME_SCALE) ;
            int yShift = (int)(1/Game.GAME_SCALE * Game.GAME_HEIGHT/2);
            xShift -= thisPlayer.getX();//(int)(thisPlayer.getX() * Game.GAME_SCALE);
            yShift -= thisPlayer.getY();//(int)(thisPlayer.getY() * Game.GAME_SCALE);
            drawBoundary(g, xShift, yShift);

            Collection<Obstacle> lObstacles = obstacles.values();

            for(Obstacle ob: lObstacles)
            {
                if(ob.getHealth() < 25 )
                    ob.draw(g, xShift, yShift);
            }
            for(Bullet bullet : bullets.values())
                bullet.draw(g, xShift, yShift);
        
            for(Obstacle ob: lObstacles)
            {
                if((ob instanceof Stone || ob instanceof Box|| ob instanceof Barrel) && ob.getHealth() >= 25)
                    ob.draw(g, xShift, yShift);
            }
            for(Item item : items.values()) {
                item.draw(g, xShift, yShift);
            }
            for(Player player : players.values()) {
                player.drawGun(g, xShift, yShift);
                player.draw(g, xShift, yShift);
                player.drawHands(g, xShift, yShift);
            }
            for(Obstacle ob: lObstacles)
            {
                if(ob.getHealth() >= 25 && !(ob instanceof Stone || ob instanceof Box|| ob instanceof Barrel))
                   ob.draw(g, xShift, yShift);
            }
            thisPlayer.drawEssentials(g);

            lock.readLock().unlock();

            g.setColor(Color.RED);
            g.setFont(new Font("Arial", 20, 15));
            g.drawString("Ping: " + ping, 0, 20);  
        } else if(gameState == State.DEAD) {
            FontMetrics metrics = g.getFontMetrics();
            g.setColor(new Color(168,168, 168, 100));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            String text = "You Died";
            g.drawString(text, Game.GAME_WIDTH/2 - metrics.stringWidth(text), Game.GAME_HEIGHT/2 - Game.GAME_HEIGHT/20);
        }
        //System.out.println("Draw loop took " + (System.currentTimeMillis() - startTime) + " ms");
    }   

    public Player getThisPlayer()
    {
        return thisPlayer;
    }
    
    public void drawBoundary(Graphics g, int xShift, int yShift)
    {
        int width = (int)(Game.GAME_SCALE * (MAX_X - MIN_X));
        int height = (int)(Game.GAME_SCALE * (MAX_Y - MIN_Y));
        int minX = (int)(Game.GAME_SCALE * (MIN_X + xShift));
        int maxX = (int)(Game.GAME_SCALE * (MAX_X + xShift));
        int minY = (int)(Game.GAME_SCALE * (MIN_Y + yShift));
        int maxY = (int)(Game.GAME_SCALE * (MAX_Y + yShift));
        
        
        
        g.setColor(new Color(88, 91, 86, 100));
        
        
        g.fillRect(minX, maxY - 2*height,width, height);
        //Top
        
        g.fillRect(minX - width, maxY - 2*height, width, 2 * height);
        //Left
        
        g.fillRect(minX - width, maxY, 2 * width, height);
        //Bottom
        
        g.fillRect(minX + width, maxY -2*height, width, height* 3);
        //Right
    }

    public static void fillCircle(Graphics g, int x, int y, int r) {
        fillOval(g, x, y, r, r);
    }

    public static void fillOval(Graphics g, int x, int y, int width, int height) {
        x = x - width / 2;
        y = y - height / 2;
        g.fillOval((int)(Game.GAME_SCALE * x), (int)(Game.GAME_SCALE * y), (int)(Game.GAME_SCALE * width), (int)(Game.GAME_SCALE * height));
    }

    public static void drawCircle(Graphics g, int x, int y, int r) {
        x = x - r / 2;
        y = y - r / 2;
        g.drawOval((int)(Game.GAME_SCALE * x), (int)(Game.GAME_SCALE * y), (int)(Game.GAME_SCALE * r), (int)(Game.GAME_SCALE * r));
    }
    public static void drawImage(Graphics g, String file, int xImage, int yImage, int iWidth, int iHeight)
    {
        if(images != null && !images.containsKey(file)) 
            images.put(file, new ImageIcon(Game.class.getResource("img/" + file + ".png")).getImage());
        g.drawImage(images.get(file), (int)(Game.GAME_SCALE * xImage), (int)(Game.GAME_SCALE * yImage), (int)(Game.GAME_SCALE * iWidth), (int)(Game.GAME_SCALE * iHeight), null);
    }

    public static void drawImageNoScale(Graphics g, String file, int xImage, int yImage, int iWidth, int iHeight) {
        if(images != null && !images.containsKey(file)) 
            images.put(file, new ImageIcon(Game.class.getResource("img/" + file + ".png")).getImage());
        g.drawImage(images.get(file), xImage,  yImage, iWidth,  iHeight, null);
    }

    public static void drawLine(Graphics g, int x1, int y1, int x2, int y2) {
        g.drawLine((int)(Game.GAME_SCALE * x1), (int)(Game.GAME_SCALE * y1), (int)(Game.GAME_SCALE * x2), (int)(Game.GAME_SCALE * y2));
    }

    public void updateScreenLocation()
    {
        try
        {
            screenLocationX = (int) getLocationOnScreen().getX();
            screenLocationY = (int) getLocationOnScreen().getY();
        }
        catch(java.awt.IllegalComponentStateException e)
        {
        }
    }

    public static int getMouseX()
    {
        return (int)MouseInfo.getPointerInfo().getLocation().getX() - screenLocationX;
    }

    public static int getMouseY()
    {   
        return (int)MouseInfo.getPointerInfo().getLocation().getY() - screenLocationY;
    }

    public static void main(String... args) {
        new Game();
    }
}
