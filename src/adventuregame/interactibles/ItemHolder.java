package adventuregame.interactibles;

import java.util.function.Consumer;

import adventuregame.Effect;
import adventuregame.Interactible;
import adventuregame.QuickTimeEvent;
import adventuregame.QuickTimeEvent.EffectQTE;
import adventuregame.QuickTimeEvent.NoUpdateQTE;
import adventuregame.QuickTimeEvent.Node;
import adventuregame.QuickTimeEvent.Node.Output;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;
import adventuregame.items.Armor;
import adventuregame.items.Weapon;

public class ItemHolder extends Interactible {
    
    public Item item;

    public ItemHolder(Item i, Room r, String preposition, String location)
    {
        if(i.isDynamicItem()) throw new UnsupportedOperationException("DynamicItem x must be in a room as x.interactible, not ItemHolder(x.item)");
        setDefaults(
            i.getName(),
            i.getDescription(),
            preposition, 
            i.getPluralDescription(), 
            "", 
            "Take", 
            "from"
        );

        item = i;
        locReference = location;
        myRoom = r;
        r.add(this);
    }

    @Override
    public void action(Unit u)
    {
        switch(item.getName())
        {
            case "Cledobl":
                if(u.getName().equals("Laur"))
                {
                    System.out.println(u.getName() + " took " + name + " " + actLocPrep + " " + locReference);
                    myRoom.remove(this);
                    u.getInventory().add(item);
                }
                else
                {
                    Consumer<Integer> effectu = (curRound) -> {
                        u.removeAllOf(Effect.Type.VITALITYDRAIN);
                        switch(curRound + 1)
                        {
                            case 1: case 2: case 3: 
                                u.updateMaxHealth(u.getMaxHealth() + curRound);
                                u.addEffect(new Effect(Effect.Type.WEAKNESS, 1, 1));
                                break;
                            case 4: case 5:
                                u.addEffect(new Effect(Effect.Type.VITALITYGROW, 3, curRound/3));
                                u.addEffect(new Effect(Effect.Type.WEAKNESS, 6, 1));
                                break;
                            case 6: case 7: 
                                u.addEffect(new Effect(Effect.Type.WEAKNESS, 10, 1));
                                break;
                            case 8: case 9:
                                u.addEffect(new Effect(Effect.Type.WEAKNESS, -1, 1));
                                break;
                        }
                    };

                    System.out.println(u.getName() + " takes the sword by the handle... ");

                    Node.L cryoutNode = new Node.L(Output.CHECK) 
                    { 
                        @Override public boolean output(String in, QuickTimeEvent q)
                        {
                            if(u.getRoom().players.size() == 1) return false;
                            else
                            {
                                Unit helper = null;
                                for(Unit u : u.getRoom().players) if(u != q.getActor()) { helper = u; break; }
                                new NoUpdateQTE(
                                    helper,
                                    new Describable() { { description = "helpcledobl"; } }, 
                                    -1,
                                    new Node.B(
                                        helper.getName()+", do you help?", 
                                        new String[] 
                                        {
                                            "Help.",
                                            "Do not help."
                                        },
                                        new Node[] 
                                        {
                                            new Node.L(Output.END) 
                                            {
                                                @Override public boolean output(String in, QuickTimeEvent q) { 
                                                    Utils.slowPrintln("You tear " + u.getName() + " their grip on the cursed blade.");
                                                    effectu.accept(q.getCurrentRound());
                                                    return true;
                                                }
                                            },
                                            new Node.L(Output.END) 
                                            {
                                                @Override public boolean output(String in, QuickTimeEvent q) { 
                                                    Utils.slowPrintln("You do nothing.");
                                                    return true;
                                                }
                                            },
                                        }
                                    )
                                ){ @Override protected void timeout() {} //timeless QTE
                                }.run();
                                return true;
                            }
                        }
                    };

                    new EffectQTE(
                        u,
                        item,
                        new Effect(Effect.Type.VITALITYDRAIN, 10, u.getMaxHealth()/10),
                        new Node.B(
                            "YOU FEEL EXTREME PAIN. YOU ARE DYING",
                            new String[] 
                            {
                                "Do nothing.",
                                "Let go.",
                                "Cry out.",
                                "Relax your grip.",
                                "Pry hand violently.",
                                "Pull harder."
                            },
                            new Node[] 
                            {
                                new Node.X(),
                                new Node.X(),
                                cryoutNode,
                                new Node.X(),
                                new Node.L(Output.END) 
                                { 
                                    @Override public boolean output(String in, QuickTimeEvent q) { 
                                        System.out.println("You pry your catatonic fingers from the lethal power of the blade's enchantment with your free hand.");
                                        effectu.accept(q.getCurrentRound());
                                        return true;
                                    } 
                                },
                                new Node.B(
                                    "YOU FEEL EXTREME PAIN. YOU ARE DYING",
                                    new String[] {
                                        "Freeze.",
                                        "Let go.",
                                        "Cry out.",
                                        "PULL HARDER."
                                    },
                                    new Node[] {
                                        new Node.X(),
                                        new Node.X(),
                                        cryoutNode,
                                        new Node.X()
                                    }
                                )
                            }
                        ) 
                    ) {
                        @Override protected void timeout() 
                        {
                            ItemHolder cleholder = Utils.getFirst(actor.getRoom().interactibles,ItemHolder.class);
                            cleholder.isEnabled = false;
                            SkeletonInteractible yourBody = new SkeletonInteractible(
                                actor.getRoom(),
                                actor.getName()+"'s body", 
                                "new-looking skeleton",
                                "gripped tightly to",
                                "",
                                "",
                                "brush",
                                "aside from it's grip on",
                                "the sword"
                            ) {
                                @Override 
                                public void action(Unit u)
                                {
                                    getRoom().interactibles.remove(this);
                                    Utils.slowPrintln("You brush the hand of the skeleton away from the sword, causing it to crumble to the floor.");
                                    new SkeletonInteractible(
                                        getRoom(), 
                                        name, 
                                        simpleDesc,
                                        "on",
                                        "new-looking skeletons",
                                        "",
                                        "loot",
                                        "",
                                        "the floor",
                                        inv,
                                        insMap
                                    );
                                    cleholder.isEnabled = true;
                                }
                            };
                            
                            for(Armor a : actor.getInventory().getArmor()) yourBody.add(a);
                            Weapon w = Utils.getFirst(actor.getInventory().getWeapons(), Weapon.class);
                            if(w != null) yourBody.add(w);
                        }
                    }.run();
                }
                break;
            default:
                System.out.println(u.getName() + " took " + name + " " + actLocPrep + " " + locReference);

                myRoom.remove(this);
                u.getInventory().add(item);
                break;
        }
    }

    @Override protected void setInspects()
    {
        put(name + ": " + description);
    }
}
