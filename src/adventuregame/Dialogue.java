package adventuregame;

import java.util.ArrayList;

import adventuregame.abstractclasses.Describable;
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

        num = 0;
        setLeafIndices(start);
    }

    private void setLeafIndices(Node cur)
    {
        if(cur.nodes == null) cur.setPath(num++);
        else for(Node n : cur.nodes) setLeafIndices(n);
    }

    public Unit getCurrentActor()
    {
        return actors.get(current.actor);
    }

    public int next()
    {
        return next(getCurrentActor());
    }

    private int next(Unit actor)
    {
        if(actor.getRoom() != null)
        {
            int path = Utils.promptList(actor.getName() + ": " + current.prompt, current.prompts);
            if(current.nodes != null)
            {
                current = current.nodes[path];
                current.setPath(path);
                return next(actors.get(current.actor));
            }
        }
        return current.pathIndex;
    }

    static class Node
    {
        int actor;
        String prompt;
        String[] prompts;
        Node[] nodes;
        int pathIndex;
        Effect outEffect;
        Room outRoom;
        Out out;

        enum Out
        {
            NONE,
            ROOM,
            EFFECTONE,
            EFFECTALL
        }
        
        public Node(Out out, Describable d) 
        { 
            this.out = out; 
            if(d instanceof Effect) outEffect = (Effect)d;
            else if (d instanceof Room) outRoom = (Room)d;
        }

        public Node(int actor, String prompt, Out out, Describable d)
        {
            this.actor = actor;
            this.prompt = prompt;
            this.out = out; 
            if(d instanceof Effect) outEffect = (Effect)d;
            else if (d instanceof Room) outRoom = (Room)d; 
        }

        public Node(int actor, String prompt, String[] prompts, Out out, Describable d)
        {
            this.actor = actor;
            this.prompt = prompt;
            this.prompts = prompts;
            this.out = out;
            if(d instanceof Effect) outEffect = (Effect)d;
            else if (d instanceof Room) outRoom = (Room)d;
        }

        public Node(int actor, String prompt, String[] prompts, Node[] nodes)
        {
            this.actor = actor;
            this.prompt = prompt;
            this.prompts = prompts;
            this.nodes = nodes;
        }

        public void setPath(int path)
        {
            pathIndex = path;
        }
    }
}