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
    private static final int TOTAL_PUNCH_TIME = (int)(300);
    //In Milli seconds
    private static final int TOTAL_ARM_EXTEND = (int)(Game.GAME_SCALE *20);

    private int x;
    private int y;
    private int health;
    private int totalHealth;
    private double direcRadian;
    private double direction;
    private long lastPunchTime;
    private boolean isCurrentlyPunching;
    private boolean isRightPunching;
    private int xPunch;

    private boolean healing;

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
    
    private int bandages;
    private int medkits;
    private int helmet;
    private int chestplate;
    private int redAmmo;
    private int blueAmmo;
    private int clip;
    private int scope;

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
        bandages = 0;
        medkits = 0;
        helmet = 0;
        chestplate = 0;
        totalHealth = 100;
        redAmmo = 0;
        blueAmmo = 0;
        scope = 1;
    }

    public void setBlueAmmo(int k) 
    {
        blueAmmo = k;
    }
    
    public void setRedAmmo(int k) {
        redAmmo = k;
    }
    public void setBandages(int k)
    {
        bandages = k;
        
    }
    
    public int getBandages()
    {
        return bandages;
    }

    public int getTotalHealth()
    {
        return totalHealth;
    }

    public void setTotalHealth(int k)
    {
        totalHealth = k;
    }
    
    public void setMedkits(int k)
    {
        medkits = k;
    }
    
    public int getMedkits()
    {
        return medkits;
    }

    public void setHelmet(int k)
    {
        helmet = k;
    }

    public void setChestplate(int k) {
        chestplate = k;
    }

    public void setScope(int k) {
        scope = k;
        if(scope == 2)
            Game.GAME_SCALE = Game.Scope.X2.getScale();
        else if(scope == 4)
            Game.GAME_SCALE = Game.Scope.X4.getScale();
        else if(scope == 8)
            Game.GAME_SCALE = Game.Scope.X8.getScale();
        else if(scope == 15)
            Game.GAME_SCALE = Game.Scope.X15.getScale();
    }

    public int getScope() {
        return scope;
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
    
    public void drawMinimap(Graphics g, Map<Integer, Obstacle> map)
    {
        Set<Integer> set = map.keySet();
        
        double scale = 0.18;
        int xShift = 300;
        int yShift = 0;
        int l = 20;
        g.setColor(new Color(214, 158, 45));
        g.fillRect(xShift - l, yShift - l, (int)(Game.MAX_X * scale) + 2 *l,(int)(Game.MAX_Y * scale) + 2*l);
        g.setColor(new Color(0x7DAE58));
        g.fillRect(xShift, yShift, (int)(Game.MAX_X * scale),(int)(Game.MAX_Y * scale));
        for(Integer ob : set)
        {
            map.get(ob).draw(g, scale, xShift, yShift);
        }
        g.setColor(Color.RED);
        Game.fillCircle(g, (int)(getX() * scale) + xShift, (int)(getY() * scale) + yShift, (int)(Game.PLAYER_SIZE * scale), 1);
    }

    /**
     * This draws the player
     * @param g the graphics
     */
    public void draw(Graphics g, int xShift, int yShift) {
        g.setColor(new Color(0xFAC47F));
        Game.fillCircle(g, x + xShift, y + yShift, Game.PLAYER_SIZE);
        Graphics2D g2 = (Graphics2D) g;
        if(chestplate > 0) {
            g2.setStroke(new BasicStroke((int)(Game.GAME_SCALE*4)));
            if(chestplate == 1) {
                g2.setColor(new Color(209, 209, 209));
            }
            else if(chestplate == 2)
                g2.setColor(new Color(140, 140, 140));
            else if(chestplate == 3)
                g2.setColor(new Color(0, 0, 0));
            Game.drawCircle(g, x + xShift, y + yShift, Game.PLAYER_SIZE);
        }
        if(helmet > 0) {
            int s = (int)(Game.PLAYER_SIZE/1.7);
            int shi = Game.PLAYER_SIZE/6;
            if(helmet == 1)
                g2.setColor(new Color(54, 165, 209));
            else if(helmet == 2)
                g2.setColor(new Color(127, 127, 127));
            else if(helmet == 3)
                g2.setColor(new Color(0 ,0, 0));
            double an = direcRadian;
            Game.fillCircle(g, x + xShift - (int)(shi * Math.cos(an)), y + yShift - (int)(shi * Math.sin(an)), s);
        }
    }

    public void drawGun(Graphics g, int xShift, int yShift) {
        if(equipped != -1) {
            guns.get(equipped).draw(g, (int)(Game.GAME_SCALE * xShift), (int)(Game.GAME_SCALE * yShift));
        }
    }

    public void drawEssentials(Graphics g)
    {
        drawWeaponSelections(g);
        drawReload(g);
        drawHealth(g);
        drawAmmoCount(g);
        drawPack(g);
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
            g2.drawString(healing ? "Healing..." : "Reloading...", x - widthRect/5, y + (int)(2.3 *heightRect));
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

        int length = (int)((double)health/totalHealth * (healthBarWidth - outline * 2));
        
        g2.setColor(Color.WHITE);
        
        if(health < 50)
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
            int j = clip;//guns.get(equipped).getAmmo();
            double length = (double)numDigits(j)/2;

            g2.drawString(""+clip, (int)( x - letterSize/2 * length), y + boxHeight/3);

            g.setFont(new Font("Arial", 30, (int)((double)letterSize/2.5)));

            if(guns.get(equipped).getType().equals("red")) {
                int k = redAmmo;
                double lengthOf = (double)numDigits(k)/2;
                g2.drawString(""+ redAmmo, (int)( x + boxWidth/2 + boxWidth/10- letterSize/4 * lengthOf + boxWidth/4), y + boxHeight/3 -boxHeight/5 + boxHeight/9);
            }
            else {
                int k = blueAmmo;
                double lengthOf = (double)numDigits(k)/2;
                
            
                g2.drawString(""+ blueAmmo, (int)( x + boxWidth/2 + boxWidth/10- letterSize/4 * lengthOf + boxWidth/4), y + boxHeight/3 -boxHeight/5 + boxHeight/9);
            }
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
        g2.setStroke(new BasicStroke((int)(Game.GAME_SCALE*2)));
        g2.setColor(new Color(0x322819));
        Game.drawCircle(g2, x + leftXOff + xShift, y + leftYOff + yShift, Game.HAND_SIZE);
        Game.drawCircle(g2, x + rightXOff + xShift, y + rightYOff + yShift, Game.HAND_SIZE);
    }
    
    public void drawPack(Graphics g)
    {
        int startHeight = Game.GAME_HEIGHT/4;
        int width = Game.GAME_WIDTH;
        int increment = Game.GAME_WIDTH/9;
        int vincrement = Game.GAME_HEIGHT/26;

        int aincrement = Game.GAME_HEIGHT/30;

        int between = 20;
        int thickness = 60;
        int height = 30;
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        
        Game.drawImageNoScale(g2, "scope" + scope, Game.GAME_WIDTH/2 - height, height, height, height);

        g2.drawString("" + bandages, width - thickness, startHeight + height + vincrement/2 - height/9);
        Game.drawImageNoScale(g2, "bandage", width - thickness/2 - thickness/6, startHeight + height - 2 * height/5 + vincrement - aincrement, thickness/2, height);
        
        g2.drawString("" + medkits, width - thickness, startHeight + 2 * height + between);
        Game.drawImageNoScale(g2, "medkit", width - thickness/2 - thickness/6, startHeight + 2 * height + between - 3 * height/5 + vincrement - aincrement, thickness/2, height);
        
        int numDigits = numDigits(blueAmmo);
        g2.drawString("" + blueAmmo, width - thickness - numDigits * 5, startHeight + 3 * height + 2 * between - 5* height/5 + vincrement + height/10);
        Game.drawImageNoScale(g2, "blueAmmo", width - thickness/2 - thickness/6, startHeight + 3 * height + 2 * between - 4* height/5 + vincrement - aincrement, thickness/2, height);

        numDigits = numDigits(redAmmo);
        g2.drawString("" + redAmmo, width - thickness - numDigits * 5, startHeight + 4 * height + 2 * between + height/3 + height/10);
        Game.drawImageNoScale(g2, "redAmmo", width - thickness/2 - thickness/6, startHeight + 4 * height + 3 * between - 5* height/5 + vincrement - aincrement, thickness/2, height);

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
                Game.drawImage(g, name, (int)(1/Game.GAME_SCALE * (Game.GAME_WIDTH - 140)) + xShift, (int)(1/Game.GAME_SCALE * (Game.GAME_HEIGHT - i * 100 - 40)) + yShift, (int)(1/Game.GAME_SCALE * iWidth), (int)(1/Game.GAME_SCALE * iLength));
            //g.drawRect((int)(Game.GAME_SCALE * (Game.GAME_WIDTH - 140)), (int)(Game.GAME_SCALE * (Game.GAME_HEIGHT - i * 100 - 40)), (int)(120 * Game.GAME_SCALE), (int)(Game.GAME_SCALE * 100));
            g.drawString("" + (3 - i + 1), (int)(1 * (Game.GAME_WIDTH - 130)), (int)((1 * (Game.GAME_HEIGHT - i * 100)) - 15 * 1));
        }

        int index = equipped == -1 ? 3 : equipped;
        g.setColor(new Color(0, 0, 0, 0.3f));
        g.fillRect((int)(1 * (Game.GAME_WIDTH - 140)), (int)(1 * (Game.GAME_HEIGHT - (3 - index + 1) * 100 - 40)), (int)((120 * 1) + 20*1), (int)(1 * 100));
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

    public void setReloading(int duration, boolean healing) {
        this.healing = healing;
        lastReloadTime = System.currentTimeMillis();
        reloadDuration = duration;
    }

    public void setEquippedIndex(int index) {
        equipped = index == 3 ? -1 : index;
    }

    public void updateClip(int index, int clip) {
        this.clip = clip;
        guns.get(index).setClip(clip);
    }


    public void updateAmmo(int index, int clip, int blue, int red) {
        Gun gun = guns.get(index);
        if(gun == null) {
            return;
        }
        this.clip = clip;
        gun.setClip(clip);

        setBlueAmmo(blue);
        setRedAmmo(red);
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
