package adventuregame;

import java.util.ArrayList;

import adventuregame.interactibles.ViewablePicture;
import adventuregame.interfaces.Describable;
import adventuregame.interfaces.Interactible;
import adventuregame.interfaces.Unit;

public class WallEntity implements Interactible{

    protected Room myRoom;
    protected Wall loc;
    protected String description;
    protected String name;

    //TODO: add door (rework door implementation), add window
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
            return "the " + getWall() + " wall";
    }

    public String getWall()
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

    public boolean isAlone()
    {
        for (Interactible i : myRoom.getInteractibles()) 
        {
            if(this != i && i instanceof ViewablePicture)
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

    @Override
    public String getActionDescription() 
    {
        ArrayList<Describable> arr = new ArrayList<>();
        for (Interactible i : myRoom.getInteractibles()) { arr.add(i); }
        return "Take " + Utils.articleOfDescribableInList(arr, this) + " " + getDescription() + (!isAlone() ? " from " + locToString() : "");
    }

    @Override
    public String getExposition()
    {
        return getDescription() + " on " + locToString();
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
}
