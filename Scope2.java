import java.awt.*;
public class Scope2 extends Scope {
    public Scope2(int x, int y) {
        super(x, y);
    }
    public void draw(Graphics g, int xShift, int yShift)
    {
        super.draw(g, xShift, yShift);
        Game.drawImage(g, "scope2", getX() - getSize()/3, getY() - getSize() / 3, 2 * getSize()/3, 2 * getSize()/3);
    }
}
