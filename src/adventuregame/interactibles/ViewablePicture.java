package adventuregame.interactibles;
import adventuregame.interfaces.Describable;
import adventuregame.interfaces.Interactible;
import adventuregame.interfaces.Unit;

import java.util.ArrayList;

import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.WallEntity;

public class ViewablePicture extends WallEntity {
    
    String txtFileName;
    
    public ViewablePicture(String fileName, Wall wall, String description, String name, Room room)
    {
        this.description = description;
        txtFileName = fileName;
        this.loc = wall;
        this.name = name;
        myRoom = room;
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
        String s = Utils.readFile(getFileName());
        System.out.println(s);
        System.out.println("Press enter to continue");
        Utils.scanner.nextLine();
    }

    public String getFileName()
    {
        return txtFileName;
    }

    @Override
    public String getActionDescription() 
    {
        String article = "the";
        if(!isAlone())
        {
            ArrayList<Describable> arr = new ArrayList<>();
            for (Interactible i : myRoom.getInteractibles()) { arr.add(i); }
            article = Utils.articleOfDescribableInList(arr, this);
        }
        return "Inspect " + article + " " + getDescription() + (!isAlone() ? " from " + locToString() : "");
    }
}
