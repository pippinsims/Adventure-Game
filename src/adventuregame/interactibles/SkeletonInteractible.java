package adventuregame.interactibles;

import java.util.ArrayList;

import adventuregame.Environment;
import adventuregame.Inventory;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;
import adventuregame.items.Armor;

public class SkeletonInteractible extends InventoryInteractible
{
    private String simpleDesc = "old dilapidated skeleton";

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
            String[] prompts = new String[] {"Take all", "Take one"};
            if(prompts[Utils.promptList("You can:", prompts)].equals("Take one"))
            {
                Item i = its.get(Utils.promptList("Which item?", Utils.descriptionsOf(its)));
                uinv.add(i);
                inv.remove(i);
            }
            else
            {
                for(Item i : new ArrayList<>(its)) if(!uinv.isFull())
                {
                    uinv.add(i);
                    inv.remove(i);
                }
                else
                {
                    Utils.slowPrint("Your inventory is full! You only took some of the items.");
                    break;
                }
            }
            boolean armorFound = false;
            for(Item i : its) if(i instanceof Armor) { armorFound = true; break; }
            if(!armorFound) description = simpleDesc;
            if(inv.isEmpty()) actionVerb = "";
        }
    }

    @Override
    public void inspect()
    {
        for(Item i : inv.getItems()) if(i instanceof Armor) 
        {
            Utils.slowPrint("It's got ");
            Armor.MaterialType mat = ((Armor)i).getMat();
            Utils.slowPrintln(Armor.armorDescs.get(mat == Armor.MaterialType.ANCIENT_RUSTED && !Environment.curPlayer.getName().equals("Valeent") ? Armor.MaterialType.RUSTED : mat));
            return;
        }
        description = simpleDesc;
        Utils.slowPrintln(getDescription());
    }
}
