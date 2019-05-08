import java.awt.*;
/**
 * An Obstacle
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Obstacle
{
    // instance variables - replace the example below with your own
    private int x;
    private int y;
    
    private int health;

    /**
     * Constructor for objects of class Stone
     * @param col the col
     * @param row the row
     */
    public Obstacle(int col, int row, int health2)
    {
        // initialise instance variables
        x = col;
        y = row;
        health = health2;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
   
    public void setHealth(int add)
    {
        health += add;
    }
    
    public int getHealth()
    {
        return health;
    }

    public abstract void draw(Graphics g);
}
