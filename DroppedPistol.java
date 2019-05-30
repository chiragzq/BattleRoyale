import java.awt.*;
/**
 * Write a description of class DroppedPistol here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DroppedPistol extends DroppedGun
{
    public DroppedPistol(int x, int y)
    {
        super(x, y);
    }
    
    public void draw(Graphics g, int xShift, int yShift)
    {
        int sizeI = getSize();
        int sizeNew = sizeI - sizeI/10;
        double multiple = 2.3;
        double mul = 2.4;
        g.setColor(new Color(14, 64, 145));
        super.draw(g, xShift, yShift);
        Game.drawImage(g, "pistol", getX() - sizeNew/2 - sizeNew/7 + sizeNew/5+ xShift, getY() - sizeNew/2 + sizeNew/3 + yShift, (int)((2*sizeNew + sizeNew/5)/mul),(int)( (2*sizeNew)/multiple/mul));
    }
}
