package adventuregame.items;

import adventuregame.Inventory;
import adventuregame.Player;
import adventuregame.Utils;
import adventuregame.abstractclasses.Item.Actor;
import adventuregame.items.Equippable.Type.Clothes;
import adventuregame.abstractclasses.Unit;

public abstract class Equippable extends Actor {

    private String simpleDesc;
    private Type type;

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

    public String getSimpleDesc() { return simpleDesc; }

    public void setDescription(String d) { description = d; }

    public final boolean isRequired() { 
        if(!(parentInv instanceof Inventory.Whole)) return false;
        switch (type) {
            case Clothes.BODY : return true;
            case Clothes.PANTS: return Utils.contains(((Inventory.Whole)parentInv).getEquipped(Clothes.BODY), Equippable.Dress.class);
            default           : return false;
        }
    }

    @Override
    public void action(Unit u, boolean isFinal) {
        if(isEquipped())
        {
            if(!isRequired()) u.getInventory().unequip(this, false);
            else Utils.slowPrintln("You can't take that off!");
        }
        else u.getInventory().equip(this, false);

        if(!isFinal && u instanceof Player) ((Player)u).ableToAct = true;
    }

    public sealed interface Type permits Type.Clothes, Type.Armor
    {
        public enum Clothes implements Type
        {
            HAT, EYE, NECK, BODY, WRIST, RING, BELT, ONBELT, PANTS, SHOES
        }

        public enum Armor implements Type
        {
            HELMET, CHESTPLATE, GAUNTLETS, LEGS, BOOTS
        }
    }

    public static class Dress extends Equippable
    {
        public Dress(String name, String description, String pluralDescription) {
            super(name, description, pluralDescription, Clothes.BODY);
        }   
    }

    public static class Shirt extends Equippable
    {
        public Shirt(String name, String description, String pluralDescription) {
            super(name, description, pluralDescription, Clothes.BODY);
        }   
    }

    public static class Pants extends Equippable
    {
        public Pants(String name, String description, String pluralDescription) {
            super(name, description, pluralDescription, Clothes.PANTS);
        }   
    }

    public static class Hat extends Equippable
    {
        public Hat(String name, String description, String pluralDescription) {
            super(name, description, pluralDescription, Clothes.HAT);
        }   

        public static class Hairpin extends Hat
        {
            public Hairpin()
            {
                super("Hair pin", "tapering wooden pin to hold back long hair", "hair pins");
            }
            
            @Override
            public void action(Unit u, boolean isFinal) {
                if(u.hasLongHair)
                    super.action(u, isFinal);
                else
                {
                    Utils.slowPrintln("Your hair isn't long enough to pin it back!");
                    if(u instanceof Player) ((Player)u).ableToAct = !isFinal;
                }
            }
        }
    }
}
