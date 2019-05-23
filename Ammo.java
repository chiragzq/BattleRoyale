import java.awt.*;

public class Ammo extends Item 
{
    public Ammo(int x, int y)
    {
        super(x, y, 15);
    }

    public void draw(Graphics g)
    {
        g.setColor(Color.BLUE);
        //g.drawRect(100, 100, 100, 100);
        int xLoc = getX();
        int yLoc = getY();
        
        int size = getSize();
        int thickness = size/3;
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(47, 105, 198));
        g2.fillRect(xLoc - size + size/3 + size/18, yLoc - size + size/3, size * 2 - size/3, size * 2 - size/3);
        g2.setColor(new Color(166, 187, 221));
        g2.fillRect(xLoc - size/9, yLoc - size/3, size/2 + size/5, size/2 + size/4 - size/7);
        
        g2.setStroke(new BasicStroke(thickness));
        g2.setColor(new Color(14, 32, 61));
        g2.drawRect(xLoc - size + thickness, yLoc - size + thickness, size * 2 - thickness, size * 2 - thickness);

    }
}