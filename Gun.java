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

    /**
     * gets player
     * @return the plaery
     */
    public Player getPlayer()
    {
        return player;
    }

    /**
     * Reloads the gun
     */
    public void reload()
    {
        if(capacity + numberInCarbine <= player.getBullets())
        {
            int add = capacity - numberInCarbine;
            player.setBullets(-add);
            numberInCarbine += add;
        }
        else
        {
            int add = player.getBullets();
            player.setBullets(-player.getBullets());
            numberInCarbine += add;
        }
    }

    public void setNum(int nums)
    {
        numberInCarbine += nums;
    }

    /**
     * Fires a bullet
     * @return the bullet fired; null if can not fire
     */
    public Bullet[] fire()
    {
        if(numberInCarbine == 0)
            return null;
        int change = (int)((int)(Math.random() * spread) - (double)spread/2);
        int xSide = (int)(Math.atan2(Game.getMouseY() - player.getY(), Game.getMouseX() - player.getX()) * 180 /Math.PI);

        int x2 = Game.getMouseX();
        int y2 = Game.getMouseY();
        int x = getPlayer().getX();
        int y = getPlayer().getY();
        double t = Math.atan2(y2 - y, x2 - x);
        Bullet b = new Bullet( (int)(Math.cos(t)*(BARREL_LENGTH+EXTENSION)) + player.getX(), (int)(Math.sin(t)*(BARREL_LENGTH+ EXTENSION)) + player.getY(), xSide + change);
        numberInCarbine--;
        Bullet[] ba = new Bullet[1];
        ba[0] = b;
        return ba;
    }

    public Bullet firing()
    {

        if(numberInCarbine == 0)
            return null;
        int change = (int)((int)(Math.random() * spread) - (double)spread/2);
        int xSide = (int)(Math.atan2(Game.getMouseY() - player.getY(), Game.getMouseX() - player.getX()) * 180 /Math.PI);

        int x2 = Game.getMouseX();
        int y2 = Game.getMouseY();
        int x = getPlayer().getX();
        int y = getPlayer().getY();
        double t = Math.atan2(y2 - y, x2 - x);
        Bullet b = new Bullet( (int)(Math.cos(t)*(BARREL_LENGTH+EXTENSION)) + player.getX(), (int)(Math.sin(t)*(BARREL_LENGTH+ EXTENSION)) + player.getY(), xSide + change);
        numberInCarbine--;
        return b;
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
<<<<<<< HEAD
    
    public void setDirection(int dir) {
        direction = dir;
    }
=======
>>>>>>> 6a96a6e10372a6797727b779fc39d7a210c06d59

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
