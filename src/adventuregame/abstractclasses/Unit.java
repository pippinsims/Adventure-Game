package adventuregame.abstractclasses;
import adventuregame.Damage;
import adventuregame.Effectable;
import adventuregame.Inventory;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.items.Armor;

//FOR PLAYER, FOLLOWERS, NPCS, AND ENEMIES

public abstract class Unit extends Effectable
{
    public abstract void updateUnit();
    
    public abstract Inventory getInventory();
    public abstract Damage getAttackDamage();
    public abstract int getWisdom();

    protected Room myRoom;
    public Room getRoom() { return myRoom; }
    public void setRoom(Room r) { myRoom = r; } //don't use this, it is called in Room.add(Unit)
    public void attack(Unit targ, Damage d, String msg) 
    { 
        Utils.slowPrintln(msg);
        targ.receiveDamage(d); 
    }

    protected String deathMsg;
    public String getDeathMessage() { return deathMsg; }

    protected Armor held = null;
}
