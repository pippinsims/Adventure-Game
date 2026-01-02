package adventuregame;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Unit;
import adventuregame.items.Armor;

public abstract class Effectable extends Describable{
    protected ArrayList<Effect> effects = new ArrayList<Effect>();
    protected float maxHealth = 10;
    protected float health = maxHealth;
    protected boolean isStunned = false; //isStunned makes Enemy units become unstunned as their next action, instead of attacking

    public enum EffectUpdateResult
    {
        DEATH,
        VERYHURT,
        HURT,
        NONE
    }

    final public EffectUpdateResult effectUpdate(Effect e)
    {
        //System.out.println("Health: " + health + " (temporary println)");

        boolean effectIsOver = false;
        EffectUpdateResult result = EffectUpdateResult.NONE;
        switch (e.getType()) 
        {
            case FIRE:
                result = receiveDamage(new Damage(e.strength, Damage.Type.FIRE, e, "You're burned by fire"));
                effectIsOver = e.cooldown.decrement();
                break;

            case POISON:
                result = receiveDamage(new Damage(e.strength, Damage.Type.BLUNT, e, "You're pierced through with poison"));
                effectIsOver = e.cooldown.decrement();
                break;

            case PSYCHSTRIKE:
                result = receiveDamage(new Damage(e.strength, Damage.Type.PSYCHIC, e, "Your mind is vexed of the psychic strike"));
                isStunned = true;
                effectIsOver = e.cooldown.decrement();
                break;

            case VITALITYDRAIN:
                maxHealth -= e.strength;
                //health > maxHealth would never happen
                Utils.slowPrintln("You're stuck by Drain, max health decreased by " + e.strength);
                result = receiveDamage(new Damage(e.strength, Damage.Type.UNBLOCKABLE));
                effectIsOver = e.cooldown.decrement();
                break;
            
            case VITALITYGROW:
                maxHealth += e.strength;
                Utils.slowPrintln("Max health increased by " + e.strength);
                effectIsOver = e.cooldown.decrement();
                break;

            case WEAKNESS:
                effectIsOver = e.cooldown.decrement();
                break;
        }

        if(effectIsOver) for(Effect check : new ArrayList<>(effects)) if(check == e)
        {
            effects.remove(e);
            break;
        }

        return result;
    }

    private Damage computeDefenseVal(Damage d)
    {
        float val = d.getValue();
        if(this instanceof Unit)
        {
            Unit u = (Unit)this;

            for(Armor i : u.getInventory().getArmor()) if(i.isEquipped())
            {
                for(Map.Entry<Damage.Type,Float> def : ((Armor)i).getDefense().entrySet()) if(def.getKey() == d.getType()) 
                {
                    float v = def.getValue();
                    if(v > 0) Utils.slowPrintln(((Armor)i).getArmorDesc() + " blocked " + v + " " + Damage.descMap.get(def.getKey()) + " damage.");
                    val -= v;
                }
            }
        }
        if(val < 0)
        {
            Utils.slowPrintln("Armor blocked all damage!");
            val = 0;
        }

        d.setValue(val);
        return d;
    }

    public EffectUpdateResult receiveDamage(Damage damage)
    {
        Utils.slowPrintln(getName() + " is hit by " + damage.getValue() + " " + Damage.descMap.get(damage.getType()) + " damage.");

        Damage newdamage = computeDefenseVal(new Damage(damage));
        
        switch (newdamage.getType()) {
            case BASIC: case BLUNT:
                health -= newdamage.getValue();
                break;
            
            case PSYCHIC:
                health -= new Random().nextFloat(newdamage.getValue() + 1); //from 0 to damage
                break;

            case FIRE:
                if(newdamage.getMode() == Damage.Mode.EFFECT && newdamage.hasEffect())
                    health -= newdamage.getEffect().strength;
                else
                    health -= newdamage.getValue() + 1;
                break;

            case UNBLOCKABLE:
                health -= damage.getValue();
                break;
        }

        if(damage.getMode() == Damage.Mode.INFLICTEFFECT && damage.hasEffect())
            addEffect(damage.getEffect());

        if(health/maxHealth > 0.8)
            return EffectUpdateResult.HURT;
        else if(health > 0)
            return EffectUpdateResult.VERYHURT;
        else
        {
            Game.kill(this);
            return EffectUpdateResult.DEATH;
        }
    }

    final public void addEffect(Effect e)
    {
        effects.add(e);

        Utils.slowPrint("You have been effected by " + e.getName());
        int dur = e.cooldown.getRemainingDuration();
        if(dur >= 0)
            Utils.slowPrintln(", and will be effected by it for " + dur + " more turn" + (dur != 1 ? "s" : "") + ".");
        else
            Utils.slowPrintln(", and will be effect by it forever.");
    }

    final public boolean hasEffect(Effect.Type t)
    {
        for(Effect e : effects) if(e.getType() == t) return true;
        return false;
    }
    
    final public void removeAllOf(Effect.Type t)
    {
        for(Effect e : new ArrayList<>(effects)) if(e.getType() == t) effects.remove(e);
    }

    final public float getHealth()
    {
        return health;
    }

    final public float getMaxHealth()
    {
        return maxHealth;
    }

    final public void updateMaxHealth(float h)
    {
        maxHealth = h;
    }

    //MARK: for testing
    public void updateAllEffectsWithoutResult()
    {
        for (int i = effects.size() - 1; i >= 0; i--)
        {
            effectUpdate(effects.get(i));
        }
    }
}