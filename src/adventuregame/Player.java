package adventuregame;

import adventuregame.Inventory.Trade;
import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.NonPlayer;
import adventuregame.abstractclasses.Unit;
import adventuregame.abstractclasses.Item.Actor;
import adventuregame.abstractclasses.Item.Affector;
import adventuregame.interactibles.wallinteractibles.Door;
import adventuregame.items.*;

import java.util.*;

public class Player extends Unit
{
    private float chanceOfPtolomy = .0f;
    private boolean ptolomyIsPresent = false;
    private int ptolomyPrintLength = Utils.currentPrintDelay;

    public int doorMoves;
    public boolean ableToAct = false;

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
        LEAVE,
        TRADE
    }
    
    private final Map<Action, String> actionTypes = Map.ofEntries(Map.entry(Action.NOTHING,  "Do nothing."),
                                                                  Map.entry(Action.INSPECT,  "Inspect your surroundings"),
                                                                  Map.entry(Action.FIGHT,    "Combat"),
                                                                  Map.entry(Action.TALK,     "Say something"),
                                                                  Map.entry(Action.INTERACT, "Do something"),
                                                                  Map.entry(Action.CAST,     "Utilize the power of the ancients"),
                                                                  Map.entry(Action.COMMUNE,  "Commune with Ptolomy's spirit"),
                                                                  Map.entry(Action.INVENTORY,"Inventory"),
                                                                  Map.entry(Action.LEAVE,    "Leave"),
                                                                  Map.entry(Action.TRADE,    "Trade"));

    public List<Action> actions = new ArrayList<Action>();

    { inv = new Inventory.Whole(); }

    public Player()
    {
        name = "Laur";

        chanceOfPtolomy = 1f;
        ptolomyIsPresent = Utils.rand.nextFloat() <= chanceOfPtolomy;
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
            case "Laur"   : male(); description = pronounsubj + " is a strange-looking man with grimy fingernails"; break;
            case "Nuel"   : male(); description = pronounsubj + " is a tallish impolite man with a perminent sneer"; break; // He can pick locks
            case "Valeent": female(); description = pronounsubj + " is a perilous-looking woman with anger issues"; break; // Notes on Valeent, skill where she randomly increments her place in the turn order by 1
            case "Peili"  : female(); description = pronounsubj + " is a consternated woman with a bewildered look and a horrendous scar across her forehead"; break; // Lodestones in her baggage
            case "Dormaah": male(); description = pronounsubj + " is a stout fish of a man, knows wild things"; break;
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
            if(myRoom.interactibles.size() - (myRoom.getDoors().size() - myRoom.getLockedDoors().size()) > 0)
                actions.add(Action.INTERACT);
        }

        if(myRoom.all().size() > 1 && !inv.isEmpty()) actions.add(Action.TRADE);

        if(myRoom.NPCs.size() != 0) actions.add(Action.FIGHT);

        actions.add(Action.TALK);

        if(name.equals("Laur")) 
        {
            actions.add(Action.CAST); //someday Valeent will
            actions.add(Action.COMMUNE);
        }

        if(inv.size() > 0) actions.add(Action.INVENTORY);

        if(!isInCombat) actions.add(Action.LEAVE);
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
            case TRADE:     trade();     break;
            default:                     break;
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
    private void fight()
    {
        ptolomyDoesSomething(new String[] {"smiles upon you","shrinks away like a weak little coward"});

        ArrayList<NonPlayer> ens = myRoom.NPCs;
        for(Weapon w : inv.getWeapons()) if(w instanceof Sword) ((Sword)w).setNumAttacks();
        if(!ens.isEmpty())
        {
            Weapon chosen = null;
            while(!ens.isEmpty())
            {   
                final int chosenEnemyIndex = ens.size() > 1 ? Utils.promptList(name.equals("Laur") ? "Which fooeeoee meets thine bloodtherstey eyee?" : "Which enemy?", Utils.namesOf(ens)) : 0;

                ArrayList<Weapon> weps = inv.getWeapons();
                if(name.equals("Peili")) weps.addFirst(new Weapon.Punch("You heave a mighty blow at the " + ens.get(chosenEnemyIndex).getModifiedDescription("sad"), new Damage(2)));
                else weps.addFirst(new Weapon.Punch("You heave a mighty blow at the " + ens.get(chosenEnemyIndex).getModifiedDescription("sad")));
                if(chosen == null) chosen = Utils.promptList(name.equals("Laur") ? "How will you vanquish yoerer foeee??" : "Choose your attack type:", weps);                

                Damage d = chosen.getDamage();
                if(hasEffect(Effect.Type.WEAKNESS)) { d = new Damage(d); d.setValue(d.getValue() - firstEffectOf(Effect.Type.WEAKNESS).strength); }
                this.attack(ens.get(chosenEnemyIndex), d, chosen.getAttackMessage());
                if(chosen instanceof Sword && ((Sword)chosen).use() && !ens.isEmpty()) System.out.println("Attack again!");
                else break;
            }
        }
        else System.out.println("No enemies.");
    }

    private void trade()
    {
        ArrayList<Unit> peeps = myRoom.all();
        peeps.remove(this);

        Unit chsn = peeps.get(Utils.promptList("With whom?", Utils.namesOf(peeps)));

        new Trade.Builder().one(this).another(chsn).build();

        ableToAct = true;
    }

    private void inspect()
    {
        ArrayList<Describable> descs = new ArrayList<>(myRoom.interactibles);
        descs.addAll(myRoom.all());
        descs.remove(this);
        Describable d = descs.get(Utils.promptList("There " + ((descs.size() == 1) ? "is an object" : "are a few objects") + " in the room:", Utils.inspectTitlesOf(descs)));
        if(d instanceof Interactible) ((Interactible)d).inspect(this);
        else Utils.slowPrintln(d.getDescription());

        Utils.slowPrint("Press Enter to continue");
        Utils.scanloop();
        ableToAct = true;
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
            Utils.scanloop();
            Utils.slowPrintln(ptolomyIsPresent ? "You sense Ptolomy's spirit chuckle deeply... Nothing else occurs." : "Interesting...\nWell, that does nothing.", ptolomyPrintLength);
        }
    }

    public void ptolomyDoesSomething(String[] possibilities) 
    {
        if(ptolomyIsPresent)
        {
            if(possibilities.length == 2) 
            {
                Utils.slowPrintln("Ptolomy's spirit... " + (Utils.rand.nextFloat() <= .5 ? possibilities[0] : possibilities[1]) + '\n', ptolomyPrintLength);
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
        
        String[] spellTypes = new String[]{"mind death all foes", "relocate door bar open", "relocate all door bars open", "FERDINAND'S FLAMBERGE"};
        switch(Utils.linearFind(spellTypes, input))
        {
            case 0:
                int lvl = 1000;
                String message = "a level " + lvl + " Psych Strike spell";
                Utils.slowPrintln("You release " + message + " on all of your foes.");
                ArrayList<Unit> targets = new ArrayList<>();
                for(NonPlayer n : myRoom.NPCs) if(n.enemies.contains(this)) targets.add(n);
                if(targets.size() == 0) Utils.slowPrint("... but you have no enemies! Nothing happens.");
                else for (Unit e : new ArrayList<>(targets)) this.attack(e, new Damage(lvl, Damage.Type.PSYCHIC, Damage.Mode.INFLICTEFFECT, new Effect(Effect.Type.PSYCHSTRIKE, lvl, lvl)), message); //need to instantiate every time, otherwise they'd all have the same instance of the effect
                break;
            case 1:
                boolean yes = false;
                for(Door d : myRoom.getDoors()) if(d.isLocked(d.getNextRoom(myRoom), "bar"))
                {    
                    d.unlock(d.getNextRoom(myRoom), "bar");
                    yes = true;
                    break;
                }
                if(yes) Utils.slowPrintln("You successfully removed "+(myRoom.getDoors().size() == 1 ? "the" : "a")+" door bar.");
                else Utils.slowPrintln("That does nothing.");
                break;
            case 2:
                yes = false;
                for(Door d : myRoom.getDoors()) if(d.isLocked(d.getNextRoom(myRoom), "bar") || d.isLocked(myRoom, "bar"))
                {    
                    if(d.isLocked(d.getNextRoom(myRoom), "bar"))
                        d.unlock(d.getNextRoom(myRoom), "bar");
                    if(d.isLocked(myRoom, "bar"))//TODO optimize this mess
                        d.unlock(d.myRoom, "bar");
                    yes = true;
                }
                if(yes) Utils.slowPrintln("You successfully removed all door bars.");
                else Utils.slowPrintln("That does nothing.");
                break;
            case 3:
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
        ArrayList<Interactible> inters = new ArrayList<>(myRoom.interactibles);
        for(Interactible i : new ArrayList<>(inters)) if(i.actionVerb.isEmpty() || !i.isEnabled || (i instanceof Door && !((Door)i).isLocked(myRoom))) Utils.remove(inters, i); //WallInteractibles override .equals() and need to be removed with Utils

        Interactible chosen = inters.get(Utils.promptList("What do you interact with?", Utils.actionDescsOf(inters)));

        ptolomyDoesSomething(new String[] {"lurks ominously","seems pleased"});

        chosen.action(this);
    }

    private void leave()
    {
        ArrayList<Interactible> doors = new ArrayList<>(myRoom.getDoors());

        new Door.Diagram(myRoom.getDoors(), this);

        if(doors.size() == 1) doors.getFirst().action(this);
        else doors.get(Utils.promptList("Which door?", Utils.actionDescsOf(doors))).action(this);
    }

    private void inventory()
    {
        ArrayList<Item> its = inv.all();
        String[] n = Utils.namesOf(its),
                 d = Utils.descriptionsOf(its),
                 prompts = new String[inv.size()];
        for(int i = 0; i < prompts.length; i++) prompts[i] = n[i] + ": " + d[i];

        Item i = inv.at(Utils.promptList("Which item do you choose? (This is your inventory, you can hold " + inv.max() + " items total)", prompts));
        if(i instanceof Affector) 
        {
            ArrayList<Describable> descs = new ArrayList<>(myRoom.all());
            descs.addAll(myRoom.interactibles);
            
            ((Affector)i).action(this, descs.get(Utils.promptList("Use this item on what?", Utils.namesOf(descs))));
        }
        else if(i instanceof Actor)
            ((Actor)i).action(this, false);
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

    private boolean isInCombat;
    @Override
    public void updateUnit()
    {
        System.out.println("\t\t\t\t\t\t\t\t--" + Utils.possessiveOf(name) + " Turn--");
        for(Effect e : new ArrayList<>(effects)) if(effectUpdate(e) == EffectUpdateResult.DEATH) return;

        doorMoves = 2;
        ableToAct = true;

        if(ptolomyIsPresent) Utils.slowPrintln(Utils.rand.nextFloat() <= .5 ? "You feel a strange presence... It's Ptolomy's spirit!" : "Ptolomy's spirit is lingering ever so elegantly", ptolomyPrintLength);

        myRoom.update();

        while(ableToAct) 
        {
            ableToAct = false;

            isInCombat = myRoom.NPCs.stream().anyMatch(n -> n.enemies.contains(this));
            setActions();
            
            //lists available actions, lets the player choose, then performs chosen action
            System.out.println();
            Game.printInfo(myRoom, false);
            System.out.println();

            performAction(Utils.promptList("You can:", getPlayerActionDescriptions()));
        }
    }

    @Override
    public Inventory.Whole getInventory() 
    {
        return inv;
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