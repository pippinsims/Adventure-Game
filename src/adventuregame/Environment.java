package adventuregame;

import adventuregame.interactibles.ItemHolder;
import adventuregame.interactibles.Table;
import adventuregame.interactibles.WallEntity.Wall;
import adventuregame.interactibles.wallentities.*;
import adventuregame.items.Sword;
import adventuregame.abstractclasses.Unit;
import adventuregame.dynamicitems.GoldenPot;
import adventuregame.dynamicitems.Torch;

import java.util.ArrayList;
import java.util.List;

public class Environment
{
    public static Room startRoom;
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
            for(Player p : allPlayers) if(!playerRooms.contains(p.getRoom())) playerRooms.add(p.getRoom());
            
            for(Room r : new ArrayList<>(playerRooms))
            {
                startRoom = r;

                for (Player p : new ArrayList<>(startRoom.players))
                {
                    curPlayer = p;
                    isLaur = curPlayer.getName().equals("Laur");
                    p.updateUnit();
                    System.out.println();
                }
                for(Dialogue d : startRoom.dialogues) if(d.atEnd) d.complete();

                if(startRoom.players.isEmpty()) playerRooms.remove(startRoom);
                if(allPlayers.isEmpty()) break;
                
                for(Enemy e : new ArrayList<>(startRoom.enemies))
                {
                    e.updateUnit();
                    System.out.println();
                    if(startRoom.players.isEmpty()) break;
                }

                if(startRoom.players.isEmpty()) playerRooms.remove(startRoom);
            }

            System.out.println("--Round End--");
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
        startRoom.players.add(p);
    }

    private static void generateMap()
    {
        Room room5 = new Room("A hallway lined with cells for prisoners; none hold anything of import.","Hallway of Prison cells");
        //average narwhal weight is 1.425 tons
        String celld = "a barren, empty, disgusting prison cell", celll = celld + ".\nThe walls are made of massive stone bricks (each probably weighs more than 25 Narwhals and a Unicorn). The ceiling is 24 feet high.\nNot a place for happy thoughts", cellf = "Stone brick prison cell.", celln = "Cell";
        startRoom = new Room(celld, celll, cellf, celln, false);
        new Door(startRoom, room5, Wall.EAST);
        new Window(startRoom, "a gloomy landscape through the close, glittering, impeccable steel bars. Dull reddish light gleams from above a mountain in the foggy distance.", Wall.WEST);
        //skeletoninteractible in cledobl room with armor
        //armor on skeletoninteractible 50% chance of reducing basic/blunt damage by 1, when repaired by smith (avg smith wouldn't be able to do this, need to follow a quest of meeting smiths and they point you to him, prodigy smith, conversant in ancient techniques): auto reduce 2 basic/blunt damage, other blockable damages have 50% chance reduce by 1 50% chance of all first 2 tiers of spells on this unit fail
        //skeletoninteractible on interact 1% of turning into skeleton enemy with same inventory
        //turn body into skeletoninteractible grasping sword if VITALITYDRAIN QTE runs out of time/kills you
        //on inspect: print description of armor set
        //on Valeent inspect: armor is from an ancient House she's read about (she's a noble) (it's the same House that used to own this dungeon)

        Room room4 = new Room("A old room left to what ever hides in the corners of this keep", "The wriggler has had his picnic with this Bort", "It is the old hidden room", "Cledobl", false);
        new Table(room4);
        new ItemHolder(
            new Sword(10, Metal.STEEL, "Cledobl", "glittering steel sword", "steel swords", "Your weapon shears the air in a gnawing arch"), 
            room4,
            "stuck in",
            "the table"
        );
        new Door(room5, room4, Wall.SOUTH);

         Room room2 = new Room("A small hall containing half of a tapestry. This half shows an empty throne for a queen. The throne is extravagant. the tapestry is ornately  with different twisting designs. This Hall leads to a door to the east", "A winding trail contains The clothing of a bull, spotted with mean thoughts; a wrong place for little clouds is holds no vapor. A vertical lid is located at the end of the way.", "The hall with a half of a tapestry showing an empty queens throne. At the end of the hall is a door.", "A hall with the Torn Tapestry", false);
        new Door(room2, room5, Wall.WEST);
        new Torch(room2, Wall.SOUTH);

        Room room3 = new Room("A bare room of little interest", "a bag of flies", "an empty room", "iron sword room", false);
        new Door(room3, room2, Wall.WEST);
        new ItemHolder(new Sword(4), room3, "on", "the floor");

        ArrayList<Enemy> ens = new ArrayList<>(List.of(new Enemy(3), new Enemy(3), new Enemy(3)));
        Room room6 = new Room("A room that stinks of goblins",       
                                "The stinky room.",
                                "goblin room",
                                true);
        for(Enemy e : ens) room6.add(e);
        room6.add(new Dialogue(
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
                            new Dialogue.Node.L<Room>(0, "And don't you dare leave again...", null, startRoom, true),
                            new Dialogue.Node.L<Room>(startRoom, true)
                        }
                    ),
                    new Dialogue.Node.L<Room>(0, "You shold shut that trap and gloink back into your cell is what!", null, startRoom, true),
                    new Dialogue.Node.B()
                }
            )
        ));
        new Door(room5, room6, Wall.NORTH);
        Room room8 = new Room("A hallway that is missing its north wall. From here you can see that you are deep underground due to a large cavern.", "A hollow cube missing its nose, and eyes, and mouth, and other stuff", "Its the hallway missing a wall showing the deep cavern", "the broken wall room", false);
        new Door(room6, room8, Wall.NORTH);
        
        
        Room room9 = new Room("a room with shrooms, a shroom room if you will.\n       \t\t\t\tAre you afraid of large spaces? Becausesss there's a mush-a-room if you catch my drift,",
                                  "Shroom Room.",
                                  "Mossy Ruin",
                                  true);
        Enemy shroomie = new Enemy(2, new Inventory(2), 1, 99999, "Mushroom Monster");
        room9.add(shroomie);
        room9.add(new Dialogue(new ArrayList<>(
            List.of(shroomie)), 
            new Dialogue.Node.L<Effect>(
                0, 
                "I see you.", 
                new String[]{
                    "Hello?", 
                    "Uh, ok.", 
                    "Die."
                },
                new Effect(Effect.Type.POISON, 1, 1), 
                true
            )
        ));
        new Door(room8, room9, Wall.EAST);

        Room room10 = new Room("There is dining hall with a crackling fireplace. The room’s smells make your stomachs churn due to the lack of real food.", "A Trystal shines, on a place for things with legs. ", "Its the dining hall", "dining hall", false);
        new Door(room8, room10, Wall.SOUTH);

        Room room11 = new Room("A plain old hallway", "a walk for old men", "still a boring hallway", "a hallway", false);
        new Door(room10, room11, Wall.SOUTH);

        Room room12 = new Room("A tall stairway connecting the first and second floors, some of the stairs have fallen to disrepair. Stay close to the wall or risk falling to your death.", "A quarrelsome bird", "it's the stairwell", "stairwell to the second floor", false);
        new Door(room11, room12, Wall.EAST);

        Room room13 = new Room("room 13", "It's Bugmar's coin", "It's the quarter's the goblins sleep... well... slept in.", "The goblin's barrak's", false);
        new Door(room11, room13, Wall.SOUTH);




















        new ViewablePicture(room2, "mad_king.txt", Wall.WEST, "patchwork depiction", "Lord Gareth the Mad");
        
        addPlayer(new Player());
        addPlayer(new Player("Nuel"));
        // addPlayer(new Player("Valeent"));
        // addPlayer(new Player("Peili"));
        // addPlayer(new Player("Dormaah"));
    }

    public static void printInfo()
    {
        System.out.println("--Info--");

        if(!startRoom.getIsFamiliar())
        {
            Utils.currentPrintDelay = Utils.MAX_PRINT_DELAY;
            Utils.slowPrintln("You're in " + startRoom.getDescription() + ".");
        }
        else
        {
            Utils.slowPrintln(startRoom.getDescription());
        }

        Utils.slowPrintDescList(startRoom.interactibles);

        Utils.slowPrintDescList(startRoom.enemies);
        
        startRoom.discover();
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
