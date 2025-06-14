package adventuregame.interfaces;
import adventuregame.Damage;
import adventuregame.Effectable;
import adventuregame.Inventory;

//FOR PLAYER, FOLLOWERS, NPCS, AND ENEMIES

public interface Unit extends Describable 
{
    public Effectable.EffectUpdateResult receiveDamage(int damage, Damage.Type type);
    public void updateUnit();
    
    public float getHealth();
    public Inventory getInventory();
    public int getAttackDamage();
    public int getWisdom();
    public String getName();
}
