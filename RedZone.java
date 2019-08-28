import java.awt.*;

/**
 * Write a description of class RedZone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RedZone
{
    private int x;
    private int y;
    private int r;
    
    
    private int fX;
    private int fY;
    private int fR;
    public RedZone(int xLoc, int yLoc, int radius)
    {
        x = xLoc;
        y = yLoc;
        r = radius;
    }
    
    public int getRadius()
    {
        return r;
    }
    
    public void setRadius(int radius)
    {
        this.r = radius;
    }
    
    public void setLocation(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public void setfLoc(int x, int y, int r)
    {
        fX = x;
        fY = y;
        fR = r;
    }
    
    public int[] getLoc()
    {
        int[] ar = {x, y, r, fX, fY, fR};
        return ar;
    }
    
    public void draw(Graphics g, double scale, int xShift, int yShift)
    {
        Game.drawImage(g, "redCircle", (int)(scale * (x - r ))+ xShift,(int)(scale * ( y - r ))+ yShift, (int)(scale * (2 * r)), (int)(scale * (2 * r)));
        
        int constan = Game.MAX_X;
        
        
        Game.drawImage(g, "redBox", (int)(scale * (x - r + xShift - constan)), (int)(scale * (y - r + yShift - constan)), (int)(scale * (constan)),  (int)(scale * (constan)));
        Game.drawImage(g, "redBox", (int)(scale * (x - r + xShift - constan)),(int)(scale * ( y + yShift - r)), (int)(scale * (constan)), (int)(scale * (r  * 2)));
        Game.drawImage(g, "redBox", (int)(scale * (x - r + xShift - constan)), (int)(scale * (y + r + yShift)), (int)(scale * (constan)), (int)(scale * (constan)));
        //Left Side
        Game.drawImage(g, "redBox", (int)(scale * (x - r + xShift)), (int)(scale * (y + r + yShift)), (int)(scale * (2 * r)), (int)(scale * (constan)));
        //Bottom
        Game.drawImage(g, "redBox", (int)(scale * (x + r + xShift)), (int)(scale * (y - r + yShift - constan)), (int)(scale * (constan)), (int)(scale * (constan)));
        Game.drawImage(g, "redBox", (int)(scale * (x + r+ xShift)), (int)(scale * (y - r + yShift)), (int)(scale * (constan)), (int)(scale * (2 * r)));
        Game.drawImage(g, "redBox", (int)(scale * (x + r + xShift)), (int)(scale * (y + r + yShift)), (int)(scale * (constan)), (int)(scale * (constan)));
        //RightSide
        
        Game.drawImage(g, "redBox", (int)(scale * (x - r+ xShift)), (int)(scale * (y - r + yShift - constan)), (int)(scale * (2 * r)), (int)(scale * (constan)));
        //Top 
    }
    
    
    public void draw(Graphics g, int xShift, int yShift)
    {
        
        Game.drawImage(g, "redCircle", x - r + xShift, y - r + yShift, 2 * r, 2 * r);
        
        int constan = Game.MAX_X;
        
        
        Game.drawImage(g, "redBox", x - r + xShift - constan, y - r + yShift - constan, constan,  constan);
        Game.drawImage(g, "redBox", x - r + xShift - constan, y + yShift - r, constan, r  * 2);
        Game.drawImage(g, "redBox", x - r + xShift - constan, y + r + yShift, constan, constan);
        //Left Side
        Game.drawImage(g, "redBox", x - r + xShift, y + r + yShift, 2 * r, constan);
        //Bottom
        Game.drawImage(g, "redBox", x + r + xShift, y - r + yShift - constan, constan, constan);
        Game.drawImage(g, "redBox", x + r+ xShift, y - r + yShift, constan, 2 * r);
        Game.drawImage(g, "redBox", x + r + xShift, y + r + yShift, constan, constan);
        //RightSide
        
        Game.drawImage(g, "redBox", x - r+ xShift, y - r + yShift - constan, 2 * r, constan);
        //Top 
        
    }
}
