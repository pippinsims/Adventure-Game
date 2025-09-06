package adventuregame;

import adventuregame.abstractclasses.Describable;

public class Effect extends Describable{
    
    //TODO fill descriptions map at start of Environment.main() using file
    Type type;
    Cooldown cooldown;
    String description;
    int strength;
    String name;

    public Effect(Type t, int duration, int strength)
    {
        type = t;
        cooldown = new Cooldown(duration, t);
        this.strength = strength;
        description = Environment.effectDescriptions.get(type).t2();
        this.name = Environment.effectDescriptions.get(type).t1();
    }

    public Effect(Type t, int duration, int strength, String name) 
    {
        type = t;
        cooldown = new Cooldown(duration, t);
        this.strength = strength;
        description = Environment.effectDescriptions.get(type).t2();
        this.name = name;
    }

    public enum Type{
        FIRE,
        PSYCHSTRIKE
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
