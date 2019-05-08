import java.awt.*;
/**
 * Write a description of class Pistol here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Pistol extends Gun
{
    private static final int BARREL_LENGTH = 55;
    public Pistol(Player play)
    {
        super(play, 30, 20);
    }

    public void draw(Graphics g)
    {
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0));
        int x = getPlayer().getX();
        int y = getPlayer().getY();
        int x2 = Game.getMouseX();
        int y2 = Game.getMouseY();
        
        double t = Math.atan2(y2 - y, x2 - x);
        
        g2.setStroke(new BasicStroke(10));
        g2.drawLine(x, y, (int)(Math.cos(t)*BARREL_LENGTH) + x, (int)(Math.sin(t)*BARREL_LENGTH) + y);
    }
    
    /**
     * Location of Right hand
     * @param directionRad the direction
     * @return dir of right
     */
    public double getRightDir(double directionRad)
    {
        return directionRad;
    }
    
    /**
     * Location of Left hand
     * @param directionRad the direction
     * @return dir of left
     */
    public double getLeftDir(double directionRad)
    {
        return directionRad;
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
        return 0;
    }
}
