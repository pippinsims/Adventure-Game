package adventuregame.abstractclasses;

import java.util.ArrayList;
import java.util.Random;

import adventuregame.Damage;
import adventuregame.Dialogue;
import adventuregame.Inventory;
import adventuregame.Utils;
import adventuregame.interactibles.wallinteractibles.Door;
import adventuregame.items.Weapon;

public abstract class NonPlayer extends Unit {
    protected Inventory inv;
    protected Damage dmg;
    protected int wisdom;
    public ArrayList<Dialogue> dialogues = new ArrayList<>();

    protected enum Action
    {
        NONE,
        DIALOGUE,
        ATTACK
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

    public Inventory getInventory() { return inv; }

    public Damage getAttackDamage() { return dmg; }

    public int getWisdom() { return wisdom; }

    protected void talk()
    {
        boolean didTalk = dialogues.getFirst().next();
        dialogues.remove(0);
        if(!didTalk) chooseAction();
    }

    public abstract void performAction(Action a);

    public void chooseAction()
    {
        if(isStunned || myRoom.players.isEmpty())
        {
            performAction(Action.NONE);
            isStunned = false;
        }
        else if (!dialogues.isEmpty() && dialogues.getFirst().getInitiator() == this)
            performAction(Action.DIALOGUE);
        else if (!myRoom.enemies.isEmpty())
            performAction(Action.ATTACK);
    }

    @Override
    public void updateUnit() {
        System.out.println("\t\t\t\t\t\t\t\t--" + name + "'" + (name.charAt(name.length() - 1) != 's' ? "s" : "") + " Turn--");

        for (int i = effects.size() - 1; i >= 0; i--) if(effectUpdate(effects.get(i)) == EffectUpdateResult.DEATH) return;

        chooseAction();
    }

    public static class Bofer extends NonPlayer
    {
        { setDefaults(10, new Inventory(10), 1, 10, "Grassy bofer", "Bofer"); }

        @Override
        public void setDefaults(int m, Inventory i, int dmg, int w, String des, String name)
        {
            maxHealth = m;
            health = maxHealth;
            inv = i;
            this.dmg = new Damage(dmg);
            wisdom = w;
            description = des;
            this.name = name;
            deathMsg = "You ended " + getName();
        }

        @Override
        public void performAction(Action a) {
            switch(a)
            {
                case ATTACK:
                    Enemy e = myRoom.enemies.getFirst();
                    Weapon w = new Weapon() {
                        {   description = "Punch"; 
                            atkmsg = "You heave a mighty blow at the " + e.getModifiedDescription("sad"); }
                        @Override public void action(Unit u, boolean isFinal) {}
                        @Override public Damage getDamage() { return getAttackDamage(); }
                    };
                    this.attack(e, w.getDamage(), w.getAttackMessage());
                    break;
                case DIALOGUE:
                    talk();
                    break;
                case NONE: 
                    if(new Random().nextInt(2) == 1) Utils.getFirst(myRoom.interactibles, Door.class).action(this);
                    else Utils.slowPrintln("Bofer does nothing.");
                break;
            }
        }
    }
}
