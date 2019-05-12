import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.awt.MouseInfo;

public class Game extends JComponent implements KeyListener, MouseListener {
    public static final int GAME_WIDTH = 1080;
    public static final int GAME_HEIGHT = 1080;
    public static final double GAME_SCALE = 0.5;
    public static final int FRAME_RATE = 30;

    public static final int PLAYER_SIZE = (int)(50 * GAME_SCALE);
    public static final int HAND_SIZE = (int)(18 * GAME_SCALE);

    private static int screenLocationX;
    private static int screenLocationY;

    private JFrame frame;
    private Map<Integer, Player> players;
    private Player thisPlayer;
    
    private List<Obstacle> obstacles;
    private Map<Integer, Bullet> bullets;
    
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

                    frame.pack();
                    frame.setVisible(true);
                    frame.setTitle("Battle Royale");
                }
            });

        lock = new ReentrantReadWriteLock();

        obstacles = new ArrayList<Obstacle>();
        obstacles.add(new Stone(200, 200));
        obstacles.add(new Tree(400, 400));
        obstacles.add(new Bush(300, 300));
        
        this.addMouseListener(this);
        players = new HashMap<Integer, Player>();
        bullets = new HashMap<Integer, Bullet>();

        network = new Network("http://localhost:3000", this, lock);

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
    }

    @Override
    public void paintComponent(Graphics g) {
        lock.readLock().lock();
        
        g.setColor(new Color(0x7DAE58));
        g.fillRect(0, 0, getWidth(), getHeight());

        //Draw the obstacle
        bullets.values().forEach((bullet) -> bullet.draw(g));
        
        players.values().forEach((player) -> player.draw(g));
        
        obstacles.forEach((obstacle) -> obstacle.draw(g));

        players.values().forEach((player) -> {
            player.drawHands(g); 
            player.drawWeaponSelections(g);
        });

        g.setColor(Color.RED);
        g.drawString("Ping: " + ping, 0, 20);

        lock.readLock().unlock();
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