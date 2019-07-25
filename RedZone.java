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
    public RedZone(int xLoc, int yLoc, int radius)
    {
        x = xLoc;
        y = yLoc;
        r = radius;
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
    
    public void draw(Graphics g, int xShift, int yShift)
    {
        
        Game.drawImage(g, "redCircle", x - r + xShift, y - r + yShift, 2 * r, 2 * r);
        
        int constan = Game.GAME_WIDTH;
        
        
        Game.drawImage(g, "redBox", x - r + xShift - constan, y - r + yShift - constan, constan,  constan);
        Game.drawImage(g, "redBox", x - r + xShift - constan, y + yShift - r, constan, r  * 2);
        Game.drawImage(g, "redBox", x - r + xShift - constan, y + r + yShift, constan, constan);
        //Left Side
        Game.drawImage(g, "redBox", x - r + xShift, y + r + yShift, 2 * r, constan);
        //Bottom
        Game.drawImage(g, "redBox", x + r + xShift, y - r + yShift, constan, constan);
        Game.drawImage(g, "redBox", x + r+ xShift, y + r + yShift, constan, 2 * r);
        Game.drawImage(g, "redBox", x + r + xShift, y + r + yShift + constan, constan, constan);
        //RightSide
        
        Game.drawImage(g, "redBox", x - r+ xShift, y - r + yShift - constan, 2 * r, constan);
        //Top 
        
    }
}
