package adventuregame.interactibles;

import adventuregame.Interactible;
import adventuregame.Room;
import adventuregame.abstractclasses.Unit;

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
            ""
        );

        myRoom = r;
        locReference = "the room";
        r.add(this);
    }

    @Override protected void setInspects()
    {
        put("Table: wooden table");
    }

    @Override public void action(Unit u) {}
}
