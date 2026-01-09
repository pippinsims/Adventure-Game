package adventuregame.abstractclasses;
import java.util.ArrayList;

import adventuregame.Damage;
import adventuregame.Effectable;
import adventuregame.Inventory;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.interactibles.wallinteractibles.Door;

//FOR PLAYER, FOLLOWERS, NPCS, AND ENEMIES

public abstract class Unit extends Effectable
{   
    public boolean hasLongHair = false;
    public boolean needsGlasses = false;
    public boolean canPickLocks = false;

    public String pronounsubj = "they", pronounobj = "them", possessiveadj = "their", possessivepro = "theirs", contraction = "they're";

    protected void male()
    {
        pronounsubj = "he"; pronounobj = "him"; possessiveadj = "his"; possessivepro = "his"; contraction = "he's"; 
    }
    protected void female()
    {
        pronounsubj = "she"; pronounobj = "her"; possessiveadj = "her"; possessivepro = "hers"; contraction = "she's"; 
    }

    public ArrayList<String> attributes = new ArrayList<>();
    protected Door lastDoor = null;
    public void setLastDoor(Door d) { lastDoor = d; }
    public Door lastDoor() { return lastDoor; }

    public abstract void updateUnit();
    
    protected Inventory.Whole inv;
    public abstract Inventory.Whole getInventory();
    public abstract int getWisdom();

    protected Room myRoom;
    public Room getRoom() { return myRoom; }
    public void setRoom(Room r) { myRoom = r; } //don't use this, it is called in Room.add(Unit)
    public void attack(Unit targ, Damage d, String msg)
    { 
        if(targ instanceof NonPlayer && !((NonPlayer)targ).enemies.contains(this)) ((NonPlayer)targ).enemies.add(this);
        Utils.slowPrintln(msg);
        targ.receiveDamage(d); 
    }

    protected String deathMsg;
    public String getDeathMessage() { return deathMsg; }
}
