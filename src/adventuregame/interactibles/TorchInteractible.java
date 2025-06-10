package adventuregame.interactibles;
import adventuregame.interfaces.Unit;
import adventuregame.interfaces.WallEntity;
import adventuregame.items.TorchItem;
import adventuregame.Room;
import adventuregame.Utils;

public class TorchInteractible implements WallEntity {

    private String description;
    private String actionDescription = "Take torch from wall.";
    private Room myRoom;
    private int loc; //1X is floor, 2X is wall, X1 is south, X2 is west, X3 is north, X4 is east
    
    public TorchInteractible(Room r, int wall)
    {
        myRoom = r;
        description = "flaming stick";
        loc = 20 + wall;
        actionDescription = "Take a torch from the " + getWall() + " wall.";
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

    public String getWall() 
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
    public void action(Unit u)
    {
        Utils.slowPrint("You have recieved a Torch!");
        u.getInventory().addItem(new TorchItem());
        myRoom.getInteractibles().remove(this);
    }

    @Override
    public boolean isWallInteractible() 
    {
        return true;
    }

    @Override
    public String getName() {
        return "Torch";
    }

    @Override
    public Room getRoom() {
        return myRoom;
    }

    @Override
    public String getActionDescription() 
    {
        return actionDescription;
    }
}
