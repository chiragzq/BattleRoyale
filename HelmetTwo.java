import java.awt.*;
public class HelmetTwo extends Armor 
{
    public HelmetTwo(int x, int y) {
        super(x, y);
    }

    public void draw(Graphics g, int xShift, int yShift) 
    {
        super.draw(g, xShift, yShift);
        Game.drawImage(g, "helmet2", getX() - getSize()/2 + xShift, getY() - getSize()/2 + yShift, 2 * (-getSize()/5 + getSize()), 2 * (getSize()-getSize()/5));
    }
}