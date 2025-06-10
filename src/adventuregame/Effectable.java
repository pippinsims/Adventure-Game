package adventuregame;

import java.util.ArrayList;
import java.util.Random;

public class Effectable{
    protected ArrayList<Effect> effects = new ArrayList<Effect>();
    protected int maxHealth = 10;
    protected int health = maxHealth;
    protected boolean isStunned = false; //isStunned makes Enemy units become unstunned as their next action

    final public boolean effectUpdate(Effect e)
    {
        Utils.slowPrintln("You have been effected by " + e.description + " for " + e.cooldown.getDuration() + " turns.");

        boolean effectResult = false, result = false;
        switch (e.getType()) {
            case FIRE: //for fire and psychstrike, result is damageresult
                result = receiveDamage(e.strength, Damage.Type.BASIC);
                effectResult = e.cooldown.decrement();
                break;

            case PSYCHSTRIKE:
                result = receiveDamage(e.strength, Damage.Type.PSYCHIC);
                isStunned = true;
                effectResult = e.cooldown.decrement();
                break;
        
            default:
                break;
        }

        if(effectResult) 
        {
            effects.remove(e);
        }

        return result;
    }

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