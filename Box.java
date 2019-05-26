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

    public Box(int xl, int yl)
    {
        super(xl, yl, 100);
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
            drawImage(g, "box", getX() - getSize()/2 + xShift, getY() - getSize()/2 + yShift, getSize(), getSize());
        }
    }
    
    public void drawImage(Graphics g, String file, int xImage, int yImage, int iWidth, int iHeight)
    {
        Image image = new ImageIcon("img/" + file + ".png").getImage();
        g.drawImage(image, xImage, yImage, iWidth, iHeight, null);
    }
}
