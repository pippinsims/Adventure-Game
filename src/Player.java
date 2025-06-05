import java.util.*;

//PLAYER NEEDS INTERACTIONUTIL 
public class Player implements Animate{
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

        if(curRoom.getNumInteractibles() > 0)
        {
            actions.add(action.INSPECT);
        }

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

    public boolean performAction(int i)
    {   
        Room curRoom = Environment.r0;
        switch(actions.get(i))
        {
            case DOOR:
                String[] doornames = new String[curRoom.getNumExits()];
                for (int j = 0 ; j < doornames.length ; j++)
                {
                    doornames[j] = "Try a";
                    switch (curRoom.getDoorDir(j)) 
                    {
                        case NORTH:
                            doornames[j] += " north";
                            break;
                        case EAST:
                            doornames[j] += "n east";
                            break;
                        case SOUTH:
                            doornames[j] += " south";
                            break;
                        case WEST: 
                            doornames[j] += " west";
                            break;
                        default:
                            throw new AssertionError();
                    
                    }
                    doornames[j] += " door";
                
                }
                curRoom = curRoom.getRoom(InteractionUtil.promptList("Which door traveler?", doornames) - 1);
                Environment.r0 = curRoom;
                
                break;

            case FIGHT:
                String[] attackTypes = new String[]{"Punch"};
                int attackDamage = 0;
                int chosenAttackType = InteractionUtil.promptList("How will you vanquish yoerer foeee??", attackTypes) - 1;
                
                int chosenEnemyIndex;
                if(curRoom.getNumEnemies() > 1)
                    chosenEnemyIndex = InteractionUtil.promptList("Which fooeeoee meets thine bloodtherstey eyee?", curRoom.getNumEnemies(), "Fight enemy &") - 1;
                else
                    chosenEnemyIndex = 0;

                switch(attackTypes[chosenAttackType])
                {
                    case "Punch": 
                        attackDamage = 1;
                        System.out.println("You heave a mighty blow at the " + curRoom.getEnemy(chosenEnemyIndex).getModifiedDescription("sad") + " and deal a serious " + attackDamage + " damage!");
                        break;
                    
                    default:
                        break;
                }
                
                Environment.playerAttackEnemy(chosenEnemyIndex, attackDamage, "basic");

                break;

            case INSPECT:
                int n = curRoom.getNumInteractibles();    

                String[] interactiblesDescriptions = new String[n];
                for(int j = 0; j < n; j++)
                    interactiblesDescriptions[j] = curRoom.getInteractible(j).getDescription();
                
                curRoom.getInteractible(InteractionUtil.promptList("There " + ((n == 1)? "is an object" : "are a few objects") + " in the room:", interactiblesDescriptions) - 1).inspectInteractible();
                
                break;

            case TALK:
                System.out.println("What do you say?");
                String s = InteractionUtil.scanner.nextLine();
                if(s.equals("Quit."))
                    return false;
                else if(s.contains("stop"))
                {
                    for (Enemy e : curRoom.getEnemies()) 
                    {
                        e.pleaResponse();
                    }
                }
                else
                    InteractionUtil.slowPrintln("Interesting...\nWell, that does nothing.");
                                   
                break;

            case CAST:
                String[] spellTypes = new String[]{"brain aneurysm"};
                int spellDamage = 0;

                System.out.println("Focus...");
                System.out.print("Speak: ");
                String spell = InteractionUtil.scanner.nextLine(); //MAYBE INPUT SUBSTRING PARSE METHOD LATER DOWN THE LINE
                    
                if (spell.contains(spellTypes[0]))
                {
                    spellDamage = 1000;
                    
                    InteractionUtil.slowPrintln("You release a level " + spellDamage + " Psych Strike spell on all of your foes.");
                    if(curRoom.getNumEnemies() == 0)
                        InteractionUtil.slowPrint("... but you have no enemies! Nothing happens.");
                    else
                    {
                        for (int j = 0; j < curRoom.getNumEnemies(); j++) 
                        {
                            Environment.playerAttackEnemy(j, spellDamage, spellTypes[0]);
                        }
                    }
                }
                break;

            default:
                break;
        }

        return true;
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
        switch (a) 
        {
            case NOTHING:
                return "Do nothing";
            
            case DOOR:
                return "Try door";
            
            case INSPECT:
                return "Inspect your surroundings";

            case FIGHT:
                return "It's kill or be killed.";

            case TALK:
                return "Say something";
                
            case CAST:
                return "Utilize the power of the ancients";

            default:
                return "[Empty Action]";
        }
    }

    @Override
    public boolean receiveDamage(int damage, String type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'receiveDamage'");
    }

    @Override
    public int getHealth() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHealth'");
    }

    @Override
    public Inventory getInventory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInventory'");
    }

    @Override
    public int getAttackDamage() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAttackDamage'");
    }

    @Override
    public int getWisdom() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWisdom'");
    }
}
