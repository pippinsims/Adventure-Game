package adventuregame.abstractclasses;
import java.util.Random;

import adventuregame.Damage;
import adventuregame.Dialogue;
import adventuregame.Environment;
import adventuregame.Inventory;
import adventuregame.Room;
import adventuregame.Utils;

public abstract class Enemy extends Unit
{
    protected Inventory inv;
    protected Damage dmg;
    protected int wisdom;
    protected String description, pluralDescription, randomDescription;
    protected String name;

    protected enum Action
    {
        NONE,
        ATTACK
    }

    protected String pluralOf(String str)
    {
        switch (str) {
            case "goblin":
            case "Screebling Squabbler":
            case "bllork":
            case "Skeleton":
                return str + 's';
            case "awkward fellow":
                return "awkward fellas";
            case "pale man": 
                return "pale men";
            
            default:
                throw new UnsupportedOperationException("No plural for '"+str+"'");
        }
    }

    public void setDefaults(int m, Inventory i, int dmg, int w, String des, String name)
    {
        maxHealth = m;
        health = maxHealth;
        inv = i;
        this.dmg = new Damage(dmg);
        wisdom = w;
        description = des;
        this.name = name == null ? generateName() : name;
        deathMsg = "You ended " + getName();
    }

    protected String generateName() 
    {
        return Utils.names1[new Random().nextInt(Utils.names1.length)] + Utils.names2[new Random().nextInt(Utils.names2.length)];
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

    protected String getRandomDescription()
    {
        String[] names = new String[]{description};
        switch(description)
        {
            case "Goblin"          : names = new String[]{"Screebling Squabbler", "pale man", "bllork", "awkward fellow"}; break;
            case "Skeleton"        : names = new String[]{"Skeleton"}; break;
        }
        return names[new Random().nextInt(names.length)];
    }

    public String getDescription()
    {
        if(Environment.isLaur)
            return randomDescription;
        else
            return description;
    }

    public void randomizeDesc() { randomDescription = getRandomDescription(); }

    public String getName() { return name; }

    public Inventory getInventory() { return inv; }

    public Damage getAttackDamage() { return dmg; }

    public int getWisdom() { return wisdom; }

    public void chooseAction(Room curRoom)
    {
        //DECISIONMAKING FOR ENEMY
        if(isStunned || curRoom.players.isEmpty())
        {
            performAction(1);
            isStunned = false;
        }
        else
            performAction(2);
    }

    public abstract void pleaResponse();

    public abstract void performAction(int i);

    @Override
    public void updateUnit() throws Exception {
        System.out.println("\t\t\t\t\t\t\t\t--" + name + "'" + (name.charAt(name.length() - 1) != 's' ? "s" : "") + " Turn--");

        for (int i = effects.size() - 1; i >= 0; i--) if(effectUpdate(effects.get(i)) == EffectUpdateResult.DEATH) return;

        boolean nodials = true;
        for(Dialogue d : myRoom.dialogues) if(!d.isAtEnd() && d.allActorsAlive() && d.getCurrentActor() == this) 
        {
            nodials &= false;
            d.next(); 
            Dialogue.processLeaf(d.getCurrent());
        }
        if(nodials) chooseAction(myRoom);
    }
    
    @Override
    public String getPluralDescription()
    {
        return pluralDescription;
    }
}
