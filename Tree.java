import java.awt.*;
/**
 * Write a description of class Tree here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tree extends Obstacle
{
    private static final int SIZE = (int)(Game.GAME_SCALE *60);
    private static final int LEAVE_SIZE = (int)(Game.GAME_SCALE *200);
    private static final int HEALTH = 120;
    private static final int SIZE_WHEN_DEAD = (int)(Game.GAME_SCALE *25);

    /**
     * Constructor for objects of class Tree
     */
    public Tree(int x2, int y2)
    {
        super(x2, y2, HEALTH);
    }
    
    public int getSize()
    {
        return (int)(SIZE * Math.sqrt((double)getHealth()/HEALTH));
    }
    

    public void draw(Graphics g, int xShift, int yShift)
    {
        
        if(getHealth() < 25)
        {
            Graphics2D g2 = (Graphics2D)g;

            g2.setColor(new Color(110, 80, 81, 125));
            Game.fillCircle(g2, getX() + xShift, getY() + yShift, SIZE_WHEN_DEAD);
        }
        else
        {
            Graphics2D g2 = (Graphics2D)g;
            
            // g2.setColor(Color.RED);
            // int thick = (int)(LEAVE_SIZE * Math.sqrt((double)getHealth()/100));
            // g2.setStroke(new BasicStroke(thick));
            // Game.fillCircle(g2, getX() + xShift, getY() + yShift, (int)(LEAVE_SIZE * Math.sqrt((double)getHealth()/100)));
            
            g2.setColor(new Color(17, 99, 43, 175));
            Game.fillCircle(g2, getX() + xShift, getY() + yShift, (int)(LEAVE_SIZE * Math.sqrt((double)getHealth()/100)));
            g2.setStroke(new BasicStroke(5));

            g2.setColor(new Color(0, 0, 0));
            Game.drawCircle(g2, getX() + xShift, getY() + yShift, (int)(SIZE * Math.sqrt((double)getHealth()/100)));
            g.setColor(new Color(124, 91, 91));
            Game.fillCircle(g, getX() + xShift, getY() + yShift, (int)(SIZE * Math.sqrt((double)getHealth()/100)));
        }
    }

    // public void drawLeaves(Graphics g, int xShift, int yShift) {
    //     if(getHealth() >= 25)
    //     {
    //         Graphics2D g2 = (Graphics2D)g;
    //         g2.setColor(new Color(17, 99, 43, 175));
    //         int thick = (int)(LEAVE_SIZE * Math.sqrt((double)getHealth()/100));
    //         g2.setStroke(new BasicStroke((thick -  getSize())/2));
    //         //g2.drawOval(getX() + xShift - thick/2, getY() + yShift - thick/2, thick, thick);
    //         Game.drawCircle(g2, getX() + xShift, getY() + yShift, (thick-getSize())/2 + getSize() + 4);
    //     }
    // }
}
