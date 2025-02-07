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

    //what if "enemies" is null or i wrong?
    public void setEnemy(int i, Enemy e)
    {
        enemies.set(i, e);
    }

    public void addEnemy(Enemy e)
    {
        enemies.add(e);
    }

    //what if "enemies" is null or i wrong?
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

    //what if "enemies" is null?
    public int getNumEnemies()
    {
        return enemies.size();
    }

    //what if "interactibles" is null?
    public int getNumInteractibles()
    {
        return interactibles.length;
    }

    //what if "interactibles" is null?
    public void setInteractible(int i, Interactible in)
    {
        interactibles[i] = in;
    }

    public String getDescription()
    {
        return description + " with " + exits.length + " doors";
    }
}