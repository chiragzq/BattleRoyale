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
    private static final int SIZE = (int)(60 * Game.GAME_SCALE);
    private static final int BORDER = (int)(5* Game.GAME_SCALE);
    private static final int HEALTH = 100;
    private static final int SIZE_WHEN_DEAD = (int)(20* Game.GAME_SCALE);

    /**
     * Constructor for objects of class Stone
     * @param col the col
     * @param row the row
     */
    public Stone(int x, int y)
    {
        // initialise instance variables
        super(x, y, HEALTH);
    }
    
    public int getSize()
    {
        return (int)(SIZE * Math.sqrt((double)getHealth()/100));
    }

    
    public void draw(Graphics g, int xShift, int yShift)
    {
        if(getHealth() < 25)
        {
            Graphics2D g2 = (Graphics2D)g;

            g2.setColor(new Color(78, 91, 83, 75));
            Game.fillCircle(g2, getX(), getY(), SIZE_WHEN_DEAD);
        }
        else
        {
            Graphics2D g2 = (Graphics2D)g;
            g2.setStroke(new BasicStroke(5));
            g2.setColor(new Color(0, 0, 0));

            Game.drawCircle(g2, getX() + xShift, getY() + yShift, getSize());

            g.setColor(new Color(168, 168, 168));
            Game.fillCircle(g, getX() + xShift, getY() + yShift, getSize());

        }
    }
}
