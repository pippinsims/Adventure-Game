package adventuregame.items;

import adventuregame.Damage;
import adventuregame.abstractclasses.Item;

public class TorchItem extends Item{

    String description = "A burning torch, providing light and warmth!";
    String name = "Torch";

    @Override
    public void action() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'action'");
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getPluralDescription()
    {
        return "torches";
    }

    @Override
    public Damage getDamage() 
    {
        return new Damage(1, Damage.Type.FIRE, "You swing the torch at your enemy!");
    }

    @Override
    public boolean isWeapon() 
    {
        return true;
    }

    @Override
    public String getName() {
        return name;
    }
}
