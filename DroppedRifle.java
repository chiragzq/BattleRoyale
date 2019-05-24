import java.awt.*;
/**
 * Write a description of class DroppedRifle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DroppedRifle extends DroppedGun
{
    public DroppedRifle(int x, int y)
    {
        super(x, y);
    }
    
    public void draw(Graphics g, int xShift, int yShift)
    {
        int sizeI = getSize();
        int sizeNew = sizeI - sizeI/10;
        double multiple = 2.3;
        g.setColor(new Color(32, 91, 181));
        super.draw(g, xShift, yShift);
        Game.drawImage(g, "rifle", getX() - sizeNew/2 + sizeNew/25 + xShift, getY() - sizeNew/2 + sizeNew/18 + yShift, (int)((2*sizeNew + sizeNew/5)/multiple),(int)( (2*sizeNew)/multiple));
    }
}
