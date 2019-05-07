
/**
 * A Bullet the Player Fires
 * 
 * @author Aaron Lo
 * @version 5-6-19
 */
public class Bullet
{
    // instance variables - replace the example below with your own
    private int x;
    private int y;
    private int direction;
    private final int SPEED = 10;
    private final int DISTANCE = 10;
    
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
    }
}
