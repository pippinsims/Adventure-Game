package adventuregame;

import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.wallentities.Door;

import java.util.ArrayList;

public class Room extends Describable
{
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();
    public ArrayList<Interactible> interactibles = new ArrayList<>();
    private ArrayList<Door> doors = new ArrayList<>();
    private String description = "a bare room";
    private String laurDescription = "an... empty place";
    private String familiarDescription = "";
    private ArrayList<Player> familiars = new ArrayList<>();
    private String name = "Barren";

    public Room()
    {

    }

    public Room(String description, String name)
    {
        this.description = description;
        this.name = name;
    }

    public Room(String d, String l, String f, String n)
    {
        description = d;
        laurDescription = l;
        familiarDescription = f;
        name = n;
    }

    public void discover()
    {
        if(!familiars.contains(Environment.curPlayer)) familiars.add(Environment.curPlayer);
    }

    public boolean getIsFamiliar() { return familiars.contains(Environment.curPlayer); }

    public void add(Interactible i)
    {
        if(i instanceof Door) doors.add((Door)i);
        interactibles.add(i);
    }

    public void add(Unit u)
    {
        if(u instanceof Player) players.add((Player)u);
        else 
        {
            enemies.add((Enemy)u);
            u.setRoom(this);
        }
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

    public String getDescription()
    {
        if(getIsFamiliar()) return familiarDescription;
        if(Environment.isLaur) return laurDescription;
        return description;
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

    @Override public String getName() { return name; }
    
    // TODO just here because it has to be, I don't know if there'd ever be a case for describing multiple of the exact same room
    @Override
    public String getPluralDescription() 
    {
        throw new UnsupportedOperationException("Unimplemented method 'getPluralDescription'");
    }
}