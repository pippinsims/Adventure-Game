package adventuregame.interactibles;

import java.util.ArrayList;

import adventuregame.Environment;
import adventuregame.Inventory;
import adventuregame.Utils;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;
import adventuregame.items.Armor;

public class SkeletonInteractible extends InventoryInteractible
{
    public SkeletonInteractible(String n, String d, String p, String pd, String pp, String a, String ap, String rd, String rpd, String l) {
        super(n, d, p, pd, pp, a, ap, rd, rpd, l);
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
            Armor.MaterialType mat = ((Armor)i).getMat();
            Utils.slowPrintln(Armor.armorDescs.get(mat == Armor.MaterialType.ANCIENT_RUSTED && !Environment.curPlayer.getName().equals("Valeent") ? Armor.MaterialType.RUSTED : mat));
            return;
        }
        description = "Old dilapidated skeleton.";
        Utils.slowPrintln(getDescription());
    }
}
