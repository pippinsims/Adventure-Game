package adventuregame;

import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.wallentities.Door;
import adventuregame.items.*;

// standard imports
import java.util.*;

public class Player extends Unit
{
    //private int playerDamage = 1;
    //private int playerWisdom = 2;

    private float chanceOfPtolomy = .0f;
    private boolean ptolomyIsPresent = false;
    private int ptolomyPrintLength;

    private String name;
    private Inventory inv = new Inventory(10); //TODO make inventories accessible!
    private Room myRoom;

    //FOR EACH ENUM, MAKE A MAP ENTRY
    enum Action 
    {
        NOTHING,
        INSPECT,
        FIGHT,
        TALK,
        INTERACT,
        CAST,
        COMMUNE,
        INVENTORY
    }
    
    private final Map<Action, String> actionTypes = Map.ofEntries(Map.entry(Action.NOTHING,  "Do nothing."),
                                                                  Map.entry(Action.INSPECT,  "Inspect your surroundings"),
                                                                  Map.entry(Action.FIGHT,    "Combat"),
                                                                  Map.entry(Action.TALK,     "Say something"),
                                                                  Map.entry(Action.INTERACT, "Do something"),
                                                                  Map.entry(Action.CAST,     "Utilize the power of the ancients"),
                                                                  Map.entry(Action.COMMUNE,  "Commune with Ptolomy's spirit"),
                                                                  Map.entry(Action.INVENTORY,"Inventory"));

    public List<Action> actions = new ArrayList<Action>();

    public Player()
    {
        name = "Laur";
        myRoom = Environment.r0;
        inv.add(new Bananarang());

        chanceOfPtolomy = 1f;
        ptolomyIsPresent = Utils.rand.nextFloat() <= chanceOfPtolomy;
        ptolomyPrintLength = 50;
    }

    public Player(String n)
    {
        name = n;
        myRoom = Environment.r0;
    }

    public Player(boolean genName)
    {
        name = Utils.names1[Utils.rand.nextInt(Utils.names1.length)] + Utils.names2[Utils.rand.nextInt(Utils.names2.length)];
        myRoom = Environment.r0;
    }

    public void setActions()
    {
        actions.clear();
        actions.add(Action.NOTHING);

        if(myRoom.interactibles.size() > 0)
        {
            actions.add(Action.INSPECT);
            actions.add(Action.INTERACT);
        }

        if(myRoom.enemies.size() != 0) actions.add(Action.FIGHT);

        actions.add(Action.TALK);
        if(true) // Would set to true once Player inspects language. Placeholder DEV true.
        {
            actions.add(Action.CAST);
        }

        if(name.equals("Laur")) actions.add(Action.COMMUNE);

        if(inv.size() > 0) actions.add(Action.INVENTORY);
    }

    public void performAction(int i) throws Exception
    {   
        switch(actions.get(i))
        {
            case FIGHT: fight(); break;
            case INSPECT: inspect(); break;
            case TALK: talk(); break;
            case CAST: castSpell(); break;
            case INTERACT: interact(); break;
            case COMMUNE: commune(); break;
            case INVENTORY: inventory(); break;
            default: break;
        }
    }

    public boolean getPtolomyIsPresent() 
    {
        return ptolomyIsPresent;
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
    private void fight() throws Exception
    {
        if(ptolomyIsPresent) ptolomyDoesSomething(new String[] {"smiles upon you","shrinks away like a weak little coward"});

        ArrayList<Enemy> ens = myRoom.enemies;
        if(ens.size() > 0)
        {       
            int chosenEnemyIndex = 0;
            if(ens.size() > 1)
                chosenEnemyIndex = Utils.promptList(name.equals("Laur") ? "Which fooeeoee meets thine bloodtherstey eyee?" : "Which enemy?", Utils.namesOf(ens));

            String[] attackTypes = getAttackTypes(inv);
            int chosenAttackType = attackTypes.length > 1 ? Utils.promptList(name.equals("Laur") ? "How will you vanquish yoerer foeee??" : "Choose your attack type:", attackTypes) : 0;

            Damage attackDamage;
            if(chosenAttackType == 0)
            {
                int dmgval = 1;
                attackDamage = new Damage(dmgval, Damage.Type.BASIC, "You heave a mighty blow at the " + ens.get(chosenEnemyIndex).getModifiedDescription("sad") + " and deal a serious " + dmgval + " damage!");
            }
            else
            {                            //chosenAttackType - 1 because of Punch
                attackDamage = inv.at(chosenAttackType - 1).getDamage();
            }

            System.out.println(attackDamage.getMessage());
            
            Environment.playerAttackEnemy(chosenEnemyIndex, attackDamage); //TODO change this implementation so environment knows which player so "you've murdered" only happens to Laur
        }
        else
            System.out.println("No enemies.");
    }

    private void inspect()
    {
        ArrayList<Interactible> inters = myRoom.interactibles;
        inters.get(Utils.promptList("There " + ((inters.size() == 1) ? "is an object" : "are a few objects") + " in the room:", Utils.descriptionsOf(inters))).inspectInteractible();
    }

    private void commune()
    {
        System.out.println();

        System.out.println("What do you say to Ptolomy's spirit?\n");
        Utils.scanner.nextLine();

        System.out.println();
        System.out.print("The reply: ");

        switch(Utils.rand.nextInt(10)) {
            case 0: Utils.slowPrint("You anger ME!!!", 75); break;
            case 1: Utils.slowPrint("Careful... lest I smite thee", 75); break;
            case 2: Utils.slowPrint("You little... cannabis plant!!!!", 75); break;
            default: Utils.slowPrintln("Ptolomy smiles... like this: =)", 75); break;
        }

        System.out.println();
    }

    private void talk()
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
            Utils.slowPrintln(ptolomyIsPresent ? "You sense Ptolomy's spirit chuckle deeply... Nothing else occurs." : "Interesting...\nWell, that does nothing.", ptolomyPrintLength);
    }

    public void ptolomyDoesSomething(String[] possibilities) 
    {
        if(possibilities.length == 2) 
        {
            Utils.slowPrintln("Ptolomy's spirit... " + (Utils.rand.nextFloat() <= .5 ? possibilities[0] : possibilities[1]), ptolomyPrintLength + '\n');
        }
        else 
        {

        }
    }

    private void castSpell() throws Exception
    {
        String[] spellTypes = new String[]{"brain aneurysm"};
        int lvl = 1000;
        String message = "You release a level " + lvl + " Psych Strike spell on all of your foes.";

        System.out.println("Focus...");
        System.out.print("Speak: ");
        String spell = Utils.scanner.nextLine(); //MAYBE INPUT SUBSTRING PARSE METHOD LATER DOWN THE LINE
        
        if(ptolomyIsPresent) ptolomyDoesSomething(new String[] {"raises an eyebrow","nods slowly"});
            
        if (spell.contains(spellTypes[0]))
        {
            Utils.slowPrintln(message);
            int s = myRoom.enemies.size();
            if(s == 0)
                Utils.slowPrint("... but you have no enemies! Nothing happens.");
            else
            {
                Collections.reverse(myRoom.enemies);
                for (int i = s - 1; i >= 0; i--)
                {
                    Environment.playerAttackEnemy(i, new Damage(lvl, Damage.Type.PSYCHIC, Damage.Mode.INFLICTEFFECT, new Effect(Effect.Type.PSYCHSTRIKE, lvl, lvl), message));
                }
                Collections.reverse(myRoom.enemies);
            }
        }
    }

    private void interact()
    {
        ArrayList<Interactible> inters = getIntersByUniqueDesc();
        String[] descriptions = getIntersActionDescriptions(inters);

        Interactible chosen = inters.get(Utils.promptList("What do you interact with?", descriptions));

        if(ptolomyIsPresent) ptolomyDoesSomething(new String[] {"lurks ominously","seems pleased"});

        chosen.action(this);

        if(chosen.isDoor())
        {             
            Door door = (Door)chosen;
            myRoom.players.remove(this);
            myRoom = door.getNextRoom(myRoom);
            myRoom.players.add(this);
        }
    }

    private void inventory()
    {
        String[] ns = Utils.namesOf(inv.getItems());
        String[] ds = Utils.descriptionsOf(inv.getItems());
        String[] prompts = new String[inv.size()];
        for(int i = 0; i < prompts.length; i++) prompts[i] = ns[i] + ": " + ds[i];

        inv.at(Utils.promptList("Which item?", prompts)).action();
    }

    private String[] getAttackTypes(Inventory inventory) 
    {
        String[] attackTypes = new String[inventory.size() + 1];
        attackTypes[0] = "Punch";
        for (int i = 1; i < attackTypes.length; i++) 
        {
            Item it = inventory.at(i - 1);
            if(it.isWeapon())
                attackTypes[i] = it.getName();
        }

        return attackTypes;
    }

    private String[] getPlayerActionDescriptions()
    {
        String[] actionDescriptions = new String[actions.size()];
       
        for(int i = 0; i < actions.size(); i++)
        {
             actionDescriptions[i] = !(actions.get(i) == Action.FIGHT && name.equals("Laur")) 
                ? actionTypes.get(actions.get(i))
                : "It's kill or be killed.";
        }

        return actionDescriptions;
    }

    private ArrayList<Interactible> getIntersByUniqueDesc()
    {
        ArrayList<Interactible> inters = new ArrayList<>();

        for (Interactible i : myRoom.interactibles) 
        {
            if(!inters.contains(i)) //This compares by description
                inters.add(i);
        }

        return inters;
    }

    private String[] getIntersActionDescriptions(ArrayList<Interactible> inters)
    {
        String[] d = new String[inters.size()];

        for(int i = 0; i < d.length; i++) d[i] = inters.get(i).getActionDescription();

        return d;
    }

    @Override
    public void updateUnit() throws Exception 
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
        
        for (int i = effects.size() - 1; i >= 0; i--) //TODO this has a mirror image in Enemy
        {
            Effect e = effects.get(i);
            switch (effectUpdate(e))
            {
                case DEATH:
                    Utils.slowPrintln("you died.");
                    break;
            
                default:
                    break;
            }
        }

        setActions();
 
        if(ptolomyIsPresent)
        {

            Utils.slowPrintln(Utils.rand.nextFloat() <= .5 ? "You feel a strange presence... It's Ptolomy's spirit!" : "Ptolomy's spirit is lingering ever so elegantly", ptolomyPrintLength);
            System.out.println();
        }

        //lists available actions, lets the player choose, then performs chosen action
        performAction(Utils.promptList("You can:", getPlayerActionDescriptions()));
    }

    @Override
    public Inventory getInventory() 
    {
        return inv;
    }

    @Override
    public int getAttackDamage() 
    {
        //TODO will be used in combat manager I assume, to get attack damage at that time for the enemy decisionmaking
        throw new UnsupportedOperationException("Unimplemented method 'getAttackDamage'");
    }

    @Override
    public int getWisdom() 
    {
        // TODO create a use for wisdom
        throw new UnsupportedOperationException("Unimplemented method 'getWisdom'");
    }

    @Override
    public String getName() 
    {
        return name;        
    }

    @Override
    public Room getRoom() 
    {
        return myRoom;
    }

    @Override
    public String getDescription() 
    {
        return "My name is " + name;
    }
}