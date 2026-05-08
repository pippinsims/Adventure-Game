package adventuregame;

import java.util.Map;

import adventuregame.Utils.Tuple;
import adventuregame.abstractclasses.Describable;

public class Effect extends Describable{

    public static final Map<Type, Tuple<String, String>> effectDescriptions = Map.ofEntries(Map.entry(Type.FIRE         , new Tuple<String,String>("fire"                , "BURNINGNESS")),
                                                                                            Map.entry(Type.PSYCHSTRIKE  , new Tuple<String,String>("psychstrike"         , "strong vexation of mind")),
                                                                                            Map.entry(Type.POISON       , new Tuple<String,String>("poison"              , "an ill feeling in thy body")),
                                                                                            Map.entry(Type.VITALITYDRAIN, new Tuple<String,String>("draining"            , "a deep physical sense of mortality")),
                                                                                            Map.entry(Type.VITALITYGROW , new Tuple<String,String>("physically enriching", "a deep physical sense of immortality")),
                                                                                            Map.entry(Type.WEAKNESS     , new Tuple<String,String>("weakness"            , "frailty")));

    //TODO fill descriptions map at start of Environment.main() using file
    Type type;
    Cooldown cooldown;
    float strength;

    public Effect(Effect e)
    {
        this(e.type, 0, e.strength, e.name);
        cooldown = new Cooldown(e.cooldown.getRemainingDuration(), e.cooldown.getType());
        description = new String(e.description);
    }

    public Effect(Type t, int duration, float strength)
    {
        this(t, duration, strength, effectDescriptions.get(t).t1);
    }

    public Effect(Type t, int duration, float strength, String name) 
    {
        type = t;
        cooldown = new Cooldown(duration, t);
        description = effectDescriptions.get(type).t2;
        this.strength = strength;
        this.name = name;
        pluralDescription = effectDescriptions.get(type).t1 + " effects";
    }

    public enum Type{
        FIRE,
        POISON,
        PSYCHSTRIKE,
        VITALITYDRAIN,
        VITALITYGROW,
        WEAKNESS
    }

    public Type getType()
    {
        return type;
    }

    public Cooldown getCooldown()
    {
        return cooldown;
    }
}

class Cooldown{
    private int duration;
    private Effect.Type type;

    public Cooldown(int duration, Effect.Type t)
    {
        this.duration = duration;
        type = t;
    }

    public Effect.Type getType()
    {
        return type;
    }

    public int getRemainingDuration()
    {
        return duration;
    }

    public boolean decrement()
    {
        return --duration == 0;
    }
}
