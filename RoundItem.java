import java.awt.*;
/**
 * Write a description of class RoundItem here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RoundItem extends Item
{
    public RoundItem(int x, int y, int size)
    {
        super(x, y, size);
    }
    
    public void draw(Graphics g, int xShift, int yShift)
    {
        int xLoc = getX();
        int yLoc = getY();
        int radius = getSize();
        int thickness = getSize()/50;
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke((int)(Game.GAME_SCALE*thickness)));
        Game.drawCircle(g2, xLoc + xShift, yLoc + yShift, radius - thickness);
        g2.setColor(new Color(186, 186, 186, 75));
        Game.fillCircle(g2, xLoc + xShift, yLoc + yShift, radius- thickness );
    }

    public void draw(Graphics g, int xShift, int yShift, int si)
    {
        int xLoc = getX();
        int yLoc = getY();
        int radius = si;
        int thickness = si/50;
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke((int)(Game.GAME_SCALE *thickness)));
        Game.drawCircle(g2, xLoc + xShift, yLoc + yShift, radius - thickness);
        g2.setColor(new Color(186, 186, 186, 75));
        Game.fillCircle(g2, xLoc + xShift, yLoc + yShift, radius- thickness );
    }
}
