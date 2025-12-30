package adventuregame;

import java.util.Map;

import adventuregame.Utils.Tuple;
import adventuregame.abstractclasses.Describable;

public class Effect extends Describable{

    public static Map<Type, Tuple<String, String>> effectDescriptions = Map.ofEntries(Map.entry(Type.FIRE         , new Tuple<String,String>("fire"                , "BURNINGNESS")),
                                                                                      Map.entry(Type.PSYCHSTRIKE  , new Tuple<String,String>("psychstrike"         , "strong vexation of mind")),
                                                                                      Map.entry(Type.POISON       , new Tuple<String,String>("poison"              , "an ill feeling in thy body")),
                                                                                      Map.entry(Type.VITALITYDRAIN, new Tuple<String,String>("draining"            , "a deep physical sense of mortality")),
                                                                                      Map.entry(Type.VITALITYGROW , new Tuple<String,String>("physically enriching", "a deep physical sense of immortality")),
                                                                                      Map.entry(Type.WEAKNESS     , new Tuple<String,String>("weakness"            , "frailty")));

    //TODO fill descriptions map at start of Environment.main() using file
    Type type;
    Cooldown cooldown;
    int strength;

    public Effect(Effect e)
    {
        type = e.type;
        cooldown = new Cooldown(e.cooldown.getRemainingDuration(), e.cooldown.getType());
        description = new String(e.description);
        strength = e.strength;
        name = e.name;
        pluralDescription = effectDescriptions.get(type).t1() + " effects";
    }

    public Effect(Type t, int duration, int strength)
    {
        type = t;
        cooldown = new Cooldown(duration, t);
        this.strength = strength;
        description = effectDescriptions.get(type).t2();
        name = effectDescriptions.get(type).t1();
        pluralDescription = effectDescriptions.get(type).t1() + " effects";
    }

    public Effect(Type t, int duration, int strength, String name) 
    {
        type = t;
        cooldown = new Cooldown(duration, t);
        this.strength = strength;
        description = effectDescriptions.get(type).t2();
        this.name = name;
        pluralDescription = effectDescriptions.get(type).t1() + " effects";
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
