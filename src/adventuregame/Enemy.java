package adventuregame;
import java.util.Random;

import adventuregame.abstractclasses.Unit;

public class Enemy extends Unit
{
    private Inventory inv;
    private Damage dmg;
    private int wisdom;
    private String description, randomDescription;
    private String name;

    public String pluralOf(String str)
    {
        switch (str) {
            case "goblin": 
            case "Screebling Squabbler": 
            case "Mushroom": 
            case "bllork":
            case "Shroomie": 
            case "Delicious Fun Guy":
            case "glittering boy":
            case "Skeleton":
                return str + 's';
            case "Those-Who-Feast":
                return str;
            case "awkward fellow":
                return "awkward fellas";
            case "pale man": 
                return "pale men";
            case "Gold Man":
                return "Gold Men";
            case "Knower of Forest Beds and Roots":
                return "Knowers of Forest Beds and Roots";
            
            default:
                throw new UnsupportedOperationException("No plural for '"+str+"'");
        }
    }

    private void setDefaults(int m, Inventory i, int dmg, int w, String des, String name)
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

    public Enemy(int health) { setDefaults(health, new Inventory(5), 4, 20, "goblin", null); }

    public Enemy(int health, Inventory inventory, int damage, int wisdom) { setDefaults(health, inventory, damage, wisdom, "goblin", null); }

    public Enemy(int health, Inventory inventory, int damage, int wisdom, String description) { setDefaults(health, inventory, damage, wisdom, description, null); }

    public Enemy(int health, Inventory inventory, int damage, int wisdom, String description, String name) { setDefaults(health, inventory, damage, wisdom, description, name); }

    private String generateName() 
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

    public String getRandomDescription()
    {
        String[] names = new String[]{description};
        switch(description)
        {
            case "goblin"          : names = new String[]{"Screebling Squabbler", "pale man", "bllork", "awkward fellow"}; break;
            case "Mushroom Monster": names = new String[]{"Mushroom","Shroomie", "Delicious Fun Guy", "Those-Who-Feast", "Knower of Forest Beds and Roots"}; break;
            case "Gold Man"        : names = new String[]{"glittering boy"}; break;
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

    public void pleaResponse()
    {
        Utils.slowPrint(getName() + ": ");
        switch(description)
        {
            case "goblin":
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
                break;
            case "Mushroom Monster":
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
                }
                break;
            case "Gold Man": 
                Utils.slowPrintln("I am the Gold Man."); 
                break;
        }
    }

    public void performAction(int i) 
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

            case "Gold Man":
                switch(i)
                {
                    case 1:
                        str = "Gold Man: I hold a thousand gifts.";
                        break;
                    case 2:
                        str = "Gold Man: I choose not to destroy you.";
                        break;
                }
                break;
        }

        Utils.slowPrintln(str);
        if(i == 2 && !description.equals("Gold Man")) this.attack(myRoom.players.get(0), getAttackDamage());
    }

    @Override
    public void updateUnit() throws Exception {
        System.out.println("--" + name + "'" + (name.charAt(name.length() - 1) != 's' ? "s" : "") + " Turn--");

        for (int i = effects.size() - 1; i >= 0; i--) if(effectUpdate(effects.get(i)) == EffectUpdateResult.DEATH) return;

        boolean nodials = true;
        for(Dialogue d : myRoom.dialogues) if(!d.atEnd && d.allActorsAlive() && d.getCurrentActor() == this) 
        {
            nodials &= false;
            d.next(); 
            Dialogue.processLeaf(d.current);
        }
        if(nodials) chooseAction(myRoom);
    }

    @Override
    public String getPluralDescription() 
    {
        if(Environment.isLaur)
            return pluralOf(randomDescription);
        else
            return pluralOf(description);
    }
}
