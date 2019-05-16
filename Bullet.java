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
    //How far the bullet traveled
    private static final int LENGTH = (int)(75* Game.GAME_SCALE);
    private static final int SPEED = (int)(35 * Game.GAME_SCALE);
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
        
        distance = 0;
        thicknessOfBullet = 3;
    }
    
    public int getDirection()
    {
        return direction;
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
    
    public int doDamage()
    {
        return DAMAGE;
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
    
    public void setX(int x) {
        int diff = x - this.x;
        this.x = x;
        backX += diff;
    }

    public void setY(int y) {
        int diff = y - this.y;
        this.y = y;
        backY += diff;
    }

    public int getBackX()
    {
        return backX;
    }
    
    public int getBackY()
    {
        return backY;
    }
    
    /**
     * Draws the Bullet
     * @param g the graphics
     */
    public void draw(Graphics g, int xShift, int yShift)
    {
        g.setColor(new Color(39, 99, 196, 255));
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(thicknessOfBullet));
        double x1 = ((double)x - backX)/3 * 2 + x;
        double y1 = ((double)y - backY)/3 * 2 + y;
        g2.drawLine(x + xShift, y + yShift, (int)x1 + xShift, (int)y1+ yShift);
        
        g.setColor(new Color(39, 99, 196, 150));
        double x2 = ((double)x - backX)/12 * 3 + x1;
        double y2 = ((double)y - backY)/12 * 3 + y1;
        g2.drawLine((int)x1 + xShift, (int)y1+ yShift, (int)x2 + xShift, (int)y2+ yShift);
        
        g.setColor(new Color(39, 99, 196, 50));
        double x3 = ((double)x - backX)/12 * 1 + x2;
        double y3 = ((double)y - backY)/12 * 1 + y2;
        g2.drawLine((int)x2 + xShift, (int)y2+ yShift, (int)x3 + xShift, (int)y3+ yShift);
        //These sets the alphas of parts of the bullet so it can fade in/out
        
    }
}
