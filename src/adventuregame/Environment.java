package adventuregame;

import adventuregame.interactibles.WallEntity.Wall;
import adventuregame.interactibles.wallentities.*;
import adventuregame.Utils.Tuple;
import adventuregame.abstractclasses.Unit;
import adventuregame.dynamicitems.GoldenPot;
import adventuregame.dynamicitems.Torch;

import java.util.ArrayList;
import java.util.Map;

public class Environment
{
    public static Room r0;
    public static ArrayList<Player> allPlayers = new ArrayList<>();
    public static Player curPlayer;
    public static boolean isLaur;
    public static Map<Effect.Type, Tuple<String, String>> effectDescriptions = Map.ofEntries(Map.entry(Effect.Type.FIRE, new Tuple<String, String>("fire effect", "BURNINGNESS")),
                                                                                             Map.entry(Effect.Type.PSYCHSTRIKE, new Tuple<String, String>("psychstrike effect","strong vexation of mind")));

    public static void main(String[] args) throws Exception 
    {
        //room r0 is the current room
        generateMap();

        printIntroduction();
        System.out.println();

        ArrayList<Room> playerRooms = new ArrayList<>();
        while(!allPlayers.isEmpty())
        {
            for (Player p : allPlayers) 
            {
                if(!playerRooms.contains(p.getRoom()))
                    playerRooms.add(p.getRoom());
            }

            for (int r = 0; r < playerRooms.size(); r++)
            {
                r0 = playerRooms.get(r);

                for (Player p : new ArrayList<>(r0.players))
                {
                    curPlayer = p;
                    isLaur = curPlayer.getName().equals("Laur");
                    p.updateUnit();
                    System.out.println();
                }

                if(r0.players.isEmpty())
                {
                    playerRooms.remove(r0);
                    r--;
                    continue;
                }
                
                for(Enemy e : new ArrayList<>(r0.enemies))
                {
                    e.updateUnit();
                    System.out.println();
                }
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
        r0.players.add(p);
    }

    private static void generateMap()
    {
        Room end = new Room("a dimly lit room.\nThere is a faint foul odor...\nThe patchwork on the wall depicts of a redheaded lunatic.\n\"Lord Gareth the Mad.\"", "Chamber");
        
        Room mossyRuin = new Room("a room with shrooms, a shroom room if you will.\n       \t\t\t\tAre you afraid of large spaces? Becausesss there's a mush-a-room if you catch my drift,", "Mossy Ruin");
        mossyRuin.add(new Enemy(2, new Inventory(2), 1, 99999, "Mushroom Monster"));
        
        new Door(end, mossyRuin, Wall.NORTH);
        new Door(end, new Room(), Wall.WEST);
        Room joiner1 = new Room();
        new Door(end, joiner1, Wall.EAST);

        Room treasureRoom = new Room("a room filled to the brim in a plentious manner. Old swords and worn chalices adorned with gems sparkle, and set your heart in motion.", "Treasure Room");
        new GoldenPot(treasureRoom);

        new Door(joiner1, treasureRoom, Wall.SOUTH);

        for (int i = 0; i < 4; i++) end.add(new Enemy(3));

        new Torch(end, Wall.EAST);
        new Torch(end, Wall.WEST);
        new Torch(end, Wall.WEST);

        new ViewablePicture(end, "mad_king.txt", Wall.WEST, "patchwork depiction", "Lord Gareth the Mad");
        
        Room hall = new Room("a long hall with many cells", "PrisonHall");
        //average narwhal weight is 1.425 tons
        String celld = "a barren, empty, disgusting prison cell", celll = celld + ".\nThe walls are made of massive stone bricks (each probably weighs more than 25 Narwhals and a Unicorn). The ceiling is 24 feet high.\nNot a place for happy thoughts", cellf = "Stone brick prison cell.", celln = "Cell";
        r0 = new Room(celld, celll, cellf, celln);
        new Door(r0, hall, Wall.EAST);
        for (int i = 1; i < 14; i++) new Door(new Room(celld, celll, cellf, celln), hall, i < 7 ? Wall.EAST : Wall.WEST);
        new Door(hall, end, Wall.NORTH);
        new Window(r0, "a gloomy landscape through the tight, glittering, impeccable steel bars. Dull reddish light gleams from above a mountain in the foggy distance.", Wall.WEST);

        addPlayer(new Player());
        // addPlayer(new Player("Nuel"));
        // addPlayer(new Player("Valeent"));
        addPlayer(new Player("Peili"));
        // addPlayer(new Player("Dormaah"));
    }

    public static void printInfo()
    {
        System.out.println("--Info--");

        if(!r0.getIsFamiliar())
        {
            Utils.currentPrintDelay = Utils.MAX_PRINT_DELAY;
            Utils.slowPrintln("You're in " + r0.getDescription() + ".");
        }
        else
        {
            Utils.slowPrintln(r0.getDescription());
        }

        Utils.slowPrintDescList(r0.interactibles);

        Utils.slowPrintDescList(r0.enemies);
        
        r0.discover();
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
