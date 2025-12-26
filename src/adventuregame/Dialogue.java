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
        return current.actor < actors.size() ? actors.get(current.actor) : null;
    }

    public void next() 
    { 
        Unit a = getCurrentActor(); 
        if(a != null) next(a); 
    }

    private void next(Unit actor)
    {
        for(Unit a : actors) if(a.getRoom() == null) return; //no actors dead
        
        int path = current.prompt != null ? Utils.promptList(actor.getName() + ": " + current.prompt, current.prompts) : -1;
        if(current instanceof Node.B && ((Node.B)current).nodes != null)
        {
            current = ((Node.B)current).nodes[path];
            next(actors.get(current.actor));
        }
        else actors.getFirst().getRoom().dialogues.remove(this);
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

            public Class<T> getGenericClass()
            {
                return (Class<T>)out.getClass();
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

            public B() {}
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

    public static void processLeaf(Dialogue.Node node)
    {
        if(node instanceof Dialogue.Node.L)
        {
            System.out.println("leaf");
            Dialogue.Node.L<?> n = (Dialogue.Node.L<?>)node;
            Player p0 = Environment.curPlayer;
            String name = p0.getName();
            if(n.out instanceof Room)
            {
                //TODO add pathfinding to make it be able to say "All players in rooms between curp.getName's room and out.getName moved back to out.getName"
                Room out = (Room)n.out;
                if(n.applyToAll)
                {
                    System.out.println("All players in " + name + "'s room moved back to " + out.getName());
                    for(Player p : Environment.curRoom.players) out.add(p);
                    Environment.curRoom.players.clear();
                }
                else
                {
                    System.out.println(name + " moved back to " + out.getName());
                    out.add(p0);
                    Environment.curRoom.players.remove(p0);
                }
            }
            else if(n.out instanceof Effect)
            {
                Effect out = (Effect)n.out;
                if(n.applyToAll)
                {
                    System.out.println("Effect '" + out.getName() + "' added to all in " + name + "'s room");
                    for(Player p : Environment.curRoom.players) p.addEffect(new Effect(out));
                }
                else
                {
                    System.out.println("Effect '" + out.getName() + "' added to " + name);
                    p0.addEffect(out);
                }
            }
            else if(n.out instanceof Item)
            {
                Item out = (Item)n.out;
                if(n.applyToAll)
                {
                    System.out.println("Item '" + out.getName() + "' added to all in " + name + "'s room");
                    for(Player p : Environment.curRoom.players) p.getInventory().add(out);
                }
                else
                {
                    System.out.println("Item '" + out.getName() + "' added to " + name);
                    p0.getInventory().add(out.clone());
                }
            }
        }
        else System.out.println("bran");
    }
}