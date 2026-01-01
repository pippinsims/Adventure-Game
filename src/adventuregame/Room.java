package adventuregame;

import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Enemy;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.wallinteractibles.Door;

import java.util.ArrayList;

public class Room extends Describable
{
    public static ArrayList<Room> rooms = new ArrayList<>();

    public  ArrayList<Enemy>        enemies       = new ArrayList<>();
    public  ArrayList<Player>       players       = new ArrayList<>();
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
    }

    public Room(String d, String l, String f, String n)
    {
        description = d;
        descMap.put("Laur", l);
        familiarDescription = f;
        name = n;
        rooms.add(this);
    }

    public Room(String d, String f, String n)
    {
        description = d;
        familiarDescription = f;
        name = n;
        rooms.add(this);
    }

    public Room(String d, String n)
    {
        description = d;
        familiarDescription = n + ".";
        name = n;
        rooms.add(this);
    }

    public void discover()
    {
        descMap.put(Game.curPlayer.getName(), familiarDescription);
    }

    public boolean doFirstDialogue()
    {
        for(Enemy e : enemies) if(e.dialogues.getFirst() != null) 
        { 
            e.dialogues.getFirst().next();
            e.dialogues.remove(0); 
            return true;
        }
        return false;
    }

    public boolean getIsFamiliar() { return familiars.contains(Game.curPlayer); }

    public void add(Interactible i)
    {
        if(i instanceof Door) doors.add((Door)i);
        interactibles.add(i);
    }

    public void add(Unit u)
    {
        if(u instanceof Player) players.add((Player)u); else enemies.add((Enemy)u);
        u.setRoom(this);
    }

    public boolean remove(Interactible i)
    {
        return interactibles.remove(i);
    }

    public boolean remove(Unit u)
    {
        return u instanceof Player ? players.remove((Player)u) : enemies.remove((Enemy)u);
    }

    public void updateDoors()
    {
        for (Door door : doors) door.setWall(this);
    }

    public ArrayList<Interactible> getUniqueInters()
    {
        ArrayList<Interactible> inters = new ArrayList<>();
        for (Interactible i : interactibles) if(!contains(inters, i)) inters.add(i); 
        
        return inters;
    }

    private boolean contains(ArrayList<? extends Interactible> arr, Interactible i)
    {
        if(!(i instanceof Door)) return arr.contains(i); //This compares by description
        Door d = (Door)i;

        //if the doors have same from room and to room TODO .equals() should just compare by reference again, but i don't know what that'd break
        for(Interactible i1 : arr) if(i1 instanceof Door && ((Door)i1).getRoom() == d.getRoom() && ((Door)i1).getNextRoom(d.getRoom()) == d.getNextRoom(d.getRoom())) return true;
        return false;
    }
}