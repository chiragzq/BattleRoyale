import java.awt.*;
/**
 * Write a description of class Pistol here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Pistol extends Gun
{
    private static final int BARREL_LENGTH =(int)( 55 * Game.GAME_SCALE);
    
    public Pistol(Player play)
    {
        super(play, 30, 20);
    }

    public String getType() {
        return "blue";
    }
    public void draw(Graphics g, int xShift, int yShift)
    {
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0));
        int x = getPlayer().getX();
        int y = getPlayer().getY();
        x = 0; xShift = Game.GAME_WIDTH / 2; y = 0; yShift = Game.GAME_HEIGHT / 2;
        g2.setStroke(new BasicStroke((int)(Game.GAME_SCALE *10)));
        
        int radius = (int)(Game.GAME_SCALE *10);
        int increment = radius/2;
        
        g2.drawLine(x + xShift, y + yShift, (int)(Math.cos(getDirection() * Math.PI / 180)*BARREL_LENGTH) + x+ xShift, (int)(Math.sin(getDirection() * Math.PI / 180)*BARREL_LENGTH) + y+ yShift);
        g.fillOval((int)(Math.cos(getDirection() * Math.PI / 180)*(BARREL_LENGTH + increment)) + x+ xShift - radius/2, (int)(Math.sin(getDirection() * Math.PI / 180)*(increment  + BARREL_LENGTH)) + y+ yShift - radius/2, radius, radius);
        
    }
    
    /**
     * Location of Right hand
     * @param directionRad the direction
     * @return dir of right
     */
    public double getRightDir(double directionRad)
    {
        return directionRad;
    }
    
    /**
     * Location of Left hand
     * @param directionRad the direction
     * @return dir of left
     */
    public double getLeftDir(double directionRad)
    {
        return directionRad;
    }
    
    /**
     * How far to extend right hand
     * @return the pixels
     */
    public int extendRight()
    {
        return 0;
    }
    
    /**
     * How far to extend left hand
     * @return the pixels
     */
    public int extendLeft()
    {
        return 0;
    }
}
