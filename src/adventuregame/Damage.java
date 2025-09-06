package adventuregame;


public class Damage {

    private int value;
    private Type type;
    private Mode mode;
    private String message;
    private Effect damageEffect;

    public static enum Type
    {
        BASIC,
        FIRE,
        PSYCHIC,
        BLUNT
    }

    public static enum Mode
    {
        DEFAULT,
        INFLICTEFFECT,
        EFFECT
    }

    public Damage(int value, Type type, String msg)
    {
        this.value = value;
        this.type = type;
        this.mode = Mode.DEFAULT;
        message = msg;
        damageEffect = null;
    }

    public Damage(int value, Type type, Mode mode, String msg) throws Exception
    {
        this.value = value;
        this.type = type;
        this.mode = mode;
        message = msg;

        if(mode == Mode.INFLICTEFFECT || mode == Mode.EFFECT)
        {
            throw new RuntimeException(mode + " should have a non-null effect! (wrong constructor)");
        }
    }

    public Damage(int value, Type type, Mode mode, Effect effect, String msg) throws Exception
    {
        this.value = value;
        this.type = type;
        this.mode = mode;
        message = msg;

        if(mode == Mode.INFLICTEFFECT || mode == Mode.EFFECT)
        {
            if(effect == null)
                throw new RuntimeException(mode + " should have a non-null effect! (wrong parameters)"); 
            damageEffect = effect;
        }
    }

    public Damage(int value, Type type, Effect inflictedEffect, String msg)
    {
        this.value = value;
        this.type = type;
        this.mode = Mode.EFFECT;
        message = msg;
        damageEffect = inflictedEffect;
    }

    public int getValue()
    {
        return value;
    }

    public Type getType()
    {
        return type;
    }

    public Mode getMode()
    {
        return mode;
    }

    public Effect getEffect()
    {
        return damageEffect;
    }

    public boolean hasEffect()
    {
        return damageEffect != null;
    }

    public String getMessage()
    {
        return message;
    }
}
