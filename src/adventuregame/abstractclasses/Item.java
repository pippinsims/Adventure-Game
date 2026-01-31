package adventuregame.abstractclasses;

import java.util.ArrayList;
import java.util.List;

import adventuregame.Inventory;
import adventuregame.Utils;

public abstract class Item extends Describable
{   
    protected Inventory parentInv;
    public void setParentInv(Inventory i) { parentInv = i; } 

    public static <T extends Item> T clone(T toClone) { return new ArrayList<T>(List.of(toClone)).getFirst(); };
    public boolean isDynamicItem() { return false; }

    public static abstract class Affector extends Item
    {
        protected ArrayList<Class<? extends Describable>> types = new ArrayList<>();
        protected final boolean check(Describable d) { return types.contains(d.getClass()); } 
        public void action(Unit u, Describable d) { Utils.slowPrintln("This item can't be used for that!"); }
    }

    public static abstract class Actor extends Item
    {
        public abstract void action(Unit u, boolean isFinal);
    }
}
