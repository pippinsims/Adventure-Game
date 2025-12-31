package adventuregame.items;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import adventuregame.Damage;
import adventuregame.Player;
import adventuregame.Utils;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;

public class Armor extends Item {

    private String armorDesc; 
    Unit equippedTo = null;

    public enum MaterialType
    {
        RUSTED,
        ANCIENT_RUSTED,
        ANCIENT
    }

    public enum PartType
    {
        HELMET,
        TORSO,
        GAUNTLETS,
        LEGS,
        BOOTS
    }

    public final static Map<MaterialType,String> armorDescs = Map.ofEntries(Map.entry(MaterialType.RUSTED, "old armor, which has seen far better days, but now it's rusted and tarnished."),
                                                                            Map.entry(MaterialType.ANCIENT_RUSTED, "old rusted armor, you notice an ancient glyph."),
                                                                            Map.entry(MaterialType.ANCIENT, "lodestone-enhanced ancient soldier's armor."));

    protected MaterialType mat;
    protected PartType type;

    public Armor(String name, String description, String pluralDescription, MaterialType mat, PartType type)
    {
        this.name = name;
        this.description = description;
        this.pluralDescription = pluralDescription;
        this.mat = mat;
        this.type = type;
        armorDesc = description;
        equippedTo = null;
    }

    public MaterialType getMat() { return mat; }
    public PartType getPart() { return type; }

    public boolean isEquipped() { return equippedTo != null; }

    @Override
    public void action(Unit u, boolean isFinal) {
        
        if(u.getInventory().getArmor().isEmpty()) throw new IllegalAccessError("Tried to equip armor that wasn't in your inventory!");

        if(equippedTo != null)
        {
            if(!u.getInventory().hasUnequippedArmor())
            {
                Utils.slowPrintln(armorDesc + " unequipped!");
                equippedTo = null;
                description = armorDesc;
            }
            else
            {
                Utils.slowPrintln("You're already holding a piece of unequipped armor! Cannot unequip.");
            }
        }
        else
        {
            Armor samePiece = null;
            for(Armor a : u.getInventory().getArmor()) if(a.isEquipped() && a.getPart() == getPart()) { samePiece = a; break;}
            if(samePiece == null)
            {
                Utils.slowPrintln(armorDesc + " equipped!");
                equippedTo = u;
                description = armorDesc + " (Equipped)";
            }
            else
            {
                equippedTo = u;
                description = armorDesc + " (Equipped)";
                samePiece.action(u, true);
                Utils.slowPrintln(armorDesc + " equipped!");
            }
        }

        if(!isFinal && u instanceof Player) ((Player)u).promptForAction();
    }

    public String getArmorDesc() { return armorDesc; }

    public Map<Damage.Type,Float> getDefense()
    {
        Map<Damage.Type,Float> outd = new HashMap<>();
        float d = 0;
        switch(mat)
        {
            case ANCIENT:
                d = 2;
                outd.put(Damage.Type.BASIC, d);
                outd.put(Damage.Type.BLUNT, d);
                d = new Random().nextInt(2);
                for(int i = 2; i < Damage.Type.values().length - 1; i++) outd.put(Damage.Type.values()[i], d);
                //50% chance of all first 2 tiers of spells on this unit fail
                break;
            case ANCIENT_RUSTED:
                d = 1;
                d *= new Random().nextInt(2);
                outd.put(Damage.Type.BASIC, d);
                outd.put(Damage.Type.BLUNT, d);
                break;
            case RUSTED:
                break;
        }
        switch(type)
        {
            case BOOTS: case GAUNTLETS: case HELMET: case LEGS:
                for(Map.Entry<Damage.Type,Float> e : outd.entrySet()) outd.put(e.getKey(), e.getValue()*0.25f);
                break;
            case TORSO:
                break;
        }

        return outd;
    }
}
