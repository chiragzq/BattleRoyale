import java.awt.*;
public class HelmetOne extends Armor 
{
    public HelmetOne(int x, int y) {
        super(x, y);
    }

    public void draw(Graphics g, int xShift, int yShift) 
    {
        super.draw(g, xShift, yShift);
        Game.drawImage(g, "helmet1", getX() - getSize()/2 + xShift, getY() - getSize()/2 + yShift, 2 * (-getSize()/5 + getSize()), 2 * (getSize()-getSize()/5));
    }
}