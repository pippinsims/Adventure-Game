package adventuregame.interactibles;

import java.util.ArrayList;
import java.util.Map;

import adventuregame.Inventory;
import adventuregame.Player.Inspect;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;
import adventuregame.items.Armor;

public class SkeletonInteractible extends InventoryInteractible
{
    protected String simpleDesc;

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
        simpleDesc = description;
        setInspects();
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
        simpleDesc = description;
        setInspects();
    }

    public SkeletonInteractible(Room r, String n, String d, String p, String pd, String pp, String a, String ap, String l, Inventory i, Map<String, Inspect> im) {
        super(r, n, d, p, pd, pp, a, ap, l);
        simpleDesc = description;
        inv = i;
        insMap = im;
        setInspects();
    }

    public SkeletonInteractible(Room r, String n, String d, String p, String pd, String pp, String a, String ap, String l) {
        super(r, n, d, p, pd, pp, a, ap, l);
        simpleDesc = description;
        inv = new Inventory(6);
        setInspects();
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
                    inv.remove(i);
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
                        inv.remove(i);
                    }
                }
                else
                {
                    Utils.slowPrint("Your inventory is full! You only took some of the items.");
                    break;
                }
            }
            setInspects();
            if(inv.isEmpty()) actionVerb = "";
        }
    }

    @Override protected void setInspects() 
    {
        put(simpleDesc);
        for(Armor a : inv.getArmor())
        {
            Armor.MaterialType mat = a.getMat();
            switch(mat)
            {
                case ANCIENT_RUSTED:
                    put("It's got " + Armor.armorDescs.get(Armor.MaterialType.RUSTED));
                    put("Valeent", new String[] {"It's got " + Armor.armorDescs.get(mat), "You notice the glyph on the armor is that of a long deceased House. You feel you remember it from one of your schoolbooks."});
                    break;
                default: put("It's got " + Armor.armorDescs.get(mat)); break;
            }
            description = simpleDesc + " with armor";
            return;
        }
        for(String name : insMap.keySet()) put(name, simpleDesc);
        description = simpleDesc;
    }
}
