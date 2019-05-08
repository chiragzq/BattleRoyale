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
    private Map<Integer, Player> players;
    private Player thisPlayer;

    private Network network;

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
        }, 0, 1000/60);

        players = new HashMap<Integer, Player>();

        network = new Network("http://localhost:3000", this);
    }

    public void setPlayer(Player player, int id) {
        players.put(id, player);
    }

    public Player getPlayer() {
        return thisPlayer;
    }

    public Map<Integer, Player> getPlayers() {
        return players;
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
        g.setColor(new Color(0x7DAE58));
        g.fillRect(0, 0, getWidth(), getHeight());

        players.values().forEach((player) -> player.draw(g));
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