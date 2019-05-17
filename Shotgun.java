import java.awt.*;
/**
 * Write a description of class Shotgun here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Shotgun extends Gun
{
    public Shotgun(Player play)
    {
        super(play, 5, 20);
    }


    public void draw(Graphics g, int xShift, int yShift)
    {
        g.setColor(new Color(0, 0, 0));
        Graphics2D g2 = (Graphics2D) g;
        int x = getPlayer().getX();
        int y = getPlayer().getY();

        g2.setStroke(new BasicStroke((int)(Game.GAME_SCALE *13)));
        g2.drawLine(x + xShift, y + yShift, (int)(Math.cos(getDirection() * Math.PI / 180)*getBarrelLength()) + x+ xShift, (int)(Math.sin(getDirection() * Math.PI / 180)*getBarrelLength()) + y + yShift);

        
        int radius = (int)(Game.GAME_SCALE *13);
        double increment = (double)radius/2 - 1;
        Game.fillCircle(g, (int)(Math.cos(getDirection() * Math.PI / 180)*(getBarrelLength()+increment)) + x + xShift, (int)(Math.sin(getDirection() * Math.PI / 180)*(getBarrelLength()+increment)) + y + yShift, radius);
    }

    /**
     * Location of Right hand
     * @param directionRad the direction
     * @return dir of right
     */
    public double getRightDir(double directionRad)
    {
        return directionRad + Math.PI / 23;
    }

    /**
     * Location of Left hand
     * @param directionRad the direction
     * @return dir of left
     */
    public double getLeftDir(double directionRad)
    {
        return directionRad + Math.PI / 33;
    }

    /**
     * How far to extend right hand
     * @return the pixels
     */
    public int extendRight()
    {
        return 0;
    }

    /**
     * How far to extend left hand
     * @return the pixels
     */
    public int extendLeft()
    {
        return 30;
    }
}
