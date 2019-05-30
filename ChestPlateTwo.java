import java.awt.*;

public class ChestPlateTwo extends Armor
{
    public ChestPlateTwo(int x, int y)
    {
        super(x, y);
    }

    public void draw(Graphics g, int xShift, int yShift) {
        super.draw(g, xShift, yShift);
        Game.drawImage(g, "armor2", getX() - getSize()/2 + xShift, getY() -getSize()/2 + yShift, 2 * (getSize() - getSize()/5), 2 * (getSize()- getSize()/5));
    }
}