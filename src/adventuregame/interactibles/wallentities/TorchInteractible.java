package adventuregame.interactibles.wallentities;

import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Unit;
import adventuregame.dynamicitems.Torch;
import adventuregame.interactibles.WallEntity;

public class TorchInteractible extends WallEntity {

    Torch self;

    public TorchInteractible(Torch self, Room room, Wall wall)
    {
        this.self = self;
        myRoom = room;
        this.wall = wall;
        
        setDefaults(
            self.name, 
            self.getDescription(), 
            "on", 
            self.getPluralDescription(), 
            "", 
            "Take", 
            "from",
            new String[]{"flaming stick", "blazing rod", "burny ol\' chunk o\' lumber"}, 
            new String[0]
        );
        
        setLocationReference();
    }

    @Override
    public void action(Unit u)
    {
        self.collectItem(u);
        Utils.slowPrint("You have received a Torch!");
    }

    @Override
    public void inspect()
    {
        Utils.slowPrintln("You take a closer look at this torch and notice nothing new. It's hot, I guess");
    }
}
