import java.util.ArrayList;

public class PlayerManager {
    public ArrayList<Player> players = new ArrayList<Player>();

    public void addPlayer(Player p)
    {
        players.add(p);
        p.setManager(this);
    }

    public void clearCurrentRooms()
    {
        for (Player p : players) 
        {
            p.getCurrentRoom().setIsCurrentRoom(false);
        }
    }

    public void setCurrentRooms()
    {
        for (Player p : players) 
        {
            p.getCurrentRoom().setIsCurrentRoom(true);
        }  
    }
}
