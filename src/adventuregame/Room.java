package adventuregame;

import adventuregame.abstractclasses.Describable;
import adventuregame.interactibles.WallEntity;
import adventuregame.interactibles.wallentities.Door;

import java.util.ArrayList;

public class Room extends Describable
{

    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Interactible> interactibles = new ArrayList<>();
    private ArrayList<Door> doors = new ArrayList<>();
    private String description = "a bare room";
    private String doormsg = "This room has";
    private boolean isDiscovered = false;
    private boolean isCurrentRoom = false;
    private String name = "Barren";

    enum direction {
        NORTH,
        EAST,
        SOUTH,
        WEST,
        NONCARDINAL
    }

    public Room()
    {

    }

    public Room(String des, String n)
    {
        this();
        description = des;
        name = n;
    }

    public void setIsDiscovered(boolean d)
    {
        isDiscovered = d;
    }

    public boolean getIsDiscovered()
    {
        return isDiscovered;
    }

    public boolean getIsCurrentRoom()
    {
        return isCurrentRoom;
    }

    public void setIsCurrentRoom(boolean c)
    {
        isCurrentRoom = c;
    }

    public void setDoorMsg(String s)
    {
        doormsg = s;
    }

    public ArrayList<Interactible> getInteractibles()
    {
        return interactibles;
    }

    public void addDoor(Door d)
    {
        doors.add(d);
        interactibles.add(d);
    }

    //TODO: add wall material, add a familiar description once it's a familiar room
    public String getDescription()
    {
        String str = description + "\n" + doormsg;

        int[] directionCounts = new int[direction.values().length];

        for(int i = 0; i < directionCounts.length; i++)
        {
            directionCounts[i] = countDoorsOf(direction.values()[i]);
        }

        boolean precursedByZeros = true;
        for (int i = 0; i < directionCounts.length; i++)
        {
            if(directionCounts[i] != 0)
            {       
                if(isFollowedByZeros(directionCounts, i) && !precursedByZeros)
                    str += " and";

                str += " " + getDirectionDescription(direction.values()[i]);                

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

    private String getDirectionDescription(direction dir)
    {
        String article = dirArticle(dir);

        return article + " " + dirToString(dir) + " door" + ((article.charAt(0) == 'a') ? "" : "s");
    }

    private String dirArticle(direction dir)
    {
        int count = countDoorsOf(dir);
        if(count == 1)
        {
            if(dir == direction.EAST)
                return "an";
            else
                return "a";
        }
        else
            return count + "";
    }

    private int countDoorsOf(direction dir)
    {
        int count = 0;
        for (Door door : doors) 
        {
            if(wallToDir(door.getWall(this)) == dir)
                count++;
        }
        return count;
    }

    private direction wallToDir(WallEntity.Wall wall)
    {
        switch (wall) {
            case NORTH:
                return direction.NORTH;
            case SOUTH:
                return direction.SOUTH;
            case EAST:
                return direction.EAST;
            case WEST:
                return direction.WEST;
            case NONE:
                return direction.NONCARDINAL;
            default:
                throw new RuntimeException("wallToDir failure");
        }
    }

    private String dirToString(direction dir)
    {   
        switch(dir)
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

    @Override
    public String getName() 
    {
        return name;
    }

    @Override
    public String getPluralDescription() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPluralDescription'");
    }
}