package adventuregame.interactibles.wallinteractibles;

import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.WallInteractible;

public class ViewablePicture extends WallInteractible {
    
    private String txtFileName;
    
    public ViewablePicture(Room room, String fileName, Wall wall, String description, String name)
    {
        setDefaults(
            name, 
            description, 
            "on", 
            "depictions", 
            "", 
            "Inspect",
            ""
        );

        myRoom = room;
        myRoom.add(this);
        this.wall = wall;
        txtFileName = fileName;

        setLocationReference();
    }

    @Override public void action(Unit u) { inspect(); }

    @Override
    public void inspect()
    {
        Utils.slowPrintln("You take a closer look at the depiction:\n");
        String s = Utils.readFile(txtFileName);
        System.out.println(s);
        System.out.println("\"" + name + "\"");
    }
}
