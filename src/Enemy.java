import java.util.Random;

public class Enemy {
    private final int maxHealth = 3;
    private int health;
    private Inventory inv;
    private int dmg;
    private int wisdom;
    private String description;

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

    public String getDescription()
    { 
        if(health < (maxHealth * 2)/3)
            description = "now bent double ";
        else if(health <= maxHealth - maxHealth/2)
            description = "slightly bruised ";
        else
            description = "";
    
        switch (new Random().nextInt(3)) 
        {
            case 0:
                description += "Screebling Squabbler";
                break;
            case 1:
                description += "poor fiend";
                break;
            case 2:
                description += "monster";
                break;
        }
        
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
