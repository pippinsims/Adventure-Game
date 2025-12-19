package adventuregame.abstractclasses;
import adventuregame.Damage;
import adventuregame.Effectable;
import adventuregame.Inventory;
import adventuregame.Room;

//FOR PLAYER, FOLLOWERS, NPCS, AND ENEMIES

public abstract class Unit extends Effectable
{
    public abstract void updateUnit() throws Exception;
    
    public abstract Inventory getInventory();
    public abstract Damage getAttackDamage();
    public abstract int getWisdom();
    public abstract String getName();
    public abstract Room getRoom();
    public abstract void attack(Unit targ, Damage d);

    public String deathMsg;
    public String getDeathMessage() { return deathMsg; }
}
