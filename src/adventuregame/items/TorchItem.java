package adventuregame.items;

import adventuregame.Damage;
import adventuregame.Effect;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;
import adventuregame.dynamicitems.Torch;

public class TorchItem extends Item{

    Torch self;
    
    public TorchItem(Torch self) { this.self = self; }

    @Override
    public void action(Unit u) {
        System.out.println("The torch is fiery... You stare deeply into the flames.");
    }

    @Override
    public Damage getDamage() throws Exception 
    {
        return new Damage(1, Damage.Type.FIRE, Damage.Mode.INFLICTEFFECT, new Effect(Effect.Type.FIRE, 3, 1), "You swing the torch at your enemy!");
    }

    @Override public boolean isWeapon() { return true; }

    @Override public String getPluralDescription() { return self.getPluralDescription(); }

    @Override public String getDescription() { return self.getDescription(); }

    @Override public String getName() { return self.getName(); }

    @Override public Item clone() { return new Torch().item(); }
}
