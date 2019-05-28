import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * The Player the person controls :)
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

    private long lastReloadTime;
    //Milliseconds
    private int reloadDuration;
    //Milliseconds
    private boolean isReloading;

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

        lastReloadTime = 0;
        reloadDuration = 0;
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
        int xShift = Game.GAME_WIDTH/2 - x;
        int yShift = Game.GAME_HEIGHT/2 - y;
        
        int xMouse = Game.getMouseX() - xShift;
        int yMouse = Game.getMouseY() - yShift;
    
        double xSide = xMouse - x;
        double ySide = yMouse - y;

        direcRadian = Math.atan2(ySide, xSide);
        direction = (Math.atan2(ySide, xSide) / Math.PI * 180);
        //System.out.println(direcRadian);
        //System.out.println(xSide + " " + ySide + ": " + direcRadian + ", " + direction);
    }

    /**
     * This draws the player
     * @param g the graphics
     */
    public void draw(Graphics g, int xShift, int yShift) {
        g.setColor(new Color(0xFAC47F));
        Game.fillCircle(g, x + xShift, y + yShift, Game.PLAYER_SIZE);
    }

    public void drawGun(Graphics g, int xShift, int yShift) {
        if(equipped != -1) {
            guns.get(equipped).draw(g, xShift, yShift);
        }
    }

    public void drawEssentials(Graphics g)
    {
        drawWeaponSelections(g);
        drawReload(g);
        drawHealth(g);
        drawAmmoCount(g);
    }

    public void drawReload(Graphics g)
    {
        long k = System.currentTimeMillis() - lastReloadTime;
        if(k < reloadDuration)
        {
            g.setFont(new Font("Arial", 20, 20));

            int width = 60;
            int height = 60;
            int x = Game.GAME_WIDTH/2 + width/5;
            int y= Game.GAME_HEIGHT/2 - Game.GAME_HEIGHT/5;
            double a = (double)(reloadDuration -k);
            double angle = a/reloadDuration * 360;

            int stroke = 5;

            Graphics2D g2 = (Graphics2D)g;
            g2.setColor(new Color(61, 68, 68, 100));
            g2.fillOval((int)(x - width/5), (int)(y -(int)((double) height/6 * 3.9) + stroke/2), width - stroke/4, height - stroke / 4);
            g.setColor(Color.WHITE);
            g.drawString("" + ((double)((int)((double)(reloadDuration -k)/100)))/10, x + width/13, y - height/40);

            g2.setStroke(new BasicStroke(stroke));
            g2.drawArc(x - width/5, y -(int)((double) height/6 * 3.9), width, height, 90, -360 + (int)angle);

            int widthRect = 100;
            int heightRect = 30;
            g2.setColor(new Color(61, 68, 68, 100));

            g2.fillRect(x - (int)((double)widthRect / 3.7), y + (int)((double)1.6*heightRect), widthRect, heightRect);
            g.setFont(new Font("Arial", 20, 17));
            g2.setColor(Color.WHITE);
            g2.drawString("Reloading...", x - widthRect/5, y + (int)(2.3 *heightRect));
        }
    }

    public void drawHealth(Graphics g)
    {
        int healthBarWidth = 400;
        int healthBarHeight = 30;
        int x = Game.GAME_WIDTH/2;
        int y = Game.GAME_HEIGHT - Game.GAME_HEIGHT/30;

        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(new Color(61, 68, 68, 100));

        g2.fillRect(x - healthBarWidth/2, y - healthBarHeight/2, healthBarWidth, healthBarHeight);

        int outline = 5;

        int length = (int)((double)health/100 * (healthBarWidth - outline * 2));
        
        g2.setColor(Color.WHITE);
        
        if(health < 30)
        {
            g2.setColor(new Color(255, (int)(255/Math.pow(x, 1)), (int)(255/Math.pow(x, 1))));
        }
        g2.fillRect(x - healthBarWidth/2 + outline, y - healthBarHeight/2 + outline, length, healthBarHeight - 2 * outline);
    }

    public void drawAmmoCount(Graphics g)
    {
        if(equipped != -1)
        {
            Graphics2D g2 = (Graphics2D)g;
            g2.setColor(new Color(61, 68, 68, 100));

            int x = Game.GAME_WIDTH/2;
            int y= Game.GAME_HEIGHT - Game.GAME_HEIGHT/10;
            int boxWidth = 100;
            int boxHeight = 50;
            g2.fillRect(x - boxWidth/2, y - boxHeight/2, boxWidth, boxHeight);

            g2.fillRect(x + boxWidth/2 + boxWidth/10, y -boxHeight/4, boxWidth/2, boxHeight*2/3);

            int letterSize = 50;
            g.setFont(new Font("Arial", 30, letterSize));
            g2.setColor(Color.WHITE);
            int j = guns.get(equipped).getAmmo();
            double length = (double)numDigits(j)/2;

            g2.drawString(""+guns.get(equipped).getAmmo(), (int)( x - letterSize/2 * length), y + boxHeight/3);

            g.setFont(new Font("Arial", 30, (int)((double)letterSize/2.5)));

            int k = guns.get(equipped).getTotalAmmo();
            double lengthOf = (double)numDigits(k)/2;

            g2.drawString(""+guns.get(equipped).getTotalAmmo(), (int)( x + boxWidth/2 + boxWidth/10- letterSize/4 * lengthOf + boxWidth/4), y + boxHeight/3 -boxHeight/5 + boxHeight/9);
        }
    }

    private int numDigits(int k)
    {
        int counter = 0;
        if(k == 0)
            counter++;

        while(k > 0)
        {
            counter++;
            k = k/10;
        }

        return counter;
    }

    public void drawHands(Graphics g, int xShift, int yShift) {
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
        

        Game.fillCircle(g, x + leftXOff + xShift, y + leftYOff + yShift, Game.HAND_SIZE);
        Game.fillCircle(g, x + rightXOff + xShift, y + rightYOff + yShift, Game.HAND_SIZE);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(0x322819));
        Game.drawCircle(g2, x + leftXOff + xShift, y + leftYOff + yShift, Game.HAND_SIZE);
        Game.drawCircle(g2, x + rightXOff + xShift, y + rightYOff + yShift, Game.HAND_SIZE);
    }

    public void drawWeaponSelections(Graphics g) {
        g.setColor(Color.BLACK);
        ((Graphics2D)g).setStroke(new BasicStroke(4));
        g.setFont(new Font("Arial", 20, 20));
        for(int i = 1;i <= 3;i ++) {
            String name = "";
            int iWidth = 200;
            int iLength = 100;
            int xShift = 0;
            int yShift = 0;
            if(guns.get(3 - i + 1) instanceof Shotgun)
            {
                name = "shotgun";
                iWidth = 162;
                iLength = 60;
                xShift = -5;
                yShift = 20;
            }
            else if(guns.get(3 - i + 1) instanceof Rifle)
            {
                name = "rifle";
                iWidth = 125;
                iLength = 100;
                xShift = 14;
                yShift = 0;
            }
            else if(guns.get(3 - i + 1) instanceof Pistol)
            {
                name = "pistol";
                iWidth = 100;
                iLength = 50;
                xShift = 25;
                yShift = 25;
            }
            else if(guns.get(3-i + 1) instanceof Sniper) {
                name = "sniper";
                iWidth = 120;
                iLength = 35;
                xShift = 14;
                yShift = 20;
            }
            else
            {
                name = "fist2";
                iWidth = 75;
                iLength = 75;
                xShift = 25;
                yShift = 15;
            }
            if(!(name.equals("fist2") && 4-i < 3)) 
                Game.drawImage(g, name, (int)(Game.GAME_SCALE * (Game.GAME_WIDTH - 140)) + xShift, (int)(Game.GAME_SCALE * (Game.GAME_HEIGHT - i * 100 - 40)) + yShift, iWidth, iLength);
            //g.drawRect((int)(Game.GAME_SCALE * (Game.GAME_WIDTH - 140)), (int)(Game.GAME_SCALE * (Game.GAME_HEIGHT - i * 100 - 40)), (int)(120 * Game.GAME_SCALE), (int)(Game.GAME_SCALE * 100));
            g.drawString("" + (3 - i + 1), (int)(Game.GAME_SCALE * (Game.GAME_WIDTH - 130)), (int)((Game.GAME_SCALE * (Game.GAME_HEIGHT - i * 100)) - 15 * Game.GAME_SCALE));
        }

        int index = equipped == -1 ? 3 : equipped;
        g.setColor(new Color(0, 0, 0, 0.3f));
        g.fillRect((int)(Game.GAME_SCALE * (Game.GAME_WIDTH - 140)), (int)(Game.GAME_SCALE * (Game.GAME_HEIGHT - (3 - index + 1) * 100 - 40)), (int)((120 * Game.GAME_SCALE) + 20*Game.GAME_SCALE), (int)(Game.GAME_SCALE * 100));
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
     * Gets the gun that the player has equipped.
     * @return the equipped gun, or null if the player is wielding fists
     */
    public Gun getEquippedGun() {
        return guns.get(equipped);
    }

    public Map<Integer, Gun> getGuns() {
        return guns;
    }

    public void setReloading(int duration) {
        lastReloadTime = System.currentTimeMillis();
        reloadDuration = duration;
    }

    public void setEquippedIndex(int index) {
        equipped = index == 3 ? -1 : index;
    }

    public void updateAmmo(int index, int clip, int spare) {
        Gun gun = guns.get(index);
        if(gun == null) {
            
            return;
        }

        gun.setClip(clip);
        gun.setSpare(spare);
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
        // faceCursor(); 
        // WHY CHIRAG!!!
        if(equipped != -1) {
            guns.get(equipped).setDirection(direction);
        }
    }
}
