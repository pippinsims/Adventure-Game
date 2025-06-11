package adventuregame;

import java.util.ArrayList;
import java.util.Random;

public class Effectable {
    protected ArrayList<Effect> effects = new ArrayList<Effect>();
    protected int maxHealth = 10;
    protected int health = maxHealth;
    protected boolean isStunned = false; //isStunned makes Enemy units become unstunned as their next action

    final public boolean effectUpdate(Effect e)
    {
        Utils.slowPrint("You have been effected by " + e.name);
        Utils.slowPrintln(", and will be effected by it for " + e.cooldown.getDuration() + " more turns.");

        boolean effectIsOver = false, result = false;
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
    final public boolean receiveDamage(int damage, Damage.Type type)
    {
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
        return health <= 0;
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