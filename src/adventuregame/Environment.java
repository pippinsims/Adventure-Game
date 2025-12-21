package adventuregame;

import adventuregame.interactibles.GoldenPotInteractible;
import adventuregame.interactibles.WallEntity.Wall;
import adventuregame.interactibles.wallentities.*;
import adventuregame.Utils.Tuple;
import adventuregame.abstractclasses.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Environment
{
    public static Room r0;
    public static ArrayList<Player> allPlayers = new ArrayList<>();
    public static Player curPlayer;
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

                ArrayList<Player> ps = r0.players;
                Collections.reverse(ps);
                for (int i = ps.size() - 1; i >= 0; i--)
                {
                    curPlayer = ps.get(i);
                    ps.get(i).updateUnit();
                    System.out.println();
                }
                Collections.reverse(ps);

                if(r0.players.isEmpty())
                {
                    playerRooms.remove(r0);
                    r--;
                    continue;
                }
                
                ArrayList<Enemy> ens = r0.enemies;
                Collections.reverse(ens);
                for (int i = ens.size() - 1; i >= 0; i--)
                {
                    ens.get(i).updateUnit();
                    System.out.println();
                }
                Collections.reverse(ens);
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
        treasureRoom.add(new GoldenPotInteractible(treasureRoom));

        new Door(joiner1, treasureRoom, Wall.SOUTH);

        for (int i = 0; i < 4; i++) end.add(new Enemy(3));

        new TorchInteractible(end, Wall.EAST);
        new TorchInteractible(end, Wall.WEST);
        new TorchInteractible(end, Wall.WEST);

        new ViewablePicture(end, "mad_king.txt", Wall.WEST, "patchwork depiction", "Lord Gareth the Mad");
        
        Room hall = new Room("A long hall with many cells", "PrisonHall");
        String celld = "A barren, empty, disgusting prison cell", celln = "Cell";
        r0 = new Room(celld, celln);
        for (int i = 0; i < 7; i++) //TODO DOOR DESCRIPTIONS JUST ABSOLUTELY DON'T WORK WHEN TOO MANY DOORS
        {
            new Door(hall, new Room(celld, celln), Wall.EAST);
            if(i == 0) new Door(hall, r0, Wall.WEST);
            else new Door(hall, new Room(celld, celln), Wall.WEST);
        }

        new Door(hall, end, Wall.NORTH);

        addPlayer(new Player());
        addPlayer(new Player("Nuel"));
        addPlayer(new Player("Valeent"));
        addPlayer(new Player("Veili"));
    }

    public static void printInfo()
    {
        System.out.println("--Info--");

        if(!r0.getIsDiscovered())
        {
            Utils.currentPrintDelay = Utils.MAX_PRINT_DELAY;
            Utils.slowPrintln("You're in " + r0.getDescription() + ".");
            r0.setIsDiscovered(true);
        }

        Utils.slowPrintDescList(r0.interactibles);

        Utils.slowPrintDescList(r0.enemies);

        if(r0.getIsDiscovered())
        {
            Utils.currentPrintDelay = 3;
        }
    }

    public static void kill(Effectable e)
    {
        if(e instanceof Enemy)
        {
            ArrayList<Enemy> all = ((Enemy)e).getRoom().enemies;
            for(int i = 0; i < all.size(); i++) if(all.get(i) == e) all.remove(i); //normal remove method compares by description
        }
        else if(e instanceof Player)
        {
            Utils.slowPrintln("you died.");
            ((Player)e).getRoom().players.remove(e);
            allPlayers.remove(e);
        }

        //prints twice on effect death
        Utils.slowPrintln(((Unit)e).getDeathMessage() + "------", 0/*200*/);
    }
}
