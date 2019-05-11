
/**
 * Write a description of class Punch here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Punch implements Weapon
{
    private static final int DAMAGE = 20;
    private Player player;
    
    public Punch(Player play)
    {
        player = play;
    }
    
    public void punch()
    {
        player.punch();
    }
    
    public int getX()
    {
        return player.xPunch();
    }
    
    public int getY()
    {
        return player.yPunch();
    }
    
    public boolean isPunching()
    {
        return player.isPunching();
    }
    
    public int doDamage()
    {
        return DAMAGE;
    }
    
    public boolean isExtended()
    {
        return player.isExtended();
    }
}