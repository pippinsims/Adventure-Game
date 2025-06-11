package adventuregame.interactibles.wallentities;
import adventuregame.interfaces.Unit;
import adventuregame.items.TorchItem;

import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.interactibles.WallEntity;

public class TorchInteractible extends WallEntity {

    public TorchInteractible(Room room, Wall wall)
    {
        myRoom = room;
        myRoom.getInteractibles().add(this);
        description = "flaming stick";
        loc = wall;
        name = "Torch";
        locationConjunction = "on";
        actionVerb = "Take";
    }

    @Override
    public void inspectInteractible()
    {
        Utils.slowPrintln("You take a closer look at this flaming stick and you notice that it is a burning torch, providing light and warmth!");
    }

    @Override
    public void action(Unit u)
    {
        Utils.slowPrint("You have recieved a Torch!");
        u.getInventory().addItem(new TorchItem());
        myRoom.getInteractibles().remove(this);
    }
}
