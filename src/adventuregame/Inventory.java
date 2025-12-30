package adventuregame;

import java.util.ArrayList;

import adventuregame.abstractclasses.Item;
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
        for(ItemStack s : items)
        {
            if(s.is(i) && s.dec())
            {
                items.remove(s);
                return true;
            }
        }
        return false;
    }

    public void decreaseAt(int i)
    {
        if(items.get(i).dec()) items.remove(i);
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
