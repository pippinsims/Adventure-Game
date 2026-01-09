package adventuregame;

import java.util.ArrayList;

import adventuregame.Utils.Tuple;
import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.InventoryInteractible;
import adventuregame.items.Armor;
import adventuregame.items.Equippable;
import adventuregame.items.Equippable.Type;
import adventuregame.items.Equippable.Type.Clothes;
import adventuregame.items.Weapon;

public class Inventory {
    protected ArrayList<ItemStack> items = new ArrayList<ItemStack>();
    protected int maxSize;
    private int maxUnequippedArmor;

    public Inventory(int size) 
    { 
        maxSize = size; 
        maxUnequippedArmor = size; 
    }

    public Inventory(int size, int uarmor) 
    { 
        maxSize = size; 
        maxUnequippedArmor = uarmor; 
    }

    public Inventory(Inventory i)
    {
        maxSize = i.size();
        maxUnequippedArmor = i.maxUnequippedArmor;
        items = new ArrayList<>(i.items);
        for(Item i1 : getItems()) i1.setParentInv(this);
    }

    public int getMaxUnequippedArmor() { return maxUnequippedArmor; }

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

    public boolean fullUnequippedArmor()
    {
        int sum = 0;
        for(Item i : getItems()) if(i instanceof Armor && ((Armor)i).isEquipped()) { sum++; if(sum == maxUnequippedArmor) return true;}
        return false;
    }

    public boolean isEmpty() { return items.isEmpty(); }

    public boolean isFull() { return size() == max(); }

    public int max() { return maxSize; }

    public boolean add(Item i)
    {
        for (ItemStack s : items) if(s.is(i))
        {
            s.inc();
            return true;
        }
        
        if(max() > items.size()) 
        {
            i.setParentInv(this);
            items.add(new ItemStack(i, 1));
            return true;
        }

        return false;
    }

    public boolean remove(Item i)
    {
        for(ItemStack s : items) if(s.is(i) && s.dec()) 
        {
            s.i.setParentInv(null);
            return items.remove(s);
        }
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
            if(isEmpty(one.t2)) take = true;
            else if(isEmpty(another.t2)) take = false;
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
                action = "give " + ((another.t1 instanceof Unit) ? ((Unit)another.t1).pronounobj : ""); 
                past = "gave";
                pronoun = ((another.t1 instanceof Unit) ? ((Unit)another.t1).possessiveadj : "Its");
                contraction = ((another.t1 instanceof Unit) ? ((Unit)another.t1).contraction : "It's");
            }

            if(isEmpty(ginv) && isEmpty(rinv)) { Utils.slowPrintln("Neither of you have items!"); return; }

            if(rinv.isFull()) {Utils.slowPrintln(pronoun+" inventory is full! You cannot "+action+" items."); return;}
            
            ArrayList<Item> its = ginv.getItems();
            for(Item i : its) Utils.slowPrintln(i.getDescription());
            if(its.size() == 1) { transaction(its.getFirst()); return; }
            
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
            if(isArmorForUnit && rinv.fullUnequippedArmor())
                Utils.slowPrintln(contraction+" already holding enough unequipped armor! Cannot take another.");
            else
            {
                rinv.add(i);
                if(isArmorForUnit) ((Armor)i).action((Unit)receiver, true);
                ginv.remove(i);

                if(giver instanceof Unit && receiver instanceof Unit)
                    Utils.slowPrintln("You " + past + " " + (take ? Utils.possessiveOf(giver.getName()) : receiver.getName()) + " " + i.getName() + "!");
            }
        }

        private boolean isEmpty(Inventory i)
        {
            if(i instanceof Whole) return i.getItems().isEmpty();
            else return i.isEmpty();
        }
    }

    public static class Whole extends Inventory
    {
        private Inventory wrists, rings, beltInv;
        private Equippable hat, eyes, neck, body, belt, pants, shoes;
        private Armor helm, chest, hands, legs, boots;

        public Whole() 
        { 
            super(10, 1);
            wrists = new Inventory(2, 0);
            rings = new Inventory(6, 0);
            beltInv = new Inventory(10, 0);
        }

        public Whole(int i) 
        { 
            super(i, 1);
            wrists  = new Inventory(2, 0);
            rings   = new Inventory(6, 0);
            beltInv = new Inventory(10, 0);
        }

        public Whole(Inventory i)
        {
            super(i.size(), i.getMaxUnequippedArmor());
            wrists  = new Inventory(2);
            rings   = new Inventory(6);
            beltInv = new Inventory(10);
            items   = new ArrayList<>(i.items);
        }

        public Whole(Whole i)
        {
            super(i.size(), i.getMaxUnequippedArmor());
            wrists  = new Inventory(i.wrists);
            rings   = new Inventory(i.rings);
            beltInv = new Inventory(i.beltInv);
            items   = new ArrayList<>(i.items);
        }

        public ArrayList<Armor> getArmor()
        {
            ArrayList<Armor> armor = new ArrayList<>();
            if(helm  != null) armor.add(helm);
            if(chest != null) armor.add(chest);
            if(hands != null) armor.add(hands);
            if(legs  != null) armor.add(legs);
            if(boots != null) armor.add(boots);
            return armor;
        }

        public ArrayList<Equippable> getNonArmor()
        {
            ArrayList<Equippable> stuff = new ArrayList<>();
            if(hat   != null) stuff.add(hat); 
            if(eyes  != null) stuff.add(eyes); 
            if(neck  != null) stuff.add(neck); 
            if(body  != null) stuff.add(body); 
            if(belt  != null) stuff.add(belt); 
            if(pants != null) stuff.add(pants); 
            if(shoes != null) stuff.add(shoes);
            return stuff;
        }

        public void equip(Equippable e, boolean silent)
        {
            if(!all().contains(e)) throw new IllegalAccessError("Tried to equip something that wasn't in your inventory!");

            for(Equippable e1 : getEquipped()) if(e1.getPart() == e.getPart()) 
            { 
                remove(e); //if inv full, couldn't unequip unless space was freed
                e.setParentInv(this);
                unequip(e1, silent);
                break;
            }

            if(!silent) Utils.slowPrintln(e.getName() + " equipped!");
            e.setDescription(e.getSimpleDesc() + " (Equipped)");
            don(e);
        }

        public void unequip(Equippable e, boolean silent)
        {
            if(!all().contains(e)) throw new IllegalAccessError("Tried to unequip something that wasn't in your inventory!");

            if(!(e instanceof Armor) || !fullUnequippedArmor())
            {
                doff(e);
                if(!silent) Utils.slowPrintln(e.getName() + " unequipped!");
                e.setDescription(e.getSimpleDesc());
            }
            else
                Utils.slowPrintln("You're already holding a piece of unequipped armor! Cannot unequip.");
        }

        public ArrayList<Equippable> getEquipped()
        {
            ArrayList<Equippable> eqs = getNonArmor();
            for(Item i : wrists .getItems()) eqs.add((Equippable)i);
            for(Item i : rings  .getItems()) eqs.add((Equippable)i);
            for(Item i : beltInv.getItems()) eqs.add((Equippable)i);
            eqs.addAll(getArmor());

            return eqs;
        }

        public ArrayList<Equippable> getEquipped(Type t)
        {
            ArrayList<Equippable> out = new ArrayList<>();
            for(Equippable e : getEquipped()) if(e.getPart() == t) out.add(e);

            return out;
        }

        //don removes it from getItems() and puts it in it's own slot, freeing up inv space.
        private void don(Equippable e)
        {
            remove(e);
            switch (e.getPart()) {
                case Clothes.BELT         : belt  = e; break;
                case Clothes.BODY         : body  = e; break;
                case Clothes.EYE          : eyes  = e; break;
                case Clothes.HAT          : hat   = e; break;
                case Clothes.NECK         : neck  = e; break;
                case Clothes.PANTS        : pants = e; break;
                case Clothes.SHOES        : shoes = e; break;
                case Type.Armor.HELMET    : helm  = (Armor)e; break;
                case Type.Armor.CHESTPLATE: chest = (Armor)e; break;
                case Type.Armor.GAUNTLETS : hands = (Armor)e; break;
                case Type.Armor.LEGS      : legs  = (Armor)e; break;
                case Type.Armor.BOOTS     : boots = (Armor)e; break;
                case Clothes.ONBELT       : beltInv  .add(e); break;
                case Clothes.RING         : rings    .add(e); break;
                case Clothes.WRIST        : wrists   .add(e); break;
            }
            e.setParentInv(this);
        }

        //doff puts it back in getItems(), but you can't doff if your inventory is full
        private void doff(Equippable e)
        {
            if(!getEquipped().contains(e)) throw new IllegalAccessError("Cannot unequip something that's not in your Inventory.Whole.getEquipped()");

            if(add(e))
            {
                switch (e.getPart()) {
                    case Clothes.BELT         : belt  = null; break;
                    case Clothes.BODY         : body  = null; break;
                    case Clothes.EYE          : eyes  = null; break;
                    case Clothes.HAT          : hat   = null; break;
                    case Clothes.NECK         : neck  = null; break;
                    case Clothes.PANTS        : pants = null; break;
                    case Clothes.SHOES        : shoes = null; break;
                    case Type.Armor.HELMET    : helm  = null; break;
                    case Type.Armor.CHESTPLATE: chest = null; break;
                    case Type.Armor.GAUNTLETS : hands = null; break;
                    case Type.Armor.LEGS      : legs  = null; break;
                    case Type.Armor.BOOTS     : boots = null; break;
                    case Clothes.ONBELT       : beltInv.remove(e); break;
                    case Clothes.RING         : rings  .remove(e); break;
                    case Clothes.WRIST        : wrists .remove(e); break;
                }
            }
            else
                System.out.println("Cannot unequip. Inventory full."); 
        }

        public ArrayList<Item> all() {
            ArrayList<Item> out = super.getItems();
            out.addAll(getEquipped());
            return out;
        }

        @Override public int size() { return all().size(); }

        @Override public Item at(int i) { return all().get(i); }

        @Override public boolean isFull() { return size() == max() + 15; }

        @Override public boolean isEmpty() { return all().isEmpty(); }
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