package adventuregame;

import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.NonPlayer;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.wallinteractibles.Door;

import java.util.ArrayList;

public class Room extends Describable
{
    public static ArrayList<Room> rooms = new ArrayList<>();
    // private static int roomnum = 0; //TODO make "playtest" functionality

    public  ArrayList<Player>       players       = new ArrayList<>();
    public  ArrayList<NonPlayer>    NPCs          = new ArrayList<>();
    public  ArrayList<Interactible> interactibles = new ArrayList<>();
    private ArrayList<Door>         doors         = new ArrayList<>();
    private ArrayList<Player>       familiars     = new ArrayList<>();
    private String familiarDescription;

    public Room()
    {
        name = "Bare";
        description = "a bare room";
        descMap.put("Laur", "an... empty place");
        familiarDescription = "Bare room.";
        rooms.add(this);
        // name = "room"+roomnum++; //TODO make "playtest" functionality
    }

    public Room(String d, String l, String f, String n)
    {
        description = d;
        descMap.put("Laur", l);
        familiarDescription = f;
        name = n;
        rooms.add(this);
        // name = "room"+roomnum++; //TODO make "playtest" functionality
    }

    public Room(String d, String f, String n)
    {
        description = d;
        familiarDescription = f;
        name = n;
        rooms.add(this);
        // name = "room"+roomnum++; //TODO make "playtest" functionality
    }

    public Room(String d, String n)
    {
        description = d;
        familiarDescription = n + ".";
        name = n;
        rooms.add(this);
        // name = "room"+roomnum++; //TODO make "playtest" functionality
    }

    public ArrayList<Unit> all()
    {
        ArrayList<Unit> all = new ArrayList<>();
        all.addAll(NPCs);
        all.addAll(players);

        return all;
    }

    public void discover()
    {
        descMap.put(Game.cur.getName(), familiarDescription);
    }

    public boolean doFirstDialogue()
    {
        for(NonPlayer n : NPCs) if(!n.dialogues.isEmpty()) 
        { 
            n.dialogues.getFirst().next();
            n.dialogues.remove(0); 
            return true;
        }
        return false;
    }

    public boolean isFamiliarTo(Unit u) { return familiars.contains(u); }

    public void add(Interactible i)
    {
        if(i instanceof Door) doors.add((Door)i);
        interactibles.add(i);

        update();
    }

    public void add(Unit u)
    {
        if(u instanceof Player) players.add((Player)u);
        else NPCs.add((NonPlayer)u);
        u.setRoom(this);

        update();
    }

    public boolean remove(Interactible i)
    {
        boolean out = Utils.remove(interactibles, i);
        if(i instanceof Door) Utils.remove(doors, i);
        
        update(); 
        return out;
    }

    public boolean remove(Unit u)
    {
        boolean out;
        if(u instanceof Player) out = players.remove(u);
        else out = NPCs.remove(u);
        
        update();
        return out;
    }

    public ArrayList<Door> getDoors()
    {
        return doors;
    }

    public ArrayList<Door> getLockedDoors()
    {
        ArrayList<Door> locked = new ArrayList<>();
        for(Door d : doors) if(d.isLocked(this)) locked.add(d);
        return locked;
    }

    public void update()
    {
        updateDoors();

        for(Interactible i : interactibles) if(i.trigger()) i.enable();
    }

    private void updateDoors()
    {
        for(Door d : doors) 
        {
            d.setWall(this);
        }
    }
}