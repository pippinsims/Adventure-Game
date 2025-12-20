package adventuregame;

import java.util.ArrayList;
import java.util.Random;

import adventuregame.abstractclasses.Describable;

public class Effectable extends Describable{
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

    final public EffectUpdateResult effectUpdate(Effect e) throws Exception
    {
        Utils.slowPrint("You have been effected by " + e.name);
        int dur = e.cooldown.getDuration();
        Utils.slowPrintln(", and will be effected by it for " + dur + " more turn" + (dur == 1 ? "" : "s") + ".");
        //System.out.println("Health: " + health + " (temporary println)");

        boolean effectIsOver = false;
        EffectUpdateResult result = EffectUpdateResult.NONE;
        switch (e.getType()) 
        {
            case FIRE: //for fire and psychstrike, result is the receiveDamage result
                result = receiveDamage(new Damage(e.strength, Damage.Type.FIRE, e, "You're burned by fire"));
                effectIsOver = e.cooldown.decrement();
                break;

            case PSYCHSTRIKE:
                result = receiveDamage(new Damage(e.strength, Damage.Type.PSYCHIC, e, "Your mind is vexed of the psychic strike"));
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

    final public EffectUpdateResult receiveDamage(Damage damage, String message)
    {
        if(message == null) Utils.slowPrintln(damage.getMessage());
        else Utils.slowPrintln(getName() + " is hit by " + message);
        switch (damage.getType()) {
            case BASIC:
                health -= damage.getValue();
                break;
            
            case PSYCHIC:
                health -= new Random().nextInt(damage.getValue() + 1); //from 0 to damage
                break;

            case FIRE:
                if(damage.getMode() == Damage.Mode.EFFECT && damage.hasEffect())
                    health -= damage.getEffect().strength;
                else
                    health -= damage.getValue() + 1;
                break;
        
            default:
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
        if(damage.getMessage().charAt(0) != '2') return receiveDamage(damage, null);
        else return receiveDamage(damage, damage.getMessage().substring(1));
    }

    final public void addEffect(Effect e)
    {
        effects.add(e);
    }

    final public float getHealth()
    {
        return health;
    }

    //TODO these are just here because they have to be, idk what to do with them and i don't feel like we'd ever use them
    @Override
    public String getPluralDescription() 
    {
        throw new UnsupportedOperationException("Unimplemented method 'getPluralDescription'");
    }

    @Override
    public String getDescription() 
    {
        throw new UnsupportedOperationException("Unimplemented method 'getDescription'");
    }

    @Override
    public String getName() 
    {
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
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