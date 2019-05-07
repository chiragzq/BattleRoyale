import java.util.*;
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
    private int direction;
    private int distance;
    //How far the bullet goes
    private static final int SPEED = 10;
    private static final int DAMAGE = 10;
    
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
        
        distance = 10;
    }
    
    /**
     * Moves the Bullet
     * @return false if the bullet should not be moving anymore; true if it can
     */
    public boolean move()
    {
        if(direction == 0)
            return false;
        y += Math.sin(Math.toRadians(direction)) * SPEED;
        x += Math.cos(Math.toRadians(direction)) * SPEED;
        direction--;
        return true;
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
}
