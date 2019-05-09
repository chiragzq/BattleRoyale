
/**
 * Write a description of class Wall here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wall
{
    // instance variables - replace the example below with your own
    private int x;
    private int y;
    private int width;
    private int height;

    /**
     * Constructor for objects of class Wall
     */
    public Wall(int x1, int y1, int width1, int height1)
    {
        // initialise instance variables
        x = x1;
        y = y1;
        width = width1;
        height = height1;
    }

    public boolean isCollision(int x)
    {
        return true;
    }
}
