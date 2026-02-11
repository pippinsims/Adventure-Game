package adventuregame.abstractclasses;

import adventuregame.Interactible;
import adventuregame.Room;
public abstract class Placeable extends Describable {
    protected Item it;
    protected Interactible in;
    private Unit currentOwner;
    protected Placeable self = this;

    public void placeInteractible(Room room)
    {
        in.setRoom(room);
        room.add(in);
        if(currentOwner != null) 
        {
            currentOwner.getInventory().remove(it);
            System.out.println(currentOwner.getName());
        }
        currentOwner = null;
    }

    public void collectItem(Unit unit)
    {
        if(in.getRoom() != null) in.getRoom().remove(in);
        currentOwner = unit;
        currentOwner.getInventory().add(it);
    }

    public void transferOwnership(Unit unit) { currentOwner = unit; }

    public Item item() { return it; }
    public Interactible interactible() { return in; }

    protected abstract void init();
}
