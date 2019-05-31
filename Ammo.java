import java.awt.*;
/**
 * This is the Ammo class which draws blue or red Ammo
 */
public class Ammo extends Item 
{
    private Color color;
    public Ammo(int x, int y, Color color)
    {
        super(x, y, 15);
        this.color = color;
    }

    public void draw(Graphics g, int xShift, int yShift)
    {
        if(color.equals(Color.BLUE))
            Game.drawImage(g, "blueAmmo", getX() + xShift - getSize(), getY() + yShift - getSize(), getSize() * 2, getSize() * 2);
        else 
            Game.drawImage(g, "redAmmo", getX() + xShift - getSize(), getY() + yShift - getSize(), getSize() * 2, getSize() * 2);
        // g.setColor(Color.BLUE);
        // //g.drawRect(100, 100, 100, 100);
        // int xLoc = getX();
        // int yLoc = getY();
        
        // int size = getSize();
        // int thickness = size/3;
        // Graphics2D g2 = (Graphics2D) g;
        // g2.setColor(new Color(80, 80, 80));
        // g2.fillRect(xLoc - size + size/3 + size/18 +  xShift, yLoc - size + size/3 + yShift, size * 2 - size/3, size * 2 - size/3);
        // g2.setColor(new Color(166, 187, 221));
        // g2.fillRect(xLoc - size/9 +  xShift, yLoc - size/3+ yShift, size/2 + size/5, size/2 + size/4 - size/7);
        
        // g2.setStroke(new BasicStroke(thickness));
        // g2.setColor(new Color(0, 0, 0));
        // g2.drawRect(xLoc - size + thickness+  xShift, yLoc - size + thickness + yShift, size * 2 - thickness, size * 2 - thickness);

    }
}