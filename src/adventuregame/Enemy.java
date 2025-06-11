package adventuregame;
import java.util.Random;

import adventuregame.interfaces.Unit;

public class Enemy extends Effectable implements Unit
{
    private Inventory inv;
    private int dmg;
    private int wisdom;
    private String description = "Screebling Squabbler";
    private String name;

    public Enemy(int h, Inventory i, int d, int w)
    {
        maxHealth = h;
        health = maxHealth;
        inv = i;
        dmg = d;
        wisdom = w;
        generateName();
    }

    public Enemy(int h, Inventory i, int d, int w, String des)
    {
        maxHealth = h;
        health = maxHealth;
        inv = i;
        dmg = d;
        wisdom = w;
        description = des;
        generateName();
    }

    public Enemy(int h)
    {
        maxHealth = h;
        health = maxHealth;
        inv = new Inventory(5);
        dmg = 2;
        wisdom = 20;
        generateName();
    }

    private void generateName() 
    {
        name = Utils.names1[new Random().nextInt(Utils.names1.length)] + Utils.names2[new Random().nextInt(Utils.names2.length)];
    }

    public String getModifiedDescription(String type)
    {
        switch (type) 
        {
            case "scary":
                type = "monster";
                break;
            case "sad":
                type = "poor fiend";
                break;
            case "random":
                type = getRandomDescription();
                break;
        }

        if(health <= maxHealth / 3)
            return " bent double " + type;
        else if(health <= (maxHealth * 2) / 3)
            return "slightly bruised " + type;
        else
            return type;
    }

    public String getRandomDescription()
    {
        if (description.equals("Screebling Squabbler"))
        {
            switch (new Random().nextInt(4)) 
            {
                default:
                    return description;
                case 1:
                    return "pale man";
                case 2:
                    return "bllork";
                case 3:
                    return "awkward fellow";
            }
        }
        else if (description.equals("Mushroom"))
            {
                switch (new Random().nextInt(5))
                {
                    default:
                        return description;
                    case 1:
                        return "Shroomie";
                    case 2:
                        return "Delicious Fun Guy";
                    case 3:
                        return "Those-Who-Feast";
                    case 4:
                        return "Knower of Forest Beds and Roots";
                }
            }
        else
            return "???";
    }

    public String getDescription()
    {
        return description;
    }

    public String getName()
    {
        return name;
    }

    public Inventory getInventory()
    {
        return inv;
    }

    public int getAttackDamage()
    {
        return dmg;
    }

    public int getWisdom()
    {
        return wisdom;
    }

    public void chooseAction(Room curRoom)
    {
        //DECISIONMAKING FOR ENEMY
        if(isStunned)
        {
            performAction(1);
            isStunned = false;
        }
        else
            performAction(2);
    }

    public void pleaResponse()
    {
        Utils.slowPrint(getName() + ": ");
        if (description.equals("Screebling Squabbler"))
        {
            switch(new Random().nextInt(3))
            {
                case 0:
                    Utils.slowPrintln("I care not for your pitifulness.");
                    break;
                case 1:
                    Utils.slowPrintln("ok.");
                    isStunned = true;
                    break;
                case 2:
                    Utils.slowPrintln("[Doesn't React]");
                    break;
                default:
                    System.err.println("error detected in Enemey.java:pleaResponse()");
                    break;
            }
        }
        else if (description.equals("Mushroom"))
        {
            isStunned = true;
            switch(new Random().nextInt(3))
            {
                case 0:
                    Utils.slowPrintln("I never wanted to fight...");
                    break;
                case 1:
                    Utils.slowPrintln("orpelm hur hur");
                    break;
                case 2:
                    Utils.slowPrintln("kubi kubi!");
                    break;
                default:
                    System.err.println("error detected in Enemey.java:pleaResponse()");
                    break;
            }
        }
    }

    public boolean performAction(int i) 
    {
        String str = "";
        
        switch (description) 
        {
            case "Screebling Squabbler":
                switch(i)
                {
                    case 1: //DO NOTHING
                        str = "The " + getModifiedDescription("sad") + " stands still, sort of like a Zucchini Mushroom.";
                        break;

                    case 2: //ATTACK
                        str = "The " + getModifiedDescription("scary") + " raises it's fiendish arms and jumps at you with startling dexterity.";
                        break;

                    default:
                        break;
                }
                break;

            case "Mushroom":
                switch(i) 
                {
                    case 1:
                        str = "The strange shroomacious being freezes solid, like a painted statue.";                        
                        break;
                
                    case 2:
                        str = "The " + getRandomDescription() + " swings the butt of a shotgun at your head to end you.";
                        break;
                }
                break;
        }

        Utils.slowPrintln(str);

        return true;
    }

    @Override
    public void updateUnit() {
        System.out.println("--" + name + "'" + (name.charAt(name.length() - 1) != 's' ? "s" : "") + " Turn--");
        
        for (int i = effects.size() - 1; i > -1; i--) 
        {
            Effect e = effects.get(i);
            // effectUpdate returns true if the effectable died
            if(e.isDamaging && effectUpdate(e) == EffectUpdateResult.DEATH)
            {
                death();
                return;
            }

            // else ...
        }
        
        chooseAction(Environment.r0);
    }

    public void death() 
    {
        Utils.slowPrintln("You ended " + getName(), 200);
        // remove enemy from the current room
        Environment.r0.getEnemies().remove(this);
    }
}
