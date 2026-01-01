package adventuregame;

import java.util.ArrayList;

import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;

public class Dialogue
{
    Unit initiator;
    ArrayList<Unit> actors;
    Node current;
    Player to;
    int num;
    boolean atEnd = false;              //atEnd when on last Node
    private boolean isComplete = false; //isComplete when Player has moved after Dialogue

    public Dialogue(Unit initiator, ArrayList<Unit> actors, Node start)
    {
        this.initiator = initiator;
        this.actors = actors;
        current = start;
    }

    private boolean allActorsAlive()
    {
        for(Unit a : actors) if(a.getRoom() == null) return false;
        return true;
    }

    public Unit getCurrentActor()
    {
        return current.actor < actors.size() ? actors.get(current.actor) : null;
    }

    public Unit getInitiator()
    {
        return initiator;
    }

    public void complete()
    {
        isComplete = true;
    }

    public Node getCurrent() { return current; }

    public boolean isAtEnd()
    {
        return atEnd;
    }

    public boolean isComplete()
    {
        return isComplete;
    }

    public boolean next() 
    { 
        to = actors.get(0).getRoom().players.get(0);
        Unit a = getCurrentActor(); 
        if(allActorsAlive()) 
        {
            next(a);
            atEnd = true;
            processLeaf();
            return true;
        }
        return false;
    }

    private void next(Unit actor)
    {   
        int path = current.prompt != null ? Utils.promptList(actor.getName() + " to " + to.getName() + ": " + current.prompt + (current.prompts != null ? "\n"+to.getName()+":" : ""), current.prompts) : -1;
        if(current instanceof Node.B && ((Node.B)current).nodes != null)
        {
            current = ((Node.B)current).nodes[path];
            next(actors.get(current.actor));
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

            public L(int actor, String prompt) 
            {
                this.actor = actor; 
                this.prompt = prompt;
            }

            public L() {} 
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
        }
    }

    public void processLeaf()
    {
        if(current instanceof Dialogue.Node.L)
        {
            Dialogue.Node.L<?> n = (Dialogue.Node.L<?>)current;
            String name = to.getName();
            if(n.out instanceof Room)
            {
                //TODO add pathfinding to make it be able to say "All players in rooms between curp.getName's room and out.getName moved back to out.getName"
                Room out = (Room)n.out;
                if(n.applyToAll)
                {
                    System.out.println("All players in " + name + "'s room moved back to " + out.getName());
                    for(Player p : Game.curRoom.players) out.add(p);
                    Game.curRoom.players.clear();
                }
                else
                {
                    System.out.println(name + " moved back to " + out.getName());
                    out.add(to);
                    Game.curRoom.players.remove(to);
                }
            }
            else if(n.out instanceof Effect)
            {
                Effect out = (Effect)n.out;
                if(n.applyToAll)
                {
                    System.out.println("Effect '" + out.getName() + "' added to all in " + name + "'s room");
                    for(Player p : Game.curRoom.players) p.addEffect(new Effect(out));
                }
                else
                {
                    System.out.println("Effect '" + out.getName() + "' added to " + name);
                    to.addEffect(out);
                }
            }
            else if(n.out instanceof Item)
            {
                Item out = (Item)n.out;
                if(n.applyToAll)
                {
                    System.out.println("Item '" + out.getName() + "' added to all in " + name + "'s room");
                    for(Player p : Game.curRoom.players) p.getInventory().add(out);
                }
                else
                {
                    System.out.println("Item '" + out.getName() + "' added to " + name);
                    to.getInventory().add(out.clone());
                }
            }
        }
    }
}