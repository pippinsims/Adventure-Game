package adventuregame.abstractclasses;

import adventuregame.Damage;

public abstract class Item extends Describable
{
    public abstract void action();
    public abstract Damage getDamage() throws Exception;
    public boolean isWeapon() { return false; }
    public abstract String getName();
}
