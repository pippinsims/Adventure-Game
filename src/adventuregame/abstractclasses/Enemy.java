package adventuregame.abstractclasses;
import java.util.ArrayList;
import java.util.Random;

import adventuregame.Damage;
import adventuregame.Dialogue;
import adventuregame.Inventory;
import adventuregame.Utils;
import adventuregame.enemies.Skeleton;
import adventuregame.interactibles.SkeletonInteractible;

public abstract class Enemy extends Unit
{
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

    // public void randomizeDesc() { randomDescription = getRandomDescription(); }

    public Inventory getInventory() { return inv; }

    public Damage getAttackDamage() { return dmg; }

    public int getWisdom() { return wisdom; }

    public void chooseAction()
    {
        //DECISIONMAKING FOR ENEMY
        if(isStunned || myRoom.players.isEmpty())
        {
            performAction(0);
            isStunned = false;
        }
        else if (!dialogues.isEmpty() && dialogues.getFirst().getInitiator() == this)
            performAction(1);
        else
            performAction(2);
    }

    public abstract void pleaResponse();

    public abstract void performAction(int i);

    @Override
    public void updateUnit() {
        System.out.println("\t\t\t\t\t\t\t\t--" + name + "'" + (name.charAt(name.length() - 1) != 's' ? "s" : "") + " Turn--");

        for (int i = effects.size() - 1; i >= 0; i--) if(effectUpdate(effects.get(i)) == EffectUpdateResult.DEATH) return;

        chooseAction();
    }

    @Override
    public EffectUpdateResult receiveDamage(Damage damage)
    {
        EffectUpdateResult out = super.receiveDamage(damage); 
        if(out == EffectUpdateResult.DEATH && this instanceof Skeleton) new SkeletonInteractible(myRoom, inv);
        
        return out;
    }
}
