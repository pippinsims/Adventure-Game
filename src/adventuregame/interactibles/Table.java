package adventuregame.interactibles;

import adventuregame.Interactible;
import adventuregame.Room;

public class Table extends Interactible {
    
    public Table(Room r)
    {
        setDefaults(
            "Table",
            "wooden table",
            "in",
            "wooden tables",
            "in",
            "",
            "",
            new String[0],
            new String[0]
        );

        myRoom = r;
        locReference = "the room";
        r.add(this);
    }

    @Override public void inspect() { System.out.println(name + ": " + description); }
}
