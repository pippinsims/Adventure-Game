package adventuregame.abstractclasses;
import adventuregame.Effectable;
import adventuregame.Inventory;
import adventuregame.Room;

//FOR PLAYER, FOLLOWERS, NPCS, AND ENEMIES

public abstract class Unit extends Effectable
{
    public abstract void updateUnit() throws Exception;
    
    public abstract Inventory getInventory();
    public abstract int getAttackDamage();
    public abstract int getWisdom();
    public abstract String getName();
    public abstract Room getRoom();
}
