
/**
 * Write a description of class Punch here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Punch implements Weapon
{
    private Player player;
    
    public Punch(Player play)
    {
        player = play;
    }
    
    public void punch()
    {
        player.punch();
    }
}