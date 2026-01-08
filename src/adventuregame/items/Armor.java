package adventuregame.items;

import java.util.HashMap;
import java.util.Map;

import adventuregame.Damage;
import adventuregame.Player;
import adventuregame.Utils;
import adventuregame.abstractclasses.Unit;

public class Armor extends Equippable {

    public enum MaterialType
    {
        RUSTED,
        ANCIENT_RUSTED,
        ANCIENT
    }

    public final static Map<MaterialType,String> armorDescs = Map.ofEntries(Map.entry(MaterialType.RUSTED, "old armor, which has seen far better days, but now it's rusted and tarnished."),
                                                                            Map.entry(MaterialType.ANCIENT_RUSTED, "old rusted armor, you notice an ancient glyph."),
                                                                            Map.entry(MaterialType.ANCIENT, "lodestone-enhanced ancient soldier's armor."));

    protected MaterialType mat;

    public Armor(String name, String description, String pluralDescription, MaterialType mat, Type.Armor type)
    {
        super(name, description, pluralDescription, null);
        this.name = name;
        this.description = description;
        this.pluralDescription = pluralDescription;
        this.mat = mat;
        this.type = type;
        simpleDesc = description;
    }

    public MaterialType getMat() { return mat; }

    @Override
    public void action(Unit u, boolean isFinal) {

        if(isEquipped()) u.getInventory().unequip(this);
        else u.getInventory().equip(this);

        if(!isFinal && u instanceof Player) ((Player)u).ableToAct = true;
    }

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
                d = Utils.rand.nextInt(2);
                for(int i = 2; i < Damage.Type.values().length - 1; i++) outd.put(Damage.Type.values()[i], d);
                //50% chance of all first 2 tiers of spells on this unit fail
                break;
            case ANCIENT_RUSTED:
                d = 1;
                d *= Utils.rand.nextInt(2);
                outd.put(Damage.Type.BASIC, d);
                outd.put(Damage.Type.BLUNT, d);
                break;
            case RUSTED:
                break;
        }
        switch((Type.Armor)type)
        {
            case BOOTS: case GAUNTLETS: case HELMET: case LEGS:
                for(Map.Entry<Damage.Type,Float> e : outd.entrySet()) outd.put(e.getKey(), e.getValue()*0.25f);
                break;
            case CHESTPLATE: break;
            default: break;
        }

        return outd;
    }

    @Override public boolean isRequired() { return false; }
}
