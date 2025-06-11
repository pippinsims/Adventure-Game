package adventuregame;

import adventuregame.interactibles.*;
import adventuregame.interfaces.Interactible;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Environment extends Utils
{
    public static Room r0;
    public static PlayerManager pm = new PlayerManager();
    public static Map<Effect.Type, String> effectDescriptions = new HashMap<>();

    // TODO: make a way to play without slow print
    public static void main(String[] args) 
    {
        //room r0 is the current room
        generateMap();

        pm.addPlayer(new Player());
        Player player1 = pm.players.get(0);

        printIntroduction();

        System.out.println();

        while(pm.players.get(0).getHealth() > 0)
        {
            printInfo();

            System.out.println();

            player1.updateUnit();
            
            System.out.println();

            ArrayList<Enemy> ens = r0.getEnemies();
            for (int i = ens.size() - 1; i > -1; i--) 
            {
                ens.get(i).updateUnit();
                System.out.println();
            }
        }
        
        scanner.close();
    }

    private static void printIntroduction()
    {
        slowPrint("In this land, you're known as ");
        if(pm.players.size() > 0)
        {
            int num = pm.players.size();
            
            for(int i = 0; i < num; i++)
            {
                slowPrintAsList(pm.players.get(i).getName(), num, i);
            }
        }
        slowPrint(" ", 1000);
        slowPrint("Adventure awaits!", 10);
        slowPrint("\n", 200);
    }

    private static void printInfo()
    {
        System.out.println("--Info--");

        if(!r0.getIsDiscovered())
        {
            currentPrintDelay = MAX_PRINT_DELAY;
            slowPrintln("You're in " + r0.getDescription() + ".");
            r0.setIsDiscovered(true);
        }

        ArrayList<Interactible> inters = r0.getInteractibles();
        ArrayList<Enemy> ens = r0.getEnemies();
        int num = inters.size();
        for(int i = 0; i < num + ens.size(); i++)
        {
            //WAIT what if there are two of the exact same interactible?! "There are two red torches on the east wall", "There are two diamond snakes"
            if(i == 0 || i == num)
                slowPrint("There is ");

            if(i < num)
                slowPrintlnAsListWithArticles(inters.get(i).getExposition(), num, i);
            else
                slowPrintlnAsListWithArticles(ens.get(i - num).getRandomDescription(), ens.size(), i - num);
        }

        if(r0.getIsDiscovered())
        {
            currentPrintDelay = 3;
        }
    }

    private static void generateMap()
    {
        loadEffectDescriptions();   

        r0 = new Room("a dimly lit room.\nThere is a faint foul odor...\nThe patchwork on the wall depicts of a redheaded lunatic.\n\"Lord Gareth the Mad.\"", "Chamber");
        r0.setDoorMsg("This room is gifted with");       
        
        Room mossyRuin = new Room("a room with shrooms, a shroom room if you will.\n       \t\t\t\tAre you afraid of large spaces? Becausesss there's a mush-a-room if you catch my drift,", "Mossy Ruin");
        mossyRuin.setDoorMsg("oh, and this room is cursed with");
        mossyRuin.getEnemies().add(new Enemy(2, new Inventory(2), 1, 99999, "Mushroom"));
        
        r0.appendRoom(mossyRuin, Room.direction.NORTH);
        r0.appendRoom(new Room(), Room.direction.WEST);
        r0.appendRoom(new Room(), Room.direction.EAST);

        Room treasureRoom = new Room("a room filled to the brim in a plentious manner. Old swords and worn chalices adorned with gems sparkle, and set your heart in motion.", "Treasure Room");
        try 
        {
            r0.getRoom(2).appendRoom(treasureRoom, Room.direction.SOUTH);
        } 
        catch (Exception e) 
        {
            System.err.println("trying to get a room that doesn't exist");
        }

        for (int i = 0; i < 4; i++)
        {
            r0.getEnemies().add(new Enemy(3));
        }

        r0.getInteractibles().add(new TorchInteractible(r0, WallEntity.Wall.EAST));
        r0.getInteractibles().add(new TorchInteractible(r0, WallEntity.Wall.WEST));
        r0.getInteractibles().add(new ViewablePicture("mad_king.txt", WallEntity.Wall.WEST, "patchwork depiction", "Lord Gareth the Mad", r0));
    }

    private static void loadEffectDescriptions() {
        Effect.effectDescriptions.put(Effect.Type.FIRE, "BURNINGNESS");
        Effect.effectDescriptions.put(Effect.Type.PSYCHSTRIKE, "strong vexation of mind");
    }

    public static void playerAttackEnemy(int index, Damage d)
    {
        //MAKE THERE BE DIFFERENT REACTIONS TO BEING ATTACKED
        Enemy e = r0.getEnemies().get(index);
        if(e.receiveDamage(d.getValue(), d.getType()) == Effectable.EffectUpdateResult.DEATH)
        {
            slowPrintln("You have murdered the " + e.getRandomDescription(), 250); // so unnecessary lol
            e.death();
        }
    }
}
