package adventuregame.interactibles.wallentities;
import adventuregame.interfaces.Unit;

import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.interactibles.WallEntity;

public class ViewablePicture extends WallEntity {
    
    String txtFileName;
    
    public ViewablePicture(Room room, String fileName, Wall wall, String description, String name)
    {
        this.description = description;
        txtFileName = fileName;
        this.loc = wall;
        this.name = name;
        myRoom = room;
        myRoom.getInteractibles().add(this);
        locationConjunction = "on";
        actionVerb = "Inspect";
    }

    @Override
    public void action(Unit u) 
    {
        inspectInteractible();
    }

    @Override
    public void inspectInteractible()
    {
        Utils.slowPrintln("You take a closer look at the depiction:\n");
        String s = Utils.readFile(txtFileName);
        System.out.println(s);
        System.out.println("\"" + name + "\"");
        System.out.println("Press enter to continue");
        Utils.scanner.nextLine();
    }
}
