package adventuregame.interactibles;

import adventuregame.Environment;
import adventuregame.Interactible;

public class WallEntity extends Interactible{

    protected Wall wall;
    protected String pluralDescription;
    protected String randomDescription;

    //TODO add window
    @Override
    public boolean isWallInteractible() 
    {
        return true;
    }

    public void setLocationReference()
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
        for (Interactible i : myRoom.interactibles) 
        {
            if(i instanceof WallEntity)
                if(this != i && this.equals(i) && ((WallEntity)i).getWallString().equals(getWallString()))
                    return false;
        }
        return true;
    }

    private String exposition()
    {
        String locPrep = normLocPrep;
        if(Environment.curPlayer.getName().equals("Laur") && wall == Wall.NORTH) locPrep = "of";
        return locPrep + " " + locReference;
    }

    @Override
    protected String getArticle()
    {
        return isAloneOnWall() ? "the" : super.getArticle();
    }

    @Override
    public String getActionDescription() 
    {
        String article = getArticle();
        return actionVerb + " " + article + " " + getDescription() + (!isAlone() ? " from " + locReference : "");
    }

    @Override
    public String getPluralDescription()
    {
        return pluralDescription + " " + exposition();
    }

    @Override
    public String getDescription()
    {
        if(Environment.curPlayer.getName().equals("Laur"))
            return randomDescription + " " + exposition();
        else
            return description + " " + exposition();
    }
}
