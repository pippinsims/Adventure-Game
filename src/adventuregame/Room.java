package adventuregame;

import adventuregame.abstractclasses.Describable;
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

    public void addDoor(Door d)
    {
        doors.add(d);
        interactibles.add(d);
    }

    //TODO add wall material, add a familiar description once it's a familiar room
    public String getDescription()
    {
        return description;
    }

    public void updateDoors()
    {
        for (Door door : doors) 
        {
            door.setWall(this);
        }
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