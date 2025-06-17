package adventuregame.abstractclasses;

import adventuregame.Damage;

public abstract class Item extends Describable
{
    public abstract void action();
    public abstract Damage getDamage();
    public boolean isWeapon() { return false; }
    public abstract String getName();
}
