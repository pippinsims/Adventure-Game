import java.util.*;

public class Player {
    //TRYING TO SET UP BASE STATS TO CREATE USEFUL STATS SYSTEM
    private int maxHealth = 20;
    private int playerDamage = 1;
    private int playerWisdom = 2;

    enum action {
        NOTHING,
        DOOR,
        INSPECT,
        FIGHT,
        TALK,
        INTERACT,
        CAST
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

        actions.add(action.TALK);
        if (true) // Would set to true once Player inspects language. Placeholder DEV true.
        {
            actions.add(action.CAST);
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

            case TALK:
                str = "Say something";
                break;
                
            case CAST:
                str = "Utilize the power of the ancients";
                break;
            default:
                str = "[Empty Action]";
                break;
        }
        return str;
    }
// NO CLUE HOW TO DO THIS YET, TRYING TO GET PLAYER DEATH SYSTEM UP AND RUNNING
/*     public boolean playerDamage(int damage, int health)
    {
        health -= damage;
        return health <= 0;
    }
   */
}
