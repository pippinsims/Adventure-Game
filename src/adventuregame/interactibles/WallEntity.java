package adventuregame.interactibles;

import adventuregame.Interactible;
import adventuregame.interfaces.Unit;

public class WallEntity extends Interactible{

    protected Wall wall;
    protected String locReference;

    //TODO add window
    @Override
    public boolean isWallInteractible() 
    {
        return true;
    }

    protected String setLocationReference()
    {
        if(wall == Wall.NONE)
            locReference = "the floor";
        else
            locReference = "the " + getWallString() + " wall";
    }

    public String getWallString()
    {
        switch (wall) {
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
        return wall;
    }

    public enum Wall
    {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        NONE;
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

    @Override
    protected String getArticle()
    {
        String a = "the";
        if(!isAloneOnWall())
        {
            a = super.getArticle();
        }

        return a;
    }

    @Override
    public String getActionDescription() 
    {
        String article = getArticle();
        return actionVerb + " " + article + " " + getDescription() + (!isAlone() ? " from " + locReference : "");
    }

    protected String locationConjunction;
    @Override
    public String getExposition()
    {//TODO understand locationConjunction and make getExposition usable in Interactible
        return getDescription() + " " + locationConjunction + " " + locReference;
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
