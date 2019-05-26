import java.awt.*;
/**
 * Write a description of class Bush here.
 * 
 * @author Aaorn Lo
 * @version (a version number or a date)
 */
public class Bush extends Obstacle
{
    // instance variables - replace the example below with your own
    private static final int SIZE = (int)(Game.GAME_SCALE *100);
    private static final int SIZE_WHEN_DEAD = (int)(Game.GAME_SCALE *20);
    private static final int OPACITY = 235;

    public Bush(int x, int y)
    {
        super(x, y, 70);
    }

    public int getSize()
    {
        return SIZE;
    }

    public void draw(Graphics g, int xShift, int yShift)
    {
        if(getHealth() <= 25)
        {
            Graphics2D g2 = (Graphics2D)g;
            g2.setColor(new Color(201, 239, 207, 200));
            Game.fillCircle(g2, getX() + xShift, getY() + yShift, SIZE_WHEN_DEAD);
        }
        else
        {
            double c = SIZE/3;
            int x = getX();
            int y = getY();
            Graphics2D g2 = (Graphics2D)g;
            g2.setColor(new Color(201, 239, 207, OPACITY));
            Game.fillCircle(g2, x + xShift, y + yShift, (int)c);
            g2.setColor(new Color(62, 145, 75, OPACITY));
            g2.setStroke(new BasicStroke((int)(c/2)));
            Game.drawCircle(g2, x + xShift, y + yShift, (int)((2*c) - c/2));
            g2.setColor(new Color(17, 99, 43, OPACITY));
            Game.drawCircle(g2, x + xShift, y + yShift, (int)(3*c - c/1.75));
        }
    }
}
