package adventuregame.abstractclasses;

import java.util.ArrayList;
import java.util.Random;

import adventuregame.Dialogue;
import adventuregame.Inventory;
import adventuregame.Utils;
import adventuregame.items.Weapon;

public abstract class NonPlayer extends Unit {
    protected Inventory inv;
    protected int wisdom;
    public ArrayList<Dialogue> dialogues = new ArrayList<>();
    public ArrayList<Unit> enemies = new ArrayList<>();
    public ArrayList<NonPlayer> friends = new ArrayList<>();

    protected enum Action
    {
        NORMAL,
        DIALOGUE,
        ATTACK
    }

    protected NonPlayer() {}

    public NonPlayer(int health, Inventory inventory, int wisdom, String description, String pluralDescription, String name) 
    { 
        this.maxHealth = health;
        this.health = health;
        inv = inventory;
        this.wisdom = wisdom;
        this.description = description;
        this.pluralDescription = pluralDescription;
        this.name = name;
        deathMsg = "You ended " + getName();
    }

    protected String pluralOf(String str)
    {
        switch (str) 
        {
            case "goblin":
            case "Screebling Squabbler":
            case "bllork":
            case "Skeleton": return str + 's';
            case "awkward fellow": return "awkward fellas";
            case "pale man": return "pale men";
            default: throw new UnsupportedOperationException("No plural for '"+str+"'");
        }
    }

    public void setDefaults(int m, Inventory i, int w, String des, String name)
    {
        maxHealth = m;
        health = maxHealth;
        inv = i;
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

    public Inventory getInventory() { return inv; }

    public int getWisdom() { return wisdom; }

    protected void talk()
    {
        boolean didTalk = dialogues.getFirst().next();
        dialogues.remove(0);
        if(!didTalk) chooseAction();
    }

    protected void attack()
    {
        for(Unit x : myRoom.all())
        {
            if(Utils.contains(enemies, x))
            {
                //TODO just attacks first enemy
                Weapon p = new Weapon.Punch(x instanceof Enemy ? "You heave a mighty blow at the " + ((Enemy)x).getModifiedDescription("sad") : "You attack "+x.getName()+"!");
                this.attack(x, p.getDamage(), p.getAttackMessage());
                if(x.isDead()) for(Unit e : new ArrayList<>(enemies)) if(e == x) enemies.remove(e);
                break;
            }
        }
    }

    public abstract void performAction(Action a);

    public void chooseAction()
    {
        for(Enemy e : myRoom.enemies) if(!Utils.contains(enemies, e)) enemies.add(e);
        for(NonPlayer f : friends) for(Unit e : f.enemies) if(!Utils.contains(enemies, e)) enemies.add(e);

        if(isStunned || myRoom.players.isEmpty())
        {
            performAction(Action.NORMAL);
            isStunned = false;
        }
        else if (!dialogues.isEmpty() && dialogues.getFirst().getInitiator() == this)
            performAction(Action.DIALOGUE);
        else if (!enemies.isEmpty() && Utils.contains(myRoom.all(), enemies.getFirst()))
            performAction(Action.ATTACK);
        else
            performAction(Action.NORMAL);
    }

    @Override
    public void updateUnit() {
        System.out.println("\t\t\t\t\t\t\t\t--" + name + "'" + (name.charAt(name.length() - 1) != 's' ? "s" : "") + " Turn--");

        for (int i = effects.size() - 1; i >= 0; i--) if(effectUpdate(effects.get(i)) == EffectUpdateResult.DEATH) return;

        chooseAction();
    }
}
