import java.awt.*;
import javax.swing.*;
/**
 * Draws the box
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Box extends Obstacle
{
    private static final int LENGTH = 100;

    public Box(int xl, int yl)
    {
        super(xl, yl, 100);
    }
    
    public int getSize()
    {
        return (int)(LENGTH * Math.sqrt((double)getHealth()/100));
    }
    
    
    public void draw(Graphics g, double scale, int xShift, int yShift)
    {
        Game.drawImageNoScale(g, "box",  (int)(scale  *(getX() - LENGTH/2))  + xShift,  (int)(scale  *(getY() - LENGTH/2))  + yShift,  (int)(scale  *LENGTH),  (int)(scale  *LENGTH));
    }
    
    public void draw(Graphics g, int xShift, int yShift)
    {
        if(getHealth() < 25)
        {
            g.setColor(new Color(99, 91, 61, 200));
            Game.fillCircle(g, getX() + xShift, getY() + yShift, 10);
        }
        else
        {
            Game.drawImage(g, "box", getX() - getSize()/2 + xShift, getY() - getSize()/2 + yShift, getSize(), getSize());
        }
    }
}
