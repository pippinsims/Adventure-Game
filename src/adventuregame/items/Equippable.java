package adventuregame.items;

import adventuregame.Inventory;
import adventuregame.Player;
import adventuregame.abstractclasses.Item.Actor;
import adventuregame.abstractclasses.Unit;

public abstract class Equippable extends Actor {

    protected String simpleDesc;

    public sealed interface Type permits Type.Clothes, Type.Armor
    {
        public enum Clothes implements Type
        {
            HAT,
            EYE,
            NECK,
            BODY,
            WRIST,
            RING,
            BELT,
            ONBELT,
            PANTS,
            SHOES
        }

        public enum Armor implements Type
        {
            HELMET,
            CHESTPLATE,
            GAUNTLETS,
            LEGS,
            BOOTS
        }
    }

    protected Type type;

    public Equippable(String name, String description, String pluralDescription, Type type)
    {
        this.name = name;
        this.description = description;
        this.pluralDescription = pluralDescription;
        this.type = type;
        simpleDesc = description;
    }

    public Type getPart() { return type; }

    public boolean isEquipped() { return parentInv instanceof Inventory.Whole && ((Inventory.Whole)parentInv).getEquipped().contains(this); }

    public abstract boolean isRequired();

    @Override
    public void action(Unit u, boolean isFinal) {
        //TODO should be pretty similar to Armor.action, except with the requirement stuff.
        // if(u.getInventory().getEquipped().contains(this))

        //armor never required. Pants required if wearing shirt, shirt or dress required
        //if(isRequired(u.getInventory()) && u.getInventory().getEquipped().contains(this)) sout

        if(!isFinal && u instanceof Player) ((Player)u).ableToAct = true;
    }

    public String getSimpleDesc() { return simpleDesc; }

    public void setDescription(String d) { description = d; }
}
