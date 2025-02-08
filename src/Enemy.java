public class Enemy {
    private final int maxHealth = 3;
    private int health;
    private Inventory inv;
    private int dmg;
    private int wisdom;
    private String description = "Screebling Squabbler";

    public Enemy(int h, Inventory i, int d, int w)
    {
        health = h;
        inv = i;
        dmg = d;
        wisdom = w;
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
        }

        if(health < (maxHealth * 2)/3)
            return "now bent double " + type;
        else if(health <= maxHealth - maxHealth/2)
            return "slightly bruised " + type;
        else
            return type;
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

    public boolean receiveDamage(int damage)
    {
        health -= damage;
        return health <= 0;
    }

    public int chooseAction(Room curRoom)
    {
        //DECISIONMAKING FOR ENEMY
        return 1;
    }
}
