package adventuregame;

import java.util.ArrayList;

import adventuregame.abstractclasses.NonPlayer;
import adventuregame.abstractclasses.Unit;

public class Game
{
    public static Room curRoom;
    public static Unit cur;
    public static ArrayList<Player> allPlayers = new ArrayList<>();
    public static boolean isLaur;
    public static ArrayList<NonPlayer> loaded = new ArrayList<>();

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

    protected final static void run()
    {
        while(!allPlayers.isEmpty())
        {
            //all players
            for(Player p : new ArrayList<>(allPlayers))
            {
                cur = p; 
                curRoom = p.getRoom(); 
                isLaur = p.getName().equals("Laur");
                p.updateUnit();
                System.out.println();
            }
            cur = null; isLaur = false;

            ArrayList<Room> playerRooms = new ArrayList<>();
            for(Player p : allPlayers) if(!playerRooms.contains(p.getRoom())) playerRooms.add(p.getRoom());

            ArrayList<NonPlayer> nonpCache = new ArrayList<>();

            //all npcs in player rooms by room
            for(Room r : playerRooms)
            {
                if(!r.NPCs.isEmpty())
                {
                    curRoom = r;
                    //aggressive go first
                    for(NonPlayer n : new ArrayList<>(r.NPCs)) for(Unit u : r.all()) if(n.enemies.contains(u))
                    {
                        nonpCache.add(n);
                        cur = n;
                        n.updateUnit();
                        System.out.println();
                        if(r.players.isEmpty()) break;
                    }
                    
                    //then peaceful
                    for(NonPlayer n : new ArrayList<>(r.NPCs)) if(!nonpCache.contains(n))
                    {
                        nonpCache.add(n);
                        cur = n;
                        n.updateUnit();
                        System.out.println();
                        if(r.players.isEmpty()) break;
                    }
                }
            }

            //all npcs in rooms that haven't been updated yet, but contain loaded npcs, by room
            for(NonPlayer n : loaded) for(NonPlayer n1 : new ArrayList<>(n.getRoom().NPCs)) if(!nonpCache.contains(n1))
            {
                nonpCache.add(n1);
                cur = n1;
                curRoom = n1.getRoom();
                n1.updateUnit();
                System.out.println();
            }
            
            Utils.slowPrintln("\t\t\t\t\t\t\t\t--Round End--");
        }
    }

    protected final static void addPlayer(Player p, Room r)
    {
        allPlayers.add(p);
        r.add(p);
    }

    public static void kill(Effectable e)
    {
        Unit u = (Unit)e; //currently theres no non-unit effectables
        if(u instanceof Player)
        {
            Utils.slowPrintln("you died.");
            allPlayers.remove(u);
        }
        else if(u instanceof NonPlayer)
        {
            if(isLaur) Utils.slowPrintln("You've murdered " + u.getName(), 0/*200*/);
            loaded.remove(u);
        }
        u.getRoom().remove(u);
        u.setRoom(null);
        Utils.slowPrintln(u.getDeathMessage() + "------", 0/*200*/);
    }

    protected final static void printIntroduction()
    {
        Utils.slowPrint("In this land, you're known as ");

        int num = Game.allPlayers.size(); 
        if(num > 0) for(int i = 0; i < num; i++) Utils.slowPrintAsList(Game.allPlayers.get(i).getName(), num, i); 
        
        Utils.slowPrint(" ", 1000);
        Utils.slowPrint("Adventure awaits!", 10);
        Utils.slowPrint("\n", 200);
    }

    public static void printInfo(Room r, boolean peek)
    {
        if(!peek) Utils.slowPrintln("--Info--");

        if(!r.isFamiliarTo(cur))
        {
            Utils.currentPrintDelay = Utils.MAX_PRINT_DELAY;
            Utils.slowPrintln("You" + (peek ? " see " : "'re in ") + r.getDescription() + ".");
        }
        else
        {
            Utils.slowPrintln(r.getDescription());
        }

        Utils.slowPrintDescList(r.interactibles);

        if(isLaur) Utils.slowPrintDescList(r.NPCs);
        else Utils.slowPrintNameList(r.NPCs);
        
        ArrayList<Player> p = new ArrayList<>(r.players);
        p.remove(cur);
        Utils.slowPrintNameList(p);
        
        r.discover();
        Utils.currentPrintDelay = 3;
    }
}