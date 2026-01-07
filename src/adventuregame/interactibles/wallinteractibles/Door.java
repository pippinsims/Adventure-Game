package adventuregame.interactibles.wallinteractibles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adventuregame.Game;
import adventuregame.Player;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.Utils.Tuple;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.WallInteractible;

public class Door extends WallInteractible
{
    Room myOtherRoom;
    static int doornum = 0;
    private Map<Room, Boolean> lockMap = new HashMap<>();
    private String key;

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

        lockMap.put(myRoom, false);
        lockMap.put(myOtherRoom, false);

        key = "normal";
    }

    public void setKey(String key) { this.key = key; }

    public String getKey() { return key; }

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
        if(isLocked(u.getRoom())) Utils.slowPrintln("You attempt to peek through the door, but it's locked!");
        else
        {
            Utils.slowPrint("You peek through the door. ");
            Game.printInfo(getNextRoom(u.getRoom()), true);
        }
    }

    @Override
    public void action(Unit u)
    {
        if(isLocked(u.getRoom())) Utils.slowPrintln("You attempt to use the door, but it's locked!");
        else
        {
            Room r = u.getRoom();
            if(r != myRoom && r != myOtherRoom) throw new UnsupportedOperationException();

            Utils.slowPrint("you used " + (Game.isLaur && getDescription().equals("Boris") ? "" : "the ") + getDescription());
            
            r.remove(u);
            getNextRoom(r).add(u);

            if(u instanceof Player)
            {
                Player p = ((Player)u);
                if(p.doorMoves-- > 0) p.ableToAct = true;
            }

            u.setLastDoor(this);
        }
    }

    final public void toggleLock(Room r) 
    {
        if(lockMap.put(r, !lockMap.get(r)) == null) badRoomException(r);
    }

    final public boolean isLocked(Room r)
    {
        if(lockMap.containsKey(r)) return lockMap.get(r);
        
        badRoomException(r);
        return false;
    }

    final public Room getNextRoom(Room r)
    {
        if(r == myRoom) return myOtherRoom;
        else if(r == myOtherRoom) return myRoom;

        badRoomException(r);
        return null;
    }

    private final void badRoomException(Room r)
    {
        System.out.println(name + ": " + description + " doesn't contain " + r.getName() + ": " + r.getDescription());
        System.out.println(name + ": " + description + " contains both " + myRoom.getName() + ": " + myRoom.getDescription() + " and " + myOtherRoom.getName() + ": " + myOtherRoom.getDescription());
        throw new UnsupportedOperationException("Door d.getNextRoom(Room x) requires x to be in d");
    }

    public static class Diagram
    {
        /*  for a room with doors in the order: east, south, south, west, west, north
            ┌─6─┐
            4   1
            5   │
            └3─2┘
        */

        public Diagram(ArrayList<Door> doors, Unit cur)
        {
            ArrayList<Integer> n = new ArrayList<>(),
                               s = new ArrayList<>(),
                               e = new ArrayList<>(),
                               w = new ArrayList<>();
            for(int i = 0; i < doors.size(); i++) 
            {
                int num = cur.lastDoor() == doors.get(i) ? 0 : (i + 1);

                switch(doors.get(i).wall)
                {
                    case NORTH: n.add(num); break;
                    case SOUTH: s.add(num); break;
                    case EAST: e.add(num); break;
                    case WEST: w.add(num); break;
                    default: break;
                }
            }

            Tuple<String,String> ns = equalize(tos(n), tos(s));
            
            System.out.println("┌"+ns.t1+"┐");
            for(int i = 0; i < Math.max(e.size(), w.size()); i++)
            {
                String f = checkInd(w, i);
                System.out.print(f);
                for(int j = f.length() - 1; j < ns.t1.length(); j++) System.out.print(" ");
                System.out.println(checkInd(e, i));
            }
            System.out.println("└"+ns.t2+"┘");
        }

        private String checkInd(ArrayList<Integer> arr, int index)
        {
            if(index > arr.size() - 1) return "│";
            return checkVal(arr.get(index));
        }

        private String checkVal(Integer val)
        {
            return val == 0 ? "u" : val.toString();
        }

        private String tos(ArrayList<Integer> arr)
        {
            String out = "";
            for(int i : arr) out += checkVal(i) + (arr.indexOf(i) == arr.size() - 1 ? "" : "─"); //arr has no repeats, so this works to determine the last print
            return out;
        }

        private Tuple<String,String> equalize(String f, String s)
        {
            int dif = f.length() - s.length();
            int mag = Math.abs(dif);
            String start = "", end = "";
            for(int i = 0; i < mag; i++) if(i < mag/2) start += "─"; else end += "─";

            return dif > 0 ? new Tuple<>(f, start + s + end) : new Tuple<>(start + f + end, s);
        }
    }
}
