import java.awt.*;
public class Scope15 extends Scope {
    public Scope15(int x, int y) {
        super(x, y);
    }
    public void draw(Graphics g, int xShift, int yShift)
    {
        super.draw(g, xShift, yShift);
        Game.drawImage(g, "scope15", getX() - getSize()/3, getY() - getSize() / 3, 2 * getSize()/3, 2 * getSize()/3);
    }
}