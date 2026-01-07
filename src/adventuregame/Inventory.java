package adventuregame;

import java.util.ArrayList;

import adventuregame.Utils.Tuple;
import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.InventoryInteractible;
import adventuregame.items.Armor;
import adventuregame.items.Weapon;

public class Inventory {
    private ArrayList<ItemStack> items = new ArrayList<ItemStack>();
    private int maxSize;

    public Inventory(int size) { maxSize = size; }

    public Inventory(Inventory i)
    {
        maxSize = size();
        items = new ArrayList<>(i.items);
    }

    public int size() { return items.size(); }

    public Item at(int i) { return items.get(i).item(); }

    public int countOf(Item i)
    {
        for(ItemStack s : items) if(s.is(i)) return s.count();
        return 0;
    }

    public int indexOf(Item i)
    {
        for(int j = 0; j < items.size(); j++) if(items.get(j).is(i)) return j;
        return -1;
    }

    public int countAt(int i) { return items.get(i).count(); }

    public ArrayList<Item> getItems()
    {
        ArrayList<Item> items = new ArrayList<>();
        for(ItemStack s : this.items) items.add(s.item());
        return items;
    }

    public ArrayList<Weapon> getWeapons()
    {
        ArrayList<Weapon> weps = new ArrayList<>();
        for(ItemStack s : this.items) if(s.item() instanceof Weapon) weps.add((Weapon)s.item());
        return weps;
    }

    public ArrayList<Armor> getArmor()
    {
        ArrayList<Armor> arms = new ArrayList<>();
        for(ItemStack s : this.items) if(s.item() instanceof Armor) arms.add((Armor)s.item());
        return arms;
    }

    public boolean hasUnequippedArmor()
    {
        for(Armor a : getArmor()) if(!a.isEquipped()) return true;
        return false;
    }

    public boolean isEmpty() { return items.isEmpty(); }

    public boolean isFull() { return size() == max(); }

    public int max() { return maxSize; }

    public boolean add(Item i)
    {
        for (ItemStack s : items) 
        {
            if(s.is(i))
            {
                s.inc();
                return true;
            }    
        }
        
        boolean canFit = max() - items.size() > 0;
        if(canFit) items.add(new ItemStack(i, 1));

        return canFit;
    }

    public boolean remove(Item i)
    {
        for(ItemStack s : items) if(s.is(i) && s.dec()) return items.remove(s);
        return false;
    }

    public void decreaseAt(int i)
    {
        if(items.get(i).dec()) items.remove(i);
    }

    public static class Trade
    {
        public static class Builder
        {
            private Tuple<Describable,Inventory> one, another; 
            public Builder() {};
            public Builder one(Unit one)                      { this.one     = new Tuple<>(one, one.getInventory()); return this; }
            public Builder one(InventoryInteractible one)     { this.one     = new Tuple<>(one, one.getInventory()); return this; }
            public Builder another(Unit ano)                  { this.another = new Tuple<>(ano, ano.getInventory()); return this; }
            public Builder another(InventoryInteractible ano) { this.another = new Tuple<>(ano, ano.getInventory()); return this; }
            public Trade build() { return new Trade(one, another); }
        }

        final Describable receiver, giver;
        final Inventory rinv, ginv;
        final String verb, action, pronoun, past, contraction;
        final boolean take;

        /** {@code Type.GIVE} = one -> another
            <p>{@code Type.TAKE} = one <- another
        */
        private Trade(Tuple<Describable, Inventory> one, Tuple<Describable, Inventory> another)
        {
            if(one.t2.isEmpty()) take = true;
            else if(another.t2.isEmpty()) take = false;
            else take = Utils.promptList("What do you do?", new String[] {"Take","Give"}) == 0;

            if(take)
            {
                giver = another.t1;
                ginv = another.t2;
                receiver = one.t1;
                rinv = one.t2;
                verb = "Take";
                action = "take";
                past = "took";
                pronoun = "Your";
                contraction = "You're";
            }
            else
            {
                giver = one.t1;
                ginv = one.t2;
                receiver = another.t1;
                rinv = another.t2;
                verb = "Give";
                action = "give them";
                past = "gave";
                pronoun = "Their";
                contraction = "They're";
            }

            if(ginv.isEmpty() && rinv.isEmpty()) { Utils.slowPrintln("Neither you or them had items!"); return; }

            if(rinv.isFull()) {Utils.slowPrintln(pronoun+" inventory is full! You cannot "+action+" items."); return;}
            ArrayList<Item> its = ginv.getItems();
            for(Item i : its) Utils.slowPrintln(i.getDescription());
            if(its.size() == 1) transaction(its.getFirst());
            
            if(Utils.promptList("You can:", new String[] {verb+" all", verb+" one"}) == 1) 
            {
                transaction(its.get(Utils.promptList("Which item?", Utils.descriptionsOf(its))));
                return;
            }
            for(Item i : new ArrayList<>(its)) if(!rinv.isFull())
                transaction(i);
            else
            {
                Utils.slowPrint(pronoun+" inventory is full! You only "+past+" some of the items.");
                break;
            }
        }

        private void transaction(Item i)
        {
            boolean isArmorForUnit = receiver instanceof Unit && i instanceof Armor; 
            if(isArmorForUnit && rinv.hasUnequippedArmor())
                Utils.slowPrintln(contraction+" already holding a piece of unequipped armor! Cannot take another.");
            else
            {
                rinv.add(i);
                if(isArmorForUnit) ((Armor)i).action((Unit)receiver, true);
                ginv.remove(i);

                if(giver instanceof Unit && receiver instanceof Unit)
                    Utils.slowPrintln("You " + past + " " + (take ? Utils.possessiveOf(giver.getName()) : receiver.getName()) + " " + i.getName() + "!");
            }
        }
    }
}

class ItemStack
{
    Item i; int c;
    ItemStack(Item item, int count) { i = item; c = count; }
    void    inc()                   { c++;                 }
    boolean dec()                   { return --c == 0;     }
    Item    item()                  { return i;            }
    int     count()                 { return c;            }
    boolean is(Item i)              { return this.i == i;  }
}