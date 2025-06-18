package adventuregame;

import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.wallentities.Door;
import adventuregame.items.*;

// standard imports
import java.util.*;

public class Player extends Unit{
    //private int playerDamage = 1;
    //private int playerWisdom = 2;
    
    private String name;
    private Inventory inv = new Inventory(10);
    private Room myRoom;

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
        myRoom = Environment.r0;
        inv.addItem(new Bananarang());
    }

    public Player(String n)
    {
        name = n;
        myRoom = Environment.r0;
    }

    public Player(boolean genName)
    {
        name = Utils.names1[new Random().nextInt(Utils.names1.length)] + Utils.names2[new Random().nextInt(Utils.names2.length)];
        myRoom = Environment.r0;
    }

    public void setActions()
    {
        actions.clear();
        actions.add(Action.NOTHING);

        if(myRoom.getInteractibles().size() > 0)
        {
            actions.add(Action.INSPECT);
            actions.add(Action.INTERACT);
        }

        if (myRoom.enemies.size() != 0)
        {
            actions.add(Action.FIGHT);
        }

        actions.add(Action.TALK);
        if (true) // Would set to true once Player inspects language. Placeholder DEV true.
        {
            actions.add(Action.CAST);
        }
    }

    public void performAction(int i)
    {   
        switch(actions.get(i))
        {
            case FIGHT:
                Fight();
                break;

            case INSPECT:
                Inspect();
                break;

            case TALK:
                Talk();         
                break;

            case CAST:
                CastSpell();
                break;
            
            case INTERACT:
                Interact();
                break;

            default:
                break;
        }
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
    
    /*
    TODO I think we should make a combat manager, which will become quite advanced, visually represented by a header
    and also make combat not happen immediately when you enter a room, you always get to do something
    like it will generate a combat scene based on certain stuff but then everyone would have position and ability, which limit them
    like if you do something that takes away your right arm of your ability then you can't use right arm, but left is still open,
    but also you might be far from them

    might even be good to have a positionmanager that tracks kind of where people are the whole time for example if something comes
    in the door behind you now its behind you, and enemies and objects have original position, maybe even you could make it so you
    can place objects in positions. Maybe every room has a list of local unique positions, and you get a list of them when you want
    to place something
    */
    private void Fight()
    {
        ArrayList<Enemy> ens = myRoom.enemies;
        if(ens.size() > 0)
        {
            String[] attackTypes = getAttackTypes(inv);
            Damage attackDamage;
            int chosenAttackType = Utils.promptList("How will you vanquish yoerer foeee??", attackTypes) - 1;
            
            int chosenEnemyIndex = 0;
            if(ens.size() > 1)
            {
                String[] prompts = new String[ens.size()];
                for(int j = 0; j < ens.size(); j++)
                {
                    prompts[j] = ens.get(j).getName();
                }
                chosenEnemyIndex = Utils.promptList("Which fooeeoee meets thine bloodtherstey eyee?", prompts) - 1;
            }

            if(attackTypes[chosenAttackType].equals("Punch"))
            {
                int dmgval = 1;
                attackDamage = new Damage(dmgval, Damage.Type.BASIC, "You heave a mighty blow at the " + ens.get(chosenEnemyIndex).getModifiedDescription("sad") + " and deal a serious " + dmgval + " damage!");
            }
            else
            {                            //chosenAttackType - 1 because of Punch
                attackDamage = inv.getItem(chosenAttackType - 1).getDamage();
            }

            System.out.println(attackDamage.getMessage());
            
            Environment.playerAttackEnemy(chosenEnemyIndex, attackDamage);
        }
        else
            System.out.println("No enemies.");
    }

    private void Inspect()
    {
        ArrayList<Interactible> inters = myRoom.getInteractibles();
        int n = inters.size();    

        String[] intersDescs = new String[n];
        for(int j = 0; j < n; j++)
            intersDescs[j] = inters.get(j).getDescription();
        
        inters.get(Utils.promptList("There " + ((n == 1)? "is an object" : "are a few objects") + " in the room:", intersDescs) - 1).inspectInteractible();
    }

    private void Talk()
    {
        System.out.println("What do you say?");
        String s = Utils.scanner.nextLine();
        if(s.contains("stop"))
        {
            for (Enemy e : myRoom.enemies)
            {
                e.pleaResponse();
            }
        }
        else
            Utils.slowPrintln("Interesting...\nWell, that does nothing.");
    }

    private void CastSpell()
    {
        String[] spellTypes = new String[]{"brain aneurysm"};
        Damage spellDamage;

        System.out.println("Focus...");
        System.out.print("Speak: ");
        String spell = Utils.scanner.nextLine(); //MAYBE INPUT SUBSTRING PARSE METHOD LATER DOWN THE LINE
            
        if (spell.contains(spellTypes[0]))
        {
            spellDamage = new Damage(1000, Damage.Type.PSYCHIC, "You release a level 1000 Psych Strike spell on all of your foes.");
            
            Utils.slowPrintln(spellDamage.getMessage());
            int s = myRoom.enemies.size();
            if(s == 0)
                Utils.slowPrint("... but you have no enemies! Nothing happens.");
            else
            {
                for (int i = s - 1; i > -1; i--)
                {
                    Environment.playerAttackEnemy(i, spellDamage);
                }
            }
        }
    }

    private void Interact()
    {
        ArrayList<Interactible> inters = myRoom.getInteractibles();
        String[] descriptions = new String[inters.size()];
        ArrayList<Interactible> doors = new ArrayList<>();

        for (int j = 0; j < descriptions.length; j++) 
        {
            Interactible cur = inters.get(j);
            descriptions[j] = cur.getActionDescription();
            if(cur.isDoor())
                doors.add(cur);
        }    

        Interactible chosen = inters.get(Utils.promptList("What do you interact with?", descriptions) - 1);

        chosen.action(this);

        if(chosen.isDoor())
        {             
            Door door = (Door)chosen;
            myRoom.players.remove(this);
            myRoom = door.getNextRoom(myRoom);
            myRoom.players.add(this);
        }
    }

    private String[] getAttackTypes(Inventory inventory) 
    {
        String[] attackTypes = new String[inventory.getSize() + 1];
        attackTypes[0] = "Punch";
        for (int i = 1; i < attackTypes.length; i++) 
        {
            Item it = inventory.getItem(i - 1);
            if(it.isWeapon())
                attackTypes[i] = it.getName();
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
    public void updateUnit() 
    {
        System.out.println("--" + name + "'" + (name.charAt(name.length() - 1) != 's' ? "s" : "") + " Turn--");

        if(name == "Laur")
        {
            for (Enemy e : myRoom.enemies) 
            {
                e.Randomize();    
            }
        }
        
        myRoom.updateDoors();

        System.out.println();

        Environment.printInfo();

        System.out.println();
        
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

        setActions();

        //lists available actions, lets the player choose, then performs chosen action
        performAction(Utils.promptList("You can:", getActionDescriptions()) - 1);
    }

    @Override
    public Room getRoom() 
    {
        return myRoom;
    }

    @Override
    public String getPluralDescription() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPluralDescription'");
    }

    @Override
    public String getDescription() {
        return "My name is " + name;
    }
}