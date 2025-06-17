package adventuregame;

import java.util.ArrayList;

import adventuregame.abstractclasses.Item;

public class Inventory {
    private ArrayList<ItemStack> items = new ArrayList<ItemStack>();
    private int maxSize;

    public Inventory(int size)
    {
        maxSize = size;
    }

    public int getSize()
    {
        return items.size();
    }

    public int getItemCount(int i)
    {
        return items.get(i).count();
    }

    public Item getItem(int i)
    {
        return items.get(i).getType();
    }

    public boolean addItem(Item i)
    {
        boolean found = false;

        for (ItemStack itemStack : items) 
        {
            if(itemStack.getType() == i)
            {
                itemStack.inc();
                found = true;
                break;
            }    
        }

        if(!found && items.size() < getCapacity())
            items.add(new ItemStack(i, 1));

        return items.size() <= getCapacity();
    }

    public int getCapacity() 
    {
        return maxSize;
    }

    public void removeOneOfItem(int i)
    {
        if(items.get(i).dec())
            items.remove(i);
    }
}

class ItemStack
{
    Item type;
    int count;

    ItemStack(Item t, int num)
    {
        type = t;
        count = num;
    }

    void inc()
    {
        count++;
    }

    boolean dec()
    {
        if(count > 0)
            count--;
        
        return count == 0;
    }

    Item getType()
    {
        return type;
    }

    int count()
    {
        return count;
    }
}
