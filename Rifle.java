import java.awt.*;
/**
 * Write a description of class Rifle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Rifle extends Gun
{
    // instance variables - replace the example below with your own
    public Rifle(Player play)
    {
        super(play, 30, 10);
    }
    
    public void draw(Graphics g)
    {
        g.setColor(new Color(0, 0, 0));
        Graphics2D g2 = (Graphics2D) g;
        int x = getPlayer().getX();
        int y = getPlayer().getY();
        
        g2.setStroke(new BasicStroke(10));
        g2.drawLine(x, y, (int)(Math.cos(getDirection() * Math.PI / 180)*getBarrelLength()) + x, (int)(Math.sin(getDirection() * Math.PI / 180)*getBarrelLength()) + y);
        
    }
    
    
    
    /**
     * Location of Right hand
     * @param directionRad the direction
     * @return dir of right
     */
    public double getRightDir(double directionRad)
    {
        return directionRad + Math.PI / 23;
    }
    
    /**
     * Location of Left hand
     * @param directionRad the direction
     * @return dir of left
     */
    public double getLeftDir(double directionRad)
    {
        return directionRad + Math.PI / 33;
    }
    
    /**
     * How far to extend right hand
     * @return the pixels
     */
    public int extendRight()
    {
        return 0;
    }
    
    /**
     * How far to extend left hand
     * @return the pixels
     */
    public int extendLeft()
    {
        return 30;
    }
}
