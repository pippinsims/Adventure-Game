package adventuregame.interfaces;

public interface WallEntity extends Interactible{

    //TODO: add door (rework door implementation), add window
    @Override
    default boolean isWallInteractible() 
    {
        return true;
    }

    public String getWall();
}
