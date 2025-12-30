package test;

import org.junit.Test;

import adventuregame.*;
import adventuregame.dynamicitems.Torch;
import adventuregame.items.Weapon;

public class testReceiveDamage {    

    @Test
    public void testDamageSuccess()
    {
        Effectable p = new Player("Guy");
        float h = p.getHealth();
        int dmg = 2;
       
        Utils.slowPrintln("damaged by fire");
        p.receiveDamage(new Damage(dmg, Damage.Type.FIRE));
        //Damaged by normal attack
        assert(p.getHealth() + dmg + 1 == h);

        h = p.getHealth();
        Effect e = new Effect(Effect.Type.FIRE, 2, 1);
        Utils.slowPrintln("inflicted with fire");
        p.receiveDamage(new Damage(dmg, Damage.Type.FIRE, Damage.Mode.INFLICTEFFECT, e));
        
        //damaged by        attack + fire
        assert(p.getHealth() + dmg + 1 == h);

        p.updateAllEffectsWithoutResult();
        p.updateAllEffectsWithoutResult();
        //damaged by       attack +fire+effect.fire
        assert(p.getHealth() + dmg + 1 + 2 == h);
    }

    @Test
    public void testTorchAttack()
    {
        Effectable k = new Player("Guy");
        float h = k.getHealth();
        Torch t = new Torch();

        k.receiveDamage(((Weapon)t.item()).getDamage());
        for(int i = 0; i < 10; i++)
        {
            k.updateAllEffectsWithoutResult();
        }

        assert(k.getHealth() == h - 5);
    }    

    @Test
    public void testEffect()
    {
        Effectable k = new Player("Guy");
        Effect e = new Effect(Effect.Type.FIRE, 2, 1);
        
        Utils.slowPrintln("inflicted with fire");
        k.receiveDamage(new Damage(2, Damage.Type.FIRE, Damage.Mode.INFLICTEFFECT, e));
        k.receiveDamage(new Damage(2, Damage.Type.FIRE, e, "burning with fire"));
    }

    @Test (expected = Exception.class)
    public void testNoEffectInflict()
    {
        Effectable k = new Player("Guy");
       
        Utils.slowPrintln("inflicted with fire");
        k.receiveDamage(new Damage(2, Damage.Type.FIRE, Damage.Mode.INFLICTEFFECT, null));
    }

    @Test (expected = Exception.class)
    public void testNoEffectEffect()
    {
        Effectable k = new Player("Guy");
        
        Utils.slowPrintln("burning with fire");
        k.receiveDamage(new Damage(2, Damage.Type.FIRE, Damage.Mode.EFFECT, null));
    }
}
