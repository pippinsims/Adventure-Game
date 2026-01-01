package test;

import adventuregame.Effect;
import adventuregame.Player;
import adventuregame.QuickTimeEvent;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.Effect.Type;
import adventuregame.QuickTimeEvent.Node;
import adventuregame.QuickTimeEvent.Node.Output;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;

public class testclass {
    
    public static void main(String[] args)
    {   
        for(String s : new String[] {
            "QTE"
        })
            switch(s)
            {
                case "QTE":
                    for(int i : new int[]{
                        //0, succeeded as far as I could tell 
                        //1, succeeded
                        //2, succeeded
                        //3, succeeded
                        //4, succeeded 
                        //5, succeeded
                        6,//player death handling, nested QTE, etc
                    })
                    {
                        System.out.println("--Begin Test--");
                        switch(i)
                        {
                            case 0: //Timeless QTE
                                new QuickTimeEvent(
                                    new Player("Guy", 10), 
                                    new Item() { @Override public void action(Unit u, boolean isFinal){} }, 
                                    -1,
                                    new Node.B("Test timeless QTE", new String[]{""}, new Node[]{new Node.X()})
                                ) { @Override protected void timeout() { throw new UnknownError("Timeless QTE timed out"); } }
                                .run();
                                break;
                            case 1: //QTE.timeout()
                                new QuickTimeEvent(
                                    new Player("Guy", 10), 
                                    new Item() { @Override public void action(Unit u, boolean isFinal){} }, 
                                    3,
                                    new Node.B("Test timeout after 3 ticks", new String[]{"",}, new Node[] {new Node.X()})
                                ) { @Override protected void timeout() { System.out.println("timed out"); } }
                                .run();
                                break;
                            case 2: //Node.X and no-output Node.L
                                new QuickTimeEvent(
                                    new Player("Guy", 10), 
                                    new Item() { @Override public void action(Unit u, boolean isFinal){} }, 
                                    -1,
                                    new Node.B("Test Node.X and no-output Node.L", new String[]
                                    {
                                        "test",
                                        "next"
                                    }, 
                                    new Node[]
                                    {
                                        new Node.X(),
                                        new Node.L(null){@Override public boolean output(String in,QuickTimeEvent q){ System.out.println("yes"); return true; }}
                                    })
                                ) { @Override protected void timeout() {} }
                                .run();
                                break;
                            case 3: //QTE.Node.L(END) output(String, QuickTimeEvent) override
                                new QuickTimeEvent(
                                    new Player("Guy", 10), 
                                    new Item() { @Override public void action(Unit u, boolean isFinal){} }, 
                                    -1,
                                    new Node.B("Test QTE.Node.L(END).output(..)", new String[]{"Test Output.END"}, 
                                    new Node[] { new Node.L(Output.END){@Override public boolean output(String in, QuickTimeEvent q){ System.out.println("got: " + in); return true; }}})
                                ) { @Override protected void timeout() {} }
                                .run();
                                break;
                            case 4: //QTE.Node.L(CHECK) output(String, QuickTimeEvent) override
                                new QuickTimeEvent(
                                    new Player("Guy", 10), 
                                    new Item() { @Override public void action(Unit u, boolean isFinal){} }, 
                                    -1,
                                    new Node.B("Test QTE.Node.L(CHECK).output(..)", new String[]{"Test Output.CHECK"}, 
                                    new Node[]
                                    {
                                        new Node.L(Output.CHECK){@Override public boolean output(String in, QuickTimeEvent q){ 
                                            return Utils.promptList("check:", new String[] {"good","bad"}) == 0; }}
                                    })
                                ) { @Override protected void timeout() {} }
                                .run();
                                break;
                            case 5: //Player.effectUpdate()
                                Player p = new Player("Guy", 10);
                                System.out.println(p.getName() + ": " + p.getHealth() + " health.");
                                new QuickTimeEvent(
                                    p, 
                                    new Item() { @Override public void action(Unit u, boolean isFinal){} }, 
                                    new Effect(Type.FIRE, 3, 1),
                                    new Node.B("Test QTE Fire effect", new String[]{""}, new Node[] {new Node.X()})
                                ) { @Override protected void timeout() {} }
                                .run();
                                System.out.println(p.getName() + ": " + p.getHealth() + " health.");
                                break;
                            case 6: //Player.effectUpdate() == EffectUpdateResult.DEATH
                                p = new Player("Guy", 3);
                                p.setRoom(new Room());
                                System.out.println(p.getName() + ": " + p.getHealth() + " health.");
                                new QuickTimeEvent(
                                    p, 
                                    new Item() { @Override public void action(Unit u, boolean isFinal){} }, 
                                    new Effect(Type.FIRE, 10, 1),
                                    new Node.B("Test QTE Effect death handling QTE.dur > health", new String[]{""}, new Node[] {new Node.X()})
                                ) { @Override protected void timeout() {} }
                                .run();
                                System.out.println(p.getName() + ": " + p.getHealth() + " health.");
                                
                                p = new Player("Guy", 3);
                                p.setRoom(new Room());
                                System.out.println(p.getName() + ": " + p.getHealth() + " health.");
                                new QuickTimeEvent(
                                    p, 
                                    new Item() { @Override public void action(Unit u, boolean isFinal){} }, 
                                    new Effect(Type.FIRE, 3, 1),
                                    new Node.B("Test QTE Effect death handling duration == health", new String[]{""}, new Node[] {new Node.X()})
                                ) { @Override protected void timeout() {} }
                                .run();
                                System.out.println(p.getName() + ": " + p.getHealth() + " health.");

                                p = new Player("Guy", 10);
                                p.setRoom(new Room());
                                System.out.println(p.getName() + ": " + p.getHealth() + " health.");
                                new QuickTimeEvent(
                                    p, 
                                    new Item() { @Override public void action(Unit u, boolean isFinal){} }, 
                                    new Effect(Type.FIRE, 3, 1),
                                    new Node.B("Test QTE Effect handling QTE.dur < health", new String[]{""}, new Node[] {new Node.X()})
                                ) { @Override protected void timeout() {} }
                                .run();
                                System.out.println(p.getName() + ": " + p.getHealth() + " health.");
                                break;
                        }
                        System.out.println("--End Test--");
                    }
                    break;
            }
        }
}
