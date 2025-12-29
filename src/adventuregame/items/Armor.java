package adventuregame.items;

import java.util.Map;
import java.util.Random;

import adventuregame.Damage;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;

public class Armor extends Item {

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
    protected String name, description, pluralDescription;

    public Armor(String name, String description, String pluralDescription, MaterialType mat, PartType type)
    {
        this.name = name;
        this.description = description;
        this.pluralDescription = pluralDescription;
        this.mat = mat;
        this.type = type;
        equippedTo = null;
    }

    public MaterialType getMat() { return mat; }

    public boolean isEquipped() { return equippedTo != null; }


    @Override
    public void action(Unit u) {
        if(equippedTo == u) 
            equippedTo = null;
        else if(equippedTo != null)
        {
            equippedTo.getInventory().remove(this);
            u.getInventory().add(this);
            equippedTo = u;
        }
        else
        {
            u.getInventory().add(this);
            equippedTo = u;
        }
    }

    public float getDefense()
    {
        float d = 0;
        switch(mat)
        {
            case ANCIENT:
                break;
            case ANCIENT_RUSTED:
                d = 1;
                d *= new Random().nextInt(2);
                break;
            case RUSTED:
                break;
        }
        switch(type)
        {
            case BOOTS:
            case GAUNTLETS:
            case HELMET:
            case LEGS:
                d *= 0.25;
                break;
            case TORSO:
                d *= 1;
                break;
        }

        return d;
    }

    @Override
    public Damage getDamage() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDamage'");
    }

    @Override public String getName() { return name; }

    @Override public Item clone() { return new Armor(name, description, pluralDescription, mat, type); }

    @Override public String getPluralDescription() { return pluralDescription; }
    
    @Override public String getDescription() { return description; }   
}
