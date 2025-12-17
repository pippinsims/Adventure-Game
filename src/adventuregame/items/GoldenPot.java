package adventuregame.items;

import adventuregame.Damage;
import adventuregame.abstractclasses.Item;

public class GoldenPot extends Item{

    public static final String[] descriptions = new String[]{"smooth, curvaceous golden pot. It has a spherical base which curves into a neck that widens at the mouth", "golden pot, no longer smooth, it has a large dent", "vaguely pot-shaped vessel, made of gold", "crumpled piece of gold"};
    public static final String defaultName = "Golden Pot";
    
    private String description = descriptions[0];
    private String name = defaultName;
    private int dmg;
    
    public GoldenPot(int dmg)
    {
        this.dmg = dmg;
        description = descriptions[this.dmg];
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void action() 
    {
        System.out.print("You hold this pot.");
    }

    @Override
    public Damage getDamage() 
    {
        return new Damage(1, Damage.Type.BLUNT, "You attempt to attack with your golden pot.");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPluralDescription() {
        return "golden pots";
    }
}
