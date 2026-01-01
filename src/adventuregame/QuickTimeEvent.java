package adventuregame;

import adventuregame.Effectable.EffectUpdateResult;
import adventuregame.QuickTimeEvent.Node.B;
import adventuregame.QuickTimeEvent.Node.Output;
import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Unit;

public abstract class QuickTimeEvent
{
    //TODO stealth scenarios with QTEs: lockpicking and sneaking past/into a room
    int currentRound;
    int maxLength;
    int dur = 2000;
    protected Unit actor;
    Describable sender;
    Effect effectToApply;

    Node current;

    public QuickTimeEvent(Unit actor, Describable sender, int length, Node.B answerTree)
    {
        this.actor = actor;
        this.sender = sender;
        maxLength = length;
        current = answerTree;
    }
    public QuickTimeEvent(Unit actor, Describable sender, Effect e, Node.B answerTree)
    {
        this.actor = actor;
        this.sender = sender;
        maxLength = e.cooldown.getRemainingDuration();
        effectToApply = e;
        current = answerTree;
    }

    public Unit getActor() { return actor; }

    public int getCurrentRound() { return currentRound; }

    class InputThread extends Thread {
        public String input = null;
        private Thread main;
        private QuickTimeEvent q;
        public InputThread(Thread main, QuickTimeEvent q) 
        { 
            this.main = main;
            this.q = q; 
        }

        @Override
        public void run() 
        {
            while(current instanceof Node.B)
            {
                int ind = Utils.promptList(current.prompt, current.prompts);
                if(ind < 0) break;

                input = current.prompts[ind];
                Node next = ((Node.B)current).nodes[ind];

                if(next instanceof Node.B) current = next;
                else if(next instanceof Node.X) continue;
                else {
                    Node.L leaf = ((Node.L)next);
                    if(leaf.output(input, q) || leaf.out != Output.CHECK) break;
                }
            }
            main.interrupt();
            this.interrupt();
        }
    }

    public void run()
    {
        InputThread inputThr = new InputThread(Thread.currentThread(), this);

        if(effectToApply != null) actor.addEffect(effectToApply);
        
        inputThr.start();
        
        String input = null;
        for(currentRound = 0; currentRound < maxLength || maxLength < 0; currentRound++)
        {
            try { Thread.sleep(dur); }
            catch (InterruptedException e) { input = inputThr.input; }

            if(input != null) return;
            if(update()) return;
        }
        timeout();
    }

    protected abstract void timeout();

    /**
     * @return {@code boolean}, true means "end QTE"
     */
    protected abstract boolean update();

    public static abstract class EffectQTE extends QuickTimeEvent
    {
        public EffectQTE(Unit actor, Describable sender, Effect e, B answerTree) { super(actor, sender, e, answerTree); }

        @Override
        protected boolean update()
        {
            return effectToApply != null && actor.effectUpdate(effectToApply) == EffectUpdateResult.DEATH;
        }
    }

    public static abstract class NoUpdateQTE extends QuickTimeEvent
    {
        public NoUpdateQTE(Unit actor, Describable sender, int length, B answerTree) { super(actor, sender, length, answerTree); }

        @Override protected boolean update() { return false; }
    }

    public static abstract class Node
    {
        String prompt;
        String[] prompts;

        public static class B extends Node
        {
            Node[] nodes;
            int maxLength;
            public B(String prompt, String[] prompts, Node[] nodes)
            {
                this.prompt = prompt;
                this.prompts = prompts;
                this.nodes = nodes;
            }
        }

        public static abstract class L extends Node
        {
            Output out;
            public L(Output type) { out = type; }
            public abstract boolean output(String in, QuickTimeEvent q);
        }

        /**
         * X nodes act as end-of-path markers, non-output leaf nodes.
         */
        public static class X extends Node { public X() {} }

        /**
         * An enum to be used in differentiating between QTE output leaf nodes: {@link Node.L}, passed to the constructor
         * <ul>
         * <li> {@code CHECK} denotes to end QTE if leaf.output(...) == true
         * <li> {@code END} denotes to run leaf.output(...) then end QTE
         * </ul>
         */
        public static enum Output
        {
            CHECK,
            END
        }
    }
}