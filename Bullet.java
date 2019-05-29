import java.awt.*;
/**
 * A Bullet the Player Fires
 * 
 * @author Aaron Lo
 * @version 5-6-19
 */
public class Bullet
{
    private int x;
    private int y;
    private int backX;
    private int backY;
    private int direction;
    
    private static final int LENGTH = (int)(100* Game.GAME_SCALE);
    private int thicknessOfBullet;
    
    /**
     * Bullet constructor
     * @param xLoc x location
     * @param yLoc y loc
     * @param direc the direction
     */
    public Bullet(int xLoc, int yLoc, int direc)
    {
        x = xLoc;
        y = yLoc;
        direction = direc;
        findTheBackEndOfTheBullet();
        
        thicknessOfBullet = 3;
    }
    
    /**
     * This gives the back end of the bullet
     */
    public void findTheBackEndOfTheBullet()
    {
        backY = y + (int)(Math.sin(Math.toRadians(direction)) * LENGTH);
        backX = x + (int)(Math.cos(Math.toRadians(direction)) * LENGTH);
    }
    
    public int getX()
    {
        return x;
        
    }
    
    public int getY()
    {
        return y;
    }
    
    public int getBackX()
    {
        return backX;
    }
    
    public int getBackY() 
    {
        return backY;
    }
    
    public void setX(int x) {
        int diff = x - this.x;
        this.x = x;
        backX += diff;
    }

    public void setY(int y) {
        int diff = y - this.y;
        this.y = y;
        backY += diff;
    }
    
    public int getThick()
    {
        return thicknessOfBullet;
    }
    
    public void setThickness(int thick)
    {
        thicknessOfBullet = thick;
    }

    /**
     * Draws the Bullet
     * @param g the graphics
     */
    public void draw(Graphics g, int xShift, int yShift)
    {
        g.setColor(new Color(39, 99, 196, 255));
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(thicknessOfBullet));
        double x1 = ((double)x - backX)/3 * 2 + x;
        double y1 = ((double)y - backY)/3 * 2 + y;
        Game.drawLine(g, x + xShift, y + yShift, (int)x1 + xShift, (int)y1+ yShift);
        
        g.setColor(new Color(39, 99, 196, 150));
        double x2 = ((double)x - backX)/12 * 3 + x1;
        double y2 = ((double)y - backY)/12 * 3 + y1;
        Game.drawLine(g, (int)x1 + xShift, (int)y1+ yShift, (int)x2 + xShift, (int)y2+ yShift);
        
        g.setColor(new Color(39, 99, 196, 50));
        double x3 = ((double)x - backX)/12 * 1 + x2;
        double y3 = ((double)y - backY)/12 * 1 + y2;
        Game.drawLine(g, (int)x2 + xShift, (int)y2+ yShift, (int)x3 + xShift, (int)y3+ yShift);
        //These sets the alphas of parts of the bullet so it can fade in/out
        
    }
}
