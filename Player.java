
/**
 * The Player the person controls
 * @author Aaron Lo
 * @version 5-6-19
 */
public class Player
{
    private int x;
    private int y;
    private int health;
    /**
     * Constructs Player at location(x, y)
     * @param xLoc the x location
     * @param yLoc the y location
     */
    public Player(int xLoc, int yLoc)
    {
        x = xLoc;
        y = yLoc;
        //The location
        
        health = 100;
        //The health of the player
    }
    
    /**
     * The file name of Player
     */
    public String getFileName()
    {
        return "player.gif";
    }
    
    /**
     * Moves in the x-axis direction
     * @param xDirection how far the player moves in the xDirection
     */
    public void xMove(int xDirection)
    {
        x += xDirection;
    }
    
    /**
     * Moves in the y-axis direction
     * @param yDirection how far the player moves in the yDirection
     */
    public void yMove(int yDirection)
    {
        y += yDirection;
    }
    
    /**
     * This returns the x loc
     * @return the x location
     */
    public int getX()
    {
        return x;
    }
    
    /**
     * This returns the y loc
     * @return the y location
     */
    public int getY()
    {
        return y;
    }
}
