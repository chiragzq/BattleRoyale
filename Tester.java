import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
/**
 * The display for Solitarie
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
    private ArrayList<Item> items;
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
        
        items = new ArrayList<Item>();
        items.add(new Ammo(100, 100));
    }
    
    /**
     * Paints the display
     * @param g the graphics
     */
    public void paintComponent(Graphics g)
    {
        //background
        //g.setColor(new Color(0, 128, 0));
        //g.fillRect(0, 0, getWidth(), getHeight());
        
        for(int i = 0; i < items.size(); i++)
        {
            items.get(i).draw(g);
        }
        
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