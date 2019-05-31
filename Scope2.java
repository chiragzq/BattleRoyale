import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
/**
 * The 2X scope
 */
public class Scope2 extends Scope {
    public Scope2(int x, int y) {
        super(x, y);
    }
    public void draw(Graphics g, int xShift, int yShift)
    {
        super.draw(g, xShift, yShift);
        //drawImage(g);
        Game.drawImage(g, "scope2", getX() - getSize()/3 + xShift, getY() - getSize() / 3 + yShift, 2 * getSize()/3, 2 * getSize()/3);
    }
    
    public void drawImage(Graphics g)
    {
        Image imgae = new ImageIcon("img/scope2.png").getImage();
        g.drawImage(imgae, getX() - getSize()/3, getY() - getSize() / 3, 2 * getSize()/3, 2 * getSize()/3, null);
    }
}
