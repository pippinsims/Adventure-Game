package adventuregame.items;
import adventuregame.Damage;
import adventuregame.interfaces.Item;

public class Bananarang implements Item 
{
    String description = "A boomerang made of a potassium rich biotic material.";
    String name = "Bananarang";

    @Override
    public void action() 
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'action'");
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
}
