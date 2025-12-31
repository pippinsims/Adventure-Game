package adventuregame.interactibles.wallinteractibles;

import adventuregame.Room;
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
        setInspects();
        
        setLocationReference();
    }

    @Override public void action(Unit u) { inspect(); }

    @Override
    protected void setInspects() {
        put("You look through the window and see "+view);
    }
}
