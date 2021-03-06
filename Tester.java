import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
/**
 * This is the tester copied over from Solitare for easy of testing
 * @author Aaron Lo
 * @version 11-7-18
 */
public class Tester extends JComponent implements MouseListener
{
    private static final int CARD_WIDTH = 73;
    private static final int CARD_HEIGHT = 97;
    private static final int SPACING = 5;  //distance between cards
    private static final int FACE_UP_OFFSET = 15;  //distance for cascading face-up cards
    private static final int FACE_DOWN_OFFSET = 5;  //distance for cascading face-down cards
    private JFrame frame;
    private ArrayList<Bullet> items;
    private ArrayList<Obstacle> ob;
    private Player player;
    private ArrayList<Item> ite;
    private RedZone redZone = new RedZone(100, 100, 50);
    /**
     * The constructor for SolitaireDisplay
     * @param game the game of Solitaire
     */
    public Tester()
    {
        

        frame = new JFrame("Solitaire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);

        this.setPreferredSize(new Dimension(CARD_WIDTH * 8 + SPACING * 9, 
                CARD_HEIGHT * 2 + SPACING * 3+ FACE_DOWN_OFFSET * 7 + 13 * FACE_UP_OFFSET));
        this.addMouseListener(this);

        frame.pack();
        frame.setVisible(true);
        
        items = new ArrayList<Bullet>();
        items.add(new BlackBullet(100, 100, 40));
        ob = new ArrayList<Obstacle>();
        ob.add(new Barrel(100, 100));
        ob.get(0).setHealth(0);

        player = new Player(100, 100, 100, 100);
        
        ite = new ArrayList<Item>();
        ite.add(new DroppedSniper(300, 300));
        ite.add(new DroppedPistol(400, 400));
        
        ite.add(new Bandage(500, 500));
        ite.add(new Scope2(600, 600));
    }
    
    /**
     * Paints the display
     * @param g the graphics
     */
    public void paintComponent(Graphics g)
    {
        //background
        Image image = new ImageIcon("img/scope2.png").getImage();
         g.drawImage(image, 400, 400, 30, 30, null);
        ite.get(3).draw(g, 0, 0);
        redZone.draw(g, 0, 0);
//         for(int i = 0; i < items.size(); i++)
//         {
//             items.get(i).draw(g, 0, 0);
//         }
//         
// //         for(Obstacle obs : ob)
// //         {
// //             obs.draw(g, 0 ,0);
// //         }
//         
//         for(Item ob: ite)
//         {
//             ob.draw(g, 0, 0);
//         }
//         player.drawWeaponSelections(g);
//         player.drawPack(g);
    }
   

    public void mouseExited(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
    }

    public void mouseClicked(MouseEvent e)
    {
    }

}