import java.util.Random;

public class Enemy implements Animate{
    private final int maxHealth = 3;
    private int health;
    private Inventory inv;
    private int dmg;
    private int wisdom;
    private String description = "Screebling Squabbler";
    private boolean isNotAttacking = false;

    public Enemy(int h, Inventory i, int d, int w)
    {
        health = h;
        inv = i;
        dmg = d;
        wisdom = w;
    }
    public Enemy(int h, Inventory i, int d, int w, String des)
        {
            health = h;
            inv = i;
            dmg = d;
            wisdom = w;
            description = des;
        }

    public Enemy(int h)
    {
        health = h;
        inv = new Inventory(5);
        dmg = 2;
        wisdom = 20;
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

        if(health < (maxHealth * 2)/3)
            return "now bent double " + type;
        else if(health <= maxHealth - maxHealth/2)
            return "slightly bruised " + type;
        else
            return type;
    }

    public String getRandomDescription()
    {
        if (description.equals("Screebling Squabbler"))
        {
            switch (new Random().nextInt(3)) 
            {
                default:
                    return description;
                case 1:
                    return "pale man";
                case 2:
                    return "bllork";
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

    public int getHealth()
    {
        return health;
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

    public boolean receiveDamage(int damage, String type)
    {
        switch (type) {
            case "basic":
                health -= damage;
                break;
            
            case "brain aneurysm":
                health -= new Random().nextInt(2);
                isNotAttacking = true;
                break;
        
            default:
                break;
        }
        return health <= 0;
        
    }

    public void chooseAction(Room curRoom)
    {
        //DECISIONMAKING FOR ENEMY
        if(isNotAttacking)
        {
            performAction(1);
            isNotAttacking = false;
        }
        else
            performAction(2);
    }

    public void pleaResponse()
    {
        if (description.equals("Screebling Squabbler"))
        {
            switch(new Random().nextInt(3))
            {
                case 0:
                    InteractionUtil.slowPrintln(getDescription() + ": I care not for your pitifulness.");
                    break;
                case 1:
                    InteractionUtil.slowPrintln(getDescription() + ": ok.");
                    isNotAttacking = true;
                    break;
                case 2:
                    InteractionUtil.slowPrintln(getDescription() + ": [Doesn't React]");
                    break;
                default:
                    System.err.println("error detected in Enemey.java:pleaResponse()");
                    break;
            }
        }
        else if (description.equals("Mushroom"))
        {
            switch(new Random().nextInt(3))
            {
                case 0:
                    InteractionUtil.slowPrintln(getDescription() + ": I never wanted to fight...");
                     isNotAttacking = true;
                    break;
                case 1:
                    InteractionUtil.slowPrintln(getDescription() + ": orpelm hur hur");
                    isNotAttacking = true;
                    break;
                case 2:
                    InteractionUtil.slowPrintln(getDescription() + ": kubi kubi!");
                     isNotAttacking = true;
                    break;
                default:
                    System.err.println("error detected in Enemey.java:pleaResponse()");
                    break;
            }
        }
    }

    @Override
    public boolean performAction(int i) 
    {
        switch(i)
        {
            case 1: //DO NOTHING
                InteractionUtil.slowPrintln("The " + getModifiedDescription("sad") + " stands still, sort of like a Zucchini Mushroom.");
                
                break;

            case 2: //ATTACK
                InteractionUtil.slowPrintln("The " + getModifiedDescription("scary") + " raises it's fiendish arms and jumps at you with startling dexterity.\nYou have no choice but to die.\nYET YOU LIVE.");
                
                break;

            default:
                break;
        }

        return true;
    }
}
