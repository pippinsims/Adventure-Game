package adventuregame.dynamicitems;

import java.util.Random;

import adventuregame.Damage;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.DynamicItem;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.InventoryInteractible;

public class GoldenPot extends DynamicItem {
    
    public static final String[] descriptions = new String[]{"smooth, curvaceous golden pot. It has a spherical base which curves into a neck that widens at the mouth", "golden pot, no longer smooth, it has a large dent", "vaguely pot-shaped vessel, made of gold", "crumpled piece of gold"};
    public static final String[] pluralDescs  = new String[]{"golden pots","dented gold pots","heavily damaged gold pots","hunks of deformed goldwork"};
    {name = "Golden Pot";}
    public int dmg = 0;

    public GoldenPot()
    {
        init();
    }

    public GoldenPot(int dmg)
    {
        this.dmg = dmg;
        init();
    }

    public GoldenPot(Unit unit)
    {
        init();
        collectItem(unit);
    }

    public GoldenPot(Room room)
    {
        init();
        in.setRoom(room);
        placeInteractible(room);
    }

    @Override protected void init()
    {
        GoldenPot parent = this;
        in = new InventoryInteractible() 
        {
            private GoldenPot self = parent;
            
            {
                String prep;
                if(new Random().nextInt(2) == 1)
                {
                    prep = "on";
                    locReference = "the floor";
                }
                else
                {
                    prep = "in";
                    locReference = "the corner";
                }

                setDefaults(
                    self.name,
                    self.description,
                    prep,
                    self.pluralDescription,
                    "",
                    "Interact with",
                    ""
                );
            }

            @Override
            public void action(Unit u) 
            {
                switch(Utils.promptList("How do you interact?", new String[]{"Kick","Take"}))
                {
                    case 0:
                        Utils.slowPrint("You kick the pot and it ");

                        switch (new Random().nextInt(3)) 
                        {
                            default:
                                Utils.slowPrintln("goes clattering against the wall.");
                                if(self.dmg < 3)
                                    self.dmg++;
                                break;

                            case 1:
                                Utils.slowPrintln("rolls across the floor.");
                                break;

                            case 2:
                                Utils.slowPrintln("tumbles, but magically teleports to it's original position, vibrating back into place.");
                                
                                if(self.dmg > 0)
                                {
                                    self.dmg--;
                                    Utils.slowPrint("It seems oddly smoother than before.");
                                    if(self.dmg != 0) Utils.slowPrintln(" It falls over because it is badly damaged.");
                                }
                                
                                break;
                        }

                        pluralDescription = pluralDescs[dmg];
                        description = descriptions[dmg];
                        break;

                    case 1:
                        Utils.slowPrint("You have received a Golden Pot!");
                        self.collectItem(u);
                                
                        break;
                }
            }

            @Override
            public void inspect() 
            {
                Utils.slowPrintln("You take a closer look at this golden pot and notice nothing new.");
            }
        };

        it = new Item() {
            private GoldenPot self = parent;

            {
                pluralDescription = self.pluralDescription;
                description = self.description;
                name = self.name;
            }

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

            @Override public boolean isDynamicItem() { return true; }

            @Override public Item clone() { return new GoldenPot().item(); }
        };
    }
}
