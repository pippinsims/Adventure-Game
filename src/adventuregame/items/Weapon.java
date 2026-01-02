package adventuregame.items;

import adventuregame.Damage;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;

public abstract class Weapon extends Item {

    protected String atkmsg;

    public abstract Damage getDamage();

    public String getAttackMessage() { return atkmsg; }

    public static class Punch extends Weapon
    {
        public Punch(String atkmsg)
        {
            description = "Punch";
            this.atkmsg = atkmsg;
        }

        @Override public void action(Unit u, boolean isFinal) {}
        @Override public Damage getDamage() { return new Damage(1, Damage.Type.BASIC); }
    }
}
