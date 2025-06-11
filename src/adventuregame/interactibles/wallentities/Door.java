package adventuregame.interactibles.wallentities;

import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.interactibles.WallEntity;
import adventuregame.interfaces.Unit;

public class Door extends WallEntity
{
    Room myOtherRoom;
    public Door(Room room1, Room room2, Wall wall)
    {
        myRoom = room1;
        myOtherRoom = room2;
        myRoom.getInteractibles().add(this);
        myOtherRoom.getInteractibles().add(this);
        description = "ordinary ol\' creaky slab o\' wood";
        loc = wall;
        name = "Door";
        locationConjunction = (loc != Wall.NORTH) ? "that leads through" : "of";
        actionVerb = "Use";
    }

    @Override
    public void inspectInteractible()
    {
        Utils.slowPrintln("You take a closer look at this gate-esque object and you notice that it is made of poplar wood, and has marks in it, as if from a sword.");
    }

    @Override
    public void action(Unit u)
    {
        Utils.slowPrint("you used " + getDescription());
    }
}
