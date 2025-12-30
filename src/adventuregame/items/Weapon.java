package adventuregame.items;

import adventuregame.Damage;
import adventuregame.abstractclasses.Item;

public abstract class Weapon extends Item {

    protected String atkmsg;

    public abstract Damage getDamage();

    public String getAttackMessage() { return atkmsg; }
}
