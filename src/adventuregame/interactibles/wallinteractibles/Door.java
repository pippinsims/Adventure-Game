package adventuregame.interactibles.wallinteractibles;

import adventuregame.Environment;
import adventuregame.Player;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.WallInteractible;

public class Door extends WallInteractible
{
    Room myOtherRoom;

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
        Utils.slowPrint("You peek through the door. ");
        Environment.printInfo(getNextRoom(Environment.curRoom), true);
    }

    @Override
    public void action(Unit u)
    {
        Room r = u.getRoom();

        Utils.slowPrint("you used " + (Environment.isLaur && getDescription().equals("Boris") ? "" : "the ") + getDescription());
        
        r.remove(u);
        getNextRoom(r).add(u);

        if(u instanceof Player)
        {
            Player p = ((Player)u);
            if(p.doorMoves-- > 0)
            {
                p.setActions();
                p.promptForAction();
            }
        }
    }

    final public Room getNextRoom(Room curRoom)
    {
        if(curRoom == myRoom)
            return myOtherRoom;
        else if(curRoom == myOtherRoom) 
            return myRoom;

        return null;
    }
}
