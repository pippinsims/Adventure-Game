package adventuregame;

import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Enemy;
import adventuregame.abstractclasses.Unit;
import adventuregame.dynamicitems.GoldenPot;
import adventuregame.interactibles.wallinteractibles.Door;
import adventuregame.items.*;

import java.util.*;

public class Player extends Unit
{
    private float chanceOfPtolomy = .0f;
    private boolean ptolomyIsPresent = false;
    private int ptolomyPrintLength;

    private Inventory inv = new Inventory(10);
    private Door firstDoor = null; //exists solely for Action.LEAVE
    public int doorMoves;

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
        INVENTORY,
        LEAVE
    }
    
    private final Map<Action, String> actionTypes = Map.ofEntries(Map.entry(Action.NOTHING,  "Do nothing."),
                                                                  Map.entry(Action.INSPECT,  "Inspect your surroundings"),
                                                                  Map.entry(Action.FIGHT,    "Combat"),
                                                                  Map.entry(Action.TALK,     "Say something"),
                                                                  Map.entry(Action.INTERACT, "Do something"),
                                                                  Map.entry(Action.CAST,     "Utilize the power of the ancients"),
                                                                  Map.entry(Action.COMMUNE,  "Commune with Ptolomy's spirit"),
                                                                  Map.entry(Action.INVENTORY,"Inventory"),
                                                                  Map.entry(Action.LEAVE,    "Leave"));

    public List<Action> actions = new ArrayList<Action>();

    public Player()
    {
        name = "Laur";
        
        inv.add(new Bananarang());
        new GoldenPot(this);

        chanceOfPtolomy = 1f;
        ptolomyIsPresent = Utils.rand.nextFloat() <= chanceOfPtolomy;
        ptolomyPrintLength = 0;//50;
        deathMsg = name + " died.";
        setDescription();
    }

    public Player(String n, int health)
    {
        name = n;        
        this.health = health;
        deathMsg = name + " died.";
        setDescription();
    }

    public Player(boolean genName)
    {
        name = Utils.names1[Utils.rand.nextInt(Utils.names1.length)] + Utils.names2[Utils.rand.nextInt(Utils.names2.length)];
        deathMsg = name + " died.";
        setDescription();
    }

    private void setDescription()
    {
        switch(name)
        {
            case "Laur"   : description = "He is a strange-looking man with grimy fingernails"; break;
            case "Nuel"   : description = "He is a tallish impolite man with a perminent sneer"; break; // He can pick locks
            case "Valeent": description = "She is a perilous-looking woman with anger issues"; break; // Notes on Valeent, skill where she randomly increments her place in the turn order by 1
            case "Peili"  : description = "She is a consternated woman with a bewildered look and a horrendous scar across her forehead"; break; // Lodestones in her baggage
            case "Dormaah": description = "He is a stout fish of a man, knows wild things"; break;
            default       : description = "They are a person"; break;
        }
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

        if(name.equals("Laur")) 
        {
            actions.add(Action.CAST); //someday Valeent will
            actions.add(Action.COMMUNE);
        }

        if(inv.size() > 0) actions.add(Action.INVENTORY);

        int numDoors = 0;
        for(Interactible i : myRoom.interactibles) if(i instanceof Door) { firstDoor = (Door)i; numDoors++;}
        if(numDoors == 1)actions.add(Action.LEAVE);
    }

    public void performAction(int i)
    {   
        switch(actions.get(i))
        {
            case FIGHT:     fight();     break;
            case INSPECT:   inspect();   break;
            case TALK:      talk();      break;
            case CAST:      castSpell(); break;
            case INTERACT:  interact();  break;
            case COMMUNE:   commune();   break;
            case INVENTORY: inventory(); break;
            case LEAVE:     leave();     break;
            default:                     break;
        }
    }

    public boolean getPtolomyIsPresent() 
    {
        return ptolomyIsPresent;
    }

    private void leave()
    {
        firstDoor.action(this);
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
    private void fight()
    {
        ptolomyDoesSomething(new String[] {"smiles upon you","shrinks away like a weak little coward"});

        ArrayList<Enemy> ens = myRoom.enemies;
        for(Weapon w : inv.getWeapons()) if(w instanceof Sword) ((Sword)w).setNumAttacks();
        if(!ens.isEmpty())
        {
            Weapon chosen = null;
            while(!ens.isEmpty())
            {   
                final int chosenEnemyIndex = ens.size() > 1 ? Utils.promptList(name.equals("Laur") ? "Which fooeeoee meets thine bloodtherstey eyee?" : "Which enemy?", Utils.namesOf(ens)) 
                                                            : 0;

                ArrayList<Weapon> weps = inv.getWeapons();
                weps.addFirst(new Weapon() {
                    {   description = "Punch"; 
                        atkmsg = "You heave a mighty blow at the " + ens.get(chosenEnemyIndex).getModifiedDescription("sad"); }
                    @Override public void action(Unit u, boolean isFinal) {}
                    @Override public Damage getDamage() { return getAttackDamage(); }
                });
                if(chosen == null) chosen = Utils.promptList(name.equals("Laur") ? "How will you vanquish yoerer foeee??" : "Choose your attack type:", weps);                

                Damage d = chosen.getDamage();
                if(hasEffect(Effect.Type.WEAKNESS)) { d = new Damage(d); d.setValue(d.getValue() - 1); }
                this.attack(myRoom.enemies.get(chosenEnemyIndex), d, chosen.getAttackMessage());
                if(chosen instanceof Sword && ((Sword)chosen).use() && !ens.isEmpty()) System.out.println("Attack again!");
                else break;
            }
        }
        else System.out.println("No enemies.");
    }

    private void inspect()
    {
        ArrayList<Describable> descs = new ArrayList<>(myRoom.interactibles);
        descs.addAll(myRoom.players);
        descs.remove(this); //TODO but "this" is listed even though it's removed
        Describable d = descs.get(Utils.promptList("There " + ((descs.size() == 1) ? "is an object" : "are a few objects") + " in the room:", Utils.inspectTitlesOf(descs)));
        if(d instanceof Interactible) ((Interactible)d).inspect();
        else Utils.slowPrintln(d.getDescription());

        Utils.slowPrint("Press Enter to continue");
        Utils.scanloop();
        promptForAction();

        //TODO after a turn where i followed dialogue that put me back in the cell, Laur could inspect everyone except with himself replacing Nuel.
    }

    private void commune()
    {
        System.out.println();

        System.out.println("What do you say to Ptolomy's spirit?\n");
        Utils.scanloop();

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
        if(!myRoom.doFirstDialogue())
        {
            System.out.println("What do you say?");
            String s = Utils.scanloop();
            if(s.contains("stop")) for (Enemy e : myRoom.enemies) e.pleaResponse();
        }
        Utils.slowPrintln(ptolomyIsPresent ? "You sense Ptolomy's spirit chuckle deeply... Nothing else occurs." : "Interesting...\nWell, that does nothing.", ptolomyPrintLength);
    }

    public void ptolomyDoesSomething(String[] possibilities) 
    {
        if(ptolomyIsPresent)
        {
            if(possibilities.length == 2) 
            {
                Utils.slowPrintln("Ptolomy's spirit... " + (Utils.rand.nextFloat() <= .5 ? possibilities[0] : possibilities[1]), ptolomyPrintLength + '\n');
            }
            else 
            {

            }
        }
    }

    private void castSpell()
    {        
        System.out.println("Focus...");
        System.out.print("Speak: ");
        String input = Utils.scanloop();
        
        ptolomyDoesSomething(new String[] {"raises an eyebrow","nods slowly"});
        
        String[] spellTypes = new String[]{"mind death all foes", "FERDINAND'S FLAMBERGE"};
        switch(Utils.linearFind(spellTypes, input))
        {
            case 0:
                int lvl = 1000;
                String message = "a level " + lvl + " Psych Strike spell";
                Utils.slowPrintln("You release " + message + " on all of your foes.");
                if(myRoom.enemies.size() == 0) Utils.slowPrint("... but you have no enemies! Nothing happens.");
                else for (Enemy e : new ArrayList<>(myRoom.enemies)) this.attack(e, new Damage(lvl, Damage.Type.PSYCHIC, Damage.Mode.INFLICTEFFECT, new Effect(Effect.Type.PSYCHSTRIKE, lvl, lvl)), message); //need to instantiate every time, otherwise they'd all have the same instance of the effect
                break;
            case 1:
                Utils.slowPrintln("You are currently not powerful enough to use \""+spellTypes[1]+"\"");
                break;
        }
        /*TODO make a simple language generator
            Get the latin sentence of the sentence, then change the words and endings with a new auto-generated set and let em try and figure that out.
            For further obfuscation, use 4 alphabet cases: Superupper, upper, lower, sublower
            superupper is just greek uppercase, sublower is just greek lowercase
            weird case rules (make a bunch and mix and match so that there are a ton of permutations):
                1. first x.length/4 letters of proper noun x equally descend in case, sublowering the rest, examples:
                    Laur -> ΛAuρ
                    Michaelangelo -> MIΓHAElanγελο
                    Appalachian -> APPAlaγηιαν
                    Ferdinand -> ΦΕRDinανδ (i think?? but you get the point, just use the table below)

                    Α α   A         "a" as in father/apple                 
                    Β β   B         "b" as in book (sometimes "v")         
                    Γ γ   G   add C "g" as in game (sometimes "y" sound)    
                    Δ δ   D         "d" as in dog (sometimes "th" in "the")
                    Ε ε   E         "e" as in bet                          
                    Ζ ζ   Z         "z" as in zoo                          
                    Η η   H         "e" as in bee (long 'e')               
                    Θ θ   Th        "th" as in think                       
                    Ι ι   I         "i" as in machine (long 'e')           
                    Κ κ   K   add Q "k" as in kite                         
                    Λ λ   L         "l" as in log                          
                    Μ μ   M         "m" as in man                          
                    Ν ν   N         "n" as in not                          
                    Ξ ξ   X   add J "x" as in box (or 'ks')                
                    Ο ο   O         "o" as in lot                          
                    Π π   P         "p" as in pet                          
                    Ρ ρ   R         "r" (rolled)                           
                    Σ σ/ς S         "s" as in sap (ς at end of word)       
                    Τ τ   T         "t" as in top                          
                    Υ υ   U/Y add W "u" as in put (or 'ü' sound)           
                    Φ φ   Ph  add V "f" (or "ph")                          
                    Χ χ   Kh        "ch" as in Scottish loch               
                    Ψ ψ   Ps        "ps" as in lapse                       
                    Ω ω   O         "o" as in boat (long 'o')
                2. First and last letters of any word that isn't the 3rd word are sublower, 
                unless it's a verb, then they're superupper, if the first letter of the en-
                glish word would be capital, just make the second so.
                    The quick brown fox jumps over the lazy dog.
                    τHε κuicκ brown φoξ ΞumpΣ οveρ τhε λazυ δoγ.
        
        ArrayList<Describable> targets = new ArrayList<>();
        boolean firstOnly = !(true/*root "omn" after targeting preposition "ad, on, pro" (to, on/in/against, for/on-behalf-of/before) *\);
        boolean condition = false;
        
        
        
        if(Utils.contains(input, new String[]{"mind death", "sicken", "destroy"})) //if spell is against enemies
        {
            for(Enemy e : myRoom.enemies)
            {
                //if spell is noun or ("cause", "bring", "invoke", etc)-verb
                //  accusative or ablative ending
                //  target = root with chosen ending after targeting preposition
                //else (if verb)
                //  accusative ending
                //  target = root with chosen ending after verb
                switch("foes"/*target*\)
                {
                    case "foes":
                        condition = true;
                        break;
                    case "goblins":
                        condition = e.getDescription().equals("goblin");
                        break;
                }
        
                if(condition) targets.add(e);
                if(firstOnly) break;
            }
        }
        else if(Utils.contains(input, new String[]{"repair", "warp", "break", "gravito"})) //if spell is toward inanimate object
        {
            //gravito makes things heavy
            ArrayList<Describable> allObjects = new ArrayList<>();
            if(!firstOnly)
            {
                ArrayList<Describable> allContainers = new ArrayList<>();
                allContainers.addAll(myRoom.enemies);
                allContainers.addAll(myRoom.players);
                allContainers.addAll(myRoom.interactibles);
        
                for(Describable c : allContainers)
                {
                    if(c instanceof InventoryInteractible) for(Item i : ((InventoryInteractible)c).getInventory().getItems()) allObjects.add(i);
                    if(c instanceof Unit                 ) for(Item i : ((Unit                 )c).getInventory().getItems()) allObjects.add(i);
                }
            }
        
            for(Describable o : allObjects)
            {
                //get target
                switch("foes"/*target*\)
                {
                    case "weapons":
                        condition = o instanceof Item && o instanceof Weapon;
                        break;
                    case "swords":
                        condition = o.getName().equals("Sword");
                        break;
                    case "potions":
                        condition = o.getDescription().equals("potion");
                }
        
                if(condition) targets.add(o);
                if(firstOnly) break;
            }
        }
        */
    }

    private void interact()
    {
        ArrayList<Interactible> inters = myRoom.getUniqueInters();
        for(Interactible i : new ArrayList<>(inters)) if(i.actionVerb.isEmpty() || !i.isEnabled) inters.remove(i);
        if(!myRoom.enemies.isEmpty()) for(Interactible i : new ArrayList<>(inters)) if(i instanceof Door) inters.remove(i);
        Interactible chosen = inters.get(Utils.promptList("What do you interact with?", Utils.actionDescsOf(inters)));

        ptolomyDoesSomething(new String[] {"lurks ominously","seems pleased"});

        chosen.action(this);
    }

    private void inventory()
    {
        String[] n = Utils.namesOf(inv.getItems()),
                 d = Utils.descriptionsOf(inv.getItems()),
                 prompts = new String[inv.size()];
        for(int i = 0; i < prompts.length; i++) prompts[i] = n[i] + ": " + d[i];

        inv.at(Utils.promptList("Which item do you choose? (This is your inventory, you can hold " + inv.max() + " items total)", prompts)).action(this, false);
    }

    private String[] getPlayerActionDescriptions()
    {
        String[] actionDescriptions = new String[actions.size()];
       
        for(int i = 0; i < actions.size(); i++)
        {
            actionDescriptions[i] = actions.get(i) == Action.FIGHT && name.equals("Laur") 
                ? "It's kill or be killed."
                : actionTypes.get(actions.get(i));
        }

        return actionDescriptions;
    }

    @Override
    public void updateUnit()
    {
        System.out.println("\t\t\t\t\t\t\t\t--" + name + "'" + (name.charAt(name.length() - 1) != 's' ? "s" : "") + " Turn--");
        
        for (int i = effects.size() - 1; i >= 0; i--) if(effectUpdate(effects.get(i)) == EffectUpdateResult.DEATH) return;

        doorMoves = 2;

        setActions();

        if(ptolomyIsPresent) Utils.slowPrintln(Utils.rand.nextFloat() <= .5 ? "You feel a strange presence... It's Ptolomy's spirit!" : "Ptolomy's spirit is lingering ever so elegantly", ptolomyPrintLength);

        promptForAction();
    }

    //lists available actions, lets the player choose, then performs chosen action
    public void promptForAction()
    {
        myRoom.updateDoors();

        System.out.println();
        Game.printInfo(myRoom, false);
        System.out.println();

        performAction(Utils.promptList("You can:", getPlayerActionDescriptions()));
    }

    @Override
    public Inventory getInventory() 
    {
        return inv;
    }

    @Override
    public Damage getAttackDamage() 
    {
        return new Damage(1, Damage.Type.BASIC); //punch
    }

    @Override
    public int getWisdom() 
    {
        // TODO create a use for wisdom
        throw new UnsupportedOperationException("Unimplemented method 'getWisdom'");
    }

    public static class Inspect
    {
        Node current;

        public Inspect(Node.Head head) { current = head.next; }

        public String get() 
        {
            String m = current.msg;
            if(current.next != null) current = current.next;
            return m;
        }

        public static class Node
        {
            protected Node next;
            protected Player person;
            protected String msg;
            private Node prev = null;

            public static class Head extends Node
            {
                public Head(Node next)
                {
                    this.next = next;
                    last().setPerson();
                }
            }

            public static class Branch extends Node
            {
                public Branch(String msg, Node next)
                {
                    this.msg = msg;
                    this.next = next;
                    next.prev = this;
                }
            }

            public static class Leaf extends Node
            {
                public Leaf(String msg)
                {
                    this.msg = msg;
                }
                public Leaf(String msg, Player person)
                {
                    this.person = person;
                    this.msg = msg;
                }
            }

            private void setPerson()
            { 
                if(prev != null)
                {
                    prev.person = person;
                    prev.setPerson();
                }
            }

            protected Node last() { return next == null ? this : next.last(); }
        }
    }
}