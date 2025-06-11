package adventuregame;

import java.util.ArrayList;
import java.util.Random;

public class Effectable {
    protected ArrayList<Effect> effects = new ArrayList<Effect>();
    protected float maxHealth = 10;
    protected float health = maxHealth;
    protected boolean isStunned = false; //isStunned makes Enemy units become unstunned as their next action

    public enum EffectUpdateResult
    {
        DEATH,
        VERYHURT,
        HURT,
        NONE;
    }

    final public EffectUpdateResult effectUpdate(Effect e)
    {
        Utils.slowPrint("You have been effected by " + e.name);
        Utils.slowPrintln(", and will be effected by it for " + e.cooldown.getDuration() + " more turns.");

        boolean effectIsOver = false;
        EffectUpdateResult result = EffectUpdateResult.NONE;
        switch (e.getType()) {
            case FIRE: //for fire and psychstrike, result is damageresult
                result = receiveDamage(e.strength, Damage.Type.BASIC);
                effectIsOver = e.cooldown.decrement();
                break;

            case PSYCHSTRIKE:
                result = receiveDamage(e.strength, Damage.Type.PSYCHIC);
                isStunned = true;
                effectIsOver = e.cooldown.decrement();
                break;
        
            default:
                break;
        }

        if(effectIsOver)
        {
            // no .equals is defined for effect and that is what we want because could be multiple
            // of a certain type of effect (remove by reference is good here)
            effects.remove(e);
        }

        return result;
    }

    // returns true if the effectable died
    final public EffectUpdateResult receiveDamage(int damage, Damage.Type type)
    {
        float startHealth = health;

        switch (type) {
            case BASIC:
                health -= damage;
                break;
            
            case PSYCHIC:
                health -= new Random().nextInt(damage + 1);
                break;

            case FIRE:
                health -= damage + 1;
                addEffect(new Effect(Effect.Type.FIRE, new Cooldown(3, Effect.Type.FIRE), damage, "fire effect"));
                break;
        
            default:
                break;
        }

        if(health/startHealth > 0.8)
        {
            return EffectUpdateResult.HURT;
        }
        else if(health != 0)
            return EffectUpdateResult.VERYHURT;
        else
            return EffectUpdateResult.DEATH;
    }

    final public void addEffect(Effect e)
    {
        effects.add(e);
    }

    final public float getHealth()
    {
        return health;
    }
}