package adventuregame;

import adventuregame.Effectable.EffectUpdateResult;
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

    static class InputThread extends Thread {
        public String input = null;
        protected Thread main;
        public InputThread(Thread main) { this.main = main; }
    }

    public void run()
    {
        QuickTimeEvent parent = this;
        InputThread inputThr = new InputThread(Thread.currentThread()) {
            public void run() 
            {
                    while(current instanceof Node.B)
                    {
                        input = null;
                        int ind = Utils.promptList(current.prompt, current.prompts);
                        if(ind < 0) break;

                        input = current.prompts[ind];
                        Node next = ((Node.B)current).nodes[ind];

                        if(next instanceof Node.B) current = next;
                        else if(next instanceof Node.X) continue;
                        else {
                            Node.L leaf = ((Node.L)next);
                            if(leaf.output(input, parent) || leaf.out != Output.CHECK) break;
                        }
                    }
                    main.interrupt();
            }
        };

        inputThr.start();
        
        if(effectToApply != null) actor.addEffect(effectToApply);

        String input = null;
        for(currentRound = 0; currentRound < maxLength || maxLength < 0; currentRound++)
        {
            try { Thread.sleep(dur); }
            catch (InterruptedException e) {
                try { 
                    inputThr.join();
                    input = inputThr.input;
                    if(input == null) return; //input was interrupted
                }
                catch (InterruptedException e1) { e1.printStackTrace(); } 
            }

            if(input == null && effectToApply != null && actor.effectUpdate(effectToApply) == EffectUpdateResult.DEATH) break; //died

            if(input != null) return; //succeeded, inputThr must be joined.
        }
        inputThr.interrupt();
        timeout();
    }

    public void setWaitDuration(int seconds)
    {
        dur = seconds * 1000;
    }

    protected abstract void timeout();

    public static abstract class Node
    {
        String prompt;
        String[] prompts;

        int len = 0;

        public int addLength() { return ++len; }

        public static class B extends Node
        {
            Node[] nodes;
            int maxLength;
            public B(String prompt, String[] prompts, Node[] nodes)
            {
                this.prompt = prompt;
                this.prompts = prompts;
                this.nodes = nodes;
                maxLength = addLength();
            }

            public int addLength() {
                super.addLength();
                int max = 0;
                for(Node n : nodes) 
                {
                    int cur = n.addLength();
                    if(cur > max) max = cur;
                }
                return len;
            }

            public int getMaxLength() { return maxLength; }
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