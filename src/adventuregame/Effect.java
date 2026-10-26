package adventuregame;

import java.util.Map;

import adventuregame.Utils.Tuple;
import adventuregame.abstractclasses.Describable;
import adventuregame.Effectable.EffectUpdateResult;

public class Effect extends Describable{

    public static final Map<Type, Tuple<String, String>> effectDescriptions = Map.ofEntries(Map.entry(Type.FIRE         , new Tuple<String,String>("fire"                , "BURNINGNESS")),
                                                                                            Map.entry(Type.PSYCHSTRIKE  , new Tuple<String,String>("psychstrike"         , "strong vexation of mind")),
                                                                                            Map.entry(Type.POISON       , new Tuple<String,String>("poison"              , "an ill feeling in thy body")),
                                                                                            Map.entry(Type.VITALITYDRAIN, new Tuple<String,String>("draining"            , "a deep physical sense of mortality")),
                                                                                            Map.entry(Type.VITALITYGROW , new Tuple<String,String>("physically enriching", "a deep physical sense of immortality")),
                                                                                            Map.entry(Type.WEAKNESS     , new Tuple<String,String>("weakness"            , "frailty")));

    //TODO fill descriptions map at start of Environment.main() using file
    private int duration;
    float strength;
    Type type;
    Effectable effectee;

    public enum Type{
        FIRE,
        POISON,
        PSYCHSTRIKE,
        VITALITYDRAIN,
        VITALITYGROW,
        WEAKNESS
    }

    public Effect(Effect e)
    {
        this(e.type, e.duration, e.strength, e.effectee);
    }
    
    public Effect(Type t, int duration, float strength)
    {
        this(t, duration, strength, null);
    }

    private Effect(Type t, int duration, float strength, Effectable effectee) 
    {
        type = t;
        this.duration = duration;
        this.strength = strength;
        this.effectee = effectee;
        Tuple<String, String> d = effectDescriptions.get(type); 
        name = d.t1;
        description = d.t2;
        pluralDescription = d.t1 + " effects";
    }

    public final EffectUpdateResult update()
    {
        if(effectee == null) throw new RuntimeException("Cannot update an Effect that has no effectee");
        //System.out.println("Health: " + e.health + " (temporary println)");

        EffectUpdateResult result = EffectUpdateResult.NONE;
        switch (getType()) 
        {
            case FIRE:
                result = effectee.receiveDamage(new Damage(strength, Damage.Type.FIRE, this, "You're burned by fire"));
                break;

            case POISON:
                result = effectee.receiveDamage(new Damage(strength, Damage.Type.BLUNT, this, "You're pierced through with poison"));
                break;

            case PSYCHSTRIKE:
                result = effectee.receiveDamage(new Damage(strength, Damage.Type.PSYCHIC, this, "Your mind is vexed of the psychic strike"));
                effectee.isStunned = true;
                break;

            case VITALITYDRAIN:
                effectee.maxHealth -= strength;
                //health > maxHealth would never happen
                Utils.slowPrintln("You're stuck by Drain, max health decreased by " + strength);
                result = effectee.receiveDamage(new Damage(strength, Damage.Type.UNBLOCKABLE));
                break;
            
            case VITALITYGROW:
                effectee.maxHealth += strength;
                Utils.slowPrintln("Max health increased by " + strength);
                break;

            case WEAKNESS:
                break;
        }

        if(--duration == 0) effectee.effects.remove(this);

        return result;
    }

    public Type getType()
    {
        return type;
    }

    public int getDuration() 
    { 
        return duration;
    }
}
