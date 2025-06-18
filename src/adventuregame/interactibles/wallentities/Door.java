package adventuregame.interactibles.wallentities;

import java.util.Random;

import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.WallEntity;

public class Door extends WallEntity
{
    Room myOtherRoom;
    String rPDes;

    public Door(Room room1, Room room2, Wall wall)
    {
        myRoom = room1;
        myRoom.addDoor(this);

        myOtherRoom = room2;
        myOtherRoom.addDoor(this);
        
        description = "door";
        pluralDescription = description + "s";
        genRandomDescription();
        this.wall = wall;
        name = "Door";
        normLocPrep = "that leads through";
        actLocPrep = normLocPrep;
        actionVerb = "Use";
        setLocationReference();
    }

    protected void genRandomDescription()
    {
        String[] names = new String[]{"ordinary ol\' creaky slab o\' wood",
                                      "regular ol\' creaky plank",
                                      "unassuming, decrepit wooden door",
                                      "Boris",
                                      "doors"};
        String[] plurals = new String[]{"ordinary ol\' creaky slabs o\' wood",
                                        names[1]+"s",
                                        names[2]+"s",
                                        names[3]+"es",
                                        names[4]+"es"};
        int r = new Random().nextInt(names.length);
        rPDes = plurals[r];
        randomDescription = names[r];
    }

    public Wall getWall(Room room)
    {
        if(room == myRoom)
            return wall;
        else if(room == myOtherRoom) 
            return complementOf(wall);
        else
            throw new RuntimeException("urk, you plugged in a room this door wasn't in, in getWall");
    }

    public void setWall(Room room)
    {
        if(room == myOtherRoom)
        {
            Room temp = myRoom;
            myRoom = myOtherRoom;
            myOtherRoom = temp;

            wall = complementOf(wall);
            setLocationReference();
        }
        else if(room != myRoom)
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

    public Room getNextRoom(Room curRoom)
    {
        if(curRoom == myRoom)
            return myOtherRoom;
        else if(curRoom == myOtherRoom) 
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
        return pluralDescription;
    }
}
