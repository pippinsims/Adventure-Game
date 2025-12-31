package adventuregame;

import adventuregame.Effectable.EffectUpdateResult;
import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Unit;
import adventuregame.interactibles.ItemHolder;
import adventuregame.interactibles.SkeletonInteractible;
import adventuregame.items.Armor;
import adventuregame.items.Sword;
import adventuregame.items.Weapon;

public class QuickTimeEvent
{
    //TODO stealth scenarios with QTEs: lockpicking and sneaking past/into a room
    int currentRound;
    int currentPrompt = 0;
    int maxLength;
    int dur = 2000;
    String[] questions;
    String[][] prompts;
    String[] goodAnswers;
    String[] badAnswers;
    Unit actor;
    private Room room; //used in output() and fail()
    Describable sender;
    Effect effectToApply;
    boolean isOneQuestion = false;

    Node current;

    public QuickTimeEvent(Unit actor, Describable sender, Node.B answerTree)
    {
        this.actor = actor;
        this.sender = sender;
        maxLength = answerTree.getMaxLength();
        isOneQuestion = maxLength == 1;
        current = answerTree;
    }

    public QuickTimeEvent(Unit actor, Describable sender, int length, String question, String[] prompts)
    {
        maxLength = length;
        this.questions = new String[] { question };
        this.prompts = new String[][] { prompts };
        this.actor = actor;
        room = actor.getRoom();
        this.sender = sender;
        isOneQuestion = true;
    }

    public QuickTimeEvent(Unit actor, Describable sender, int length, String[] questions, String[][] prompts, String[] goodAnswers, String[] badAnswers)
    {
        maxLength = length;
        this.questions = questions;
        this.goodAnswers = goodAnswers;
        this.badAnswers = badAnswers != null ? badAnswers : new String[0]; 
        this.prompts = prompts;
        this.actor = actor;
        room = actor.getRoom();
        this.sender = sender;
    }

    public QuickTimeEvent(Unit actor, Describable sender, String[] questions, String[][] prompts, String[] goodAnswers, String[] badAnswers, Effect e)
    {
        maxLength = e.cooldown.getRemainingDuration();
        this.questions = questions;
        this.goodAnswers = goodAnswers;
        this.badAnswers = badAnswers != null ? badAnswers : new String[0]; 
        this.prompts = prompts;
        this.actor = actor;
        room = actor.getRoom();
        this.sender = sender;
        effectToApply = e;
    }

    static class InputThread extends Thread {
        public String input = null;
        protected Thread main;
        public InputThread(Thread main) { this.main = main; }
    }

    public boolean go()
    {
        boolean out = run();
        if(!out) fail();
        return out;
    }

    private boolean run()
    {
        InputThread inputThr = new InputThread(Thread.currentThread()) {
            public void run() 
            {
                if(isOneQuestion)
                { 
                    int in = Utils.promptList(questions[0], prompts[0]);
                    if(in < 0) return;
                    input = output(prompts[0][in]) ? "" : null;
                    main.interrupt();
                }
                else
                {
                    while(true)
                    {
                        input = Utils.advancedPromptList(questions, prompts, currentPrompt);
                        if(input == null) return;
                        
                        if(Utils.linearFind(goodAnswers, input) >= 0) //TODO would be nice if this could also be a puzzle with a text input not just promptlist
                        {
                            if(actor.getHealth() > 0 && output(input)) main.interrupt();
                            break;
                        }
                        else if(Utils.linearFind(badAnswers, input) >= 0) currentPrompt++; //TODO Node structure (as mentioned in another to-do) would make this a bit nicer, could have multiple paths
                    }
                }
                // System.out.println("did finish");
            }
        };

        inputThr.start();
        
        if(effectToApply != null) actor.addEffect(effectToApply);

        String input = null;
        for(currentRound = 0; currentRound < maxLength; currentRound++)
        {
            try { Thread.sleep(dur); } //TODO still breaks on "Cry Out."
            catch (InterruptedException e) {
                try { 
                    inputThr.join();
                    input = inputThr.input;
                    if(input == null) return false; //does: if isOneQuestion return InputThread.output(x)
                }
                catch (InterruptedException e1) { e1.printStackTrace(); } 
            }

            if(input == null && effectToApply != null && actor.effectUpdate(effectToApply) == EffectUpdateResult.DEATH) break; //died

            if(input != null) return true; //succeeded
        }
        inputThr.interrupt();
        return false; //ran out of time
    }

    public void setWaitDuration(int seconds)
    {
        dur = seconds * 1000;
    }

    //expand this for every new QuickTimeEvent
    private boolean output(String in)
    {
        if(sender instanceof Sword && sender.getName().equals("Cledobl"))
        {
            boolean succeeded = false;
            if(in.equals("Cry out.")) 
            {
                if(room.players.size() == 1) return false;
                else
                {
                    Unit helper = null;
                    for(Unit u : room.players) if(u != actor) helper = u;
                    QuickTimeEvent help = new QuickTimeEvent(
                        helper, 
                        new Describable() { { description = "helpcledobl"; } },
                        maxLength - currentRound, 
                        helper.getName() + ", do you help?", 
                        new String[] {"Help.","Do not help."}
                    );
                    succeeded = help.go();
                }
            }
            else if(in.equals("Pry hand violently."))
            {
                System.out.println("You pry your catatonic fingers from the lethal power of the blade's enchantment with your free hand.");
                succeeded = true;
            }
            if(succeeded) 
            {
                actor.removeAllOf(Effect.Type.VITALITYDRAIN);
                switch(currentRound + 1)
                {
                    case 1: case 2: case 3: 
                        actor.maxHealth += currentRound;
                        actor.addEffect(new Effect(Effect.Type.WEAKNESS, 1, 1));
                        break;
                    case 4: case 5: 
                        actor.addEffect(new Effect(Effect.Type.VITALITYGROW, 3, currentRound/3));
                        actor.addEffect(new Effect(Effect.Type.WEAKNESS, 6, 1));
                        break;
                    case 6: case 7: 
                        actor.addEffect(new Effect(Effect.Type.WEAKNESS, 10, 1));
                        break;
                    case 8: case 9:
                        actor.addEffect(new Effect(Effect.Type.WEAKNESS, -1, 1));
                        break;
                }
            }
            return succeeded;
        }
        else if(sender.getDescription().equals("helpcledobl"))
        {
            if(in.equals("Help."))
            {
                actor.removeAllOf(Effect.Type.VITALITYDRAIN);
                return true;
            }
            return false;
        }

        throw new UnsupportedOperationException("QuickTimeEvent answer: '" + in + "' not recognized.");
    }

    private void fail()
    {
        switch(sender.getName())
        {
            case "Cledobl":
                ItemHolder cleholder = Utils.getFirst(room.interactibles,ItemHolder.class);
                cleholder.isEnabled = false;
                SkeletonInteractible yourBody = new SkeletonInteractible(
                    room,
                    actor.getName()+"'s body", 
                    "new-looking skeleton",
                    "gripped tightly to",
                    "",
                    "",
                    "brush",
                    "aside from it's grip on",
                    "the sword"
                )
                {
                    @Override 
                    public void action(Unit u)
                    {
                        getRoom().interactibles.remove(this);
                        Utils.slowPrintln("You brush the hand of the skeleton away from the sword, causing it to crumble to the floor.");
                        new SkeletonInteractible(
                            getRoom(), 
                            name, 
                            simpleDesc,
                            "on",
                            "new-looking skeletons",
                            "",
                            "loot",
                            "",
                            "the floor",
                            inv,
                            insMap
                        );
                        cleholder.isEnabled = true;
                    }
                };
                
                for(Armor a : actor.getInventory().getArmor()) yourBody.add(a);
                Weapon w = Utils.getFirst(actor.getInventory().getWeapons(), Weapon.class);
                if(w != null) yourBody.add(w);
                break;
        }
    }

    public static class Node
    {
        String prompt;
        String[] prompts;

        public static class B extends Node
        {
            Node[] nodes;
            B(String prompt, String[] prompts, Node[] nodes)
            {
                this.prompt = prompt;
                this.prompts = prompts;
                this.nodes = nodes;
                for (Node node : nodes) {
                    node.setlen
                }
            }

            public int getMaxLength()
            {
                return 10;
            }
        }

        public static class L extends Node
        {
            Output out;
            L(Output type) { out = type; }
        }

        public static enum Output
        {
            GOOD,
            NEUTRAL
        }
    }
}