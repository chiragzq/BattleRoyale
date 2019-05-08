import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.awt.MouseInfo;

public class Game extends JComponent implements KeyListener, MouseListener{
    public static final int GAME_WIDTH = 1080;
    public static final int GAME_HEIGHT = 1080;
    public static final double GAME_SCALE = 1;

    public static final int PLAYER_SIZE = (int)(50 * GAME_SCALE);
    public static final int HAND_SIZE = (int)(18 * GAME_SCALE);

    private static int screenLocationX;
    private static int screenLocationY;

    private JFrame frame;
    private List<Player> players;
    private List<Obstacle> obstacles;
    private List<Bullet> bullets;

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
        new Timer().schedule(new TimerTask(){
                @Override
                public void run() {
                    repaint();
                    updateScreenLocation();
                    moveBullets();
                }
            }, 100, 1000/30);

        players = new ArrayList<Player>();
        players.add(new Player(500, 500));
        bullets = new ArrayList<Bullet>();
        obstacles = new ArrayList<Obstacle>();
        obstacles.add(new Stone(200, 200));
        obstacles.add(new Tree(400, 400));
        this.addMouseListener(this);
    }

    public void mouseClicked(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
        bullets.add(players.get(0).getGun().fire());
    }

    public void mouseReleased(MouseEvent e)
    {
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // if (listener == null)
        //     return;
        // int code = e.getKeyCode();
        // if (code == KeyEvent.VK_LEFT)
        //     listener.leftPressed();
        // else if (code == KeyEvent.VK_RIGHT)
        //     listener.rightPressed();
        // else if (code == KeyEvent.VK_DOWN)
        //     listener.downPressed();
        // else if (code == KeyEvent.VK_UP)
        //     listener.upPressed();
        // else if (code == KeyEvent.VK_SPACE)
        //     listener.spacePressed();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(new Color(0x7DAE58));
        g.fillRect(0, 0, getWidth(), getHeight());
        if(bullets.size() != 0)
        {
            bullets.forEach((bullet) -> bullet.draw(g));
        }
        //Draws the Bullets

        players.forEach((player) -> player.draw(g));
        // Draws the player with guns

        obstacles.forEach((obstacle)-> obstacle.draw(g));
        //Draw the obstacle

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

    public void moveBullets()
    {
        bullets.forEach((bullet) -> bullet.move());
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