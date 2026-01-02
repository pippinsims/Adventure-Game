package adventuregame.interactibles.wallinteractibles;

import adventuregame.Game;
import adventuregame.Player;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.WallInteractible;

public class Door extends WallInteractible
{
    //TODO add doors locking from 1 or both sides, then make doors lock when in combat
    //TODO add door diagram for rooms with multiple doors on the same wall like this:
    /*
        X6---X
        |    1
        5    |
        4    |
        X--32X
     */
    Room myOtherRoom;
    static int doornum = 0;

    public Door(Room room1, Room room2, Wall wall)
    {
        setDefaults(
            "Door", 
            "door", 
            "that leads through",
            "doors", 
            "that lead through", 
            "Use",
            ""
        );
        int r = Utils.rand.nextInt(5);
        descMap.put("Laur", new String[]{"ordinary ol\' creaky slab o\' wood" , "regular ol\' creaky plank" , "unassuming, decrepit wooden door" , "Boris"  , "doors"}[r]);
        pDescMap.put("Laur", new String[]{"ordinary ol\' creaky slabs o\' wood", "regular ol\' creaky planks", "unassuming, decrepit wooden doors", "Borises", "doorses"}[r]);

        name = "door"+doornum++;

        myRoom = room1;
        myRoom.add(this);

        myOtherRoom = room2;
        myOtherRoom.add(this);
        
        this.wall = wall;

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
        else if(room != myRoom) throw new RuntimeException("urk, you plugged in a room this door wasn't in, in getWall");
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
    public void setInspects()
    {
        put("You take a closer look at this gate-esque object and you notice that it is made of poplar wood, and has marks in it, as if from a sword.");
    }

    @Override
    public void inspect(Unit u)
    {
        Utils.slowPrint("You peek through the door. ");
        Game.printInfo(getNextRoom(u.getRoom()), true);
    }

    @Override
    public void action(Unit u)
    {
        Room r = u.getRoom();
        if(r != myRoom && r != myOtherRoom) throw new UnsupportedOperationException();

        Utils.slowPrint("you used " + (Game.isLaur && getDescription().equals("Boris") ? "" : "the ") + getDescription());
        
        r.remove(u);
        getNextRoom(r).add(u);

        if(u instanceof Player)
        {
            Player p = ((Player)u);
            if(p.doorMoves-- > 0)
            {
                p.setActions();
                p.ableToAct = true;
            }
        }
    }

    final public Room getNextRoom(Room room)
    {
        if(room == myRoom)
            return myOtherRoom;
        else if(room == myOtherRoom) 
            return myRoom;

        System.out.println(name + ": " + description + " doesn't contain " + room.getName() + ": " + room.getDescription());
        System.out.println(name + ": " + description + " contains both " + myRoom.getName() + ": " + myRoom.getDescription() + " and " + myOtherRoom.getName() + ": " + myOtherRoom.getDescription());
        throw new UnsupportedOperationException("Door d.getNextRoom(Room x) requires x to be in d");
    }
}
