package adventuregame.interactibles;

import java.util.ArrayList;

import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.interfaces.Describable;
import adventuregame.interfaces.Interactible;
import adventuregame.interfaces.Unit;

public class WallEntity implements Interactible{

    protected Room myRoom;
    protected Wall loc;
    protected String description;
    protected String name;

    //TODO add window
    @Override
    public boolean isWallInteractible() 
    {
        return true;
    }

    protected String locToString()
    {
        if(loc == Wall.NONE)
            return "the floor";
        else
            return "the " + getWallString() + " wall";
    }

    public String getWallString()
    {
        switch (loc) {
            case SOUTH:
                return "south";
            case WEST:
                return "west";
            case NORTH:
                return "north";
            case EAST:
                return "east";
            case NONE:
                return "none";
            default:
                return "error";
        }  
    }

    public WallEntity.Wall getWall()
    {
        return loc;
    }

    @Override
    public Room getRoom()
    {
        return myRoom;
    }

    public enum Wall
    {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        NONE;
    }

    protected boolean isAlone()
    {
        for (Interactible i : myRoom.getInteractibles()) 
        {
            if(this != i && i.getDescription() == getDescription())
                return false;
        }
        return true;
    }

    protected boolean isAloneOnWall()
    {
        for (Interactible i : myRoom.getInteractibles()) 
        {
            if(i instanceof WallEntity)
                if(this != i && i.getDescription() == getDescription() && ((WallEntity)i).getWallString() == getWallString())
                    return false;
        }
        return true;
    }

    public String getDescription() 
    {
        return description;
    }

    public String getName() 
    {
        return name;
    }

    protected String actionVerb;
    @Override
    public String getActionDescription() 
    {
        String article = "the";
        if(!isAloneOnWall())
        {
            ArrayList<Describable> arr = new ArrayList<>();
            for (Interactible i : myRoom.getInteractibles()) { arr.add(i); }
            article = Utils.articleOfDescribableInList(arr, this);
        }
        return actionVerb + " " + article + " " + getDescription() + (!isAlone() ? " from " + locToString() : "");
    }

    protected String locationConjunction;
    @Override
    public String getExposition()
    {
        return getDescription() + " " + locationConjunction + " " + locToString();
    }
    
    @Override
    public void action(Unit u) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'action'");
    }

    @Override
    public void inspectInteractible() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'inspectInteractible'");
    }

    @Override
    public boolean isDoor() 
    {
        return false;
    }
}
