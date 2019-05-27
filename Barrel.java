import java.awt.*;
/**
 * Write a description of class Barrel here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Barrel extends Obstacle
{
    private static final int SIZE = 60;
    private static final int DEATH = 20;
    public Barrel(int x, int y)
    {
        super(x, y, 100);
    }

    public int getSize()
    {
        return (int)(SIZE * Math.sqrt((double)getHealth()/100));
    }

    public void draw(Graphics g, int xShift, int yShift)
    {
        //border:2E2E2E
        //out:7B7B7B
        //in:585858
        //circle: 1C1817
        if(getHealth() < 25)
        {
            Game.drawImage(g, "barrel_death", getX() + xShift - DEATH/2, getY() + yShift - DEATH/2, DEATH, DEATH);
            //Credits to Surviv
        }
        else {
            g.setColor(new Color(0x2E2E2E));
            Game.fillCircle(g, getX()  + xShift, getY() + yShift, getSize());

            g.setColor(new Color(0x7b7b7b));
            Game.fillCircle(g, getX()  + xShift, getY() + yShift, (int)(getSize() / 1.1));

            g.setColor(new Color(0x585858));
            Game.fillCircle(g, getX()  + xShift, getY() + yShift, (int)(getSize() / 1.3));

            g.setColor(new Color(0x1c1817));
            Game.fillCircle(g, getX() + xShift - getSize() / 10, getY() + yShift - getSize() / 10, (int)(getSize() / 8.5));
        }
    }
}
