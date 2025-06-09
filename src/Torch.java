public class Torch implements Interactible {
    private boolean lit;
    public String description;
    private int loc; //1X is floor, 2X is wall, X1 is south, X2 is west, X3 is north, X4 is east
    
    public Torch(boolean isLit, int wall)
    {
        description = "flaming stick";
        lit = isLit;
        loc = 20 + wall;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public String getExposition()
    {
        return getDescription() + (loc / 10 == 2? " on the " + getWall() + " wall" : " on the floor");
    }

    @Override
    public void inspectInteractible()
    {
        Utils.slowPrintln("You take a closer look at this flaming stick and you notice that it is a burning torch, providing light and warmth!");
    }

    private String getWall() 
    {
        switch (loc % 10) {
            case 1:
                return "south";
            case 2:
                return "west";
            case 3:
                return "north";
            case 4:
                return "east";
            default:
                return "error";
        }    
    }

    @Override
    public void action()
    {
        lit = !lit;
    }

    @Override
    public boolean isWallInteractible() 
    {
        return true;
    }
}
