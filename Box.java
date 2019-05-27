import java.awt.*;
import javax.swing.*;
/**
 * Write a description of class Box here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Box extends Obstacle
{
    private static final int LENGTH = 100;
    private static Image image; 

    public Box(int xl, int yl)
    {
        super(xl, yl, 100);
        if(image == null)
            image = new ImageIcon(getClass().getResource("img/box.png")).getImage();
    }
    
    public int getSize()
    {
        return (int)(LENGTH * Math.sqrt((double)getHealth()/100));
    }
    
    public void draw(Graphics g, int xShift, int yShift)
    {
        if(getHealth() < 25)
        {
            g.setColor(new Color(99, 91, 61, 200));
            Game.fillCircle(g, getX() + xShift, getY() + yShift, 10);
        }
        else
        {
            drawImage(g, getX() - getSize()/2 + xShift, getY() - getSize()/2 + yShift, getSize(), getSize());
        }
    }
    
    public void drawImage(Graphics g, int xImage, int yImage, int iWidth, int iHeight)
    {
        g.drawImage(image, xImage, yImage, iWidth, iHeight, null);
    }
}
