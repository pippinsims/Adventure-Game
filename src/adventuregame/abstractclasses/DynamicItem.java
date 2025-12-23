package adventuregame.abstractclasses;

import adventuregame.Interactible;
import adventuregame.Room;

public abstract class DynamicItem extends Describable {
    protected Item it;
    protected Interactible in;
    private Unit currentOwner;
    protected DynamicItem self = this;

    public void placeInteractible(Room room)
    {
        in.setRoom(room);
        room.add(in);
        if(currentOwner != null) currentOwner.getInventory().remove(it);
        currentOwner = null;
    }

    public void collectItem(Unit unit)
    {
        if(in.getRoom() != null) in.getRoom().remove(in);
        currentOwner = unit;
        currentOwner.getInventory().add(it);
    }
}
