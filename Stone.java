import java.awt.*;
/**
 * Write a description of class Stone here.
 * 
 * @author Aaron Lo
 * @version 5-8-19
 */
public class Stone extends Obstacle
{
    // instance variables - replace the example below with your own
    private static final int SIZE = 60;
    private static final int BORDER = 5;
    private static final int HEALTH = 100;

    /**
     * Constructor for objects of class Stone
     * @param col the col
     * @param row the row
     */
    public Stone(int col, int row)
    {
        // initialise instance variables
        super(col, row, HEALTH);
    }

    
    public void draw(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(5));
        g2.setColor(new Color(0, 0, 0));
        
        Game.drawCircle(g2, getX(), getY(), (int)(SIZE * Math.sqrt((double)getHealth()/100)));
        
        g.setColor(new Color(168, 168, 168));
        Game.fillCircle(g, getX(), getY(), (int)(SIZE * Math.sqrt((double)getHealth()/100)));
        
    }
}
