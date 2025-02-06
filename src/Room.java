public class Room {

    private Room[] exits = new Room[10];
    private Enemy[] enemies = new Enemy[10];
    private Interactible[] interactibles = new Interactible[10];
    private String description = "a bare room";

    public Room(Room[] ex, Enemy[] e, Interactible[] i, String des)
    {
        exits = ex;
        enemies = e;
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
        if(enemies == null)
            return null;
        else
            return enemies[i];
    }

    public void setEnemy(int i, Enemy e)
    {
        enemies[i] = e;
    }

    public Enemy[] getEnemies()
    {
        return enemies;
    }

    public int getNumExits()
    {
        return exits.length;
    }

    public int getNumEnemies()
    {
        return enemies.length;
    }

    public int getNumInteractibles()
    {
        return interactibles.length;
    }

    public String getDescription()
    {
        return description + " with " + exits.length + " doors";
    }
}