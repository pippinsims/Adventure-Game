package adventuregame.enemies;

import java.util.ArrayList;

import adventuregame.Inventory;
import adventuregame.Utils;
import adventuregame.abstractclasses.Enemy;
import adventuregame.abstractclasses.Item;

public class Skeleton extends Enemy{

    public Skeleton()
    {
        setDefaults(20, new Inventory(6), 5, 0, "skeleton", "Oess");
        pluralDescription = "skeletons";
    }

    public Skeleton(ArrayList<Item> armor, Item weapon)
    {
        setDefaults(20, new Inventory(6), 5, 0, "skeleton", "Oess");
        pluralDescription = "skeletons";
        for(Item i : armor) inv.add(i);
        inv.add(weapon);
    }

    public void pleaResponse()
    {
        Utils.slowPrint(getName() + ": it moves it's jaw, but due to the lack of both lips and vocal chords, no words come out.");
    }

    @Override
    public void performAction(int i) {
        switch(Action.values()[i])
        {
            case NONE:
                Utils.slowPrintln("The " + getModifiedDescription("sad") + " is motionless.");
                break;

            case ATTACK:
                Utils.slowPrintln("The " + getModifiedDescription("scary") + " attacks you with its weapon");
                this.attack(myRoom.players.get(0), getAttackDamage());
                break;
        }
    }
}
