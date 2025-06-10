package adventuregame.items;
import adventuregame.Damage;
import adventuregame.Utils;
import adventuregame.interfaces.Item;

public class Bananarang implements Item 
{
    String description = "A boomerang made of a potassium rich biotic material.";
    
    @Override
    public void action() 
    {
        Utils.slowPrint("You wail the bananarang at your enemy with KILLER INSTINCT!");
        Utils.slowPrintln(" It kills him.", 200);
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDescription'");
    }

    @Override
    public Damage getDamage() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDamage'");
    }
}
