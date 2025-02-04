public class Inventory {
    private Item[] items;
    private int[] itemCounts;

    public Inventory(int size)
    {
        items = new Item[size];
        itemCounts = new int[size];
    }

    public int getItemCount(int i)
    {
        return itemCounts[i];
    }

    public Item getItem(int i)
    {
        return items[i];
    }

    public void removeOneOfItem(int i)
    {
        if(--itemCounts[i] == 0)
        {
            items[i] = null;
        }
    }
}
