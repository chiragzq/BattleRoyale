import java.awt.*;

/**
 * The Player the person controls
 * @author Aaron Lo
 * @author Chirag Kaushik
 * @version 5-6-19
 */
public class Player
{
    private static final int TOTAL_PUNCH_TIME = (int)(Game.GAME_SCALE *300);
    //In Milli seconds
    private static final int TOTAL_ARM_EXTEND = (int)(Game.GAME_SCALE *25);
    private static final int DEGREE_ARM_MOVES_IN = 60;

    private int x;
    private int y;
    private int health;
    private double direcRadian;
    private double direction;
    private int bulletLoad;
    private long lastPunchTime;
    private boolean isCurrentlyPunching;
    private boolean isRightPunching;
    private int xPunch;
    //The x of the punch
    private int yPunch;
    //The y of the punch
    private boolean isExtended;
    //Whether the hand has been exteded
    private Gun gun;
    private Punch punch;

    /**
     * Constructs Player at location(x, y)
     * @param xLoc the x location
     * @param yLoc the y location
     */
    public Player(int xLoc, int yLoc, int direction, int health)
    {
        this.x = xLoc;
        this.y = yLoc;
        this.direction = direction;
        //The location
        
        this.health = health;
        //The health of the player
        punch = new Punch(this);
        gun = new Rifle(this);
    }
    
    /**
     * gets healet
     */
    public int getHealth()
    {
        return health;
    }
    
    public Punch getPunch()
    {
        return punch;
    }
    
    /**
     * Gets the gun
     */
    public Gun getGun()
    {
        return gun;
    }

    /**
     * Gets how many bullets
     * @return the number of bullets
     */
    public int getBullets()
    {
        return bulletLoad;
    }

    /**
     * Sets the number of bullets
     * @param remove how many to add
     */
    public void setBullets(int remove)
    {
        bulletLoad += remove;
    }

    /**
     * This faces the player towards the cursor
     */
    public void faceCursor()
    {
        int xMouse = Game.getMouseX();
        int yMouse = Game.getMouseY();

       
        
        double xSide = xMouse - x;
        double ySide = yMouse - y;
        
        
        direcRadian = Math.atan2(ySide, xSide);
        direction = (Math.atan2(ySide, xSide) / Math.PI * 180);
    }
    
    /**
     * Gets if the player is punching or not
     */
    public boolean isPunching()
    {
        return isCurrentlyPunching;
    }
    
    /**
     * Gets the x of the hand punching
     */
    public int xPunch()
    {
        return xPunch;
    }
    
    /**
     * Gets the y of the hand punching
     */
    public int yPunch()
    {
        return yPunch;
    }
    
    /**
     * gets if the arm is extended
     */
    public boolean isExtended()
    {
        System.out.println(isExtended);
        return isExtended;
    }
    

    /**
     * Punches
     */
    public void punch()
    {
        if(!isCurrentlyPunching)
        {
            lastPunchTime = System.currentTimeMillis();
            int d = (int)(Math.random() * 2);
            if(d == 0)
                isRightPunching = false;
            else
                isRightPunching = true;
        }
    }

    /**
     * This draws the player
     * @param g the graphics
     */
    public void draw(Graphics g) {
        if(gun != null) {
            gun.draw(g);
        }

        g.setColor(new Color(0xFAC47F));
        Game.fillCircle(g, x, y, Game.PLAYER_SIZE);

        double handExtendRight = 0;
        double handExtendLeft = 0;
        
        double directionRad = direcRadian;
        double rightDir = directionRad + Math.PI / 4.5;
        double leftDir = directionRad - Math.PI / 4.5;
        
        isCurrentlyPunching = false;

        if(gun == null)
        {
            double interval = TOTAL_PUNCH_TIME / 20;
            double extend = (double)TOTAL_ARM_EXTEND / 10;
            if(isRightPunching)
            {
                //This moves the right hand forward
                if((double)(System.currentTimeMillis() - lastPunchTime) < TOTAL_PUNCH_TIME/2)
                {
                    handExtendRight = ((double)(System.currentTimeMillis() - lastPunchTime) / interval * extend);
                    isCurrentlyPunching = true;
                    //This half animates the arm going forward
                }
                else if((double)(System.currentTimeMillis() - lastPunchTime) < TOTAL_PUNCH_TIME)
                {
                    handExtendRight = (((TOTAL_PUNCH_TIME - (double)(System.currentTimeMillis() - lastPunchTime))/interval) * extend);
                    isCurrentlyPunching = true;
                    //This half animates the arm going backwards
                }
                //This moves the arm
            }
            else
            {
                //This moves the left hand forward
                if((double)(System.currentTimeMillis() - lastPunchTime) < TOTAL_PUNCH_TIME/2)
                {
                    handExtendLeft = ((double)(System.currentTimeMillis() - lastPunchTime) / interval * extend);
                    isCurrentlyPunching = true;
                    //This half animates the arm going forward
                }
                else if((double)(System.currentTimeMillis() - lastPunchTime) < TOTAL_PUNCH_TIME)
                {
                    handExtendLeft = (((TOTAL_PUNCH_TIME - (double)(System.currentTimeMillis() - lastPunchTime))/interval) * extend);
                    isCurrentlyPunching = true;
                    //This half animates the arm going backwards
                }
                //This moves the arm
            }
        }
        else
        {
            rightDir = gun.getRightDir(directionRad);
            leftDir = gun.getLeftDir(directionRad);
            handExtendLeft = gun.extendLeft();
            handExtendRight = gun.extendRight();
        }
        
        if(isCurrentlyPunching)
        {
            if(isRightPunching)
                rightDir -= Math.PI * handExtendRight / TOTAL_ARM_EXTEND / 4.5;
            else
                leftDir += Math.PI * handExtendLeft / TOTAL_ARM_EXTEND / 4.5;
        }
        
        int leftXOff = (int)((Game.PLAYER_SIZE / 2 + Game.HAND_SIZE / 4 + handExtendLeft) * Math.cos(leftDir));
        int leftYOff= (int)((Game.PLAYER_SIZE / 2 + Game.HAND_SIZE / 4 + handExtendLeft) * Math.sin(leftDir));

        int rightXOff = (int)((Game.PLAYER_SIZE / 2 + Game.HAND_SIZE / 4 + handExtendRight) * Math.cos(rightDir));
        int rightYOff = (int)((Game.PLAYER_SIZE / 2 + Game.HAND_SIZE / 4 + handExtendRight) * Math.sin(rightDir));

        int constant = 3;
        if(isRightPunching)
        {
            xPunch = x + rightXOff;
            yPunch = y + rightYOff;
            if(handExtendRight < TOTAL_ARM_EXTEND + TOTAL_PUNCH_TIME/Game.FRAME_RATE/constant && handExtendRight > TOTAL_ARM_EXTEND - TOTAL_PUNCH_TIME/Game.FRAME_RATE/constant)
                isExtended = true;
            else
                isExtended = false;
        }
        else
        {
            xPunch = x + leftXOff;
            yPunch = y + leftYOff;
            if(handExtendLeft < TOTAL_ARM_EXTEND + TOTAL_PUNCH_TIME/Game.FRAME_RATE/constant && handExtendLeft > TOTAL_ARM_EXTEND - TOTAL_PUNCH_TIME/Game.FRAME_RATE/constant)
                isExtended = true;
            else
                isExtended = false;
        }
        
        Game.fillCircle(g, x + leftXOff, y + leftYOff, Game.HAND_SIZE);
        Game.fillCircle(g, x + rightXOff, y + rightYOff, Game.HAND_SIZE);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(0x322819));
        Game.drawCircle(g2, x + leftXOff, y + leftYOff, Game.HAND_SIZE);
        Game.drawCircle(g2, x + rightXOff, y + rightYOff, Game.HAND_SIZE);
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

    public double getDirection() {
        return direction;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setDirection(int direction) {
        this.direction = direction;
        direcRadian = Math.toRadians(direction);
        if(gun != null) {
            gun.setDirection(direction);
        }
    }
}
