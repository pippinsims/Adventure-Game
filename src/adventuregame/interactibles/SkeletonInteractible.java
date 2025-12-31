package adventuregame.interactibles;

import java.util.ArrayList;

import adventuregame.Inventory;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;
import adventuregame.items.Armor;

public class SkeletonInteractible extends InventoryInteractible
{
    private String simpleDesc = "old dilapidated skeleton";

    public SkeletonInteractible(Room r)
    {
        super(r, "Skeleton", 
              "skeleton bone pile",
              "on",
              "",
              "",
              "loot",
              "",
              "the floor");
        inv = new Inventory(6);
    }

    public SkeletonInteractible(Room r, Inventory i)
    {
        super(r, "Skeleton", 
              "skeleton bone pile",
              "on",
              "",
              "",
              "loot",
              "",
              "the floor");
        inv = i;
    }

    public SkeletonInteractible(Room r, String n, String d, String p, String pd, String pp, String a, String ap, String l, Inventory i) {
        super(r, n, d, p, pd, pp, a, ap, l);
        inv = i;
    }

    public SkeletonInteractible(Room r, String n, String d, String p, String pd, String pp, String a, String ap, String l) {
        super(r, n, d, p, pd, pp, a, ap, l);
        inv = new Inventory(6);
    }

    @Override 
    public void action(Unit u)
    {
        Inventory uinv = u.getInventory();
        if(uinv.isFull())
            Utils.slowPrintln("Your inventory is full! You cannot loot this.");
        else
        {
            ArrayList<Item> its = inv.getItems();
            Utils.slowPrintln("You check the skeleton for items...");
            for(Item i : its) Utils.slowPrintln(i.getDescription());
            String[] prompts = new String[] {"Take all", "Take one"};
            if(prompts[Utils.promptList("You can:", prompts)].equals("Take one"))
            {
                Item i = its.get(Utils.promptList("Which item?", Utils.descriptionsOf(its)));
                if(i instanceof Armor && u.getInventory().hasUnequippedArmor())
                    Utils.slowPrintln("You're already holding a piece of unequipped armor! Cannot take another."); //TODO add Trade action
                else
                {
                    uinv.add(i);
                    if(i instanceof Armor) i.action(u, true);
                    remove(i);
                }
            }
            else
            {
                for(Item i : new ArrayList<>(its)) if(!uinv.isFull())
                {
                    if(i instanceof Armor && u.getInventory().hasUnequippedArmor())
                        Utils.slowPrintln("You're already holding a piece of unequipped armor! Cannot take another."); //TODO add Trade action
                    else
                    {
                        uinv.add(i);
                        if(i instanceof Armor) i.action(u, true);
                        remove(i);
                    }
                }
                else
                {
                    Utils.slowPrint("Your inventory is full! You only took some of the items.");
                    break;
                }
            }
            if(!inv.getArmor().isEmpty()) return;
            description = simpleDesc;
            if(inv.isEmpty()) actionVerb = "";
        }
    }

    @Override protected void setInspects() {}

    @Override
    public void add(Item i)
    {
        if(i instanceof Armor) 
        {
            Armor.MaterialType mat = ((Armor)i).getMat();
            switch(mat)
            {
                case ANCIENT_RUSTED:
                    put("", "It's got " + Armor.armorDescs.get(Armor.MaterialType.RUSTED));
                    put("Valeent", new String[] {"It's got " + Armor.armorDescs.get(mat), "You notice the glyph on the armor is that of a long deceased House. You feel you remember it from one of your schoolbooks."});
                    break;
                default: put("", "It's got " + Armor.armorDescs.get(mat)); break;
            }
        }
        inv.add(i);
    }

    @Override
    public void remove(Item i)
    {
        inv.remove(i);
        if(inv.getArmor().isEmpty()) for(String name : insMap.keySet()) put(name, simpleDesc);
    }
}
