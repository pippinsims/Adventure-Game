package adventuregame.interactibles;

import adventuregame.Inventory;
import adventuregame.Room;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;

public class ItemHolder extends InventoryInteractible {

    public Item item;

    public ItemHolder(Item i, Room r, String preposition, String location)
    {
        if(i.isPlaceable()) throw new UnsupportedOperationException("DynamicItem x must be in a room as x.interactible, not ItemHolder(x.item)");
        setDefaults(
            i.getName(),
            i.getDescription(),
            preposition, 
            i.getPluralDescription(), 
            "", 
            "Take", 
            "from"
        );
        setInspects();

        inv = new Inventory(1);
        inv.add(i);
        locReference = location;
        myRoom = r;
        r.add(this);
        item = i;
    }

    @Override
    public void action(Unit u)
    {
        System.out.println(u.getName() + " took " + name + " " + actLocPrep + " " + locReference);

        myRoom.remove(this);
        u.getInventory().add(item);
    }

    @Override protected void setInspects()
    {
        put(name + ": " + description);
    }
}
