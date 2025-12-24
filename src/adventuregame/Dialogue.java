package adventuregame;

import java.util.ArrayList;

import adventuregame.abstractclasses.Unit;

public class Dialogue
{
    ArrayList<Unit> actors;
    Node current;

    public Dialogue(ArrayList<Unit> actors, Node start)
    {
        this.actors = actors;
        current = start;
    }

    public int next()
    {
        int path = Utils.promptList(current.prompt, current.prompts);
        if(current.nodes != null)
        {
            current = current.nodes[path];
            current.setPath(path);
            return next();
        }
        return current.pathNum;
    }

    private class Node
    {
        String prompt;
        String[] prompts;
        Node[] nodes;
        int pathNum;

        public Node(Unit actor, String prompt, String[] prompts)
        {
            this.prompt = actor.getName() + ": " + prompt;
            this.prompts = prompts;
            pathNum = prompts.length;
        }

        public Node(Unit actor, String prompt, String[] prompts, Node[] nodes)
        {
            this.prompt = actor.getName() + ": " + prompt;
            this.prompts = prompts;
            this.nodes = nodes;
            pathNum = prompts.length;
        }

        public void setPath(int path)
        {
            pathNum += path;
        }
    }
}