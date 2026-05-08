package adventuregame;

import java.util.ArrayList;

import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.NonPlayer;
import adventuregame.abstractclasses.Unit;

public class Dialogue
{
    Unit initiator;
    ArrayList<Unit> actors;
    Node start;
    Node current;
    Player to;
    int num;
    boolean atEnd = false;              //atEnd when on last Node
    boolean doRepeat = false;
    private boolean isComplete = false; //isComplete when Player has moved after Dialogue

    public Dialogue(Unit initiator, ArrayList<Unit> actors, Node start)
    {
        this.initiator = initiator;
        this.actors = actors;
        this.start = current = start;
    }

    private boolean allActorsAlive()
    {
        return actors.stream().anyMatch(a -> a.isDead());
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

    public void setRepeat() { doRepeat = true; }

    public Node getCurrent() { return current; }

    public boolean isAtEnd() { return atEnd; }

    public boolean isComplete() { return isComplete; }

    public boolean next() 
    { 
        to = actors.get(0).getRoom().players.get(0);
        Unit a = getCurrentActor(); 
        if(allActorsAlive()) 
        {
            next(a);
            ((L<?>)current).output(this);
            if(allActorsAlive() && doRepeat) current = start;
            else atEnd = true;
            return true;
        }
        return false;
    }

    private void next(Unit actor)
    {   
        int path = current.prompt != null ? Utils.promptList(actor.getName() + " to " + to.getName() + ": " + current.prompt + (current.prompts != null ? "\n"+to.getName()+":" : ""), current.prompts) : -1;
        if(current instanceof B && ((B)current).nodes != null)
        {
            current = ((B)current).nodes[path];
            next(actors.get(current.actor));
        }
    }

    static abstract class Node
    {
        int actor;
        String prompt;
        String[] prompts;
    }

    static class L<T extends Describable> extends Node //L for Leaf
    {
        T out;
        boolean applyToAll;

        public L(int actor, String prompt, String[] prompts, T out, boolean applyToAll)
        {
            this(out, applyToAll);
            this.actor = actor;
            this.prompt = prompt;
            this.prompts = prompts;
        }

        public L(T out, boolean applyToAll)
        {
            this.out = out;
            this.applyToAll = applyToAll;
        }

        public L() {}

        public void output(Dialogue parent) 
        {
            Player to = parent.to;
            if(out instanceof Room)
            {
                Room r = (Room)out;
                //TODO add pathfinding to make it be able to say "All players in rooms between curp.getName's room and out.getName moved back to out.getName"
                if(applyToAll)
                {
                    Utils.slowPrintln("All players in " + Utils.possessiveOf(to.getName()) + " room moved back to " + r.getName());
                    for(Player p : to.getRoom().clearPlayers()) r.add(p);
                }
                else
                {
                    Utils.slowPrintln(to.getName() + " room moved back to " + r.getName());
                    to.getRoom().remove(to); r.add(to);
                }
                parent.setRepeat();
            }
            else if(out instanceof Effect)
            {
                Effect e = (Effect)out;
                if(applyToAll)
                {
                    Utils.slowPrintln("Effect '" + e.getName() + "' added to all in " + Utils.possessiveOf(to.getName()) + " room");
                    for(Player p : Game.curRoom.players) p.addEffect(new Effect(e));
                }
                else
                {
                    Utils.slowPrintln("Effect '" + e.getName() + "' added to " + to.getName());
                    to.addEffect(e);
                }
            }
            else if(out instanceof Item)
            {
                Item i = (Item)out;
                if(applyToAll)
                {
                    Utils.slowPrintln("Item '" + i.getName() + "' added to all in " + Utils.possessiveOf(to.getName()) + " room");
                    for(Player p : Game.curRoom.players) p.getInventory().add(Item.clone(i));
                }
                else
                {
                    Utils.slowPrintln("Item '" + i.getName() + "' added to " + to.getName());
                    to.getInventory().add(i);
                }
            }
        }
    }

    static class X extends L<Describable>
    {
        public X() {}
        public X(int actor, String prompt) 
        {
            this.actor = actor; 
            this.prompt = prompt;
        }
        @Override public void output(Dialogue parent) {}
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

    public static void aggroAllOfSameType(Unit from)
    {
        Utils.slowPrintln("All the " + from.getPluralDescription() + " prepare to fight!");
        for(NonPlayer to : from.getRoom().NPCs) if(from.getClass().isInstance(to)) to.setHostile();
    }
}