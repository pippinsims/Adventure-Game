package adventuregame.abstractclasses;

import java.util.ArrayList;
import java.util.List;

public abstract class Item extends Describable
{
    public abstract void action(Unit u, boolean isFinal);
    public final Item clone() { return new ArrayList<Item>(List.of(this)).getFirst(); };
    public boolean isDynamicItem() { return false; }
}
