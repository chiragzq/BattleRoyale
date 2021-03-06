import java.awt.*;
/**
 * A Sniper gun that is dropped
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DroppedSniper extends DroppedGun
{
    public DroppedSniper(int x1, int y1)
    {
        super(x1, y1);
    }
    
    public void draw(Graphics g, int xShift, int yShift)
    {
        int sizeI = getSize();
        int sizeNew = sizeI - sizeI/10;
        double multiple = 3.2;
        g.setColor(new Color(168, 16, 16));
        super.draw(g, xShift, yShift);
        Game.drawImage(g, "sniper", getX() - sizeNew/2 + sizeNew/40 - sizeNew/23 + xShift, getY() - sizeNew/2 + sizeNew/5 + yShift, (int)((2*sizeNew + sizeNew * 1.5)/multiple),(int)( (2*sizeNew)/multiple));
    }
}
