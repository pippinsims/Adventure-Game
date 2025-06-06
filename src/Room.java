import java.util.ArrayList;

public class Room {

    private Room[] exits = new Room[10];
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    private Interactible[] interactibles = new Interactible[10];
    private String description = "a bare room";
    private direction[] dirs = new direction[exits.length];
    private String doormsg = "This room has";

    enum direction {
        NORTH,
        EAST,
        SOUTH,
        WEST,
        NONCARDINAL
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

    public Room(Room[] ex, String des, Interactible[] i)
    {
        exits = ex;
        interactibles = i;
        description = des;
    }

    public Room(Room[] ex, String des)
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

    public void setDoorMsg(String s)
    {
        doormsg = s;
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
        String str = description + "\n" + doormsg;

        int[] directionCounts = new int[direction.values().length];

        for(int i = 0; i < directionCounts.length; i++)
        {
            directionCounts[i] = countDoors(direction.values()[i]);
        }

        boolean precursedByZeros = true;
        for (int i = 0; i < directionCounts.length; i++)
        {
            if(directionCounts[i] != 0)
            {
                if(isFollowedByZeros(directionCounts, i) && !precursedByZeros)
                    str += " and";
                
                str += " " + getDirDes(direction.values()[i]);

                if (!isFollowedByZeros(directionCounts, i))
                    str += ",";

                precursedByZeros = false;
            }
        }
        return str;
    }

    /**
     * Checks if all items after item at index i in int array arr are 0.
     * @param arr The array to check.
     * @param i The starting point index.
     * @return True if all items after item at i are 0.
     */
    private boolean isFollowedByZeros(int[] arr, int i)
    {
        for(int j = i + 1; j < arr.length; j++)
        {
            if(arr[j] != 0)
                return false;
        }

        return true;
    }

    private direction getDoorDir(int i)
    {
        return dirs[i];
    }

    public String getDoorDes(int i)
    {
        String str = getDoorDesArticle(i);
        
        str += " " + dirToString(getDoorDir(i)) + " door";

        return str;
    }

    public String getDirDes(direction d)
    {
        String str = getDoorDesAmount(d);
        
        str += " " + dirToString(d) + " door";

        if(countDoors(d) > 1)
            str += "s";

        return str;
    }

    private int countDoors(direction d)
    {
        int sum = 0;
        for(int i = 0; i < dirs.length; i++)
        {
            if(dirs[i] == d)
                sum++;
        }
        return sum;
    }

    private String getDoorDesArticle(int i)
    {
        direction d = getDoorDir(i);
        if(countDoors(d) == 1)
            return "the";
        else
        {
            if(getDoorDir(i) == direction.EAST)
                return "an";
            else
                return "a";
        }
    }

    private String getDoorDesAmount(direction d)
    {
        if(countDoors(d) == 1)
        {
            if(d == direction.EAST)
                return "an";
            else
                return "a";
        }
        else
            return countDoors(d) + "";
    }

    private String dirToString(direction d)
    {   
        switch(d)
        {
            case NORTH:
                return "north-facing";
            case EAST:
                return "east-facing";
            case SOUTH:
                return "south-facing";
            case WEST: 
                return "west-facing";
            case NONCARDINAL:
                return "non-cardinally-directed";
            default:
                throw new AssertionError();
        }
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