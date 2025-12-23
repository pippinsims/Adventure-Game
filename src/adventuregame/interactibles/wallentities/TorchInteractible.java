package adventuregame.interactibles.wallentities;

import java.util.Random;

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
        name = self.name;
        description = self.getDescription();
        myRoom = room;
        this.wall = wall;
        actionVerb = "Take";
        actLocPrep = "from";
        normLocPrep = "on";
        randomDescription = getRandomDescription();
        setLocationReference();
    }

    @Override
    protected String getRandomDescription()
    {
        String[] names = new String[]{"flaming stick",
                                      "blazing rod",
                                      "burny ol\' chunk o\' lumber"};
        
        return names[new Random().nextInt(names.length)];
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

    @Override public String getPluralDescription() { return self.getPluralDescription(); }
}
