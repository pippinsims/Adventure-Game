package adventuregame.interactibles;

import adventuregame.Interactible;
import adventuregame.Inventory;

public abstract class InventoryInteractible extends Interactible {
    Inventory inv;

    public Inventory getInventory()
    {
        return inv;
    }
}
