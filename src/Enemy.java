import java.util.Random;

public class Enemy implements Animate{
    private final int maxHealth = 3;
    private float health;
    private Inventory inv;
    private int dmg;
    private int wisdom;
    private String description = "Screebling Squabbler";
    private boolean isNotAttacking = false;
    private String name;

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
        generateName();
    }

    private void generateName() 
    {
        switch(new Random().nextInt(5))
        {
            case 0: name = "Bo"; break;
            case 1: name = "Kua"; break;
            case 2: name = "An"; break;
            case 3: name = "Lis"; break;
            case 4: name = "Yi"; break;
        }

        switch (new Random().nextInt(7)) 
        {
            case 0: name += "sandual"; break;
            case 1: name += "\'hananah"; break;
            case 2: name += "mon"; break;
            case 3: name += "tio"; break;
            case 4: name += "narsh"; break;
            case 5: name += "poaf"; break;
            case 6: name += "duan"; break;
        }
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
            return "now bent double " + type;
        else if(health <= (maxHealth * 2) / 3)
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

    public String getName()
    {
        return name;
    }

    public float getHealth()
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
        InteractionUtil.slowPrint(getName() + ": ");
        if (description.equals("Screebling Squabbler"))
        {
            switch(new Random().nextInt(3))
            {
                case 0:
                    InteractionUtil.slowPrintln("I care not for your pitifulness.");
                    break;
                case 1:
                    InteractionUtil.slowPrintln("ok.");
                    isNotAttacking = true;
                    break;
                case 2:
                    InteractionUtil.slowPrintln("[Doesn't React]");
                    break;
                default:
                    System.err.println("error detected in Enemey.java:pleaResponse()");
                    break;
            }
        }
        else if (description.equals("Mushroom"))
        {
            isNotAttacking = true;
            switch(new Random().nextInt(3))
            {
                case 0:
                    InteractionUtil.slowPrintln("I never wanted to fight...");
                    break;
                case 1:
                    InteractionUtil.slowPrintln("orpelm hur hur");
                    break;
                case 2:
                    InteractionUtil.slowPrintln("kubi kubi!");
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

        InteractionUtil.slowPrintln(str);

        return true;
    }
}
