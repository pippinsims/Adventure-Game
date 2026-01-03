package test;

import java.util.ArrayList;
import java.util.Map;

import adventuregame.Effect;
import adventuregame.Game;
import adventuregame.Gzouca;
import adventuregame.Player;
import adventuregame.QuickTimeEvent;
import adventuregame.Room;
import adventuregame.Utils;
import adventuregame.Effect.Type;
import adventuregame.QuickTimeEvent.EffectQTE;
import adventuregame.QuickTimeEvent.NoUpdateQTE;
import adventuregame.QuickTimeEvent.Node;
import adventuregame.QuickTimeEvent.Node.Output;
import adventuregame.abstractclasses.Describable;
import adventuregame.abstractclasses.Enemy;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;
import adventuregame.items.Cards;
import adventuregame.items.Cards.Card;

public class testclass {
    
    public static void main(String[] args)
    {   
        for(String s : new String[] {
            // "Room scenarios",
            "Gzouca",
            // "Dialogue",
            // "QTE" succeeded
        })
            switch(s)
            {
                case "Room scenarios":
                    for(int i : new int[]{
                        0,
            // case FIGHT:     fight();     break;
            // case INSPECT:   inspect();   break;
            // case TALK:      talk();      break;
            // case CAST:      castSpell(); break;
            // case INTERACT:  interact();  break;
            // case COMMUNE:   commune();   break;
            // case INVENTORY: inventory(); break;
            // case LEAVE:     leave();     break;
                    })
                    {
                        System.out.println("--Begin Test Room Scenarios--");
                        switch(i)
                        {
                            case 0: //test fight()
                                System.out.println("--Combat--");
                                System.out.println("Test 1, kill enemy.");
                                Game.curRoom  = new Room();
                                Game.cur = new Player("Guy", 10);
                                Game.curRoom.add(new Enemy.Goblin(1));
                                Game.cur.setRoom(Game.curRoom);
                                Game.cur.updateUnit();

                                System.out.println("Test 2, get killed.");
                                Game.curRoom  = new Room();
                                Game.cur = new Player("Guy", 1);
                                Game.curRoom.add(Game.cur);
                                Game.curRoom.add(new Enemy.Goblin(10));
                                for(Enemy e : Game.curRoom.enemies) e.setRoom(Game.curRoom);
                                Game.cur.setRoom(Game.curRoom);
                                Game.cur.updateUnit();
                                for(Enemy e : Game.curRoom.enemies) e.chooseAction();

                                break;
                        }
                    }
                    break;
                case "Dialogue":
                    for(int i : new int[]{
                        0,
                        1
                    })
                    {
                        System.out.println("--Begin Test Dialogue--");
                        switch(i)
                        {
                            case 0: //Timeless QTE
                        }
                    }
                    break;
                case "Gzouca":
                    ArrayList<Card> c = new ArrayList<>();
                    c.add(new Card(Gzouca.Type.Gzouca, Card.Type.p1));
                    c.add(new Card(Gzouca.Type.Gzouca, Card.Type.p2));
                    c.add(new Card(Gzouca.Type.Gzouca, Card.Type.p10));
                    c.add(new Card(Gzouca.Type.Gzouca, Card.Type.p4));
                    c.add(new Card(Gzouca.Type.Gzouca, Card.Type.Zero));
                    c.add(new Card(Gzouca.Type.Gzouca, Card.Type.n1));
                    c.add(new Card(Gzouca.Type.Gzouca, Card.Type.n2));
                    c.add(new Card(Gzouca.Type.Gzouca, Card.Type.n3));
                    Cards gopcards = new Cards(new ArrayList<>(c));
                    c.add(new Card(Gzouca.Type.Gzouca, Card.Type.p1));
                    c.add(new Card(Gzouca.Type.Gzouca, Card.Type.p2));
                    c.add(new Card(Gzouca.Type.Gzouca, Card.Type.p3));
                    c.add(new Card(Gzouca.Type.Gzouca, Card.Type.p10));
                    c.add(new Card(Gzouca.Type.Gzouca, Card.Type.Zero));
                    c.add(new Card(Gzouca.Type.Gzouca, Card.Type.n1));
                    c.add(new Card(Gzouca.Type.Gzouca, Card.Type.n2));
                    c.add(new Card(Gzouca.Type.Gzouca, Card.Type.n3));
                    Cards targincards = new Cards(new ArrayList<>(c));
                    Map<String, Cards> peeps = Map.ofEntries(Map.entry("Gop", gopcards),
                                                             Map.entry("Targin", targincards));
                    new Gzouca(peeps);
                    break;
                case "QTE":
                    for(int i : new int[]{
                        //0, succeeded as far as I could tell 
                        //1, succeeded
                        //2, succeeded
                        //3, succeeded
                        //4, succeeded 
                        //5, succeeded
                        //6  succeeded as far as I could tell
                    })
                    {
                        System.out.println("--Begin Test QTE--");
                        switch(i)
                        {
                            case 0: //Timeless QTE
                                new NoUpdateQTE(
                                    new Player("Guy", 10), 
                                    new Item() { @Override public void action(Unit u, boolean isFinal){} }, 
                                    -1,
                                    new Node.B("Test timeless QTE", new String[]{""}, new Node[]{new Node.X()})
                                ) { @Override protected void timeout() { throw new UnknownError("Timeless QTE timed out"); } }
                                .run();
                                break;
                            case 1: //QTE.timeout()
                                new NoUpdateQTE(
                                    new Player("Guy", 10), 
                                    new Item() { @Override public void action(Unit u, boolean isFinal){} }, 
                                    3,
                                    new Node.B("Test timeout after 3 ticks", new String[]{"",}, new Node[] {new Node.X()})
                                ) { @Override protected void timeout() { System.out.println("timed out"); } }
                                .run();
                                break;
                            case 2: //Node.X and no-output Node.L
                                new NoUpdateQTE(
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
                                new NoUpdateQTE(
                                    new Player("Guy", 10), 
                                    new Item() { @Override public void action(Unit u, boolean isFinal){} }, 
                                    -1,
                                    new Node.B("Test QTE.Node.L(END).output(..)", new String[]{"Test Output.END"}, 
                                    new Node[] { new Node.L(Output.END){@Override public boolean output(String in, QuickTimeEvent q){ System.out.println("got: " + in); return true; }}})
                                ) { @Override protected void timeout() {} }
                                .run();
                                break;
                            case 4: //QTE.Node.L(CHECK) output(String, QuickTimeEvent) override
                                new NoUpdateQTE(
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
                                System.out.println("-Test 1-");
                                Player p = new Player("Guy1", 3);
                                p.setRoom(new Room());
                                System.out.println(p.getName() + ": " + p.getHealth() + " health.");
                                new EffectQTE(
                                    p, 
                                    new Item() { @Override public void action(Unit u, boolean isFinal){} }, 
                                    new Effect(Type.FIRE, 10, 1),
                                    new Node.B("Test QTE Effect death handling QTE.dur > health", new String[]{""}, new Node[] {new Node.X()})
                                ) 
                                { @Override protected void timeout() { System.out.println("Test FAILED! Should not have timed out!"); } }
                                .run();
                                System.out.println(p.getName() + ": " + p.getHealth() + " health.");
                                
                                System.out.println("-Test 2-");
                                p = new Player("Guy2", 3);
                                p.setRoom(new Room());
                                System.out.println(p.getName() + ": " + p.getHealth() + " health.");
                                new EffectQTE(
                                    p, 
                                    new Item() { @Override public void action(Unit u, boolean isFinal){} }, 
                                    new Effect(Type.FIRE, 3, 1),
                                    new Node.B("Test QTE Effect death handling duration == health", new String[]{""}, new Node[] {new Node.X()})
                                ) 
                                { @Override protected void timeout() { System.out.println("Test FAILED! Should not have timed out!"); } }
                                .run();
                                System.out.println(p.getName() + ": " + p.getHealth() + " health.");
                                
                                System.out.println("-Test 3-");
                                p = new Player("Guy3", 10);
                                p.setRoom(new Room());
                                System.out.println(p.getName() + ": " + p.getHealth() + " health.");
                                new EffectQTE(
                                    p, 
                                    new Item() { @Override public void action(Unit u, boolean isFinal){} }, 
                                    new Effect(Type.FIRE, 3, 1),
                                    new Node.B("Test QTE Effect handling QTE.dur < health", new String[]{""}, new Node[] {new Node.X()})
                                ) 
                                { @Override protected void timeout() { System.out.println("Test SUCCESS! Timed out normally."); } }
                                .run();
                                System.out.println(p.getName() + ": " + p.getHealth() + " health.");
                                break;
                            case 6: //Nested QTEs threads do not conflict
                                Player p1 = new Player("Guy", 10);
                                p1.setRoom(new Room());
                                System.out.println(p1.getName() + ": " + p1.getHealth() + " health.");
                                new EffectQTE(
                                    p1, 
                                    new Item() { @Override public void action(Unit u, boolean isFinal){} }, 
                                    new Effect(Type.FIRE, -1, 1),
                                    new Node.B(
                                        "prompt for nested QTE", 
                                        new String[]{"start nested QTE"}, 
                                        new Node[] {
                                            new Node.L(Output.CHECK){
                                                @Override
                                                public boolean output(String in, QuickTimeEvent q) 
                                                {
                                                    new NoUpdateQTE(
                                                        p1,
                                                        new Describable(){{description="testdesc";}},
                                                        -1,
                                                        new Node.B("inner QTE check prompt:", 
                                                        new String[] {"yes","no"},
                                                        new Node[]{ 
                                                            new Node.L(Output.END) {
                                                                @Override public boolean output(String in, QuickTimeEvent q) {
                                                                    System.out.println("you answered yes (answer #1)");
                                                                    return true;
                                                                }
                                                            },
                                                            new Node.L(Output.END) {
                                                                @Override public boolean output(String in, QuickTimeEvent q) {
                                                                    System.out.println("you answered no (answer #2)");
                                                                    return true;
                                                                }
                                                            },
                                                        })
                                                    )
                                                    { @Override protected void timeout() {} } //timeless QTE
                                                    .run();
                                                    return false; //unbeatable QTE
                                                }
                                            }
                                        }
                                    )
                                ) 
                                { 
                                    @Override protected void timeout() { System.out.println("Timed out normally."); }
                                }
                                .run();
                                System.out.println(p1.getName() + ": " + p1.getHealth() + " health.");
                                break;
                        }
                        System.out.println("--End Test--");
                    }
                    break;
                }
        }
}
