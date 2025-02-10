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

    public boolean performAction(int i)
    {   
        //Would be kind of nice if all of the stuff relating to player actions were in the Player class, 
        //but with that said most of his actions affect the environment
        Room curRoom = Environment.r0;
        switch(actions.get(i))
        {
            case action.DOOR:
                curRoom = curRoom.getRoom(InteractionUtil.promptList("Which door traveler?", curRoom.getNumExits(), "Try door &") - 1);
                
                break;

            case action.FIGHT:
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
                
                if(curRoom.getEnemy(chosenEnemyIndex).receiveDamage(attackDamage))
                {
                    InteractionUtil.slowPrintln("You have murdered the " + curRoom.getEnemy(chosenEnemyIndex).getRandomDescription(), 250);
                    curRoom.slayEnemy(chosenEnemyIndex);
                }

                break;

            case action.INSPECT:
                String[] interactiblesDescriptions = new String[curRoom.getNumInteractibles()];
                for(int j = 0; j < interactiblesDescriptions.length; j++)
                    interactiblesDescriptions[j] = curRoom.getInteractible(j).getDescription();
                Interactible chosenInteractible = curRoom.getInteractible(InteractionUtil.promptList("There are a few objects in the room:", interactiblesDescriptions) - 1);

                switch (chosenInteractible.getDescription()) {
                    case "flaming stick":
                        InteractionUtil.slowPrintln("You take a closer look at this flaming stick and you notice that it is a burning torch, providing light and warmth!");
                        break;
                    
                    case "depiction": //(if description == depiction chosenInteractible must be an instance of ViewablePicture)
                        ViewablePicture chosenPicture = (ViewablePicture)chosenInteractible;
                        InteractionUtil.slowPrintln("You take a closer look at the depiction:\n");
                        String s = InteractionUtil.readFile(chosenPicture.getFileName());
                        System.out.println(s);
                        System.out.println("Press enter to continue");
                        InteractionUtil.scanner.nextLine();

                        break;
                
                    default:
                        break;
                }
                
                break;

            case action.TALK:
                System.out.println("What do you say?");
                String s = InteractionUtil.scanner.nextLine();
                if(s.equals("Quit."))
                    return false;
                else if(s.contains("stop"))
                    ; //here somehow perhaps make enemy react
                else
                    InteractionUtil.slowPrintln("Interesting...\nWell, that does nothing.");
                                   
                break;

            case action.CAST:
                String[] spellTypes = new String[]{"brain aneurysm"};
                int spellDamage = 0;

                System.out.println("Focus...");
                System.out.print("Speak: ");
                String spell = InteractionUtil.scanner.nextLine(); //MAYBE INPUT SUBSTRING PARSE METHOD LATER DOWN THE LINE
                    
                if (spell.contains("brain aneurysm"))
                {
                    spellDamage = 1000;
                    //idk yet. maybe getEnemies then damage one of them?
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

    @Override
    public boolean receiveDamage(int damage) {
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
