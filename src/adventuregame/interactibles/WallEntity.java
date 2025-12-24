package adventuregame.interactibles;

import adventuregame.Interactible;

public class WallEntity extends Interactible{

    protected Wall wall;

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

    @Override
    protected String getArticle()
    {
        return isAloneOnWall() ? "the" : super.getArticle();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) return true;

        if(obj == null || getClass() != obj.getClass()) return false;

        WallEntity w = (WallEntity) obj;
        return this.getDescription().equals(w.getDescription()) && this.locReference.equals(w.locReference);
    }
}
