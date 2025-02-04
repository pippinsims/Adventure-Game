import java.util.*;

public class Player {
    
    enum action {
        NOTHING,
        DOOR,
        INSPECT
    }

    public List<action> actions = new ArrayList<action>();

    public void setActions(Room curRoom)
    {
        actions.clear();
        actions.add(action.NOTHING);

        for(int i = 0; i < curRoom.getNumRooms(); i++)
        {
            actions.add(action.DOOR);
        }

        actions.add(action.INSPECT);
    }

    public String[] getActionDescriptions()
    {   
        String[] actionDescriptions = new String[actions.size()];
        int doorNumber = 0;
        for(int i = 0; i < actionDescriptions.length; i++)
        {
            actionDescriptions[i] = actionToString(actions.get(i)) + ((actions.get(i) == action.DOOR) ? ++doorNumber : "");
            /*if(actions.get(i) == action.DOOR)
               {
                   doorNumber++;
                   actionDescriptions[i] += doorNumber;
               }*/
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
        
            default:
                str = "[Empty Action]";
                break;
        }
        return str;
    }

    public void performAction(int i)
    {
        //PERFORM SOME ACTION
    }
}
