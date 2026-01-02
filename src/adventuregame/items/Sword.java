package adventuregame.items;

import java.util.Random;

import adventuregame.Damage;
import adventuregame.Game.Metal;
import adventuregame.Game;
import adventuregame.Player;
import adventuregame.abstractclasses.Unit;

public class Sword extends Weapon {

    Damage dmg;
    public int numAttacks = 0;
    Metal material;
    String[] atkmsgs;

    public Sword(float damage)
    {
        dmg = new Damage(damage, Damage.Type.BASIC);
        atkmsg = "You swing this blade in a blinding blur of DEATH.";
        material = Metal.IRON;
        name = "Sword";
        description = "nondescript iron sword";
        pluralDescription = "iron swords";

        setNumAttacks();
    }

    public Sword(float damage, Metal material, String name, String description, String pDes, String atkmsg)
    {
        dmg = new Damage(damage, Damage.Type.BASIC);
        this.atkmsg = atkmsg;
        this.material = material;
        this.name = name;
        this.description = description;
        this.pluralDescription = pDes;

        setNumAttacks();
    }

    public Sword(float damage, Metal material, String name, String description, String pDes, String[] atkmsgs)
    {
        dmg = new Damage(damage, Damage.Type.BASIC);
        this.atkmsgs = atkmsgs;
        this.material = material;
        this.name = name;
        this.description = description;
        this.pluralDescription = pDes;

        setNumAttacks();
    }

    public void setNumAttacks()
    {
        switch(material)
        {
            case GOLD: numAttacks = 0; break;
            case COPPER:
            case TARIRON:
            case IRON:
            case SILVER:
            case LODESTONE: numAttacks = 1; break;
            case STEEL: numAttacks = 2; break;
        }
    }

    public boolean use() 
    { 
        return --numAttacks > 0;
    }

    @Override public void action(Unit u, boolean isFinal) { 
        System.out.println("You inspect this sword: " + description); 
        if(!isFinal && u instanceof Player) ((Player)u).ableToAct = true;
    }

    @Override public Damage getDamage() { 
        if(atkmsgs != null && Game.isLaur) atkmsg = atkmsgs[new Random().nextInt(atkmsgs.length)];
        return dmg; }
}
