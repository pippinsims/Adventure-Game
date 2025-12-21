package adventuregame.interactibles.wallentities;
import adventuregame.items.TorchItem;

import java.util.Random;

import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.WallEntity;

public class TorchInteractible extends WallEntity {

    public TorchInteractible(Room room, Wall wall)
    {
        myRoom = room;
        myRoom.interactibles.add(this);
        description = "torch";
        this.wall = wall;
        name = "Torch";
        normLocPrep = "on";
        actLocPrep = "from";
        actionVerb = "Take";
        pluralDescription = "torches";
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
    public void inspect()
    {
        Utils.slowPrintln("You take a closer look at this torch and notice nothing new. It's hot, I guess");
    }

    @Override
    public void action(Unit u)
    {
        Utils.slowPrint("You have received a Torch!");
        u.getInventory().add(new TorchItem());
        myRoom.interactibles.remove(this);
    }
}
