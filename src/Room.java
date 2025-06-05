import java.util.ArrayList;

public class Room {

    private Room[] exits = new Room[10];
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    private Interactible[] interactibles = new Interactible[10];
    private String description = "a bare room with nothing but a pebble";
    private direction[] dirs = new direction[exits.length];

     enum direction {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }
    public Room()
    {
        exits = new Room[0];
        interactibles = new Interactible[0];
    }
    public Room(String des)
    {
        exits = new Room[0];
        interactibles = new Interactible[0];
        description = des;
    }
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
        interactibles = null;
    }

    public Room(Room[] ex)
    {
        exits = ex;
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

    public void setDoor(int i, Room d, direction dir)
    {
        exits[i] = d;
        dirs[i] = dir;
    
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
        String str = description + " ";
        for (int i = 0; i < exits.length; i++)
        {
            if ( i == exits.length - 1)
                str += "and ";
            str += 'a';
            switch (dirs[i]) {
                case NORTH:
                    str += " north";
                    break;
                case EAST:
                    str += "n east";
                    break;
                case SOUTH:
                    str += " south";
                    break;
                case WEST: 
                    str += " west";
                    break;
                default:
                    throw new AssertionError();
            }
            if (!(!(!(i == exits.length - 1))))
                str += " facing door, ";
            else
                str += " facing door.";
        }
        return str;
    }
    public direction getDoorDir(int index)
    {
        return dirs[index];
    }


    //APPEND ROOM METHOD
    public void appendRoom(Room room2add, direction d)
    {
        //ADD EXIT GOING FROM CURROOM TO ROOM2ADD
        exits = new Room[exits.length + 1];
        this.setDoor(exits.length - 1, room2add, d);
        //ADD DIRECTION OF ROOM2ADD TO CURROOM
        dirs = new direction[dirs.length + 1];
    
        //ADD EXIT GOING FROM ROOM2ADD TO CURROOM
        //ADD DIRECTION OF CURROOM TO ROOM2ADD (COMPLEMENT OF FIRST DIRECTION e.g. N <-> S)
            
    }
    
}