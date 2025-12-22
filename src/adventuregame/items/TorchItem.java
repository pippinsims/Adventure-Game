package adventuregame.items;

import adventuregame.Damage;
import adventuregame.Effect;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;

public class TorchItem extends Item{

    String description = "A burning torch, providing light and warmth!";
    String name = "Torch";

    @Override
    public void action(Unit u) {
        System.out.println("The torch is fiery... You stare deeply into the flames.");
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
    public Damage getDamage() throws Exception 
    {
        return new Damage(1, Damage.Type.FIRE, Damage.Mode.INFLICTEFFECT, new Effect(Effect.Type.FIRE, 3, 1), "You swing the torch at your enemy!");
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
