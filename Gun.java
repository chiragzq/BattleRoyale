import java.awt.*;
/**
 * Gun
 * 
 * @author Aaron Lo
 * @version 5-7-19
 */
public abstract class Gun
{
    private final static int BARREL_LENGTH = (int)(90);
    
    private double direction;
    private Player player;
    private int numberInCarbine;
    private int spare;
    
    public Gun(Player player2) 
    {
        player = player2;
        direction = player2.getDirection();

        numberInCarbine = 0;
        spare = 0;
    }
    
    
    /**
     * Constructor
     * @param player2 the player
     * @param cap how much bullets the gun can hold
     */
    public Gun(Player player2, int cap, int spre)
    {
        player = player2;
        direction = player2.getDirection();

        numberInCarbine = 0;
        spare = 0;
    }
    
    public int getAmmo()
    {
        return numberInCarbine;
    }
    
    public int getTotalAmmo()
    {
        return spare;
    }

    /**
     * gets player
     * @return the plaery
     */
    public Player getPlayer()
    {
        return player;
    }

    /**
     * Gets the barrelLength
     */
    public int getBarrelLength()
    {
        return BARREL_LENGTH;
    }

    public double getDirection() {
        return direction;
    }

    public void faceCursor() {
        int xShift = Game.GAME_WIDTH/2 - player.getX();
        int yShift = Game.GAME_HEIGHT/2 - player.getY();
        int xMouse = Game.getMouseX() - xShift;
        int yMouse = Game.getMouseY() - yShift;

        double xSide = xMouse - player.getX();
        double ySide = yMouse - player.getY();

        direction = (int)(Math.atan2(ySide, xSide) / Math.PI * 180);
    }
    
    public void setDirection(int dir) {
        direction = dir;
    }

    public void setClip(int clip) {
        numberInCarbine = clip;
    }

    public void setSpare(int spare) {
        this.spare = spare;
    }

    public abstract String getType();

    /**
     * Draws the gun
     * @param g the gun
     */
    public abstract void draw(Graphics g, int xShift, int yShift);

    /**
     * Location of Right hand
     * @return dir of right
     */
    public abstract double getRightDir(double dire);

    /**
     * Location of Left hand
     * @return dir of left
     */
    public abstract double getLeftDir(double dire);

    /**
     * How far to extend right hand
     * @return the pixels
     */
    public abstract int extendRight();

    /**
     * How far to extend left hand
     * @return the pixels
     */
    public abstract int extendLeft();

}
