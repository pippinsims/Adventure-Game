import java.util.ArrayList;

public class Room {

    private Room[] exits;
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    private ArrayList<Interactible> interactibles = new ArrayList<Interactible>();
    private String description = "a bare room";
    private direction[] dirs;
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
        dirs = new direction[0];
    }

    public Room(String des)
    {
        this();
        description = des;
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

    public ArrayList<Enemy> getEnemies()
    {
        return enemies;
    }

    public ArrayList<Interactible> getInteractibles()
    {
        return interactibles;
    }

    public int getNumExits()
    {
        return exits.length;
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
        Room[] newExits = new Room[exits.length + 1];
        for(int i = 0; i < exits.length; i++)
        {
            newExits[i] = exits[i];
        }
        exits = newExits;
        exits[exits.length - 1] = room2add;
        
        //ADD DIRECTION OF ROOM2ADD TO CURROOM
        direction[] newDirs = new direction[dirs.length + 1];
        for(int i = 0; i < dirs.length; i++)
        {
            newDirs[i] = dirs[i];
        }
        dirs = newDirs;
        dirs[dirs.length - 1] = d;
    
        //ADD EXIT GOING FROM ROOM2ADD TO CURROOM
        newExits = new Room[room2add.exits.length + 1];
        for(int i = 0; i < room2add.exits.length; i++)
        {
            newExits[i] = room2add.exits[i];
        }
        room2add.exits = newExits;
        room2add.exits[room2add.exits.length - 1] = this;

        //ADD DIRECTION OF CURROOM TO ROOM2ADD (COMPLEMENT OF FIRST DIRECTION e.g. N <-> S)
        newDirs = new direction[room2add.dirs.length + 1];
        for(int i = 0; i < room2add.dirs.length; i++)
        {
            newDirs[i] = room2add.dirs[i];
        }
        room2add.dirs = newDirs;
        room2add.dirs[room2add.dirs.length - 1] = complementOf(d);
    }

    direction complementOf(direction d)
    {
        switch (d) 
        {
            case SOUTH:
                return direction.NORTH;

            case WEST:
                return direction.EAST;

            case NORTH:
                return direction.SOUTH;

            case EAST:
                return direction.WEST;

            default:
                return direction.NONCARDINAL;
        }
    }
}