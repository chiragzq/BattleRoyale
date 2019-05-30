import java.awt.*;
public class Scope8 extends Scope {
    public Scope8(int x, int y) {
        super(x, y);
    }
    public void draw(Graphics g, int xShift, int yShift)
    {
        super.draw(g, xShift, yShift);
        Game.drawImage(g, "scope8", getX() - getSize()/3 + xShift, getY() - getSize() / 3 + yShift, 2 * getSize()/3, 2 * getSize()/3);
    }
}