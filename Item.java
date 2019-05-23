import java.awt.*;
public abstract class Item 
{
    private int x;
    private int y;
    private int size;
    public Item(int xLoc, int yLoc, int size) //Size is half the length
    {
        x = xLoc;
        y = yLoc;
        this.size = size;
    }

    public void setX(int xLoc)
    {
        x = xLoc;
    }

    public void setY(int yLoc)
    {
        y = yLoc;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getSize()
    {
        return size;
    }

    public abstract void draw(Graphics g);
}