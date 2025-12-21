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
    private boolean isDiscovered = false;
    private boolean isCurrentRoom = false;
    private String name = "Barren";

    public Room()
    {

    }

    public Room(String des, String n)
    {
        description = des;
        name = n;
    }

    public void setIsDiscovered(boolean d)
    {
        isDiscovered = d;
    }

    public boolean getIsDiscovered()
    {
        return isDiscovered;
    }

    public boolean getIsCurrentRoom()
    {
        return isCurrentRoom;
    }

    public void setIsCurrentRoom(boolean c)
    {
        isCurrentRoom = c;
    }

    public void add(Interactible i)
    {
        if(i instanceof Door) doors.add((Door)i);
        interactibles.add(i);
    }

    public boolean remove(Interactible i)
    {
        return interactibles.remove(i);
    }

    public void add(Unit u)
    {
        if(u instanceof Player) players.add((Player)u);
        else enemies.add((Enemy)u);
    }

    public boolean remove(Unit u)
    {
        return u instanceof Player ? players.remove((Player)u) : enemies.remove((Enemy)u);
    }

    public void updateDoors()
    {
        for (Door door : doors) door.setWall(this);
    }

    //TODO add wall material, add a familiar description once it's a familiar room
    public String getDescription()
    {
        return description;
    }

    public ArrayList<Interactible> getIntersByUniqueDesc()
    {
        ArrayList<Interactible> inters = new ArrayList<>();
        for (Interactible i : interactibles) if(!inters.contains(i)) inters.add(i); //This compares by description
        return inters;
    }

    @Override
    public String getName() 
    {
        return name;
    }

    
    // TODO just here because it has to be, I don't know if there'd ever be a case for describing multiple of the exact same room
    @Override
    public String getPluralDescription() 
    {
        throw new UnsupportedOperationException("Unimplemented method 'getPluralDescription'");
    }
}