package adventuregame;

import java.util.HashMap;
import java.util.Map;

import adventuregame.abstractclasses.Describable;

public class Effect extends Describable{
    
    // fill map at start of Environment.main() using file
    static Map<Type,String> effectDescriptions = new HashMap<>();
    Type type;
    Cooldown cooldown;
    String description;
    int strength;
    String name;
    final boolean isDamaging;

    public Effect(Type t, Cooldown c, int s, String name) {
        type = t;
        cooldown = c;
        strength = s;
        description = effectDescriptions.get(type);
        this.name = name;
        switch(type) {
            case FIRE:
                isDamaging = true;
                break;
            case PSYCHSTRIKE:
                isDamaging = true;
                break;
            default:
                isDamaging = false;
        }
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

    @Override
    public String getPluralDescription() {
        return description + " effects";
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

    public int getDuration()
    {
        return duration;
    }

    public boolean decrement()
    {
        return --duration == 0;
    }
}
