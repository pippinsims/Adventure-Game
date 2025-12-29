package adventuregame.enemies;

import java.util.Random;

import adventuregame.Inventory;
import adventuregame.Utils;
import adventuregame.abstractclasses.Enemy;

public class Goblin extends Enemy {

    public Goblin(int health) { setDefaults(health, new Inventory(5), 4, 20, "goblin with pointy ears", null); 
        pluralDescription = "goblins with pointy ears";
    }

    public Goblin(int health, Inventory inventory, int damage, int wisdom) { setDefaults(health, inventory, damage, wisdom, "goblin with pointy ears", null);
        pluralDescription = "goblins with pointy ears";
     }
    
    public Goblin(int health, Inventory inventory, int damage, int wisdom, String description, String pluralDescription) { setDefaults(health, inventory, damage, wisdom, description, null);
        this.pluralDescription = pluralDescription; 
     }

    public Goblin(int health, Inventory inventory, int damage, int wisdom, String description, String pluralDescription, String name) { setDefaults(health, inventory, damage, wisdom, description, name); 
        this.pluralDescription = pluralDescription; 
    }

    @Override 
    public void pleaResponse()
    {
        Utils.slowPrint(getName() + ": ");
        switch(new Random().nextInt(3))
        {
            case 0:
                Utils.slowPrintln("I pity thee not.");
                break;
            case 1:
                Utils.slowPrintln("ok.");
                isStunned = true;
                break;
            case 2:
                Utils.slowPrintln("[Doesn't React]");
                break;
        }
    }

    @Override
    public void performAction(int i)
    {
        switch(Action.values()[i])
        {
            case NONE:
                Utils.slowPrintln("The " + getModifiedDescription("sad") + " stands still, sort of like a Zucchini Mushroom.");
                break;

            case ATTACK:
                Utils.slowPrintln("The " + getModifiedDescription("scary") + " raises it's fiendish arms and jumps at you with startling dexterity.");
                this.attack(myRoom.players.get(0), getAttackDamage());
                break;
        }
    }
}
