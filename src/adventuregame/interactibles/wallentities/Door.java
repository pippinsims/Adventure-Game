package adventuregame.interactibles.wallentities;

import adventuregame.Environment;
import adventuregame.Interactible;
import adventuregame.Player;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.WallEntity;

public class Door extends WallEntity
{
    Room myOtherRoom;
    private boolean autoUse = false;

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

    public void autoUse()
    {
        autoUse = true;
    }

    @Override
    public void inspect()
    {
        Utils.slowPrintln("You take a closer look at this gate-esque object and you notice that it is made of poplar wood, and has marks in it, as if from a sword.");
    }

    @Override
    public void action(Unit u)
    {
        Room r = u.getRoom();
        
        if(!autoUse)
        {
            if(Utils.promptList("You can:", new String[] {"Peek", "Use"}) == 0)
            {
                Environment.printInfo(getNextRoom(r), true);
                Utils.slowPrint("Press Enter to continue");
                Utils.scanner.nextLine();
                if(u instanceof Player) ((Player)u).promptForAction();
                return;
            }
        }

        Utils.slowPrint("you used " + (Environment.isLaur && getDescription().equals("Boris") ? "" : "the ") + getDescription());
        
        for(Interactible i : r.interactibles) 
            {System.out.println(i.getDescription());
                if(i instanceof Door)
                {
                    System.out.println(": " + ((Door)i).myRoom.getDescription() + " to " + (((Door)i).myOtherRoom.getDescription()));
                }
            }
        r.remove(u);
        
        //TODO this breaks when you leave cledobl room with leave()
        Environment.printInfo(r, false); //prints cledobl room
        Environment.printInfo(myRoom, false); //prints hallway (6 west, 8 east???)
        Environment.printInfo(myOtherRoom, false); //prints window room?!?

        getNextRoom(r).add(u);
        autoUse = false;
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
