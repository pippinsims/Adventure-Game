package adventuregame;

import adventuregame.interfaces.Describable;

import java.util.HashMap;
import java.util.Map;

public class Effect implements Describable{
    
    // fill map at start of Environment.main() using file
    static Map<Type,String> effectDescriptions = new HashMap<>();
    Type type;
    Cooldown cooldown;
    String description;
    int strength;
    String name;

    public Effect(Type t, Cooldown c, int s, String name) {
        type = t;
        cooldown = c;
        strength = s;
        description = effectDescriptions.get(type);
    }

    public enum Type{
        FIRE,
        PSYCHSTRIKE;
    }

    public Type getType()
    {
        return type;
    }

    public Cooldown getCooldown()
    {
        return cooldown;
    }

    @Override
    public String getDescription() 
    {
        return description;
    }

    @Override
    public String getName() {
        return name;
    }
}

class Cooldown{
    private int duration;
    private Effect.Type type;

    public Cooldown(int d, Effect.Type t)
    {
        duration = d;
        type = t;
    }

    public Effect.Type getType()
    {
        return type;
    }

    public int getDuration()
    {
        return duration;
    }

    public boolean decrement()
    {
        return --duration == 0;
    }
}
