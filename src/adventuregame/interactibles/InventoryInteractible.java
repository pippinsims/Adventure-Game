package adventuregame.interactibles;

import adventuregame.Interactible;
import adventuregame.Inventory;
import adventuregame.abstractclasses.Item;

public abstract class InventoryInteractible extends Interactible {
    protected Inventory inv;

    public InventoryInteractible() {};

    public InventoryInteractible(String n, String d, String p, String pd, String pp, String a, String ap, String rd, String rpd, String l) {
        super(n, d, p, pd, pp, a, ap, rd, rpd, l);
    }

    public Inventory getInventory()
    {
        return inv;
    }

    public void add(Item i)
    {
        inv.add(i);
    }
}
