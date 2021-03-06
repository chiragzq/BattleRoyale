import java.awt.*;
/**
 * This isa client class Tree, which draws it at the constructred location, and its size decreases as health decreases
 * @author Aaron Lo
 * @version ????
 */
public class Tree extends Obstacle
{
    private static final int SIZE = (int)(1 *60);
    private static final int LEAVE_SIZE = (int)(1 *200);
    private static final int HEALTH = 120;
    private static final int SIZE_WHEN_DEAD = (int)(1 *25);

    /**
     * Constructor for objects of class Tree
     */
    public Tree(int x2, int y2)
    {
        super(x2, y2, HEALTH);
    }

    public int getSize()
    {
        return (int)(SIZE * Math.sqrt((double)getHealth()/HEALTH));
    }

    public void draw(Graphics g, double scale, int xShift, int yShift)
    {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(new Color(17, 99, 43));
        Game.fillCircle(g2, (int)(scale*(getX())) + xShift, (int)(scale*(getY())) + yShift, (int)(scale*((int)(LEAVE_SIZE * scale))), 1);
    }

    public void draw(Graphics g, int xShift, int yShift)
    {

        if(getHealth() < 25)
        {
            Graphics2D g2 = (Graphics2D)g;

            g2.setColor(new Color(110, 80, 81, 125));
            Game.fillCircle(g2, getX() + xShift, getY() + yShift, SIZE_WHEN_DEAD);
        }
        else
        {
            Graphics2D g2 = (Graphics2D)g;

            // g2.setColor(Color.RED);
            // int thick = (int)(LEAVE_SIZE * Math.sqrt((double)getHealth()/100));
            // g2.setStroke(new BasicStroke(thick));
            // Game.fillCircle(g2, getX() + xShift, getY() + yShift, (int)(LEAVE_SIZE * Math.sqrt((double)getHealth()/100)));

            g2.setColor(new Color(17, 99, 43, 175));
            Game.fillCircle(g2, getX() + xShift, getY() + yShift, (int)(LEAVE_SIZE * Math.sqrt((double)getHealth()/100)));
            g2.setStroke(new BasicStroke((int)(Game.GAME_SCALE*5)));

            g2.setColor(new Color(0, 0, 0));
            Game.drawCircle(g2, getX() + xShift, getY() + yShift, (int)(SIZE * Math.sqrt((double)getHealth()/100)));
            g.setColor(new Color(124, 91, 91));
            Game.fillCircle(g, getX() + xShift, getY() + yShift, (int)(SIZE * Math.sqrt((double)getHealth()/100)));
        }
    }

    // public void drawLeaves(Graphics g, int xShift, int yShift) {
    //     if(getHealth() >= 25)
    //     {
    //         Graphics2D g2 = (Graphics2D)g;
    //         g2.setColor(new Color(17, 99, 43, 175));
    //         int thick = (int)(LEAVE_SIZE * Math.sqrt((double)getHealth()/100));
    //         g2.setStroke(new BasicStroke((thick -  getSize())/2));
    //         //g2.drawOval(getX() + xShift - thick/2, getY() + yShift - thick/2, thick, thick);
    //         Game.drawCircle(g2, getX() + xShift, getY() + yShift, (thick-getSize())/2 + getSize() + 4);
    //     }
    // }
}
