package adventuregame.interactibles.wallentities;

import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.WallEntity;

public class Door extends WallEntity
{
    Room myOtherRoom;

    public Door(Room room1, Room room2, Wall wall)
    {
        myRoom = room1;
        myRoom.add(this);

        myOtherRoom = room2;
        myOtherRoom.add(this);
        
        this.wall = wall;

        setDefaults(
            "Door", 
            "door", 
            "that leads through",
            "doors", 
            "that lead through", 
            "Use",
            "",
            new String[]{"ordinary ol\' creaky slab o\' wood" , "regular ol\' creaky plank" , "unassuming, decrepit wooden door" , "Boris"  , "doors"},
            new String[]{"ordinary ol\' creaky slabs o\' wood", "regular ol\' creaky planks", "unassuming, decrepit wooden doors", "Borises", "doorses"}
        );

        setLocationReference();
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
        switch (wall) 
        {
            case SOUTH: return Wall.NORTH;
            case WEST : return Wall.EAST;
            case NORTH: return Wall.SOUTH;
            case EAST : return Wall.WEST;
            default   : return wall;
        } 
    }

    @Override
    public void inspect()
    {
        Utils.slowPrintln("You take a closer look at this gate-esque object and you notice that it is made of poplar wood, and has marks in it, as if from a sword.");
    }

    @Override
    public void action(Unit u)
    {
        Utils.slowPrint("you used the " + getDescription());
        
        Room r = u.getRoom();
        r.remove(u);
        u.setRoom(getNextRoom(r));
        u.getRoom().add(u);
    }

    public Room getNextRoom(Room curRoom)
    {
        if(curRoom == myRoom)
            return myOtherRoom;
        else if(curRoom == myOtherRoom) 
            return myRoom;
        else
            return null;
    }
}
