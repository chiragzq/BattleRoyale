import java.awt.*;
/**
 * Write a description of class DroppedGun here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DroppedGun extends Item
{
    public DroppedGun(int x, int y)
    {
        super(x, y, 65);
    }
    
    public void draw(Graphics g, int xShift, int yShift)
    {
        int xLoc = getX();
        int yLoc = getY();
        int radius = getSize();
        int thickness = getSize()/50;
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(thickness));
        Game.drawCircle(g2, xLoc + xShift, yLoc + yShift, radius - thickness);
        g2.setColor(new Color(186, 186, 186, 75));
        Game.fillCircle(g2, xLoc + xShift, yLoc + yShift, radius- thickness );
        
    }
    
}