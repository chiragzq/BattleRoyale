import java.awt.*;
/**
 * Write a description of class Bandage here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bandage extends RoundItem
{
    public Bandage(int x, int y)
    {
        super(x, y, 15);
    }
    
    public void draw(Graphics g, int xShift, int yShift)
    {
        int sizeI = getSize();
        g.setColor(new Color(0, 0, 0));
        super.draw(g, xShift, yShift);
        Game.drawImage(g, "bandage", getX() - sizeI, getY() - sizeI, 2 * sizeI, 2 * sizeI);
    }
}
