package adventuregame.items;

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
        description = "door key";
    }

    public DoorKey(String code)
    {
        this.code = code;
        name = "Door key";
        description = "door key";
    }

    { types.add(Door.class); }
    
    @Override 
    public void action(Unit u, Describable d) {
        if(check(d))
        {
            Door door = (Door)d;
            if(door.getKey().equals(code))
            {
                if(door.isLocked(u.getRoom()) || door.isLocked(door.getNextRoom(u.getRoom())))
                {
                    if(door.isLocked(u.getRoom()))
                        door.toggleLock(u.getRoom());
                    if(door.isLocked(door.getNextRoom(u.getRoom())))
                        door.toggleLock(door.getNextRoom(u.getRoom()));
                    Utils.slowPrintln("Door unlocked!");
                }
                else
                {
                    door.toggleLock(door.getNextRoom(u.getRoom()));
                    door.toggleLock(u.getRoom());
                    Utils.slowPrintln("Door locked!");
                }
            }
        }
        else
            Utils.slowPrintln("This item can't be used for that!"); 
    }
}
