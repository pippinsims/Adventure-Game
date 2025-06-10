package adventuregame;

public class Damage {

    private int value;
    private Type t;
    private String message;
    
    public enum Type{
        BASIC,
        FIRE,
        PSYCHIC;
    }

    public Damage(int v, Type ty, String msg)
    {
        value = v;
        t = ty;
        message = msg;
    }

    public int getValue()
    {
        return value;
    }

    public Type getType()
    {
        return t;
    }

    public String getMessage()
    {
        return message;
    }
}
