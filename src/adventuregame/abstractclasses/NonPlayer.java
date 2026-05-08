package adventuregame.abstractclasses;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import adventuregame.Damage;
import adventuregame.Dialogue;
import adventuregame.Effect;
import adventuregame.Game;
import adventuregame.Inventory;
import adventuregame.Player;
import adventuregame.Utils;
import adventuregame.interactibles.SkeletonInteractible;
import adventuregame.items.Armor;
import adventuregame.items.Weapon;

public abstract class NonPlayer extends Unit {
    protected Inventory.Whole inv;
    protected int wisdom;
    private boolean hostile = false;
    public ArrayList<Dialogue> dialogues = new ArrayList<>();
    public ArrayList<Unit> enemies = new ArrayList<>();
    public ArrayList<Unit> friends = new ArrayList<>();

    protected enum Action
    {
        NORMAL,
        DIALOGUE,
        ATTACK
    }

    protected NonPlayer() {}

    public NonPlayer(int health, Inventory.Whole inventory, int wisdom, String description, String pluralDescription, String name) 
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

    public void setHostile()
    {
        hostile = true;
    }

    public void setNotHostile()
    {
        hostile = false;
        for(Unit u : new ArrayList<>(enemies)) if(u instanceof Player) enemies.remove(u);
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

    public void setDefaults(int m, Inventory.Whole i, int w, String des, String name)
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
        return Utils.names1[Utils.rand.nextInt(Utils.names1.length)] + Utils.names2[Utils.rand.nextInt(Utils.names2.length)];
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
            case "Goblin"  : names = new String[]{"Screebling Squabbler", "pale man", "bllork", "awkward fellow"}; break;
            case "Skeleton": names = new String[]{"Skeleton"}; break;
        }
        return names[Utils.rand.nextInt(names.length)];
    }

    public Inventory.Whole getInventory() { return inv; }

    public int getWisdom() { return wisdom; }

    protected void talk()
    {
        boolean didTalk = dialogues.getFirst().next();
        dialogues.remove(0);
        if(!didTalk) chooseAction();
    }

    protected Weapon defaultWeapon()
    {
        return new Weapon.Punch("You punch!");
    }

    public void updateAwareness()
    {
        if(hostile) for(Player p : myRoom.players) if(!enemies.contains(p)) enemies.add(p);
        for(Unit f : friends) if(f instanceof NonPlayer) for(Unit e : ((NonPlayer)f).enemies) if(!enemies.contains(e)) enemies.add(e);
    }

    protected void attack()
    {
        for(Unit u : myRoom.all())
        {
            if(enemies.contains(u))
            {
                //TODO just attacks first enemy
                Weapon chsn = defaultWeapon();
                for(Weapon w : inv.getWeapons()) if(w.getDamage().getValue() > chsn.getDamage().getValue()) chsn = w;
                this.attack(u, chsn.getDamage(), "You attack " + u.getName() + "!");
                if(u.isDead()) enemies.remove(u);
                break;
            }
        }
    }

    public abstract void performAction(Action a);

    public void chooseAction()
    {
        updateAwareness();

        if(isStunned || myRoom.players.isEmpty())
        {
            performAction(Action.NORMAL);
            isStunned = false;
        }
        else if (!dialogues.isEmpty() && dialogues.getFirst().getInitiator() == this)
            performAction(Action.DIALOGUE);
        else if (Utils.overlap(enemies, myRoom.all()))
            performAction(Action.ATTACK);
        else
            performAction(Action.NORMAL);
    }

    @Override
    public void updateUnit() {
        System.out.println("\t\t\t\t\t\t\t\t--" + Utils.possessiveOf(name) + " Turn--");

        for(Effect e : new ArrayList<>(effects)) if(effectUpdate(e) == EffectUpdateResult.DEATH) return;

        chooseAction();
    }

    public static class Goblin extends NonPlayer {

        { pluralDescription = "goblins"; }

        public Goblin(int health) { 
            super();
            setDefaults(health, new Inventory.Whole(5), 20, "goblin with pointy ears", null);
        }

        public Goblin(int health, Inventory.Whole inventory, int wisdom) { 
            super();
            setDefaults(health, inventory, wisdom, "goblin with pointy ears", null);
        }

        @Override
        public void setDefaults(int m, Inventory.Whole i, int w, String des, String name)
        {
            super.setDefaults(m, i, w, des, name);

            int r = Utils.rand.nextInt(4);
            descMap.put("Laur", (new String[] {"Screeblin Squabbler","pale man","awkward fellow","bllork"})[r]);
            pDescMap.put("Laur", (new String[] {"Screeblin Squabblers","pale men","awkward fellas","bllorks"})[r]);
        }


        @Override protected Weapon defaultWeapon()
        {
            try{
                return inv.getWeapons().getFirst();
            }
            catch(NoSuchElementException e)
            {
                return new Weapon.Punch("The " + getModifiedDescription("scary") + " raises it's fiendish arms and jumps at you with startling dexterity.", new Damage(4));
            }
        }

        @Override
        public void performAction(Action a) {
            switch(a)
            {
                case NORMAL:
                    Utils.slowPrintln("The " + getModifiedDescription("sad") + " stands still" + (Game.isLaur ? "." : ", sort of like a Zucchini Mushroom."));
                    break;

                case DIALOGUE: talk(); break;
                case ATTACK: attack(); break;
            }
        }
    }

    public static class Skeleton extends NonPlayer{

        {
            pluralDescription = "skeletons";
        }

        public Skeleton()
        {
            super();
            setDefaults(20, new Inventory.Whole(6), 0, "skeleton", "Oess");
            setHostile();
        }

        public Skeleton(Inventory.Whole i)
        {
            super();
            setDefaults(20, i, 0, "skeleton", "Oess");
            for(Armor a : i.getArmor()) i.equip(a, true);
            setHostile();
        }

        @Override protected Weapon defaultWeapon()
        {
            try{
                return inv.getWeapons().getFirst();
            }
            catch(NoSuchElementException e)
            {
                return new Weapon.Punch(getName() + " punches!", new Damage(5));
            }
        }

        @Override
        public EffectUpdateResult receiveDamage(Damage damage)
        {
            EffectUpdateResult out = super.receiveDamage(damage); 
            if(out == EffectUpdateResult.DEATH && this instanceof Skeleton) new SkeletonInteractible(myRoom, inv);
            
            return out;
        }

        @Override
        public void performAction(Action a) {
            switch(a)
            {
                case NORMAL:
                    Utils.slowPrintln("The " + getModifiedDescription("sad") + " is motionless.");
                    break;

                case DIALOGUE: talk(); break;
                case ATTACK: attack(); break;
            }
        }
    }
}
