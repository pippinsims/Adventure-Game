package adventuregame.interactibles.wallentities;

import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.WallEntity;

public class Window extends WallEntity {
    
    String view;

    public Window(Room room, String view, Wall wall)
    {
        myRoom = room;
        myRoom.add(this);
        this.wall = wall;

        setDefaults(
            "Window",
            "window",
            "in",
            "windows",
            "",
            "Look through",
            "",
            new String[0],
            new String[0]
        );

        this.view = view;
        
        setLocationReference();
    }

    @Override public void action(Unit u) { inspect(); }

    @Override
    public void inspect()
    {
        Utils.slowPrintln("You look through the window and see "+view+"\nPress enter to continue");
        Utils.scanner.nextLine();
    }
}
