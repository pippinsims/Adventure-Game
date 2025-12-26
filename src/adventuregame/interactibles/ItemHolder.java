package adventuregame.interactibles;

import adventuregame.Interactible;
import adventuregame.Room;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;

public class ItemHolder extends Interactible {
    
    public Item item;

    public ItemHolder(Item i, Room r, String preposition, String location)
    {
        if(i.isDynamicItem()) throw new UnsupportedOperationException("DynamicItem x must be in a room as x.interactible, not ItemHolder(x.item)");
        setDefaults(
            i.getName(),
            i.getDescription(),
            preposition, 
            i.getPluralDescription(), 
            "", 
            "Take", 
            "from", 
            new String[0], 
            new String[0]
        );

        item = i;
        locReference = location;
        myRoom = r;
        r.add(this);
    }

    @Override
    public void action(Unit u)
    {
        System.out.println(u.getName() + " took " + name + " " + actLocPrep + " " + locReference);
        myRoom.remove(this);
        u.getInventory().add(item);
    }

    @Override
    public void inspect()
    {
        System.out.println(name + ": " + description);
    }
}
