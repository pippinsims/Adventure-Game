package adventuregame.items;
import adventuregame.Damage;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;

public class Bananarang extends Item 
{
    String description = "A boomerang made of a potassium rich, biotic material.";
    String name = "Bananarang";

    @Override
    public void action(Unit u) 
    {
        System.out.println("You go to take a bite... but then... you probably shouldn't");
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Damage getDamage() 
    {
       return new Damage(5, Damage.Type.BASIC, "You wail the bananarang at your enemy with KILLER INSTINCT!\nIt kills him.");
    }

    @Override
    public boolean isWeapon() 
    {
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPluralDescription() {
        return "bananarangs";
    }
}
