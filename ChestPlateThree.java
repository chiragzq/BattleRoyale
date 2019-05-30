import java.awt.*;

public class ChestPlateThree extends Armor
{
    public ChestPlateThree(int x, int y)
    {
        super(x, y);
    }

    public void draw(Graphics g, int xShift, int yShift) {
        super.draw(g, xShift, yShift);
        Game.drawImage(g, "armor3", getX() - getSize()/2 + xShift, getY() -getSize()/2 + yShift, 2 * (getSize() - getSize()/5), 2 * (getSize()- getSize()/5));
    }
}