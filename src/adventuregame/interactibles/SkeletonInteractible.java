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
    public SkeletonInteractible(Room r, Inventory i)
    {
        super(r, "Skeleton", 
              "skeleton bone pile",
              "lying",
              "",
              "",
              "loot",
              "on",
              "",
              "",
              "the floor");
        inv = i;
    }

    public SkeletonInteractible(Room r, String n, String d, String p, String pd, String pp, String a, String ap, String rd, String rpd, String l) {
        super(r, n, d, p, pd, pp, a, ap, rd, rpd, l);
        inv = new Inventory(6);
    }

    @Override 
    public void action(Unit u)
    {
        if(actionVerb.equals("loot"))
        {
            Inventory uinv = u.getInventory();
            if(uinv.isFull())
            {
                Utils.slowPrintln("You check the skeleton for items...");
                String[] prompts = new String[] {"Take all", "Take one"};
                if(prompts[Utils.promptList("You can:", prompts)].equals("Take one"))
                {
                    ArrayList<Item> its = inv.getItems();
                    Item i = its.get(Utils.promptList("Which item?", Utils.descriptionsOf(its)));
                    uinv.add(i);
                    its.remove(i);   
                }
                else
                {
                    for(Item i : new ArrayList<>(inv.getItems())) if(!uinv.isFull())
                    {
                        uinv.add(i);
                        inv.remove(i);
                    }
                    
                    if(!inv.isEmpty()) Utils.slowPrint("Your inventory is full! You only took some of the items.");
                }
                if(inv.isEmpty()) actionVerb = "";
            }
            else Utils.slowPrintln("Your inventory is full! You cannot loot this.");
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
        description = "old dilapidated skeleton.";
        Utils.slowPrintln(getDescription());
    }
}
