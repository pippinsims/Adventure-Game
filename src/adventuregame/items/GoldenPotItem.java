package adventuregame.items;

import adventuregame.Damage;
import adventuregame.Utils;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;
import adventuregame.dynamicitems.GoldenPot;

public class GoldenPotItem extends Item {
    
    private GoldenPot self;
    
    public GoldenPotItem(GoldenPot self) { this.self = self; }

    @Override
    public void action(Unit u) 
    {
        System.out.print("You hold this pot. ");
        if(Utils.promptList("Do you want to place it on the ground?", new String[] {"Yes", "No"}) == 0) self.placeInteractible(u.getRoom());
    }

    @Override
    public Damage getDamage() 
    {
        return new Damage(1, Damage.Type.BLUNT, "You attempt to attack with your golden pot.");
    }

    @Override public String getPluralDescription() { return self.getPluralDescription(); }

    @Override public String getDescription() { return self.getDescription(); }
    
    @Override public String getName() { return self.getName(); }

    @Override public Item clone() { return new GoldenPot().item(); }
}
