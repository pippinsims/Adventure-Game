package adventuregame.items;
import adventuregame.Damage;
import adventuregame.Player;
import adventuregame.abstractclasses.Unit;

public class Bananarang extends Weapon
{
    { 
        description = "A boomerang made of a potassium rich, biotic material.";
        name = "Bananarang";
        pluralDescription = "bananarangs";
        atkmsg = "You wail the bananarang at your enemy with KILLER INSTINCT!\nIt kills him.";
    }
    @Override
    public void action(Unit u, boolean isFinal) 
    {
        System.out.println("You go to take a bite... but then... you probably shouldn't");
        if(!isFinal && u instanceof Player) ((Player)u).promptForAction();
    }


    @Override
    public Damage getDamage() 
    {
       return new Damage(5, Damage.Type.BASIC);
    }
}
