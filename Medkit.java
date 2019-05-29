import java.awt.*;
/**
 * Write a description of class Medkit here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Medkit extends RoundItem
{
    public Medkit(int x, int y)
    {
         super(x, y, 30);
    }
    
    public void draw(Graphics g, int xShift, int yShift)
    {
        int sizeI = getSize()/2 - getSize()/5;
        g.setColor(new Color(0, 0, 0));
        super.draw(g, xShift, yShift);
        Game.drawImage(g, "medkit", getX() - sizeI + xShift, getY() - sizeI + yShift, 2 * sizeI, 2 * sizeI);
    }
}
