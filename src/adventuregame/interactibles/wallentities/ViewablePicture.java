package adventuregame.interactibles.wallentities;

import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.WallEntity;

public class ViewablePicture extends WallEntity {
    
    private String txtFileName;
    
    public ViewablePicture(Room room, String fileName, Wall wall, String description, String name)
    {
        myRoom = room;
        myRoom.add(this);
        this.wall = wall;
        txtFileName = fileName;

        setDefaults(
            name, 
            description, 
            "on", 
            "depictions", 
            "", 
            "Inspect",
            "",
            "",
            "scrawlings"
        );
        
        setLocationReference();
    }

    @Override
    public void action(Unit u) 
    {
        inspect();
    }

    @Override
    public void inspect()
    {
        Utils.slowPrintln("You take a closer look at the depiction:\n");
        String s = Utils.readFile(txtFileName);
        System.out.println(s);
        System.out.println("\"" + name + "\"");
        System.out.println("Press enter to continue");
        Utils.scanner.nextLine();
    }
}
