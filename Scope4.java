import java.awt.*;
/**
 * the 4X Scope
 */
public class Scope4 extends Scope {
    public Scope4(int x, int y) {
        super(x, y);
    }
    public void draw(Graphics g, int xShift, int yShift)
    {
        super.draw(g, xShift, yShift);
        Game.drawImage(g, "scope4", getX() - getSize()/3 + xShift, getY() - getSize() / 3 + yShift, 2 * getSize()/3, 2 * getSize()/3);
    }
}