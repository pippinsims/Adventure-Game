public class Room {

    private Room[] doors = new Room[10];
    private Enemy[] enemies = new Enemy[10];
    private Interactible[] interactibles = new Interactible[10];
    private String description = "a bare room";

    public Room(Room[] d, Enemy[] e, Interactible[] i, String des)
    {
        doors = d;
        enemies = e;
        interactibles = i;
        description = des;
    }

    public Room(String des, Room[] d)
    {
        description = des;
        doors = d;
        enemies = null;
        interactibles = null;
    }

    public Room(Room[] d)
    {
        doors = d;
        enemies = null;
        interactibles = null;
    }

    public Interactible getInteractible(int i)
    {
        return interactibles[i];
    }

    public Room getRoom(int i)
    {
        return doors[i];
    }

    public void setRoom(int i, Room d)
    {
        doors[i] = d;
    }

    public Enemy getEnemy(int i)
    {
        if(enemies == null)
            return null;
        else
            return enemies[i];
    }

    public int getNumRooms()
    {
        return doors.length;
    }

    public String getDescription()
    {
        return description + " with " + doors.length + " doors";
    }
}