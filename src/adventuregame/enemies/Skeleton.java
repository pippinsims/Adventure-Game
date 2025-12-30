package adventuregame.enemies;

import adventuregame.Inventory;
import adventuregame.Utils;
import adventuregame.abstractclasses.Enemy;

public class Skeleton extends Enemy{

    public Skeleton()
    {
        setDefaults(20, new Inventory(6), 5, 0, "skeleton", "Oess");
        pluralDescription = "skeletons";
    }

    public Skeleton(Inventory i)
    {
        setDefaults(20, new Inventory(6), 5, 0, "skeleton", "Oess");
        pluralDescription = "skeletons";
        inv = i;
    }

    public void pleaResponse()
    {
        Utils.slowPrint(getName() + ": it moves it's jaw, but due to the lack of both lips and vocal chords, no words come out.");
    }

    @Override
    public void performAction(int i) 
    {
        switch(Action.values()[i])
        {
            case NONE:
                Utils.slowPrintln("The " + getModifiedDescription("sad") + " is motionless.");
                break;

            case DIALOGUE:
                boolean didTalk = dialogues.getFirst().next();
                dialogues.remove(0);
                if(!didTalk) chooseAction();
                break;

            case ATTACK:
                this.attack(myRoom.players.get(0), getAttackDamage(), "The " + getModifiedDescription("scary") + " attacks you with its weapon");
                break;
        }
    }
}
