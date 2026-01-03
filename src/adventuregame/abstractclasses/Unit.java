package adventuregame.abstractclasses;
import java.util.ArrayList;

import adventuregame.Damage;
import adventuregame.Effectable;
import adventuregame.Inventory;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.items.Armor;

//FOR PLAYER, FOLLOWERS, NPCS, AND ENEMIES

public abstract class Unit extends Effectable
{   
    public ArrayList<String> attributes = new ArrayList<>();

    public abstract void updateUnit();
    
    public abstract Inventory getInventory();
    public abstract int getWisdom();

    protected Room myRoom;
    public Room getRoom() { return myRoom; }
    public void setRoom(Room r) { myRoom = r; } //don't use this, it is called in Room.add(Unit)
    public void attack(Unit targ, Damage d, String msg)
    { 
        if(targ instanceof NonPlayer && !(targ instanceof Enemy)) ((NonPlayer)targ).enemies.add(this);
        Utils.slowPrintln(msg);
        targ.receiveDamage(d); 
    }

    protected String deathMsg;
    public String getDeathMessage() { return deathMsg; }

    protected Armor held = null;
}
