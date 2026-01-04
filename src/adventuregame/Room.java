package adventuregame;

import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Enemy;
import adventuregame.abstractclasses.NonPlayer;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.wallinteractibles.Door;

import java.util.ArrayList;

public class Room extends Describable
{
    public static ArrayList<Room> rooms = new ArrayList<>();

    public  ArrayList<Enemy>        enemies       = new ArrayList<>();
    public  ArrayList<Player>       players       = new ArrayList<>();
    public  ArrayList<NonPlayer>    NPCs          = new ArrayList<>();
    public  ArrayList<Interactible> interactibles = new ArrayList<>();
    private ArrayList<Door>         doors         = new ArrayList<>();
    private ArrayList<Player>       familiars     = new ArrayList<>();
    private String familiarDescription;

    private static int roomnum = 0;

    public Room()
    {
        name = "Bare";
        description = "a bare room";
        descMap.put("Laur", "an... empty place");
        familiarDescription = "Bare room.";
        rooms.add(this);
        name = "room"+roomnum++;
    }

    public Room(String d, String l, String f, String n)
    {
        description = d;
        descMap.put("Laur", l);
        familiarDescription = f;
        name = n;
        rooms.add(this);
        name = "room"+roomnum++;
    }

    public Room(String d, String f, String n)
    {
        description = d;
        familiarDescription = f;
        name = n;
        rooms.add(this);
        name = "room"+roomnum++;
    }

    public Room(String d, String n)
    {
        description = d;
        familiarDescription = n + ".";
        name = n;
        rooms.add(this);
        name = "room"+roomnum++;
    }

    public ArrayList<NonPlayer> allNPCs()
    {
        ArrayList<NonPlayer> all = new ArrayList<>();
        all.addAll(enemies);
        all.addAll(NPCs);

        return all;
    }

    public ArrayList<Unit> all()
    {
        ArrayList<Unit> all = new ArrayList<>();
        all.addAll(enemies);
        all.addAll(NPCs);
        all.addAll(players);

        return all;
    }

    public void discover()
    {
        descMap.put(Game.cur.getName(), familiarDescription);
    }

    public boolean doFirstDialogue()
    {
        ArrayList<NonPlayer> all = new ArrayList<>(enemies);
        all.addAll(NPCs);
        for(NonPlayer n : all) if(n.dialogues.getFirst() != null) 
        { 
            n.dialogues.getFirst().next();
            n.dialogues.remove(0); 
            return true;
        }
        return false;
    }

    public boolean isFamiliarTo(Unit u) { return familiars.contains(u); }

    public void add(Interactible i)
    {
        if(i instanceof Door) doors.add((Door)i);
        interactibles.add(i);
    }

    public void add(Unit u)
    {
        if(u instanceof Player) 
            players.add((Player)u); 
        else if(u instanceof Enemy) 
            enemies.add((Enemy)u);
        else
            NPCs.add((NonPlayer)u);
        u.setRoom(this);
    }

    public boolean remove(Interactible i)
    {
        return interactibles.remove(i);
    }

    public boolean remove(Unit u)
    {
        if(u instanceof Player) return players.remove(u); 
        else if(u instanceof Enemy) return enemies.remove(u);
        else return NPCs.remove(u);
    }

    public ArrayList<Door> getDoors()
    {
        return doors;
    }

    public void updateDoors()
    {
        for (Door door : doors) door.setWall(this);
    }

    public ArrayList<Interactible> getUniqueInters()
    {
        ArrayList<Interactible> inters = new ArrayList<>();
        for (Interactible i : interactibles) if(!inters.contains(i)) inters.add(i); 
        
        return inters;
    }
}