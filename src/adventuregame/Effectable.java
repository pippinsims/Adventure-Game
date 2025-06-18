package adventuregame;

import java.util.ArrayList;
import java.util.Random;

import adventuregame.abstractclasses.Describable;

public class Effectable extends Describable{
    protected ArrayList<Effect> effects = new ArrayList<Effect>();
    protected float maxHealth = 10;
    protected float health = maxHealth;
    protected boolean isStunned = false; //isStunned makes Enemy units become unstunned as their next action

    public enum EffectUpdateResult
    {
        DEATH,
        VERYHURT,
        HURT,
        NONE
    }

    final public EffectUpdateResult effectUpdate(Effect e)
    {
        Utils.slowPrint("You have been effected by " + e.name);
        Utils.slowPrintln(", and will be effected by it for " + e.cooldown.getDuration() + " more turns.");

        boolean effectIsOver = false;
        EffectUpdateResult result = EffectUpdateResult.NONE;
        switch (e.getType()) 
        {
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
            for(int i = 0; i < effects.size(); i++)
            {
                if(effects.get(i) == e)
                {
                    effects.remove(i);
                    break;
                }
            }
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
                health -= new Random().nextInt(damage + 1); //from 0 to damage
                addEffect(new Effect(Effect.Type.PSYCHSTRIKE, new Cooldown(1, Effect.Type.PSYCHSTRIKE), damage, "psychstrike effect"));
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
        else if(health > 0)
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

    @Override
    public String getPluralDescription() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPluralDescription'");
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDescription'");
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }
}