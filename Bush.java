import java.awt.*;
/**
 * A bush
 * 
 * @author Aaorn Lo
 * @version (a version number or a date)
 */
public class Bush extends Obstacle
{
    // instance variables - replace the example below with your own
    private static final int SIZE = (int)(1 *125);
    private static final int SIZE_WHEN_DEAD = (int)(1 *20);
    private static final int OPACITY = 235;

    public Bush(int x, int y)
    {
        super(x, y, 70);
    }

    public int getSize()
    {
        return SIZE;
    }

    public void draw(Graphics g, double scale, int xShift, int yShift)
    {
        Game.drawImageNoScale(g, "bush", (int)(scale * getX())  + xShift, (int)(scale * getY()) + yShift, (int)(scale * SIZE), (int)(scale * SIZE));
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
            Game.drawImage(g, "bush", x + xShift, y + yShift, SIZE, SIZE);
        }
    }
}
