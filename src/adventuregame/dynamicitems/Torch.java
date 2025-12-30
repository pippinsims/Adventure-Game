package adventuregame.dynamicitems;

import adventuregame.Damage;
import adventuregame.Effect;
import adventuregame.Player;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.DynamicItem;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.WallInteractible;
import adventuregame.items.Weapon;

public class Torch extends DynamicItem {

    {
        name = "Torch";
        description = "burning torch, providing light and warmth";
        pluralDescription = "torches";
    }

    public Torch()
    {
        init();
    }

    public Torch(Unit unit)
    {
        init();
        collectItem(unit);
    }

    public Torch(Room room, WallInteractible.Wall wall)
    {
        init();
        in.setRoom(room);
        ((WallInteractible)in).setWall(wall);
        ((WallInteractible)in).setLocationReference();
        placeInteractible(room);
    }

    @Override protected void init()
    {
        Torch parent = this;
        in = new WallInteractible() {
            Torch self = parent;
            
            {
                setDefaults(
                    self.name, 
                    self.getDescription(), 
                    "on", 
                    self.getPluralDescription(), 
                    "", 
                    "Take", 
                    "from" //TODO Laur descs {"flaming stick", "blazing rod", "burny ol\' chunk o\' lumber"}
                );
            }

            @Override
            public void action(Unit u)
            {
                self.collectItem(u);
                Utils.slowPrint("You have received a Torch!");
            }

            @Override
            public void inspect()
            {
                Utils.slowPrintln("You take a closer look at this torch and notice nothing new. It's hot, I guess");
            }
        };

        it = new Weapon() {
            Torch self = parent;
            {
                pluralDescription = self.pluralDescription;
                description = self.description;
                name = self.name;
                atkmsg = "You swing the torch at your enemy!";
            }

            @Override public void action(Unit u, boolean isFinal) { 
                System.out.println("The torch is fiery... You stare deeply into the flames.");
                if(!isFinal && u instanceof Player) ((Player)u).promptForAction();
             }

            @Override public Damage getDamage() 
            {
                return new Damage(1, Damage.Type.FIRE, Damage.Mode.INFLICTEFFECT, new Effect(Effect.Type.FIRE, 3, 1));
            }
            
            @Override public boolean isDynamicItem() { return true; }
        };
    }
}
