package adventuregame;

import adventuregame.interactibles.GoldenPotInteractible;
import adventuregame.interactibles.WallEntity.Wall;
import adventuregame.interactibles.wallentities.Door;
import adventuregame.interactibles.wallentities.TorchInteractible;
import adventuregame.interactibles.wallentities.ViewablePicture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Environment extends Utils
{
    public static Room r0;
    public static ArrayList<Player> allPlayers = new ArrayList<>();
    public static Player curPlayer;
    public static Map<Effect.Type, String> effectDescriptions = new HashMap<>();
    private static ArrayList<Room> playerRooms = new ArrayList<>();

    public static void main(String[] args) 
    {
        //room r0 is the current room
        generateMap();

        printIntroduction();

        System.out.println();

        while(!allPlayers.isEmpty())
        {
            for (Player p : allPlayers) 
            {
                if(!playerRooms.contains(p.getRoom()))
                    playerRooms.add(p.getRoom());    
            }

            //TODO i don't like all these weird backwards forloops, but they are there to stop concurrentmodification errors
            for (int j = playerRooms.size() - 1; j > -1; j--) 
            {
                r0 = playerRooms.get(j);

                ArrayList<Player> ps = r0.players;
                for (int i = ps.size() - 1; i > -1; i--) 
                {
                    curPlayer = ps.get(i);
                    ps.get(i).updateUnit();
                    System.out.println();
                }

                if(r0.players.isEmpty())
                {
                    playerRooms.remove(r0);
                    continue;
                }
                
                ArrayList<Enemy> ens = r0.enemies;
                for (int i = ens.size() - 1; i > -1; i--) 
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
        loadEffectDescriptions();   

        r0 = new Room("a dimly lit room.\nThere is a faint foul odor...\nThe patchwork on the wall depicts of a redheaded lunatic.\n\"Lord Gareth the Mad.\"", "Chamber");
        r0.setDoorMsg("This room is gifted with");       
        
        Room mossyRuin = new Room("a room with shrooms, a shroom room if you will.\n       \t\t\t\tAre you afraid of large spaces? Becausesss there's a mush-a-room if you catch my drift,", "Mossy Ruin");
        mossyRuin.setDoorMsg("oh, and this room is cursed with");
        mossyRuin.enemies.add(new Enemy(2, new Inventory(2), 1, 99999, "Mushroom"));
        
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

        //TODO make Nuel percieve the world in a normal way (rewrite all the output so its normal but save the weird for Laur)
        addPlayer(new Player());
        addPlayer(new Player("Nuel"));

        new GoldenPotInteractible(treasureRoom);
    }

    private static void loadEffectDescriptions() 
    {
        Effect.effectDescriptions.put(Effect.Type.FIRE, "BURNINGNESS");
        Effect.effectDescriptions.put(Effect.Type.PSYCHSTRIKE, "strong vexation of mind");
    }

    public static void playerAttackEnemy(int index, Damage d)
    {
        //TODO MAKE THERE BE DIFFERENT REACTIONS TO BEING ATTACKED
        Enemy e = r0.enemies.get(index);
        if(e.receiveDamage(d.getValue(), d.getType()) == Effectable.EffectUpdateResult.DEATH)
        {
            slowPrintln("You have murdered the " + e.getRandomDescription(), 250); // so unnecessary lol
            e.death();
        }
    }
}
