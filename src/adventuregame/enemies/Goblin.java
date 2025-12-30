package adventuregame.enemies;

import java.util.Random;

import adventuregame.Inventory;
import adventuregame.Utils;
import adventuregame.abstractclasses.Enemy;

public class Goblin extends Enemy {

    { pluralDescription = "goblins"; }

    public Goblin(int health) { 
        setDefaults(health, new Inventory(5), 4, 20, "goblin with pointy ears", null);
    }

    public Goblin(int health, Inventory inventory, int damage, int wisdom) { 
        setDefaults(health, inventory, damage, wisdom, "goblin with pointy ears", null);
    }

    @Override
    public void setDefaults(int m, Inventory i, int dmg, int w, String des, String name)
    {
        super.setDefaults(m, i, dmg, w, des, name);

        int r = Utils.rand.nextInt(4);
        descMap.put("Laur", (new String[] {"Screeblin Squabbler","pale man","awkward fellow","bllork"})[r]);
        pDescMap.put("Laur", (new String[] {"Screeblin Squabblers","pale men","awkward fellas","bllorks"})[r]);
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

            case DIALOGUE:
                boolean didTalk = dialogues.getFirst().next();
                dialogues.remove(0);
                if(!didTalk) chooseAction();
                break;

            case ATTACK:
                this.attack(myRoom.players.get(0), getAttackDamage(), "The " + getModifiedDescription("scary") + " raises it's fiendish arms and jumps at you with startling dexterity.");
                break;
        }
    }
}
