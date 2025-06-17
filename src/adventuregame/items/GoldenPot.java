package adventuregame.items;

import adventuregame.Damage;
import adventuregame.abstractclasses.Item;

public class GoldenPot extends Item{

    public static final String defaultDescription = "a smooth, curvaceous golden pot. It has a spherical base which curves into a neck that widens at the mouth.";
    public static final String defaultName = "Golden Pot";
    
    private String description = defaultDescription;
    private String name = defaultName;
    
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
