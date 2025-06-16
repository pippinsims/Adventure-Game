package adventuregame.interfaces;

import adventuregame.Damage;

public interface Item extends Describable
{
    public void action();
    public Damage getDamage();
    public default boolean isWeapon() { return false; }
    public String getName();
}
