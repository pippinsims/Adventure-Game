package adventuregame.items;

import java.util.Random;

import adventuregame.Damage;
import adventuregame.interfaces.Item;

public class GoldenPot implements Item{

    String description = "a smooth, curvaceous golden pot. It has a spherical base which curves into a neck that widens at the mouth.";

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void action() 
    {
        
        System.out.print("You hold this pot.");

        // System.out.print("You kick the pot and it ");

        // switch (new Random().nextInt(3)) 
        // {
        //     default:
        //         System.out.println("goes clattering against the wall.");
        //         break;

        //     case 1:
        //         System.out.println("rolls across the floor.");
        //         break;

        //     case 2:
        //         System.out.println("tumbles, but magically teleports to it's original position, vibrating back into place.");
        // }
    }

    @Override
    public Damage getDamage() {
        // TODO make damagingitem like wallentity, so we don't have to have this method in every item
        throw new UnsupportedOperationException("Unimplemented method 'getDamage'");
    }

    @Override
    public boolean isWeapon() {
        return false;
    }

    @Override
    public String getName() {
        return "Golden Pot";
    }

}
