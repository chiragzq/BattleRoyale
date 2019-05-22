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
        return LENGTH;
    }
    
    public void draw(Graphics g, int xShift, int yShift)
    {
        drawImage(g, "box", getX() - LENGTH/2 + xShift, getY() - LENGTH/2 + yShift, LENGTH, LENGTH);
    }
    
    public void drawImage(Graphics g, String file, int xImage, int yImage, int iWidth, int iHeight)
    {
        Image image = new ImageIcon("img/" + file + ".png").getImage();
        g.drawImage(image, xImage, yImage, iWidth, iHeight, null);
    }
}
