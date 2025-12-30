package adventuregame;

import java.util.Map;

public class Damage {

    private float value;
    private Type type;
    private Mode mode = Mode.DEFAULT;
    private Effect damageEffect = null;

    public static enum Type
    {
        BASIC,
        BLUNT,
        FIRE,
        PSYCHIC,
        UNBLOCKABLE
    }

    public static Map<Type, String> descMap = Map.ofEntries(Map.entry(Type.BASIC, "basic"),
                                                               Map.entry(Type.BLUNT, "blunt"),
                                                               Map.entry(Type.FIRE, "fire"),
                                                               Map.entry(Type.PSYCHIC, "psychic"),
                                                               Map.entry(Type.UNBLOCKABLE, "unblockable"));

    public static enum Mode
    {
        DEFAULT,
        INFLICTEFFECT,
        EFFECT
    }

    public Damage(Damage toClone)
    {
        value = toClone.getValue();
        type = toClone.getType();
        mode = toClone.getMode();
        damageEffect = toClone.getEffect();
    }

    public Damage(float value)
    {
        this.value = value;
        this.type = Type.BASIC;
    }

    public Damage(float value, Type type)
    {
        this.value = value;
        this.type = type;
    }

    public Damage(float value, Type type, Mode mode)
    {
        this.value = value;
        this.type = type;
        this.mode = mode;

        if(mode == Mode.INFLICTEFFECT || mode == Mode.EFFECT)
        {
            throw new RuntimeException(mode + " should have a non-null effect! (wrong constructor)");
        }
    }

    public Damage(float value, Type type, Mode mode, Effect effect)
    {
        this.value = value;
        this.type = type;
        this.mode = mode;

        if(mode == Mode.INFLICTEFFECT || mode == Mode.EFFECT)
        {
            if(effect == null) throw new IllegalArgumentException(mode + " should have a non-null effect!");
            damageEffect = effect;
        }
    }

    public Damage(float value, Type type, Effect inflictedEffect, String msg)
    {
        this.value = value;
        this.type = type;
        this.mode = Mode.EFFECT;
        damageEffect = inflictedEffect;
    }

    public float getValue()
    {
        return value;
    }

    public void setValue(float value)
    {
        this.value = value;
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
}
