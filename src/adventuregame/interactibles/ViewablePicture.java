package adventuregame.interactibles;
import adventuregame.interfaces.Unit;
import adventuregame.interfaces.WallEntity;
import adventuregame.Room;
import adventuregame.Utils;

public class ViewablePicture implements WallEntity {
    String txtFileName;
    String description;
    String name;
    private int wall; //1X is floor, 2X is wall, X1 is south, X2 is west, X3 is north, X4 is east
    private String actionDescription = "Inspect ViewablePicture";
    
    public ViewablePicture(String fileName, int w, String des, String n)
    {
        description = des;
        txtFileName = fileName;
        wall = w;
        name = n;
        actionDescription = "Inspect " + description;
    }

    @Override
    public void action(Unit u) 
    {
        inspectInteractible();
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public String getExposition()
    {
        return getDescription() + " on the " + getWall() + " wall";
    }

    @Override
    public void inspectInteractible()
    {
        Utils.slowPrintln("You take a closer look at the depiction:\n");
        String s = Utils.readFile(getFileName());
        System.out.println(s);
        System.out.println("Press enter to continue");
        Utils.scanner.nextLine();
    }

    public String getWall() 
    {
        switch (wall) {
            case 1: //Why is south 1 this is so weird
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

    public String getFileName()
    {
        return txtFileName;
    }

    @Override
    public boolean isWallInteractible() 
    {
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Room getRoom() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoom'");
    }

    @Override
    public String getActionDescription() {
        return actionDescription;
    }
}
