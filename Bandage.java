import java.awt.*;
/**
 * Draws the bandage image
 * 
 * @author Aaron Lo
 */
public class Bandage extends RoundItem
{
    public Bandage(int x, int y)
    {
        super(x, y, 30);
    }
    
    public void draw(Graphics g, int xShift, int yShift)
    {
        int sizeI = getSize()/2 - getSize()/5; 
        g.setColor(new Color(0, 0, 0));
        super.draw(g, xShift, yShift);
        Game.drawImage(g, "bandage", getX() - sizeI + xShift, getY() - sizeI + yShift, 2 * sizeI, 2 * sizeI);
    }
}
