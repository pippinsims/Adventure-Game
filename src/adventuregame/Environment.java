package adventuregame;

import adventuregame.interactibles.ItemHolder;
import adventuregame.interactibles.SkeletonInteractible;
import adventuregame.interactibles.Table;
import adventuregame.interactibles.WallInteractible.Wall;
import adventuregame.interactibles.wallinteractibles.*;
import adventuregame.items.Armor;
import adventuregame.items.Sword;
import adventuregame.abstractclasses.Enemy;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;
import adventuregame.dynamicitems.GoldenPot;
import adventuregame.dynamicitems.Torch;
import adventuregame.enemies.Goblin;
import adventuregame.enemies.Skeleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Environment
{
    public static Room curRoom;
    public static Player curPlayer;
    public static ArrayList<Player> allPlayers = new ArrayList<>();
    public static boolean isLaur;

    public enum Metal
    {
        COPPER,
        TARIRON,
        IRON,
        SILVER,
        GOLD,
        LODESTONE,
        STEEL
    }

    public static void main(String[] args)
    {
        generateMap();

        printIntroduction();
        System.out.println();

        while(!allPlayers.isEmpty())
        {
            //all players
            for(Player p : new ArrayList<>(allPlayers))
            {
                curPlayer = p; curRoom = p.getRoom(); isLaur = p.getName().equals("Laur");
                p.updateUnit();
                System.out.println();
            }
            curPlayer = null; isLaur = false;

            ArrayList<Room> playerRooms = new ArrayList<>();
            for(Player p : allPlayers) if(!Utils.contains(playerRooms, p.getRoom())) playerRooms.add(p.getRoom());

            //all enemies in player rooms
            for(Room r : playerRooms) 
            {
                if(r == null) throw new UnsupportedOperationException("this is cooked");
                if(!r.enemies.isEmpty())
                {
                    curRoom = r;
                    for(Enemy e : new ArrayList<>(r.enemies))
                    {
                        e.updateUnit();
                        System.out.println();
                        if(r.players.isEmpty()) break;
                        // for(Player p : r.players) System.out.println(p.getName());
                    }
                }
            }

            System.out.println("\t\t\t\t\t\t\t\t--Round End--");
        }
        
        Utils.scanner.close();
    }

    private static void printIntroduction()
    {
        Utils.slowPrint("In this land, you're known as ");
        if(allPlayers.size() > 0)
        {
            int num = allPlayers.size();
            
            for(int i = 0; i < num; i++)
            {
                Utils.slowPrintAsList(allPlayers.get(i).getName(), num, i);
            }
        }
        Utils.slowPrint(" ", 1000);
        Utils.slowPrint("Adventure awaits!", 10);
        Utils.slowPrint("\n", 200);
    }

    private static void addPlayer(Player p)
    {
        allPlayers.add(p);
        curRoom.add(p); //TODO for now, all players spawn in curRoom
    }

    private static void generateMap()
    {
        Room hall = new Room("a long hall with many cells","Prison hallway");
        //average narwhal weight is 1.425 tons
        String celld = "a barren, empty, disgusting prison cell", celll = celld + ".\nThe walls are made of massive stone bricks (each probably weighs more than 25 Narwhals and a Unicorn). The ceiling is 24 feet high.\nNot a place for happy thoughts", cellf = "Stone brick prison cell.", celln = "Cell";
        curRoom = new Room(celld, celll, cellf, celln);
        new Door(curRoom, hall, Wall.EAST);
        new Window(curRoom, "a gloomy landscape through the close, glittering, impeccable steel bars. Dull reddish light gleams from above a mountain in the foggy distance.", Wall.WEST);
        //turn body into skeletoninteractible grasping sword if VITALITYDRAIN QTE runs out of time/kills you

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
        );
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
                getRoom().interactibles.remove(this);
                if(new Random().nextInt(10) == 9)
                {
                    Utils.slowPrintln("You attempt to brush away the skeleton, but it reacts, bones clinking, and assumes a combat stance!");
                    getRoom().add(new Skeleton(inv));
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
        new Door(cell14, hall, Wall.WEST);
        new ItemHolder(new Sword(4), cell14, "on", "the floor");

        ArrayList<Enemy> ens = new ArrayList<>(List.of(new Goblin(3), new Goblin(3), new Goblin(3)));
        Room chamber = new Room("a dimly lit room.\nThere is a faint foul odor...\nThe patchwork on the wall depicts of a redheaded lunatic.\n\"Lord Gareth the Mad.\"",                    
                                "The Chamber.",
                                "Chamber");
        for(Enemy e : ens) chamber.add(e);
        ens.getFirst().dialogues.add(
            new Dialogue(
                ens.getFirst(),
                new ArrayList<>(List.of(ens.get(0),ens.get(1))),
                new Dialogue.Node.B(
                    0,
                    "You're not supposed to be out'n'about!", 
                    new String[] {
                        "Well... we are!",
                        "Uh, ok. What should we do?",
                        "Thou shalt not oppose ME."
                    }, 
                    new Dialogue.Node[]
                    {
                        new Dialogue.Node.B(
                            0,
                            "Get the *BLORCK* back in your cell!", 
                            new String[] {
                                "No.", 
                                "Fine.",
                                "Ok!"
                            }, 
                            new Dialogue.Node[] 
                            {
                                new Dialogue.Node.L<>(0, "Then you die."),
                                new Dialogue.Node.L<Room>(0, "And don't you dare leave again...", null, curRoom, true),
                                new Dialogue.Node.L<Room>(curRoom, true)
                            }
                        ),
                        new Dialogue.Node.L<Room>(0, "You shold shut that trap and gloink back into your cell is what!", null, curRoom, true),
                        new Dialogue.Node.L<>()
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
        ) {{ descMap.put("Laur", "toad-sized TABLEstool"); }};
        
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
        // addPlayer(new Player("Nuel", 10));
        addPlayer(new Player("Valeent", 10));
        // addPlayer(new Player("Peili", 12));
        // addPlayer(new Player("Dormaah", 10));
    }

    public static void printInfo(Room r, boolean peek)
    {
        if(!peek) System.out.println("--Info--");

        if(!r.getIsFamiliar())
        {
            Utils.currentPrintDelay = Utils.MAX_PRINT_DELAY;
            Utils.slowPrintln("You" + (peek ? " see " : "'re in ") + r.getDescription() + ".");
        }
        else
        {
            Utils.slowPrintln(r.getDescription());
        }

        Utils.slowPrintDescList(r.interactibles);

        Utils.slowPrintDescList(r.enemies);
        
        r.discover();
        Utils.currentPrintDelay = 3;
    }

    public static void kill(Effectable e)
    {
        Unit u = (Unit)e;
        if(u instanceof Enemy)
        {
            ArrayList<Enemy> all = u.getRoom().enemies;
            for(int i = 0; i < all.size(); i++) if(all.get(i) == u) all.remove(i); //normal remove method compares by description (.equals())
            if(curPlayer.getName().equals("Laur")) Utils.slowPrintln("You've murdered " + u.getName(), 0/*200*/);
        }
        else if(u instanceof Player)
        {
            Utils.slowPrintln("you died." + u.getName());
            u.getRoom().players.remove(u);
            allPlayers.remove(u);
            for(Player x : allPlayers) System.out.println(x.getName());
        }
        u.setRoom(null);
        Utils.slowPrintln(u.getDeathMessage() + "------", 0/*200*/);
    }
}
