package adventuregame.dynamicitems;

import adventuregame.Damage;
import adventuregame.Effect;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.DynamicItem;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.WallEntity;

public class Torch extends DynamicItem {

    public String name = "Torch";

    public Torch()
    {
        init();
    }

    public Torch(Unit unit)
    {
        init();
        collectItem(unit);
    }

    public Torch(Room room, WallEntity.Wall wall)
    {
        init();
        in.setRoom(room);
        ((WallEntity)in).setWall(wall);
        ((WallEntity)in).setLocationReference();
        placeInteractible(room);
    }

    @Override public String getPluralDescription() { return "torches"; }

    @Override public String getDescription() { return "burning torch, providing light and warmth"; }

    @Override public String getName() { return name; }

    @Override protected void init()
    {
        Torch parent = this;
        in = new WallEntity() {
            Torch self = parent;
            
            {
                setDefaults(
                    self.name, 
                    self.getDescription(), 
                    "on", 
                    self.getPluralDescription(), 
                    "", 
                    "Take", 
                    "from",
                    new String[]{"flaming stick", "blazing rod", "burny ol\' chunk o\' lumber"}, 
                    new String[0]
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

        it = new Item() {
            Torch self = parent;

            @Override public void action(Unit u) { System.out.println("The torch is fiery... You stare deeply into the flames."); }

            @Override public Damage getDamage() throws Exception 
            {
                return new Damage(1, Damage.Type.FIRE, Damage.Mode.INFLICTEFFECT, new Effect(Effect.Type.FIRE, 3, 1), "You swing the torch at your enemy!");
            }

            @Override public boolean isWeapon() { return true; }

            @Override public String getPluralDescription() { return self.getPluralDescription(); }

            @Override public String getDescription() { return self.getDescription(); }

            @Override public String getName() { return self.getName(); }

            @Override public Item clone() { return new Torch().item(); }
        };
    }
}
