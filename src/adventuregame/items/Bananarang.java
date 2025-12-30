package adventuregame.items;
import adventuregame.Damage;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;

public class Bananarang extends Item 
{
    { 
        description = "A boomerang made of a potassium rich, biotic material.";
        name = "Bananarang";
        pluralDescription = "bananarangs";
    }
    @Override
    public void action(Unit u) 
    {
        System.out.println("You go to take a bite... but then... you probably shouldn't");
    }


    @Override
    public Damage getDamage() 
    {
       return new Damage(5, Damage.Type.BASIC, "You wail the bananarang at your enemy with KILLER INSTINCT!\nIt kills him.");
    }

    @Override public boolean isWeapon() { return true; }

    @Override public Item clone() { return new Bananarang(); }
}
