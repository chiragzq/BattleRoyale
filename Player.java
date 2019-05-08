import java.awt.*;

/**
 * The Player the person controls
 * @author Aaron Lo
 * @version 5-6-19
 */
public class Player
{
    private static final int TOTAL_PUNCH_TIME = 300;
    //In Milli seconds
    private static final int TOTAL_ARM_EXTEND = 25;

    private int x;
    private int y;
    private int health;
    private int direction;
    private long lastPunchTime;
    private boolean isRightPunching;
    private boolean isCurrentlyPunching;

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

    /**
     * This faces the player towards the cursor
     */
    public void faceCursor()
    {
        int xMouse = Game.getMouseX();
        int yMouse = Game.getMouseY();

        double xSide = xMouse - x;
        double ySide = yMouse - y;

        direction = (int)(Math.atan2(ySide, xSide) / Math.PI * 180);
    }

    /**
     * Punches
     */
    public void punch()
    {
        if(!isCurrentlyPunching)
        {
            lastPunchTime = System.currentTimeMillis();
            isRightPunching = !isRightPunching;
        }
    }

    /**
     * This draws the player
     * @param g the graphics
     */
    public void draw(Graphics g) {
        faceCursor();

        g.setColor(new Color(0xFAC47F));
        Game.fillCircle(g, x, y, Game.PLAYER_SIZE);

        double directionRad = direction * Math.PI / 180;
        double rightDir = directionRad + Math.PI / 5;
        double leftDir = directionRad - Math.PI / 5;

        isCurrentlyPunching = false;

        double handExtendRight = 0;
        double handExtendLeft = 0;

        if(isRightPunching)
        {
            //This moves the right hand forward
            if((double)(System.currentTimeMillis() - lastPunchTime) < TOTAL_PUNCH_TIME/2)
            {
                double interval = TOTAL_PUNCH_TIME / 20;
                double extend = (double)TOTAL_ARM_EXTEND / 10;
                handExtendRight = ((double)(System.currentTimeMillis() - lastPunchTime) / interval * extend);
                isCurrentlyPunching = true;
                //This half animates the arm going forward
            }
            else if((double)(System.currentTimeMillis() - lastPunchTime) < TOTAL_PUNCH_TIME)
            {
                double interval = TOTAL_PUNCH_TIME / 20;
                double extend = (double)TOTAL_ARM_EXTEND / 10;
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
                double interval = TOTAL_PUNCH_TIME / 20;
                double extend = (double)TOTAL_ARM_EXTEND / 10;
                handExtendLeft = ((double)(System.currentTimeMillis() - lastPunchTime) / interval * extend);
                isCurrentlyPunching = true;
                //This half animates the arm going forward
            }
            else if((double)(System.currentTimeMillis() - lastPunchTime) < TOTAL_PUNCH_TIME)
            {
                double interval = TOTAL_PUNCH_TIME / 20;
                double extend = (double)TOTAL_ARM_EXTEND / 10;
                handExtendLeft = (((TOTAL_PUNCH_TIME - (double)(System.currentTimeMillis() - lastPunchTime))/interval) * extend);
                isCurrentlyPunching = true;
                //This half animates the arm going backwards
            }
            //This moves the arm
        }

        int leftXOff = (int)((Game.PLAYER_SIZE / 2 + Game.HAND_SIZE / 4 + handExtendLeft) * Math.cos(leftDir));
        int leftYOff = (int)((Game.PLAYER_SIZE / 2 + Game.HAND_SIZE / 4 + handExtendLeft) * Math.sin(leftDir));

        int rightXOff = (int)((Game.PLAYER_SIZE / 2 + Game.HAND_SIZE / 4 + handExtendRight) * Math.cos(rightDir));
        int rightYOff = (int)((Game.PLAYER_SIZE / 2 + Game.HAND_SIZE / 4 + handExtendRight) * Math.sin(rightDir));

        // System.out.println(x + leftXOff + " " + (y + leftYOff));
        Game.fillCircle(g, x + leftXOff, y + leftYOff, Game.HAND_SIZE);
        Game.fillCircle(g, x + rightXOff, y + rightYOff, Game.HAND_SIZE);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(0x322819));
        Game.drawCircle(g2, x + leftXOff, y + leftYOff, Game.HAND_SIZE);
        Game.drawCircle(g2, x + rightXOff, y + rightYOff, Game.HAND_SIZE);
    }
}
