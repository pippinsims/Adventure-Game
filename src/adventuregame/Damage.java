package adventuregame;

public class Damage {

    private int value;
    private Type type;
    private String message;
    
    public enum Type{
        BASIC,
        FIRE,
        PSYCHIC,
        BLUNT;
    }

    public Damage(int value, Type type, String msg)
    {
        this.value = value;
        this.type = type;
        message = msg;
    }

    public int getValue()
    {
        return value;
    }

    public Type getType()
    {
        return type;
    }

    public String getMessage()
    {
        return message;
    }
}
