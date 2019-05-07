import java.awt.*;

/**
 * The Player the person controls
 * @author Aaron Lo
 * @version 5-6-19
 */
public class Player
{
    private int x;
    private int y;
    private int health;
    private int direction;

    /**
     * Constructs Player at location(x, y)
     * @param xLoc the x location
     * @param yLoc the y location
     */
    public Player(int xLoc, int yLoc)
    {
        x = xLoc;
        y = yLoc;
        direction = 20;
        //The location
        
        health = 100;
        //The health of the player
    }
    
    /**
     * Moves in the x-axis direction
     * @param xDirection how far the player moves in the xDirection
     */
    public void xMove(int xDirection)
    {
        x += xDirection;
    }
    
    /**
     * Moves in the y-axis direction
     * @param yDirection how far the player moves in the yDirection
     */
    public void yMove(int yDirection)
    {
        y += yDirection;
    }
    
    /**
     * This returns the x loc
     * @return the x location
     */
    public int getX()
    {
        return x;
    }
    
    /**
     * This returns the y loc
     * @return the y location
     */
    public int getY()
    {
        return y;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0xFAC47F));
        Game.fillCircle(g, x, y, Game.PLAYER_SIZE);
        
        double directionRad = direction * Math.PI / 180;
        double rightDir = directionRad + Math.PI / 5;
        double leftDir = directionRad - Math.PI / 5;

        int leftXOff = (int)((Game.PLAYER_SIZE / 2 + Game.HAND_SIZE / 4) * Math.cos(leftDir));
        int leftYOff = (int)((Game.PLAYER_SIZE / 2 + Game.HAND_SIZE / 4) * Math.sin(leftDir));

        int rightXOff = (int)((Game.PLAYER_SIZE / 2 + Game.HAND_SIZE / 4) * Math.cos(rightDir));
        int rightYOff = (int)((Game.PLAYER_SIZE / 2 + Game.HAND_SIZE / 4) * Math.sin(rightDir));

        System.out.println(x + leftXOff + " " + (y + leftYOff));
        Game.fillCircle(g, x + leftXOff, y + leftYOff, Game.HAND_SIZE);
        Game.fillCircle(g, x + rightXOff, y + rightYOff, Game.HAND_SIZE);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(0x322819));
        Game.drawCircle(g2, x + leftXOff, y + leftYOff, Game.HAND_SIZE);
        Game.drawCircle(g2, x + rightXOff, y + rightYOff, Game.HAND_SIZE);
    }
}
