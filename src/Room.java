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
        return interactibles[i];
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
        if(i >= enemies.size())
            return null;
        else
            return enemies.get(i);
    }

    public void setEnemy(int i, Enemy e)
    {
        enemies.set(i, e);
    }

    public void addEnemy(Enemy e)
    {
        enemies.add(e);
    }

    public void slayEnemy(int i)
    {
        enemies.remove(i);
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
        return enemies.size();
    }

    public int getNumInteractibles()
    {
        return interactibles.length;
    }

    public void setInteractible(int i, Interactible in)
    {
        interactibles[i] = in;
    }

    public String getDescription()
    {
        return description + " with " + exits.length + " doors";
    }
}