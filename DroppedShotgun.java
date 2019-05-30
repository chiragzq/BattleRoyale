import java.awt.*;
/**
 * Write a description of class DroppedShotgun here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DroppedShotgun extends DroppedGun
{
    public DroppedShotgun(int x, int y)
    {
        super(x, y);
    }
    
    public void draw(Graphics g, int xShift, int yShift)
    {
        int sizeI = getSize();
        int sizeNew = sizeI - sizeI/10;
        double multiple = 2.3;
        double mul = 1.7;
        g.setColor(new Color(168, 16, 16));
        super.draw(g, xShift, yShift);
        Game.drawImage(g, "shotgun", getX() - sizeNew/2 - sizeNew/7 + xShift, getY() - sizeNew/2 + sizeNew/4 + yShift, (int)((2*sizeNew + sizeNew/5)/mul),(int)( (2*sizeNew)/multiple/mul));
    }
}
