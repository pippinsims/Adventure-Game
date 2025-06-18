package adventuregame;
import java.util.Random;

import adventuregame.abstractclasses.Unit;

public class Enemy extends Unit
{
    private Inventory inv;
    private int dmg;
    private int wisdom;
    private String description, randomDescription;
    private String name;
    private Room myRoom;

    public String pluralOf(String description)
    {
        switch (description) {
            case "goblin": case "Screebling Squabbler": case "Mushroom": case "bllork": case "awkward fellow": case "Shroomie": case "Delicious Fun Guy": case "Knower of Forest Beds and Roots":
                return description + 's';
            case "pale man" : 
                return "pale men";
            case "Those-Who-Feast":
                return description;

            default:
                throw new UnsupportedOperationException("Used an impossible description for Enemy");
        }
    }

    public Enemy(int h, Inventory i, int d, int w)
    {
        maxHealth = h;
        health = maxHealth;
        inv = i;
        dmg = d;
        wisdom = w;
        generateName();
        description = "goblin";
    }

    public Enemy(int health, Inventory inv, int dmg, int wis, String des)
    {
        maxHealth = health;
        this.health = maxHealth;
        this.inv = inv;
        this.dmg = dmg;
        wisdom = wis;
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
        description = "goblin";
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
            return "bent double " + type;
        else if(health <= (maxHealth * 2) / 3)
            return "slightly bruised " + type;
        else
            return type;
    }

    public String getRandomDescription()
    {
        String[] names = new String[]{description};
        if (description.equals("goblin"))
            names = new String[]{"Screebling Squabbler", "pale man", "bllork", "awkward fellow"};
        else if (description.equals("Mushroom Monster"))
            names = new String[]{"Mushroom","Shroomie", "Delicious Fun Guy", "Those-Who-Feast", "Knower of Forest Beds and Roots"};

        return names[new Random().nextInt(names.length)];
    }

    public String getDescription()
    {
        if(Environment.curPlayer.getName() == "Laur")
            return randomDescription;
        else
            return description; //description = getRandomDescription(); when it's Laur's turn
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
        if (description.equals("goblin"))
        {
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
                default:
                    System.err.println("error detected in Enemey.java:pleaResponse()");
                    break;
            }
        }
        else if (description.equals("Mushroom Monster"))
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
            case "goblin":
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

            case "Mushroom Monster":
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
        Environment.r0.enemies.remove(this);
    }

    @Override
    public Room getRoom() 
    {
        return myRoom;
    }

    @Override
    public String getPluralDescription() 
    {
        if(Environment.curPlayer.getName() == "Laur")
            return pluralOf(randomDescription);
        else
            return pluralOf(description);
    }

    public void Randomize() 
    {
        randomDescription = getRandomDescription();
    }
}
