import java.util.*;
import java.awt.*;
/**
 * A Bullet the Player Fires
 * 
 * @author Aaron Lo
 * @version 5-6-19
 */
public class Bullet
{
    private int x;
    private int y;
    private int backX;
    private int backY;
    private int direction;
    private int distance;
    //How far the bullet goes
    private static final int LENGTH = 75;
    private static final int SPEED = 35;
    private static final int DAMAGE = 10;
    private int thicknessOfBullet;
    
    /**
     * Bullet constructor
     * @param xLoc x location
     * @param yLoc y loc
     * @param direc the direction
     */
    public Bullet(int xLoc, int yLoc, int direc)
    {
        x = xLoc;
        y = yLoc;
        direction = direc;
        findTheBackEndOfTheBullet();
        
        distance = 100;
        thicknessOfBullet = 4;
    }
    
    /**
     * Moves the Bullet
     * @return false if the bullet should not be moving anymore; true if it can
     */
    public boolean move()
    {
        if(distance == 0)
            return false;
        y += Math.sin(Math.toRadians(direction)) * SPEED;
        x += Math.cos(Math.toRadians(direction)) * SPEED;
        distance--;
        findTheBackEndOfTheBullet();
        return true;
    }
    
    
    /**
     * This gives the back end of the bullet
     */
    public void findTheBackEndOfTheBullet()
    {
        backY = y + (int)(Math.sin(Math.toRadians(direction)) * LENGTH);
        backX = x + (int)(Math.cos(Math.toRadians(direction)) * LENGTH);
    }
    
    
    /**
     * This gets the x
     * @return the x Loc
     */
    public int getX()
    {
        return x;
    }
    
    /**
     * This gets the y location
     * @return the y Loc
     */
    public int getY()
    {
        return y;
    }
    
    /**
     * Draws the Bullet
     * @param g the graphics
     */
    public void draw(Graphics g)
    {
        g.setColor(new Color(39, 99, 196, 255));
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(thicknessOfBullet));
        double x1 = ((double)x - backX)/3 * 2 + x;
        double y1 = ((double)y - backY)/3 * 2 + y;
        g2.drawLine(x, y, (int)x1, (int)y1);
        
        g.setColor(new Color(39, 99, 196, 150));
        double x2 = ((double)x - backX)/12 * 3 + x1;
        double y2 = ((double)y - backY)/12 * 3 + y1;
        g2.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
        
        g.setColor(new Color(39, 99, 196, 50));
        double x3 = ((double)x - backX)/12 * 1 + x2;
        double y3 = ((double)y - backY)/12 * 1 + y2;
        g2.drawLine((int)x2, (int)y2, (int)x3, (int)y3);
        //These sets the alphas of parts of the bullet so it can fade in/out
        
    }
}
