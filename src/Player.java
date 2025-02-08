import java.util.*;

public class Player {
    
    enum action {
        NOTHING,
        DOOR,
        INSPECT,
        FIGHT,
        TALK,
        INTERACT
    }

    public List<action> actions = new ArrayList<action>();

    public void setActions(Room curRoom)
    {
        actions.clear();
        actions.add(action.NOTHING);

        if (curRoom.getRoom(0) != null)
        {
            actions.add(action.DOOR);
        }

        actions.add(action.INSPECT);

        if (curRoom.getNumEnemies() != 0)
        {
            actions.add(action.FIGHT);
        }
    }

    public String[] getActionDescriptions()
    {   
        String[] actionDescriptions = new String[actions.size()];
       
        for(int i = 0; i < actionDescriptions.length; i++)
        {
            actionDescriptions[i] = actionToString(actions.get(i));
        }
        return actionDescriptions;
    }

    private String actionToString(action a)
    {
        String str;
        switch (a) {
            case NOTHING:
                str = "Do nothing";
                break;
            
            case DOOR:
                str = "Try door ";
                break;
            
            case INSPECT:
                str = "Inspect your surroundings";
                break;
            case FIGHT:
                str = "It's kill or be killed.";
                break;
            default:
                str = "[Empty Action]";
                break;
        }
        return str;
    }

   
}
