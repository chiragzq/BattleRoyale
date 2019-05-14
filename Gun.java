import java.awt.*;
/**
 * Gun
 * 
 * @author Aaron Lo
 * @version 5-7-19
 */
public abstract class Gun implements Weapon
{
    private final static int BARREL_LENGTH = (int)(80 * Game.GAME_SCALE);
    private final static int EXTENSION = (int)(Game.GAME_SCALE *0);
    private int capacity;
    private int spread;
    private double direction;
    private Player player;
    private int numberInCarbine = 999;
    

    /**
     * Constructor
     * @param player2 the player
     * @param cap how much bullets the gun can hold
     */
    public Gun(Player player2, int cap, int spre)
    {
        player = player2;
        capacity = cap;
        spread = spre;
        direction = player2.getDirection();
    }
    
    public int getAmmo()
    {
        return numberInCarbine;
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
        int xMouse = Game.getMouseX();
        int yMouse = Game.getMouseY();

        double xSide = xMouse - player.getX();
        double ySide = yMouse - player.getY();

        direction = (int)(Math.atan2(ySide, xSide) / Math.PI * 180);
    }
    
    public void setDirection(int dir) {
        direction = dir;
    }

    /**
     * Draws the gun
     * @param g the gun
     */
    public abstract void draw(Graphics g);

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
