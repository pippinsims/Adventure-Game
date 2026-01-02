package adventuregame;

import java.util.ArrayList;

import adventuregame.abstractclasses.Enemy;
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
            for(Player p : allPlayers) if(!Utils.contains(playerRooms, p.getRoom())) playerRooms.add(p.getRoom());

            ArrayList<NonPlayer> nonpCache = new ArrayList<>();

            //all loaded enemies or in player rooms
            for(Room r : playerRooms)
            {
                if(!r.enemies.isEmpty())
                {
                    curRoom = r;
                    for(Enemy e : new ArrayList<>(r.enemies))
                    {
                        nonpCache.add(e);
                        cur = e;
                        e.updateUnit();
                        System.out.println();
                        if(r.players.isEmpty()) break;
                    }
                }
            }

            //all NPCs in player rooms
            for(Room r : playerRooms) 
            {
                if(!r.NPCs.isEmpty())
                {
                    curRoom = r;
                    for(NonPlayer n : new ArrayList<>(r.NPCs))
                    {
                        nonpCache.add(n);
                        cur = n;
                        n.updateUnit();
                        System.out.println();
                        if(r.players.isEmpty()) break;
                    }
                }
            }

            //all loaded NonPlayers
            for(NonPlayer n : loaded)
            {
                if(!Utils.contains(nonpCache, n))
                {
                    cur = n;
                    curRoom = n.getRoom();
                    n.updateUnit();
                    System.out.println();
                }
            }

            Utils.slowPrintln("\t\t\t\t\t\t\t\t--Round End--");
        }
    }

    protected final static void addPlayer(Player p)
    {
        allPlayers.add(p);
        curRoom.add(p); //TODO for now, all players spawn in curRoom
    }

    public static void kill(Effectable e)
    {
        Unit u = (Unit)e; //currently theres no non-unit effectables
        if(u instanceof Enemy)
            if(isLaur) Utils.slowPrintln("You've murdered " + u.getName(), 0/*200*/);
        else if(u instanceof Player)
        {
            Utils.slowPrintln("you died.");
            allPlayers.remove(u);
        }
        else if(u instanceof NonPlayer)
            loaded.remove(u);
        u.getRoom().remove(u);
        u.setRoom(null);
        Utils.slowPrintln(u.getDeathMessage() + "------", 0/*200*/);
    }

    protected final static void printIntroduction()
    {
        Utils.slowPrint("In this land, you're known as ");
        if(Game.allPlayers.size() > 0)
        {
            int num = Game.allPlayers.size();
            
            for(int i = 0; i < num; i++)
            {
                Utils.slowPrintAsList(Game.allPlayers.get(i).getName(), num, i);
            }
        }
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

        Utils.slowPrintDescList(r.enemies);

        Utils.slowPrintNameList(r.NPCs); //TODO integrate and test NPCs fully

        ArrayList<Player> p = new ArrayList<>(r.players);
        p.remove(cur);
        Utils.slowPrintNameList(p);
        
        r.discover();
        Utils.currentPrintDelay = 3;
    }
}