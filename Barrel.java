import java.awt.*;
/**
 * Write a description of class Barrel here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Barrel extends Obstacle
{
    private static final int SIZE = 60;
    public Barrel(int x, int y)
    {
        super(x, y, 100);
    }
    
    public int getSize()
    {
        return (int)(SIZE * Math.sqrt((double)getHealth()/100));
    }
    
    public void draw(Graphics g, int xShift, int yShift)
    {
        g.setColor(new Color(109, 112, 117));
        Game.fillCircle(g, getX()  + xShift, getY() + yShift, getSize());
    }
}
