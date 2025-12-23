package adventuregame.dynamicitems;

import adventuregame.DynamicItem;
import adventuregame.Room;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.GoldenPotInteractible;
import adventuregame.items.GoldenPotItem;

public class GoldenPot extends DynamicItem {
    
    public static final String[] descriptions = new String[]{"smooth, curvaceous golden pot. It has a spherical base which curves into a neck that widens at the mouth", "golden pot, no longer smooth, it has a large dent", "vaguely pot-shaped vessel, made of gold", "crumpled piece of gold"};
    public static final String[] pluralDescs  = new String[]{"golden pots","dented gold pots","heavily damaged gold pots","hunks of deformed goldwork"};
    public String name = "Golden Pot";
    public int dmg = 0;

    public GoldenPot(int dmg)
    {
        this.dmg = dmg;
        it = new GoldenPotItem(this);
        in = new GoldenPotInteractible(this, null);
    }

    public GoldenPot(Unit unit)
    {
        it = new GoldenPotItem(this);
        in = new GoldenPotInteractible(this, null);
        collectItem(unit);
    }

    public GoldenPot(Room room)
    {
        it = new GoldenPotItem(this);
        in = new GoldenPotInteractible(this, room);
        placeInteractible(room);
    }

    @Override
    public String getPluralDescription() {
        return pluralDescs[dmg];
    }

    @Override
    public String getDescription() {
        return descriptions[dmg];
    }

    @Override
    public String getName() {
        return name;
    }
}
