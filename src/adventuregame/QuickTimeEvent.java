package adventuregame;

import adventuregame.Effectable.EffectUpdateResult;
import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Unit;
import adventuregame.items.Sword;

public class QuickTimeEvent
{
    //TODO stealth scenarios with QTEs: lockpicking and sneaking past/into a room
    int currentRound;
    int currentPrompt = 0;
    int maxLength;
    int dur = 2000;
    boolean doBreak = false;
    String[] questions;
    String[][] prompts;
    String[] goodAnswers;
    String[] badAnswers;
    Unit actor;
    Describable sender;
    Effect effectToApply;
    boolean isOneQuestion = false;

    public QuickTimeEvent(Unit actor, Describable sender, int length, String question, String[] prompts)
    {
        maxLength = length;
        this.questions = new String[] { question };
        this.prompts = new String[][] { prompts };
        this.actor = actor;
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
        this.sender = sender;
        effectToApply = e;
    }
    static class InputThread extends Thread {
        public String input = null;
        protected Thread main;
        public InputThread(Thread main) { this.main = main; }
    }

    public boolean run()
    {
        InputThread inputThr = new InputThread(Thread.currentThread()) {
            public void run() 
            {
                if(isOneQuestion)
                { 
                    input = output(prompts[0][Utils.promptList(questions[0], prompts[0])]) ? "" : null;
                    main.interrupt();
                }
                else
                {
                    while(true)
                    {
                        input = Utils.advancedPromptList(questions, prompts, currentPrompt);
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
            if(input == null && effectToApply != null && actor.effectUpdate(effectToApply) == EffectUpdateResult.DEATH) break; //died

            if(input != null) return true; //succeeded
            
            try { Thread.sleep(dur); }
            catch (InterruptedException e) {
                try { 
                    inputThr.join();
                    input = inputThr.input;
                    if(input == null) return false; //does: if isOneQuestion return InputThread.output(x)
                }
                catch (InterruptedException e1) { e1.printStackTrace(); } 
            }
        }
        Utils.restartScanner();
        return false; //ran out of time
    }

    public void setWaitDuration(int seconds)
    {
        dur = seconds * 1000;
    }

    public void breakEvent()
    {
        doBreak = true;
    }

    //expand this for every new QuickTimeEvent
    private boolean output(String in)
    {
        if(sender instanceof Sword && sender.getName().equals("Cledobl"))
        {
            boolean succeeded = false;
            if(in.equals("Cry out.")) 
            {
                if(actor.getRoom().players.size() == 1) return false;
                else
                {
                    Unit helper = null;
                    for(Unit u : actor.getRoom().players) if(u != actor) helper = u;
                    QuickTimeEvent help = new QuickTimeEvent(
                        helper, 
                        new Describable() {
                            @Override public String getPluralDescription() { return null; }
                            @Override public String getDescription() { return "helpcledobl"; }
                            @Override public String getName() { return null; }
                        },
                        maxLength - currentRound, 
                        helper.getName() + ", do you help?", 
                        new String[] {"Help.","Do not help."}
                    );
                    succeeded = help.run();
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
}