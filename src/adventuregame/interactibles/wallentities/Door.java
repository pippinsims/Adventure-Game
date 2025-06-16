package adventuregame.interactibles.wallentities;

import java.util.Random;

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
        myRoom.addDoor(this);

        myOtherRoom = room2;
        myOtherRoom.addDoor(this);
        
        generateDescription();
        loc = wall;
        name = "Door";
        locationConjunction = (loc != Wall.NORTH) ? "that leads through" : "of";
        actionVerb = "Use";
    }

    @Override
    public Wall getWall()
    {
        throw new RuntimeException("Door can't use this method");
    }

    public Wall getWall(Room room)
    {
        if(room.equals(myRoom))
            return loc;
        else if(room.equals(myOtherRoom)) 
            return complementOf(loc);
        else
            throw new RuntimeException("urk, you plugged in a room this door wasn't in, in getWall");
    }

    private Wall complementOf(Wall wall)
    {
        switch (wall) {
            case SOUTH:
                return Wall.NORTH;
            case WEST:
                return Wall.EAST;
            case NORTH:
                return Wall.SOUTH;
            case EAST:
                return Wall.WEST;

            default:
                return wall;
        } 
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

    private void generateDescription()
    {
        switch (new Random().nextInt(5)) {
            case 0:
                description = "ordinary ol\' creaky slab o\' wood";
                break;
            
            case 1:
                description = "regular ol\' creaky plank";
                break;
            case 2:
                description = "unassuming, decrepit wooden door";
                break;
            case 3:
                description = "Boris";
                break;

            case 4:
                description = "doors";
                break;
        
            default:
                break;
        }
    }

    public Room getNextRoom(Room curRoom)
    {
        if(curRoom.equals(myRoom))
            return myOtherRoom;
        else if(curRoom.equals(myOtherRoom)) 
            return myRoom;
        else
            throw new RuntimeException("urk, you plugged in a room this door wasn't in");
    }

    @Override
    public boolean isDoor()
    {
        return true;
    }
}
