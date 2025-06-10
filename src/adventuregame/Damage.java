package adventuregame;

public class Damage {

    private int value;
    private Type t;
    
    public enum Type{
        BASIC,
        FIRE,
        PSYCHIC;
    }

    public Damage(int v, Type ty)
    {
        value = v;
        t = ty;
    }

    public int getValue()
    {
        return value;
    }

    public Type getType()
    {
        return t;
    }
}
