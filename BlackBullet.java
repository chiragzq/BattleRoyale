import java.awt.*;
/**
 *A bullet with the color black
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BlackBullet extends Bullet
{
    private int x;
    private int y;
    private int backX;
    private int backY;
    private int direction;
    
    //How far the bullet traveled
    private static final int LENGTH = (int)(75* Game.GAME_SCALE);
    private int thicknessOfBullet;
    
    /**
     * Bullet constructor
     * @param xLoc x location
     * @param yLoc y loc
     * @param direc the direction
     */
    public BlackBullet(int xLoc, int yLoc, int direc)
    {
        super(xLoc, yLoc, direc);
    }
   

    /**
     * Draws the Bullet
     * @param g the graphics
     */
    public void draw(Graphics g, int xShift, int yShift)
    {
        int x = getX();
        int y = getY();
        int backX = getBackX();
        int backY = getBackY();
        int thicknessOfBullet = getThick();
        
        g.setColor(new Color(76, 82, 91, 255));
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(thicknessOfBullet));
        double x1 = ((double)x - backX)/3 * 2 + x;
        double y1 = ((double)y - backY)/3 * 2 + y;
        g2.drawLine(x + xShift, y + yShift, (int)x1 + xShift, (int)y1+ yShift);
        
        g.setColor(new Color(76, 82, 91, 150));
        double x2 = ((double)x - backX)/12 * 3 + x1;
        double y2 = ((double)y - backY)/12 * 3 + y1;
        g2.drawLine((int)x1 + xShift, (int)y1+ yShift, (int)x2 + xShift, (int)y2+ yShift);
        
        g.setColor(new Color(76, 82, 91, 50));
        double x3 = ((double)x - backX)/12 * 1 + x2;
        double y3 = ((double)y - backY)/12 * 1 + y2;
        g2.drawLine((int)x2 + xShift, (int)y2+ yShift, (int)x3 + xShift, (int)y3+ yShift);
        //These sets the alphas of parts of the bullet so it can fade in/out
        
    }
}
