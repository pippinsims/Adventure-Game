package adventuregame.dynamicitems;

import adventuregame.Room;
import adventuregame.abstractclasses.DynamicItem;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.WallEntity;
import adventuregame.interactibles.wallentities.TorchInteractible;
import adventuregame.items.TorchItem;

public class Torch extends DynamicItem {

    public String name = "Torch";

    public Torch(Unit unit)
    {
        it = new TorchItem(this);
        in = new TorchInteractible(this, null, null);
        collectItem(unit);
    }

    public Torch(Room room, WallEntity.Wall wall)
    {
        it = new TorchItem(this);
        in = new TorchInteractible(this, room, wall);
        placeInteractible(room);
    }

    @Override public String getPluralDescription() { return "torches"; }

    @Override public String getDescription() { return "burning torch, providing light and warmth"; }

    @Override public String getName() { return name; }
}
