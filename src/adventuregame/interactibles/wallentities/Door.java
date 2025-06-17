package adventuregame.interactibles.wallentities;

import java.util.Random;

import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.WallEntity;

public class Door extends WallEntity
{
    Room myOtherRoom;
    String pDes;

    public Door(Room room1, Room room2, Wall wall)
    {
        myRoom = room1;
        myRoom.addDoor(this);

        myOtherRoom = room2;
        myOtherRoom.addDoor(this);
        
        generateDescription();
        this.wall = wall;
        name = "Door";
        normLocPrep = (wall != Wall.NORTH) ? "that leads through" : "of";
        actLocPrep = normLocPrep;
        actionVerb = "Use";
        setLocationReference();
    }

    public Wall getWall(Room room)
    {
        if(room.equals(myRoom))
            return wall;
        else if(room.equals(myOtherRoom)) 
            return complementOf(wall);
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
                pDes = "ordinary ol\' creaky slabs o\' wood";
                break;
            
            case 1:
                description = "regular ol\' creaky plank";
                pDes = description + "s";
                break;
            case 2:
                description = "unassuming, decrepit wooden door";
                pDes = description + "s";
                break;
            case 3:
                description = "Boris";
                pDes = description + "es";
                break;

            case 4:
                description = "doors";
                pDes = description + "es";
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

    @Override
    public String getPluralDescription()
    {
        return pDes;
    }
}
