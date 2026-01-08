package adventuregame.items;

import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Unit;
import adventuregame.abstractclasses.Item.Affector;
import adventuregame.interactibles.wallinteractibles.Door;

public class DoorKey extends Affector {
    
    final String code;

    public DoorKey()
    {
        code = "normal";
        name = "Door key";
        description = code + " door key";
    }

    public DoorKey(String code)
    {
        if(code.equals("bar")) throw new UnsupportedOperationException("No such thing as a doorbar key");
        this.code = code;
        name = "Door key";
        description = code + " door key";
    }

    { types.add(Door.class); }
    
    @Override 
    public void action(Unit u, Describable d) {
        if(check(d))
        {
            Door door = (Door)d;
            Room r = u.getRoom(), o = door.getNextRoom(r);
            if(door.getLocks(r).containsKey(code))
            {
                if(door.isLocked(r, code) || door.isLocked(o, code))
                {
                    door.unlock(code);
                    Utils.slowPrintln("Door unlocked!");
                }
                else
                {
                    door.lock(code);
                    Utils.slowPrintln("Door locked!");
                }
            }
        }
        else super.action(u, d);
    }
}
