package test;

import org.junit.Test;

import adventuregame.*;
import adventuregame.items.TorchItem;

public class testReceiveDamage {    

    @Test
    public void testDamageSuccess() throws Exception
    {
        Effectable p = new Effectable();
        float h = p.getHealth();
        int dmg = 2;
       
        p.receiveDamage(new Damage(dmg, Damage.Type.FIRE, "damaged by fire"));
        //Damaged by normal attack
        assert(p.getHealth() + dmg + 1 == h);

        h = p.getHealth();
        Effect e = new Effect(Effect.Type.FIRE, 2, 1);
        p.receiveDamage(new Damage(dmg, Damage.Type.FIRE, Damage.Mode.INFLICTEFFECT, e,  "inflicted with fire"));
        
        //damaged by        attack + fire
        assert(p.getHealth() + dmg + 1 == h);

        p.updateAllEffectsWithoutResult();
        p.updateAllEffectsWithoutResult();
        //damaged by       attack +fire+effect.fire
        assert(p.getHealth() + dmg + 1 + 2 == h);
    }

    @Test
    public void testTorchAttack() throws Exception
    {
        Effectable k = new Effectable();
        float h = k.getHealth();
        TorchItem t = new TorchItem(null);

        k.receiveDamage(t.getDamage());
        for(int i = 0; i < 10; i++)
        {
            k.updateAllEffectsWithoutResult();
        }

        assert(k.getHealth() == h - 5);
    }    

    @Test
    public void testEffect() throws Exception
    {
        Effectable k = new Effectable();
        Effect e = new Effect(Effect.Type.FIRE, 2, 1);
        
        k.receiveDamage(new Damage(2, Damage.Type.FIRE, Damage.Mode.INFLICTEFFECT, e, "inflicted with fire"));
        k.receiveDamage(new Damage(2, Damage.Type.FIRE, e, "burning with fire"));
    }

    @Test (expected = Exception.class)
    public void testNoEffectInflict() throws Exception
    {
        Effectable k = new Effectable();
       
        k.receiveDamage(new Damage(2, Damage.Type.FIRE, Damage.Mode.INFLICTEFFECT, null, "inflicted with fire"));
    }

    @Test (expected = Exception.class)
    public void testNoEffectEffect() throws Exception
    {
        Effectable k = new Effectable();
        
        k.receiveDamage(new Damage(2, Damage.Type.FIRE, Damage.Mode.EFFECT, null, "burning with fire"));
    }
}
