package adventuregame;

import adventuregame.interactibles.wallentities.Door;
import adventuregame.interfaces.Interactible;
import adventuregame.interfaces.Item;
// local imports
import adventuregame.interfaces.Unit;
import adventuregame.items.*;

// standard imports
import java.util.*;

public class Player extends Effectable implements Unit{
    //private int playerDamage = 1;
    //private int playerWisdom = 2;
    
    private String name;
    private Inventory inv = new Inventory(10);

    enum Action {
        NOTHING,
        INSPECT,
        FIGHT,
        TALK,
        INTERACT,
        CAST
    }

    public List<Action> actions = new ArrayList<Action>();

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
        actions.add(Action.NOTHING);

        if(curRoom.getInteractibles().size() > 0)
        {
            actions.add(Action.INSPECT);
            actions.add(Action.INTERACT);
        }

        if (curRoom.getEnemies().size() != 0)
        {
            actions.add(Action.FIGHT);
        }

        actions.add(Action.TALK);
        if (true) // Would set to true once Player inspects language. Placeholder DEV true.
        {
            actions.add(Action.CAST);
        }
    }

    public boolean performAction(int i)
    {   
        Room curRoom = Environment.r0;
        switch(actions.get(i))
        {
            /*
            TODO: I think we should make a combat manager, which will become quite advanced, visually represented by a header
            and also make combat not happen immediately when you enter a room, you always get to do something
            like it will generate a combat scene based on certain stuff but then everyone would have position and ability, which limit them
            like if you do something that takes away your right arm of your ability then you can't use right arm, but left is still open,
            but also you might be far from them

            might even be good to have a positionmanager that tracks kind of where people are the whole time for example if something comes
            in the door behind you now its behind you, and enemies and objects have original position, maybe even you could make it so you
            can place objects in positions. Maybe every room has a list of local unique positions, and you get a list of them when you want
            to place something
            */
            case FIGHT:
                String[] attackTypes = generateAttackTypes(inv);
                Damage attackDamage;
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

                if(attackTypes[chosenAttackType].equals("Punch"))
                {
                    int dmgval = 1;
                    attackDamage = new Damage(dmgval, Damage.Type.BASIC, "You heave a mighty blow at the " + curRoom.getEnemies().get(chosenEnemyIndex).getModifiedDescription("sad") + " and deal a serious " + dmgval + " damage!");
                }
                else
                {   //chosenAttackType - 1 because of Punch
                    attackDamage = inv.getItem(chosenAttackType - 1).getDamage();
                }

                System.out.println(attackDamage.getMessage());
                
                Environment.playerAttackEnemy(chosenEnemyIndex, attackDamage);

                break;

            case INSPECT:
                ArrayList<Interactible> inters = curRoom.getInteractibles();
                int n = inters.size();    

                String[] intersDescs = new String[n];
                for(int j = 0; j < n; j++)
                    intersDescs[j] = inters.get(j).getDescription();
                
                inters.get(Utils.promptList("There " + ((n == 1)? "is an object" : "are a few objects") + " in the room:", intersDescs) - 1).inspectInteractible();
                
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
                Damage spellDamage;

                System.out.println("Focus...");
                System.out.print("Speak: ");
                String spell = Utils.scanner.nextLine(); //MAYBE INPUT SUBSTRING PARSE METHOD LATER DOWN THE LINE
                    
                if (spell.contains(spellTypes[0]))
                {
                    spellDamage = new Damage(1000, Damage.Type.PSYCHIC, "You release a level 1000 Psych Strike spell on all of your foes.");
                    
                    Utils.slowPrintln(spellDamage.getMessage());
                    if(curRoom.getEnemies().size() == 0)
                        Utils.slowPrint("... but you have no enemies! Nothing happens.");
                    else
                    {
                        for (int j = Environment.r0.getEnemies().size() - 1; j > -1; j--)
                        {
                            Environment.playerAttackEnemy(j, spellDamage);
                        }
                    }
                }
                break;
            
            case INTERACT:
                inters = Environment.r0.getInteractibles();
                String[] descriptions = new String[inters.size()];
                ArrayList<Interactible> doors = new ArrayList<>();

                for (int j = 0; j < descriptions.length; j++) 
                {
                    Interactible cur = inters.get(j);
                    descriptions[j] = cur.getActionDescription();
                    if(cur.getName().equals("Door"))
                        doors.add(cur);
                }    

                Interactible chosen = inters.get(Utils.promptList("What do you interact with?", descriptions) - 1);

                chosen.action(this);

                if(chosen.getName().equals("Door"))
                {             
                    try 
                    {
                        Door door = (Door)chosen;
                        curRoom = door.getNextRoom(curRoom);
                    } 
                    catch (Exception e) 
                    {
                        System.err.println("trying to get a room that doesn't exist");
                    }
                    
                    Environment.r0 = curRoom;
                }
                break;

            default:
                break;
        }

        return true;
    }   

    private String[] generateAttackTypes(Inventory inventory) 
    {
        String[] attackTypes = new String[inventory.getSize() + 1];
        attackTypes[0] = "Punch";
        for (int i = 0; i < attackTypes.length - 1; i++) 
        {
            Item j = inventory.getItem(i);
            if(j.isWeapon())
                attackTypes[i + 1] = j.getName();
        }

        return attackTypes;
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

    private String actionToString(Action a)
    {
        switch (a) 
        {
            case NOTHING:
                return "Do nothing";
            
            case INSPECT:
                return "Inspect your surroundings";

            case FIGHT:
                return "It's kill or be killed.";

            case TALK:
                return "Say something";
                
            case CAST:
                return "Utilize the power of the ancients";

            case INTERACT:
                return "Do something";

            default:
                return "[Empty Action]";
        }
    }

    public Room getCurrentRoom()
    {//MAKE THIS THE CURRENT ROOM THAT THIS PLAYER IS IN
        return Environment.r0;
    }

    @Override
    public Inventory getInventory() 
    {
        return inv;
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

    @Override
    public void updateUnit() {
        System.out.println("--" + name + "'" + (name.charAt(name.length() - 1) != 's' ? "s" : "") + " Turn--");

        for (Effect e : effects) 
        {
            if(effectUpdate(e) != EffectUpdateResult.NONE)
            {
                switch (e.getType()) 
                {
                    case FIRE: case PSYCHSTRIKE:
                        Utils.slowPrintln("you died.");
                        break;
                
                    default:
                        break;
                }
            }
        }

        setActions(Environment.r0);

        //lists available actions, lets the player choose, then performs chosen action
        performAction(Utils.promptList("You can:", getActionDescriptions()) - 1);
    }
}
