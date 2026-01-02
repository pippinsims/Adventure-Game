package adventuregame.abstractclasses;
import java.util.Random;

import adventuregame.Damage;
import adventuregame.Inventory;
import adventuregame.Utils;
import adventuregame.interactibles.SkeletonInteractible;

public abstract class Enemy extends NonPlayer
{
    public abstract void pleaResponse();

    @Override
    public void chooseAction()
    {
        //DECISIONMAKING FOR ENEMY
        if(isStunned || myRoom.players.isEmpty())
        {
            performAction(Action.NONE);
            isStunned = false;
        }
        else if (!dialogues.isEmpty() && dialogues.getFirst().getInitiator() == this)
            performAction(Action.DIALOGUE);
        else
            performAction(Action.ATTACK);
    }

    @Override
    public EffectUpdateResult receiveDamage(Damage damage)
    {
        EffectUpdateResult out = super.receiveDamage(damage); 
        if(out == EffectUpdateResult.DEATH && this instanceof Skeleton) new SkeletonInteractible(myRoom, inv);
        
        return out;
    }

    public static class Skeleton extends Enemy{

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
        public void performAction(Action a) 
        {
            switch(a)
            {
                case NONE:
                    Utils.slowPrintln("The " + getModifiedDescription("sad") + " is motionless.");
                    break;

                case DIALOGUE:
                    talk();
                    break;

                case ATTACK:
                    this.attack(myRoom.players.get(0), getAttackDamage(), "The " + getModifiedDescription("scary") + " attacks you with its weapon");
                    break;
            }
        }
    }

    public static class Goblin extends Enemy {

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
        public void performAction(Action a)
        {
            switch(a)
            {
                case NONE:
                    Utils.slowPrintln("The " + getModifiedDescription("sad") + " stands still, sort of like a Zucchini Mushroom.");
                    break;

                case DIALOGUE:
                    talk();
                    break;

                case ATTACK:
                    this.attack(myRoom.players.get(0), getAttackDamage(), "The " + getModifiedDescription("scary") + " raises it's fiendish arms and jumps at you with startling dexterity.");
                    break;
            }
        }
    }
}
