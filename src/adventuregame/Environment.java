package adventuregame;

import adventuregame.abstractclasses.Describable;
import adventuregame.interactibles.GoldenPotInteractible;
import adventuregame.interactibles.WallEntity.Wall;
import adventuregame.interactibles.wallentities.Door;
import adventuregame.interactibles.wallentities.TorchInteractible;
import adventuregame.interactibles.wallentities.ViewablePicture;

import java.util.ArrayList;
import java.util.Map;

public class Environment extends Utils
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

            for (int j = 0; j < playerRooms.size(); j++)
            {
                r0 = playerRooms.get(j);

                ArrayList<Player> ps = r0.players;
                for (int i = ps.size() - 1; i >= 0; i--) //TODO the fact that this is descending messes up player order, fix it
                {
                    curPlayer = ps.get(i);
                    ps.get(i).updateUnit();
                    System.out.println();
                }

                if(r0.players.isEmpty())
                {
                    playerRooms.remove(r0);
                    j--;
                    continue;
                }
                
                ArrayList<Enemy> ens = r0.enemies;
                for (int i = ens.size() - 1; i >= 0; i--) //backwards to account for removals if an enemy dies
                {
                    ens.get(i).updateUnit();
                    System.out.println();
                }
            }

            System.out.println("--Round End--");
        }
        
        scanner.close();
    }

    private static void printIntroduction()
    {
        slowPrint("In this land, you're known as ");
        if(allPlayers.size() > 0)
        {
            int num = allPlayers.size();
            
            for(int i = 0; i < num; i++)
            {
                slowPrintAsList(allPlayers.get(i).getName(), num, i);
            }
        }
        slowPrint(" ", 1000);
        slowPrint("Adventure awaits!", 10);
        slowPrint("\n", 200);
    }

    private static void addPlayer(Player p)
    {
        allPlayers.add(p);
        r0.players.add(p);
    }

    private static void generateMap()
    {
        r0 = new Room("a dimly lit room.\nThere is a faint foul odor...\nThe patchwork on the wall depicts of a redheaded lunatic.\n\"Lord Gareth the Mad.\"", "Chamber");
        
        Room mossyRuin = new Room("a room with shrooms, a shroom room if you will.\n       \t\t\t\tAre you afraid of large spaces? Becausesss there's a mush-a-room if you catch my drift,", "Mossy Ruin");
        mossyRuin.enemies.add(new Enemy(2, new Inventory(2), 1, 99999, "Mushroom Monster"));
        
        new Door(r0, mossyRuin, Wall.NORTH);
        new Door(r0, new Room(), Wall.WEST);
        Room joiner1 = new Room();
        new Door(r0, joiner1, Wall.EAST);

        Room treasureRoom = new Room("a room filled to the brim in a plentious manner. Old swords and worn chalices adorned with gems sparkle, and set your heart in motion.", "Treasure Room");
        
        new Door(joiner1, treasureRoom, Wall.SOUTH);

        for (int i = 0; i < 4; i++)
        {
            r0.enemies.add(new Enemy(3));
        }

        new TorchInteractible(r0, Wall.EAST);
        new TorchInteractible(r0, Wall.WEST);
        new TorchInteractible(r0, Wall.WEST);

        new ViewablePicture(r0, "mad_king.txt", Wall.WEST, "patchwork depiction", "Lord Gareth the Mad");

        addPlayer(new Player());
        addPlayer(new Player("Nuel"));

        treasureRoom.interactibles.add(new GoldenPotInteractible(treasureRoom));
    }

    public static void playerAttackEnemy(int index, Damage d)
    {
        //TODO MAKE THERE BE DIFFERENT REACTIONS TO BEING ATTACKED
        Enemy e = r0.enemies.get(index);
        if(e.receiveDamage(d) == Effectable.EffectUpdateResult.DEATH)
        {
            slowPrintln("You have murdered the " + e.getRandomDescription(), 1/*250*/); // so unnecessary lol
            e.death();
        }
    }

    public static void printInfo()
    {
        System.out.println("--Info--");

        if(!r0.getIsDiscovered())
        {
            currentPrintDelay = MAX_PRINT_DELAY;
            slowPrintln("You're in " + r0.getDescription() + ".");
            r0.setIsDiscovered(true);
        }

        ArrayList<Describable> dess = new ArrayList<>();
        ArrayList<Interactible> inters = r0.interactibles;
        for (Interactible i : inters) { dess.add(i); }
        Utils.slowPrintDescList(dess);

        dess.clear();
        ArrayList<Enemy> ens = r0.enemies;
        for (Enemy e : ens) { dess.add(e); }
        Utils.slowPrintDescList(dess);

        if(r0.getIsDiscovered())
        {
            currentPrintDelay = 3;
        }
    }
}
