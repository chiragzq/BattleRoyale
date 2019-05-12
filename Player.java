import java.awt.*;
import java.util.*;

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
    private static final int TOTAL_ARM_EXTEND = (int)(Game.GAME_SCALE *20);

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
    private Map<Integer, Gun> guns;
    private int equipped; //-1 for fists

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
        guns = new HashMap<Integer, Gun>();
        equipped = -1;
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
     * This draws the player
     * @param g the graphics
     */
    public void draw(Graphics g) {
        if(equipped != -1) {
            guns.get(equipped).draw(g);
        }

        g.setColor(new Color(0xFAC47F));
        Game.fillCircle(g, x, y, Game.PLAYER_SIZE);
    }

    public void drawHands(Graphics g) {
        g.setColor(new Color(0xFAC47F));
        double handExtendRight = 0;
        double handExtendLeft = 0;
        
        double directionRad = direcRadian;
        double rightDir = directionRad + Math.PI / 5;
        double leftDir = directionRad - Math.PI / 5;
        
        isCurrentlyPunching = false;

        if(equipped == -1)
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
            Gun gun = guns.get(equipped);
            rightDir = gun.getRightDir(directionRad);
            leftDir = gun.getLeftDir(directionRad);
            handExtendLeft = gun.extendLeft();
            handExtendRight = gun.extendRight();
        }
        
        if(isCurrentlyPunching)
        {
            if(isRightPunching)
                rightDir -= Math.PI * handExtendRight / TOTAL_ARM_EXTEND / 6;
            else
                leftDir += Math.PI * handExtendLeft / TOTAL_ARM_EXTEND / 6;
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

    public void drawWeaponSelections(Graphics g) {
        g.setColor(Color.BLACK);
        for(int i = 1;i <= 3;i ++) {
            g.drawRect((int)(Game.GAME_SCALE * (Game.GAME_WIDTH - 180)), (int)(Game.GAME_SCALE * (Game.GAME_HEIGHT - i * 120 - 40)), (int)(140 * Game.GAME_SCALE), (int)(Game.GAME_SCALE * 120));
            g.drawString("" + (3 - i + 1), (int)(Game.GAME_SCALE * (Game.GAME_WIDTH - 170)), (int)(Game.GAME_SCALE * (Game.GAME_HEIGHT - i * 120)));
        }

        int index = equipped == -1 ? 3 : equipped;
        g.setColor(new Color(0, 0, 0, 0.3f));
        g.fillRect((int)(Game.GAME_SCALE * (Game.GAME_WIDTH - 180)), (int)(Game.GAME_SCALE * (Game.GAME_HEIGHT - index * 120 - 40)), (int)(140 * Game.GAME_SCALE), (int)(Game.GAME_SCALE * 120));
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

    /**
     * gets healet
     */
    public int getHealth()
    {
        return health;
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
     * Gets the gun that the player has equipped.
     * @return the equipped gun, or null if the player is wielding fists
     */
    public Gun getEquippedGun() {
        return guns.get(equipped);
    }

    public void setEquippedIndex(int index) {
        equipped = index == 3 ? -1 : index;
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

    /**
     * Sets the number of bullets
     * @param remove how many to add
     */
    public void setBullets(int remove)
    {
        bulletLoad += remove;
    }


    public void setDirection(int direction) {
        this.direction = direction;
        direcRadian = Math.toRadians(direction);
        if(equipped != -1) {
            guns.get(equipped).setDirection(direction);
        }
    }
}
