import java.awt.Graphics;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.awt.MouseInfo;
import java.awt.Image;

public class Game extends JComponent implements KeyListener, MouseListener {
    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 720;
    public static final double GAME_SCALE = 1;
    public static final int FRAME_RATE = 30;

    public static final int PLAYER_SIZE = (int)(50 * GAME_SCALE);
    public static final int HAND_SIZE = (int)(18 * GAME_SCALE);

    private static int screenLocationX;
    private static int screenLocationY;
    
    private static final int MIN_X = 0;
    private static final int MIN_Y = 0;
    private static final int MAX_X = 2000;
    private static final int MAX_Y = 2000;
    

    private JFrame frame;
    private Map<Integer, Player> players;
    private Player thisPlayer;
    
    private Map<Integer, Obstacle> obstacles;
    private Map<Integer, Bullet> bullets;
    
    private Map<Integer, Item> items;
    
    private Network network;
    private ReadWriteLock lock;
    private int ping;

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
                            (int)(GAME_WIDTH * GAME_SCALE), (int)(GAME_HEIGHT * GAME_SCALE)
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
        
        network = new Network("http://localhost:5000", this, lock);
        //network = new Network("http://apcs-survivio.herokuapp.com", this, lock);
        //network = new Network("https://chiragzq-survivio.dev.harker.org", this, lock);

        new Timer().schedule(new TimerTask(){
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
                }
            }, 100, 1000/FRAME_RATE);
    }

    public void setPing(int millis) {
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
    }
    

    @Override
    public void paintComponent(Graphics g) {
        lock.readLock().lock();

        g.setColor(new Color(0x7DAE58));
        g.fillRect(0, 0, getWidth(), getHeight());
        int xShift = Game.GAME_WIDTH/2;
        int yShift = Game.GAME_HEIGHT/2;
        if(thisPlayer != null)
        {
            xShift -= thisPlayer.getX();
            yShift -= thisPlayer.getY();
            drawBoundary(g);
        }
        

        for(Obstacle ob: obstacles.values())
        {
            if(ob.getHealth() < 25)
                ob.draw(g, xShift, yShift);
        }
        //Draws death states
        
        //Draw the obstacle
        
        for(Bullet bullet : bullets.values())
            bullet.draw(g, xShift, yShift);
       
        for(Obstacle ob: obstacles.values())
        {
            if(ob instanceof Stone && ob.getHealth() >= 25)
                ob.draw(g, xShift, yShift);
        }

        for(Item item: items.values())
            item.draw(g, xShift, yShift);

        for(Player player : players.values()) {
            player.drawGun(g, xShift, yShift);
            player.draw(g, xShift, yShift);
            player.drawHands(g, xShift, yShift);
        }
        
        for(Obstacle ob: obstacles.values())
        {
            if(!(ob instanceof Stone) && ob.getHealth() >= 25)
                ob.draw(g, xShift, yShift);
        }
        
        

        if(thisPlayer != null) {
            thisPlayer.drawEssentials(g);
            //thisPlayer.drawDeath(g);
        }

        lock.readLock().unlock();

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", 20, 15));
        g.drawString("Ping: " + ping, 0, 20);  
    }
    
    public void drawBoundary(Graphics g)
    {
        int xShift = Game.GAME_WIDTH/2 - thisPlayer.getX();
        int yShift = Game.GAME_HEIGHT/2 - thisPlayer.getY();
        
        
        int width = MAX_X - MIN_X;
        int height = MAX_Y - MIN_Y;
        int minX = MIN_X + xShift;
        int maxX = MAX_X + xShift;
        int minY = MIN_Y + yShift;
        int maxY = MAX_Y + yShift;
        
        
        
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
        g.fillOval(x, y, width, height);
    }

    public static void drawCircle(Graphics g, int x, int y, int r) {
        x = x - r / 2;
        y = y - r / 2;
        g.drawOval(x, y, r, r);
    }
    public static void drawImage(Graphics g, String file, int xImage, int yImage, int iWidth, int iHeight)
    {
        Image image = new ImageIcon("img/" + file + ".png").getImage();
        g.drawImage(image, xImage, yImage, iWidth, iHeight, null);
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