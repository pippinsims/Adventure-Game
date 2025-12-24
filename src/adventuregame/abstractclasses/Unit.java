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

    protected Room myRoom;
    public Room getRoom() { return myRoom; }
    public void setRoom(Room r) { myRoom = r; }
    public void attack(Unit targ, Damage d) { targ.receiveDamage(d); }

    protected String deathMsg;
    public String getDeathMessage() { return deathMsg; }
}
