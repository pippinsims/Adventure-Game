package adventuregame;

import adventuregame.interactibles.*;
import adventuregame.interactibles.WallInteractible.Wall;
import adventuregame.interactibles.wallinteractibles.*;
import adventuregame.items.*;
import adventuregame.QuickTimeEvent.EffectQTE;
import adventuregame.QuickTimeEvent.NoUpdateQTE;
import adventuregame.QuickTimeEvent.Node;
import adventuregame.QuickTimeEvent.Node.Output;
import adventuregame.abstractclasses.*;
import adventuregame.dynamicitems.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Environment extends Game
{
    public static void main(String[] args)
    {
        generateMap();

        printIntroduction();
        System.out.println();

        run();
        
        Utils.scanner.close();
    }

    private static void generateMap()
    {
        Room hall = new Room("a long hall with many cells","Prison hallway");
        //average narwhal weight is 1.425 tons
        String celld = "a barren, empty, disgusting prison cell", celll = celld + ".\nThe walls are made of massive stone bricks (each probably weighs more than 25 Narwhals and a Unicorn). The ceiling is 24 feet high.\nNot a place for happy thoughts", cellf = "Stone brick prison cell.", celln = "Cell";
        curRoom = new Room(celld, celll, cellf, celln);
        new Door(curRoom, hall, Wall.EAST);
        new Window(curRoom, "a gloomy landscape through the close, glittering, impeccable steel bars. Dull reddish light gleams from above a mountain in the foggy distance.", Wall.WEST);

        Room cell2 = new Room(celld, celll, cellf, celln);
        new Table(cell2);
        Interactible cleholder = new ItemHolder(
            new Sword(10, Metal.STEEL, "Cledobl", "glittering steel sword", "steel swords", new String[] {
                "Your weapon shears the air in a gnawing arch",
                "The blade scythes with unerring vanquishity",
                "Time slows as a sinister blur seeps towards the enemy",
                "THE SOUL OF CLEDOBL DOTH ADMIRE IT'S VICTIM'S DEMISE",
                "Scccreech of laughter echos lengthwise across the back of Laur's mind!",
                "As you swing this sabre, Laur imagines what it'd be like to be dead.\nNot fun.",
                "Ptolomy cackles like he witnessing da hyena.\n\n\t\t\tCledobl kills. (._.)",
                "Something deep within Laur *SNAPS* like twigs in the wild fire of his burning love and passion. Nothing to do now but destroy."
            }),
            cell2,
            "stuck in",
            "the table"
        ) {
            @Override public void action(Unit u)
            {
                ItemHolder holder = this;
        
                if(u.getName().equals("Laur"))
                    super.action(u);
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
                            holder.isEnabled = false;
                            SkeletonInteractible yourBody = new SkeletonInteractible(
                                actor.getRoom(),
                                Utils.possessiveOf(actor.getName())+" body", 
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
                                    getRoom().remove(this);
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
                                    holder.isEnabled = true;
                                }
                            };
                    
                            for(Armor a : actor.getInventory().getArmor()) yourBody.add(a);
                            Weapon w = Utils.getFirst(actor.getInventory().getWeapons(), Weapon.class);
                            if(w != null) yourBody.add(w);
                        }
                    }.run();
                }
            }
        };
        
        cleholder.isEnabled = false;
        new Door(cell2, hall, Wall.EAST);
        SkeletonInteractible cleskelly = 
        new SkeletonInteractible(
            cell2,
            "Ancient Skeleton", 
            "old dilapidated skeleton",
            "bent over",
            "",
            "",
            "brush",
            "aside from",
            "the table"
        )
        {
            @Override 
            public void action(Unit u)
            {
                getRoom().remove(this);
                if(Utils.rand.nextInt(10) == 9)
                {
                    Utils.slowPrintln("You attempt to brush away the skeleton, but it reacts, bones clinking, and assumes a combat stance!");
                    getRoom().add(new NonPlayer.Skeleton(inv));
                }
                else
                {
                    Utils.slowPrintln("You brush the hand of the skeleton away from the sword, causing it to crumble to the floor.");
                    new SkeletonInteractible(
                        getRoom(), 
                        name, 
                        simpleDesc,
                        "on",
                        "",
                        "",
                        "loot",
                        "",
                        "the floor",
                        inv,
                        insMap
                    );
                }
                cleholder.isEnabled = true;
            }
        };
        for(Item i : new ArrayList<>(List.of(
            new Armor("Ancient Boot" , "rusty boots", "", Armor.MaterialType.ANCIENT_RUSTED, Armor.PartType.BOOTS),
            new Armor("Ancient Gaunt", "rusty gauntlets", "", Armor.MaterialType.ANCIENT_RUSTED, Armor.PartType.GAUNTLETS),
            new Armor("Ancient Helm" , "rusty helmet", "", Armor.MaterialType.ANCIENT_RUSTED, Armor.PartType.HELMET),
            new Armor("Ancient Legs" , "rusty greaves", "", Armor.MaterialType.ANCIENT_RUSTED, Armor.PartType.LEGS),
            new Armor("Ancient Torso", "rusty chestpiece", "", Armor.MaterialType.ANCIENT_RUSTED, Armor.PartType.TORSO),
            new Sword(5)
        ))) cleskelly.add(i);

        for (int i = 2; i < 13; i++) new Door(new Room(celld, celll, cellf, celln), hall, i < 7 ? Wall.EAST : Wall.WEST);
        Room cell14 = new Room(celld, celll, cellf, celln);
        new Door(cell14, hall, Wall.WEST).toggleLock(cell14);
        new ItemHolder(new DoorKey(), cell14, "on", "the floor");
        new ItemHolder(new Sword(4), cell14, "on", "the floor");
        NonPlayer bofe = new NonPlayer(10, new Inventory(10), 0, "Grassy bofer", "Grassy bofers", "Bofer") 
        {
            @Override
            public void performAction(Action a)
            {
                switch(a)
                {
                    case ATTACK: attack(); break;
                    case DIALOGUE: talk(); break;
                    case NORMAL:
                        if(Utils.rand.nextInt(2) == 1)
                        {
                            ArrayList<Door> d = myRoom.getDoors();
                            d.get(Utils.rand.nextInt(d.size())).action(this);
                        }
                        else Utils.slowPrintln("Bofer does nothing.");
                        break;
                }
            }
        };
        loaded.add(bofe);
        cell14.add(bofe);

        NonPlayer daed = new NonPlayer(10, new Inventory(10), 0, "An interesting looking fellow who has greenish eyes", "Interesting fellows", "Daedalus") 
        {
            @Override
            public void performAction(Action a) 
            {
                for(Unit u : bofe.enemies) if(!Utils.contains(enemies, u)) enemies.add(u);

                switch(a)
                {
                    case ATTACK: attack(); break;
                    case DIALOGUE: talk(); break;
                    case NORMAL:
                        if(!myRoom.NPCs.contains(bofe))
                        {
                            for(Door d : myRoom.getDoors())
                            {
                                if(d.getNextRoom(myRoom).NPCs.contains(bofe))
                                {
                                    d.action(this);
                                    return;
                                }
                            }
                            Utils.slowPrintln("Daedalus has no Bofer. :(");
                        }
                        else
                            Utils.slowPrintln("Daedalus sits contented.");
                        break;
                }
            }
        };
        loaded.add(daed);
        cell14.add(daed);
        bofe.friends.add(daed);
        daed.friends.add(bofe);

        Room chamber = new Room(
            "a dimly lit room.\nThere is a faint foul odor...\nThe patchwork on the wall depicts of a redheaded lunatic.\n\"Lord Gareth the Mad.\"",                    
            "The Chamber.",
            "Chamber"
        );
        
        NonPlayer e = new NonPlayer.Goblin(3);
        chamber.add(e);
        chamber.add(new NonPlayer.Goblin(3));
        chamber.add(new NonPlayer.Goblin(3));
        for(NonPlayer n : chamber.NPCs) for(NonPlayer n1 : chamber.NPCs) if(n != n1) n.friends.add(n1);
        e.dialogues.add(
            new Dialogue(
                e,
                new ArrayList<>(List.of(e)),
                new Dialogue.B(
                    0,
                    "You're not supposed to be out'n'about!", 
                    new String[] {
                        "Well... we are!",
                        "Uh, ok. What should we do?",
                        "Thou shalt not oppose ME."
                    }, 
                    new Dialogue.Node[]
                    {
                        new Dialogue.B(
                            0,
                            "Get the *BLORCK* back in your cell!", 
                            new String[] {
                                "No.", 
                                "Fine.",
                                "Ok!"
                            }, 
                            new Dialogue.Node[] 
                            {
                                new Dialogue.X(0, "Then you die.") { 
                                    @Override public void output(Dialogue parent) 
                                    {
                                        Utils.slowPrintln("All the " + parent.actors.get(0).getPluralDescription() + " prepare to fight!");
                                        Dialogue.aggroAllOfSameType(parent.actors.get(actor));
                                    }
                                },
                                new Dialogue.L<Room>(0, "And don't you dare leave again...", null, curRoom, true) { @Override public void output(Dialogue parent) { Dialogue.playersToRoom(parent.to, out); } },
                                new Dialogue.L<Room>(curRoom, true) { @Override public void output(Dialogue parent) { Dialogue.playersToRoom(parent.to, out); } }
                            }
                        ),
                        new Dialogue.L<Room>(0, "You shold shut that trap and gloink back into your cell is what!", null, curRoom, true) {
                            @Override public void output(Dialogue parent) { Dialogue.playersToRoom(parent.to, out); }
                        },
                        new Dialogue.X() { 
                            @Override public void output(Dialogue parent) 
                            {
                                Utils.slowPrintln("All the " + parent.actors.get(0).getPluralDescription() + " prepare to fight!");
                                Dialogue.aggroAllOfSameType(parent.actors.get(actor));
                            }
                        }
                    }
                )
            )
        );
        new Door(hall, chamber, Wall.NORTH);
        
        Room mossyRuin = new Room("a room with shrooms, a shroom room if you will.\n       \t\t\t\tAre you afraid of large spaces? Becausesss there's a mush-a-room if you catch my drift,",
                                  "Shroom Room.",
                                  "Mossy Ruin");
        new Interactible(
            mossyRuin,
            "Big mushroom",
            "table-sized toadstool",
            "on",
            "",
            "",
            "",
            "",
            "the floor"
        ) {
            { descMap.put("Laur", "toad-sized TABLEstool"); }
            @Override public void action(Unit u) {}
            @Override protected void setInspects() { put(name, description); }
        };
        
        Room joiner1 = new Room();

        new Door(chamber, mossyRuin, Wall.NORTH);
        new Door(chamber, new Room(), Wall.WEST);
        new Door(chamber, joiner1, Wall.EAST);

        Room treasureRoom = new Room("a room filled to the brim in a plenteous manner. Old swords and worn chalices adorned with gems sparkle, and set your heart in motion",
                                     "Treasure Room");
        new GoldenPot(treasureRoom);
        new Door(joiner1, treasureRoom, Wall.SOUTH);

        new Torch(chamber, Wall.EAST);
        new Torch(chamber, Wall.WEST);
        new Torch(chamber, Wall.WEST);

        new ViewablePicture(chamber, "mad_king.txt", Wall.WEST, "patchwork depiction", "Lord Gareth the Mad");
        
        addPlayer(new Player());
        addPlayer(new Player("Nuel", 10));
        addPlayer(new Player("Valeent", 10));
        addPlayer(new Player("Peili", 12));
        addPlayer(new Player("Dormaah", 10));
    }
}
