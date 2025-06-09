import java.util.ArrayList;

public class Inventory {
    private ArrayList<Item> items = new ArrayList<Item>();
    private int[] itemCounts;

    public Inventory(int size)
    {
        itemCounts = new int[size];
    }

    public int getItemCount(int i)
    {
        return itemCounts[i];
    }

    public Item getItem(int i)
    {
        return items.get(i);
    }

    public boolean addItem(Item i)
    {
        if(items.contains(i))
            itemCounts[items.indexOf(i)]++;
        else if(items.size() < itemCounts.length)
            items.add(i);
        else
            return false;
        
        return true;
    }

    public void removeOneOfItem(int i)
    {
        if(--itemCounts[i] == 0)
        {
            items.remove(i);
        }
    }
}
