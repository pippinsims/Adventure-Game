package adventuregame;

import java.util.ArrayList;
import java.util.Random;

import adventuregame.abstractclasses.Describable;

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
                result = receiveDamage(new Damage(e.strength, Damage.Type.UNBLOCKABLE, "You're stuck by Drain, max health decreased by " + e.strength));
                effectIsOver = e.cooldown.decrement();
                break;
            
            case VITALITYGROW:
                maxHealth += e.strength;
                System.out.println("Max health increased by " + e.strength);
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

    final public EffectUpdateResult receiveDamage(Damage damage, String message)
    {
        if(message == null) Utils.slowPrintln(damage.getMessage());
        else Utils.slowPrintln(getName() + " is hit by " + message);
        switch (damage.getType()) {
            case BASIC: case BLUNT:
                health -= damage.getValue();
                break;
            
            case PSYCHIC:
                health -= new Random().nextFloat(damage.getValue() + 1); //from 0 to damage
                break;

            case FIRE:
                if(damage.getMode() == Damage.Mode.EFFECT && damage.hasEffect())
                    health -= damage.getEffect().strength;
                else
                    health -= damage.getValue() + 1;
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
            Environment.kill(this);
            return EffectUpdateResult.DEATH;
        }
    }

    final public EffectUpdateResult receiveDamage(Damage damage)
    {
        return receiveDamage(damage, damage.getMessage().charAt(0) != '2' ? null : damage.getMessage().substring(1));
    }

    final public void addEffect(Effect e)
    {
        effects.add(e);

        Utils.slowPrint("You have been effected by " + e.name);
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

    //MARK: for testing
    public void updateAllEffectsWithoutResult() throws Exception
    {
        for (int i = effects.size() - 1; i >= 0; i--)
        {
            effectUpdate(effects.get(i));
        }
    }
}