package adventuregame.interfaces;

import adventuregame.Damage;

public interface Item extends Describable
{
    public void action();
    public Damage getDamage();
    public boolean isWeapon();
    public String getName();
}
