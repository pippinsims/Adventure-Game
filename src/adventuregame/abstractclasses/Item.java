package adventuregame.abstractclasses;

import java.util.ArrayList;
import java.util.List;

public abstract class Item extends Describable
{   
    public final Item clone() { return new ArrayList<Item>(List.of(this)).getFirst(); };
    public boolean isDynamicItem() { return false; }

    public static abstract class Affector extends Item
    {
        protected ArrayList<Class<? extends Describable>> types = new ArrayList<>();
        protected final boolean check(Describable d) { return types.contains(d.getClass()); } 
        public abstract void action(Unit u, Describable d);
    }

    public static abstract class Actor extends Item
    {
        public abstract void action(Unit u, boolean isFinal);
    }
}
