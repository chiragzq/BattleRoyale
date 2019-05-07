import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.Timer;

public class Game extends JComponent implements KeyListener {
    public static final int GAME_WIDTH = 1080;
    public static final int GAME_HEIGHT = 1080;
    public static final double GAME_SCALE = 1;

    public static final int PLAYER_SIZE = (int)(50 * GAME_SCALE);
    public static final int HAND_SIZE = (int)(20 * GAME_SCALE);

    private JFrame frame;
    private List<Player> players;

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
            }
        }, 100);
        players = new ArrayList<Player>();
        players.add(new Player(0, 0));
        players.add(new Player(500, 500));
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

        players.forEach((player) -> player.draw(g));
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

    public static void main(String... args) {
        new Game();
    }  
}