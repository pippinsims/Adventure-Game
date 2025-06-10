package adventuregame.interfaces;
import adventuregame.Inventory;

//FOR PLAYER, FOLLOWERS, NPCS, AND ENEMIES

public interface Unit extends Describable
{
    public boolean performAction(int i);
    public boolean receiveDamage(int damage, String type);
    
    public float getHealth();
    public Inventory getInventory();
    public int getAttackDamage();
    public int getWisdom();
    public String getName();
}
