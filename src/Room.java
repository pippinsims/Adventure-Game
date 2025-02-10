import java.util.ArrayList;

public class Room {

    private Room[] exits = new Room[10];
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    private Interactible[] interactibles = new Interactible[10];
    private String description = "a bare room";

    public Room(Room[] ex, Interactible[] i, String des)
    {
        exits = ex;
        interactibles = i;
        description = des;
    }

    public Room(String des, Room[] ex)
    {
        description = des;
        exits = ex;
        enemies = null;
        interactibles = null;
    }

    public Room(Room[] ex)
    {
        exits = ex;
        enemies = null;
        interactibles = null;
    }

    public Interactible getInteractible(int i)
    {
        return interactibles != null ? interactibles[i] : null;
    }

    public Room getRoom(int i)
    {
        return exits[i];
    }

    public void setRoom(int i, Room d)
    {
        exits[i] = d;
    }

    public Enemy getEnemy(int i)
    {
        return enemies != null && i < enemies.size() ? enemies.get(i) : null;
    }

    
    public void setEnemy(int i, Enemy e)
    {
        if(enemies != null)
        {
            if(0 <= i && i <= enemies.size() - 1)
                enemies.set(i, e);
            else
                System.err.println("setEnemy fail. Index outside of Array bounds. You are stupid.");
        }
        else 
           System.err.println("setEnemy fail. Array doesn't exist. You are stupid.");
    }

    public void addEnemy(Enemy e)
    {
        enemies.add(e);
    }

    public void slayEnemy(int i)
    {
        if(enemies != null)
        {
            if(0 <= i && i <= enemies.size() - 1)
                enemies.remove(i);
            else
                System.err.println("slayEnemy fail. Index outside of Array bounds. You are stupid.");
        }
        else 
           System.err.println("slayEnemy fail. Array doesn't exist. You are stupid.");
        
    }

    public ArrayList<Enemy> getEnemies()
    {
        return enemies;
    }

    public int getNumExits()
    {
        return exits.length;
    }

    public int getNumEnemies()
    {
        if(enemies != null)
            return enemies.size();
        else
            return 0;
    }

    public int getNumInteractibles()
    {
        if(interactibles != null)
            return interactibles.length;
        else
            return 0;
    }

    public void setInteractible(int i, Interactible in)
    {
        if (interactibles != null)
        {
            if(in != null)
            {
                if(0 <= i && i <= interactibles.length - 1)
                    interactibles[i] = in;
                else
                    System.err.println("setInteractible fail. Index outside of Array bounds. You are stupid.");
            }
            else 
                System.err.println("setInteractible fail. You're trying to put a null into interactibles. You are stupid.");
         }
         else
            System.err.println("setInteractible fail. Array does not exist. You are confusing me.");
    }

    public String getDescription()
    {
        return description + " with " + exits.length + " doors";
    }
}