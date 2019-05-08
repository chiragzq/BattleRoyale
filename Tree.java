import java.awt.*;
/**
 * Write a description of class Tree here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tree extends Obstacle
{
    private static final int SIZE = 60;
    private static final int LEAVE_SIZE = 200;
    private static final int BORDER = 5;
    private static final int HEALTH = 100;

    /**
     * Constructor for objects of class Tree
     */
    public Tree(int x2, int y2)
    {
        super(x2, y2, HEALTH);
    }
    
    public int getSize()
    {
        return (int)(SIZE * Math.sqrt((double)getHealth()/100));
    }

    public void draw(Graphics g)
    {
        
        Graphics2D g2 = (Graphics2D)g;
        
        g2.setColor(new Color(17, 99, 43, 150));
        Game.fillCircle(g2, getX(), getY(), (int)(LEAVE_SIZE * Math.sqrt((double)getHealth()/100)));
        g2.setStroke(new BasicStroke(5));
        
        
        g2.setColor(new Color(0, 0, 0));
        
        Game.drawCircle(g2, getX(), getY(), (int)(SIZE * Math.sqrt((double)getHealth()/100)));
        
        g.setColor(new Color(124, 91, 91));
        Game.fillCircle(g, getX(), getY(), (int)(SIZE * Math.sqrt((double)getHealth()/100)));
    }
}
