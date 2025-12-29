package adventuregame;

import adventuregame.interactibles.ItemHolder;
import adventuregame.interactibles.SkeletonInteractible;
import adventuregame.interactibles.Table;
import adventuregame.interactibles.WallEntity.Wall;
import adventuregame.interactibles.wallentities.*;
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

    public static void main(String[] args) throws Exception 
    {
        //room r0 is the current room
        generateMap();

        printIntroduction();
        System.out.println();

        ArrayList<Room> playerRooms = new ArrayList<>();
        //TODO seems like this whole loop could be optimized and shrunken
        while(!allPlayers.isEmpty())
        {
            for(Player p : allPlayers)
            {
                boolean found = false;
                for(Room r : playerRooms) { if(r == p.getRoom()) { found = true; break; } } //must compare by reference not description
                if(!found) playerRooms.add(p.getRoom());
            }
            
            for(Room r : new ArrayList<>(playerRooms))
            {
                curRoom = r;

                for (Player p : new ArrayList<>(r.players))
                {
                    curPlayer = p;
                    isLaur = p.getName().equals("Laur");
                    p.updateUnit();
                    System.out.println();
                    if(r.players.isEmpty()) break;
                }
                for(Dialogue d : r.dialogues) if(d.atEnd) d.complete();

                if(r.players.isEmpty()) 
                {
                    playerRooms.remove(r);
                    continue;
                }
                if(allPlayers.isEmpty()) break;
                
                for(Enemy e : new ArrayList<>(r.enemies))
                {
                    e.updateUnit();
                    System.out.println();
                    if(r.players.isEmpty()) break;
                }

                if(r.players.isEmpty()) playerRooms.remove(r);
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
        curRoom.players.add(p);
    }

    private static void generateMap()
    {
        Room hall = new Room("a long hall with many cells","Prison hallway");
        //average narwhal weight is 1.425 tons
        String celld = "a barren, empty, disgusting prison cell", celll = celld + ".\nThe walls are made of massive stone bricks (each probably weighs more than 25 Narwhals and a Unicorn). The ceiling is 24 feet high.\nNot a place for happy thoughts", cellf = "Stone brick prison cell.", celln = "Cell";
        curRoom = new Room(celld, celll, cellf, celln, false);
        new Door(curRoom, hall, Wall.EAST);
        new Window(curRoom, "a gloomy landscape through the close, glittering, impeccable steel bars. Dull reddish light gleams from above a mountain in the foggy distance.", Wall.WEST);
        //skeletoninteractible in cledobl room with armor
        //armor on skeletoninteractible 50% chance of reducing basic/blunt damage by 1, when repaired by smith (avg smith wouldn't be able to do this, need to follow a quest of meeting smiths and they point you to him, prodigy smith, conversant in ancient techniques): auto reduce 2 basic/blunt damage, other blockable damages have 50% chance reduce by 1 50% chance of all first 2 tiers of spells on this unit fail
        //skeletoninteractible on interact 1% of turning into skeleton enemy with same inventory
        //turn body into skeletoninteractible grasping sword if VITALITYDRAIN QTE runs out of time/kills you
        //on inspect: print description of armor set
        //on Valeent inspect: armor is from an ancient House she's read about (she's a noble) (it's the same House that used to own this dungeon)

        Room cell2 = new Room(celld, celll, cellf, celln, false);
        new Table(cell2);
        Interactible cleholder = new ItemHolder(
            new Sword(10, Metal.STEEL, "Cledobl", "glittering steel sword", "steel swords", "Your weapon shears the air in a gnawing arch"), 
            cell2,
            "stuck in",
            "the table"
        );
        cleholder.isEnabled = false;
        new Door(cell2, hall, Wall.EAST);
        ArrayList<Item> clegear = new ArrayList<>(List.of(
                                                        new Armor("Ancient Boot" , "", "", Armor.MaterialType.ANCIENT_RUSTED, Armor.PartType.BOOTS),
                                                        new Armor("Ancient Gaunt", "", "", Armor.MaterialType.ANCIENT_RUSTED, Armor.PartType.GAUNTLETS),
                                                        new Armor("Ancient Helm" , "", "", Armor.MaterialType.ANCIENT_RUSTED, Armor.PartType.HELMET),
                                                        new Armor("Ancient Legs" , "", "", Armor.MaterialType.ANCIENT_RUSTED, Armor.PartType.LEGS),
                                                        new Armor("Ancient Torso", "", "", Armor.MaterialType.ANCIENT_RUSTED, Armor.PartType.TORSO)
                                                    ));
        SkeletonInteractible cleskelly = 
        new SkeletonInteractible("Ancient Skeleton", 
                         "Old dilapidated skeleton with armor",
                         "bent over",
                         "",
                         "",
                         "brush aside",
                         "from",
                         "",
                         "",
                         "the table"
                        ){
                            @Override 
                            public void action(Unit u)
                            {
                                switch(actionVerb)
                                {
                                    case "brush aside":
                                        if(new Random().nextInt(10) == 9)
                                        {
                                            getRoom().interactibles.remove(this);
                                            getRoom().add(
                                                new Skeleton(
                                                    clegear,
                                                    new Sword(5)
                                                )
                                            );
                                        }
                                        else
                                        {
                                            Utils.slowPrintln("You brush the hand of the skeleton away from the sword.");
                                            cleholder.isEnabled = true;
                                            actionVerb = "loot";
                                            normalLocPrep = "lying on";
                                            locReference = "the floor";
                                        }
                                        break;
                                    case "loot":
                                        Inventory uinv = u.getInventory();
                                        if(uinv.isFull())
                                        {
                                            Utils.slowPrintln("You check the skeleton for items...");
                                            String[] prompts = new String[] {"Take all", "Take one"};
                                            if(prompts[Utils.promptList("You can:", prompts)].equals("Take one"))
                                            {
                                                ArrayList<Item> its = inv.getItems();
                                                Item i = its.get(Utils.promptList("Which item?", Utils.descriptionsOf(its)));
                                                uinv.add(i);
                                                its.remove(i);   
                                            }
                                            else
                                            {
                                                for(Item i : new ArrayList<>(inv.getItems())) if(!uinv.isFull())
                                                {
                                                    uinv.add(i);
                                                    inv.remove(i);
                                                }
                                                
                                                if(!inv.isEmpty()) Utils.slowPrint("Your inventory is full! You only took some of the items.");
                                            }
                                            if(inv.isEmpty()) actionVerb = "";
                                        }
                                        else Utils.slowPrintln("Your inventory is full! You cannot loot this.");
                                        break;
                                }
                            }

                            @Override
                            public void inspect()
                            {
                                for(Item i : inv.getItems()) if(i instanceof Armor) 
                                { 
                                    if(curPlayer.getName().equals("Valeent"))
                                    {
                                        Utils.slowPrintln(Armor.armorDescs.get(((Armor)i).getMat()));
                                        return;
                                    }
                                    else
                                    {
                                        Utils.slowPrintln(Armor.armorDescs.get(Armor.MaterialType.RUSTED));
                                        return;
                                    }
                                }
                                description = "Old dilapidated skeleton.";
                                Utils.slowPrintln(getDescription());
                            }
                        };
        for(Item i : clegear) cleskelly.add(i);
        cleskelly.add(new Sword(5));

        for (int i = 2; i < 13; i++) new Door(new Room(celld, celll, cellf, celln, false), hall, i < 7 ? Wall.EAST : Wall.WEST);
        Room cell14 = new Room(celld, celll, cellf, celln, false);
        new Door(cell14, hall, Wall.WEST);
        new ItemHolder(new Sword(4), cell14, "on", "the floor");

        ArrayList<Enemy> ens = new ArrayList<>(List.of(new Goblin(3), new Goblin(3), new Goblin(3)));
        Room chamber = new Room("a dimly lit room.\nThere is a faint foul odor...\nThe patchwork on the wall depicts of a redheaded lunatic.\n\"Lord Gareth the Mad.\"",                    
                                "The Chamber.",
                                "Chamber",
                                true);
        for(Enemy e : ens) chamber.add(e);
        chamber.add(new Dialogue(
            new ArrayList<>(ens), 
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
                            new Dialogue.Node.B(0, "Then you die."),
                            new Dialogue.Node.L<Room>(0, "And don't you dare leave again...", null, curRoom, true),
                            new Dialogue.Node.L<Room>(curRoom, true)
                        }
                    ),
                    new Dialogue.Node.L<Room>(0, "You shold shut that trap and gloink back into your cell is what!", null, curRoom, true),
                    new Dialogue.Node.B()
                }
            )
        ));
        new Door(hall, chamber, Wall.NORTH);
        
        Room mossyRuin = new Room("a room with shrooms, a shroom room if you will.\n       \t\t\t\tAre you afraid of large spaces? Becausesss there's a mush-a-room if you catch my drift,",
                                  "Shroom Room.",
                                  "Mossy Ruin",
                                  true);
        Interactible mushroom = new Interactible("Big mushroom",
                                                 "table-sized toadstool",
                                                 "on",
                                                 "",
                                                 "",
                                                 "",
                                                 "",
                                                 "toad-sized TABLEstool",
                                                 "",
                                                 "the floor"
                                                );
        mossyRuin.add(mushroom);
        
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
        addPlayer(new Player("Nuel"));
        // addPlayer(new Player("Valeent"));
        // addPlayer(new Player("Peili"));
        // addPlayer(new Player("Dormaah"));
    }

    public static void printInfo(Room r, boolean peek)
    {
        System.out.println("--Info--");

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
            Utils.slowPrintln("you died.");
            u.getRoom().players.remove(u);
            allPlayers.remove(u);
        }

        Utils.slowPrintln(u.getDeathMessage() + "------", 0/*200*/);
    }
}
