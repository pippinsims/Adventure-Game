package adventuregame.interactibles.wallinteractibles;

import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.WallInteractible;

public class Window extends WallInteractible {
    
    String view;

    public Window(Room room, String view, Wall wall)
    {
        setDefaults(
            "Window",
            "window",
            "in",
            "windows",
            "",
            "Look through",
            ""
        );

        myRoom = room;
        myRoom.add(this);
        this.wall = wall;
        this.view = view;
        
        setLocationReference();
    }

    @Override public void action(Unit u) { inspect(); }

    @Override
    public void inspect()
    {
        Utils.slowPrintln("You look through the window and see "+view);
    }
}
