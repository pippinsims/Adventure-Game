package adventuregame;

// local imports
import adventuregame.interfaces.Unit;
import adventuregame.items.*;

// standard imports
import java.util.*;

public class Player implements Unit{

    private int maxHealth = 20;
    //private int playerDamage = 1;
    //private int playerWisdom = 2;
    private int health = maxHealth;
    private String name;
    private Inventory inv = new Inventory(10);
    //private PlayerManager pm;

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

    public Player()
    {
        name = "Laur";
        inv.addItem(new Bananarang());
    }

    public Player(String n)
    {
        name = Utils.names1[new Random().nextInt(Utils.names1.length)] + Utils.names2[new Random().nextInt(Utils.names2.length)];
    }

    public void setActions(Room curRoom)
    {
        actions.clear();
        actions.add(action.NOTHING);

        if (curRoom.getNumExits() != 0)
        {
            actions.add(action.DOOR);
        }

        if(curRoom.getInteractibles().size() > 0)
        {
            actions.add(action.INSPECT);
        }

        if (curRoom.getEnemies().size() != 0)
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
                    doornames[j] = "Door " + (j + 1) + ", " + curRoom.getDoorDes(j) + ".";           
                }
                
                try 
                {
                    curRoom = curRoom.getRoom(Utils.promptList("Which door traveler?", doornames) - 1);
                } 
                catch (Exception e) 
                {
                    System.err.println("trying to get a room that doesn't exist");
                }
                
                Environment.r0 = curRoom;

                break;

            case FIGHT:
                String[] attackTypes = new String[]{"Punch", "Bananarang"};
                int attackDamage = 0;
                int chosenAttackType = Utils.promptList("How will you vanquish yoerer foeee??", attackTypes) - 1;
                
                int chosenEnemyIndex;
                ArrayList<Enemy> ens = curRoom.getEnemies();
                if(ens.size() > 1)
                {
                    String[] prompts = new String[ens.size()];
                    for(int j = 0; j < ens.size(); j++)
                    {
                        prompts[j] = ens.get(j).getName();
                    }
                    chosenEnemyIndex = Utils.promptList("Which fooeeoee meets thine bloodtherstey eyee?", prompts) - 1;
                }
                else
                    chosenEnemyIndex = 0;

                switch(attackTypes[chosenAttackType])
                {
                    case "Punch": 
                        attackDamage = 1;
                        System.out.println("You heave a mighty blow at the " + curRoom.getEnemies().get(chosenEnemyIndex).getModifiedDescription("sad") + " and deal a serious " + attackDamage + " damage!");
                        break;
                    
                    case "Bananarang":
                        attackDamage = 3;
                        inv.getItem(0).action();
                        break;

                    default:
                        break;
                }
                
                Environment.playerAttackEnemy(chosenEnemyIndex, attackDamage, "basic");

                break;

            case INSPECT:
                int n = curRoom.getInteractibles().size();    

                String[] interactiblesDescriptions = new String[n];
                for(int j = 0; j < n; j++)
                    interactiblesDescriptions[j] = curRoom.getInteractibles().get(j).getDescription();
                
                curRoom.getInteractibles().get(Utils.promptList("There " + ((n == 1)? "is an object" : "are a few objects") + " in the room:", interactiblesDescriptions) - 1).inspectInteractible();
                
                break;

            case TALK:
                System.out.println("What do you say?");
                String s = Utils.scanner.nextLine();
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
                    Utils.slowPrintln("Interesting...\nWell, that does nothing.");
                                   
                break;

            case CAST:
                String[] spellTypes = new String[]{"brain aneurysm"};
                int spellDamage = 0;

                System.out.println("Focus...");
                System.out.print("Speak: ");
                String spell = Utils.scanner.nextLine(); //MAYBE INPUT SUBSTRING PARSE METHOD LATER DOWN THE LINE
                    
                if (spell.contains(spellTypes[0]))
                {
                    spellDamage = 1000;
                    
                    Utils.slowPrintln("You release a level " + spellDamage + " Psych Strike spell on all of your foes.");
                    if(curRoom.getEnemies().size() == 0)
                        Utils.slowPrint("... but you have no enemies! Nothing happens.");
                    else
                    {
                        for (int j = 0; j < curRoom.getEnemies().size(); j++) 
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

    public void setManager(PlayerManager m)
    {
        //pm = m;
    }

    private String actionToString(action a)
    {
        switch (a) 
        {
            case NOTHING:
                return "Do nothing";
            
            case DOOR:
                return "Try a door";
            
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

    public Room getCurrentRoom()
    {//MAKE THIS THE CURRENT ROOM THAT THIS PLAYER IS IN
        return Environment.r0;
    }

    @Override
    public boolean receiveDamage(int damage, String type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'receiveDamage'");
    }

    @Override
    public float getHealth() 
    {
        return health;
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

    @Override
    public String getName() 
    {
        return name;        
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDescription'");
    }
}
