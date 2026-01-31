package adventuregame.interactibles.wallinteractibles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adventuregame.Game;
import adventuregame.Player;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.Utils.Tuple;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.WallInteractible;
import adventuregame.items.Equippable;
import adventuregame.items.Equippable.Hat.Hairpin;

public class Door extends WallInteractible
{
    Room myOtherRoom;
    // private static int doornum = 0; //TODO make "playtest" functionality
    private ArrayList<Unit> disablers = new ArrayList<>();
    private final Map<Room, Map<String, Boolean>> lockMap;

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

        // name = "door"+doornum++; //TODO make "playtest" functionality

        this.wall = wall;

        myRoom = room1;
        myRoom.add(this);

        myOtherRoom = room2;
        myOtherRoom.add(this);

        setLocationReference();

        lockMap = Map.ofEntries(
            Map.entry(myRoom, new HashMap<>()), 
            Map.entry(myOtherRoom, new HashMap<>())
        );
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

    @Override
    public String getDescription() {
        return (isLocked(myRoom, "bar") ? "barred " : "") + super.getDescription();
    }

    @Override
    public String getPluralDescription() {
        return (isLocked(myRoom, "bar") ? "barred " : "") + super.getPluralDescription();
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
    
    public void disabler(Unit u)
    {
        disablers.add(u);
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
        Room r = u.getRoom();
        if(isLocked(r, "bar"))
        {
            Utils.slowPrintln("You unbar the door.");
            unlock(r, "bar");
            if(u instanceof Player) ((Player)u).ableToAct = true;
        }
        else if(isLocked(r))  
        {
            if(u.canPickLocks && Utils.contains(u.getInventory().items, Equippable.Hat.Hairpin.class))
            {
                Equippable.Hat.Hairpin h = null;
                for(Item i : u.getInventory().items) if(i instanceof Hairpin) { h = (Hairpin)i; break; }
                if(new Lockpick(u, h).outcome)
                {
                    if(h.level < 9) 
                    {
                        Utils.slowPrintln("You became more proficient at using this hairpin as a lock pick!");
                        h.level++; //TODO temporary leveling system
                    }
                    Utils.slowPrintln("You picked the lock!");
                    for(String k : lockMap.get(r).keySet()) if(!k.equals("bar")) { unlock(k); break; }
                }
                else
                    Utils.slowPrintln("You failed to pick the lock.");
            }
            else
            {
                Utils.slowPrintln("You attempt to use the door, but it's locked!");
                if(u instanceof Player) ((Player)u).ableToAct = true;
            }
        }
        else if((isLocked(getNextRoom(r), "bar")))
        {
            Utils.slowPrintln("You attempt to use the door, but it's barred from the other side!");
            if(u instanceof Player) ((Player)u).ableToAct = true;
        }
        else
        {
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

    public final void unlock(Room r, String key)
    {
        if(checkKey(r, key)) lockMap.get(r).put(key, false);
        else Utils.slowPrintln("That key doesn't work on this door.");
    }

    public final void unlock(String key)
    {
        unlock(myRoom, key);
        unlock(myOtherRoom, key);
    }

    public final void lock(Room r, String key)
    {
        if(checkKey(r, key)) lockMap.get(r).put(key, true);
        else Utils.slowPrintln("That key doesn't work on this door.");
    }
    
    public final void lock(String key)
    {
        lock(myRoom, key);
        lock(myOtherRoom, key);
    }

    public final void addLock(String key, boolean doLock)
    {
        lockMap.get(myRoom).put(key, doLock);
        lockMap.get(myOtherRoom).put(key, doLock);
    }

    public final void addBar(Room r, boolean doLock)
    {
        if(!lockMap.containsKey(r)) badRoomException(r);

        lockMap.get(r).put("bar", doLock);
    }

    public final boolean isLocked(Room r)
    {
        if(!lockMap.containsKey(r)) badRoomException(r);

        return lockMap.get(r).containsValue(true);
    }

    public final boolean isLocked(Room r, String key)
    {
        return checkKey(r, key) && lockMap.get(r).get(key);
    }

    public final Map<String, Boolean> getLocks(Room r)
    {
        return lockMap.get(r);
    }

    public final boolean checkKey(Room r, String k)
    {
        if(!lockMap.containsKey(r)) badRoomException(r);

        return lockMap.get(r).containsKey(k);
    }

    public final Room getNextRoom(Room r)
    {
        if(r == myRoom) return myOtherRoom;
        else if(r == myOtherRoom) return myRoom;

        badRoomException(r);
        return null;
    }

    public final Room getNextRoom()
    {
        return getNextRoom(myRoom);
    }

    private final void badRoomException(Room r)
    {
        System.out.println(name + ": " + description + " doesn't contain " + r.getName() + ": " + r.getDescription());
        System.out.println(name + ": " + description + " contains both " + myRoom.getName() + ": " + myRoom.getDescription() + " and " + myOtherRoom.getName() + ": " + myOtherRoom.getDescription());
        throw new UnsupportedOperationException("Door d.getNextRoom(Room x) requires x to be in d");
    }

    @Override
    public String getActionDescription() {
        String verb = actionVerb;
        if(isLocked(myRoom, "bar")) verb = "Unbar";
        else if(isLocked(myRoom) && Game.cur.canPickLocks) verb = "Pick the lock of";
        return verb + " " + getArticle() + " " + getDescription() + " " + actLocPrep + " " + locReference; 
    }

    @Override 
    protected boolean trigger() 
    {
        return !Utils.overlap(disablers, myRoom.all());
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

    /*
    ─│┌┐└┘━┃┏┓┗┛═║╔╗╚╝
    maybe a rotate thing?
            ┌─━─┐
            │┌─╗│
            ││ ││
            │└━┘║
            └───┘
     */
    public static class Lockpick
    {
        public final boolean outcome;

        public Lockpick(Unit u, Item pick)
        {
            int chance = 0;
            if(pick instanceof Hairpin) chance = ((Hairpin)pick).level;

            outcome = Utils.rand.nextInt(10) < chance;
        }
    }
}
