package adventuregame;

import java.util.ArrayList;

import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;

public class Dialogue
{
    ArrayList<Unit> actors;
    Node current;
    int num;

    public Dialogue(ArrayList<Unit> actors, Node start)
    {
        this.actors = actors;
        current = start;
    }

    public Unit getCurrentActor()
    {
        return actors.get(current.actor); //TODO make Dialogue only run if all actors are present
    }

    public void next()
    {
        next(getCurrentActor());
    }

    private void next(Unit actor)
    {
        if(actor.getRoom() != null)
        {
            int path = Utils.promptList(actor.getName() + ": " + current.prompt, current.prompts);
            if(current instanceof Node.B && ((Node.B)current).nodes != null)
            {
                current = ((Node.B)current).nodes[path];
                next(actors.get(current.actor));
            }
            else actors.getFirst().getRoom().dialogues.remove(this);
        }
    }

    static abstract class Node
    {
        int actor;
        String prompt;
        String[] prompts;
        
        static class L<T extends Describable> extends Node //L for Leaf
        {
            T out;
            boolean applyToAll;

            public L(int actor, String prompt, String[] prompts, T out, boolean applyToAll)
            {
                this.actor = actor;
                this.prompt = prompt;
                this.prompts = prompts;
                this.out = out;
                this.applyToAll = applyToAll;
            }

            public L(T out, boolean applyToAll)
            {
                this.out = out;
                this.applyToAll = applyToAll;
            }
        }

        static class B extends Node //B for Branch
        {
            Node[] nodes;

            public B(int actor, String prompt, String[] prompts, Node[] nodes)
            {
                this.actor = actor;
                this.prompt = prompt;
                this.prompts = prompts;
                this.nodes = nodes;
            }

            public B(int actor, String prompt)
            {
                this.actor = actor;
                this.prompt = prompt;
            }
        }

        //Thought this would be cool but it was unnecessary, now it's here as a builder pattern reference
        // static class LNodeBuilder<T extends Describable>
        // {
        //     T out;
        //     public LNodeBuilder(T out) { this.out = out; }
        //     int actor = -1         ; public LNodeBuilder<T> actor(int a)        { actor = a  ; return this; }
        //     String prompt = null   ; public LNodeBuilder<T> prompt(String p)    { prompt = p ; return this; }
        //     String[] prompts = null; public LNodeBuilder<T> prompts(String[] p) { prompts = p; return this; }
        //     boolean all = false    ; public LNodeBuilder<T> all()               { all = true ; return this; }
        //     public L<T> build() { return new L<T>(actor, prompt, prompts, out, all); }
        // }
    }

    public static void processNode(Dialogue.Node node)
    {
        if(node instanceof Dialogue.Node.L) getOutput((Dialogue.Node.L<?>)node);
    }

    private static <T extends Describable> void getOutput(Dialogue.Node.L<T> node)
    {
        if(node.out instanceof Room)
        {
            //TODO add pathfinding to make it be able to say "All players in rooms between curp.getName's room and out.getName moved back to out.getName"
            Room out = (Room)node.out;
            System.out.println("All players in " + Environment.curPlayer.getName() + "'s room moved back to " + out.getName());
            for(Player p : Environment.curRoom.players) out.add(p);
            Environment.curRoom.players.clear();
        }
        else if(node.out instanceof Effect)
        {
            Effect out = (Effect)node.out;
            if(node.applyToAll)
            {
                System.out.println("Effect '" + out.getName() + "' added to all in " + Environment.curPlayer.getName() + "'s room");
                for(Player p : Environment.curRoom.players) p.addEffect(new Effect(out));
            }
            else
            {
                System.out.println("Effect '" + out.getName() + "' added to " + Environment.curPlayer.getName());
                Environment.curPlayer.addEffect(out);
            }
        }
        else if(node.out instanceof Item)
        {
            Item out = (Item)node.out;
            if(node.applyToAll)
            {
                System.out.println("Effect '" + out.getName() + "' added to all in " + Environment.curPlayer.getName() + "'s room");
                for(Player p : Environment.curRoom.players) p.getInventory().add(out);
            }
            else
            {
                System.out.println("Effect '" + out.getName() + "' added to " + Environment.curPlayer.getName());
                Environment.curPlayer.getInventory().add(out.clone());
            }
        }
    }
}