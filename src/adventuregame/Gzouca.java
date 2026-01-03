package adventuregame;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import adventuregame.items.Cards;
import adventuregame.items.Cards.Card;

public class Gzouca {
    //games
    /*
    Gzouca
        at the start of the game each player submits 8 cards from their personal stashes
        the dealer randomly chooses 5 from each player's submission and deals them to that player
        then the dealer deals a card from the normal stack, one card each round. Any round, players
        must play a card from their deck and then either fold, set, or call, setting means they stop drawing
        cards and stay with their total, calling is just the same as not setting.
        The goal of the game is to have your total less than or equal to the chosen goal. the chosen
        goal point value is normally 18. After 5 rounds or both set there is a winner and that is a hand,
        normal games go to 3 hands, and players usually bet on rounds and collect the winnings each hand
        
        normally you cannot submit duplicate cards

        normal cards:
        +10 to -10 integers inclusive
        the Being: +(roundnum) or -(anywhere from handnum to roundnum)
        the Beast: flipped sign of the Being
        
        special cards:
        +/- cards, choose whether to add or subtract the card magnitude from your total
        flip card, ex. "flip 2, 4" flips sign of any 2 cards or 4 cards only for you
        tiebreak card, +/- 1, but whoever has more tiebreakers wins the tie

        to make this work with couch multiplayer, just make instead of numbers there are:

        on turn print at the top of screen on of these (not the decimal system):
        Scale:
        0        |1        |2        |3        |4        |5        |6        |7        |8        |9        |10      |+        |-
        hole     |pill     |cystal   |ghost    |tome     |kite     |red      |yellow   |green    |blue     |titan   |strong   |weak
        dark     |stone    |mite     |snowflake|tree     |cave     |cube     |pyramid  |prism    |sphere   |statue  |mad      |sane
        whirlpool|jon boat |lodestone|tomb     |sea      |wood     |snake    |frog     |lizard   |dragon   |mansion |waxing   |waning

    Sabac
        4 suits 15 cards per suit, pip cards numbered 1-11, commander = 12, mistress = 13, master = 14, ace = 15
    Uno (Loppi)
        2 players or teams, switching team each turn, one is trying to get to -10, the other to 10, 
        one pile in the middle, only care about magnitude have to play next in order, if you have 
        none you pick up one. so if he plays a 1 you have to play a 0 or draw and then he has to 
        play a 1 again or draw, both of you draw one at the start

        or just a game with unlimited people start at 0 where you go until one runs out of cards,
        adding up, and wrap at 10->0, can only play the next magnitude
    Quarto
     */

    public static enum Type
    {
        Gzouca,
        Loppi
    }

    private final String[] color = new String[] {"hole"     ,"pill"    ,"cystal"   ,"ghost"    ,"tome","kite","red"  ,"yellow" ,"green" ,"blue"  ,"titan"  ,"strong","weak", "bittern","bugel","barley","hay"};
    private final String[] shape = new String[] {"dark"     ,"stone"   ,"mite"     ,"snowflake","tree","cave","cube" ,"pyramid","prism" ,"sphere","statue" ,"mad"   ,"sane", "night","morning","fate","feeling"};
    private final String[] beast = new String[] {"whirlpool","jon boat","lodestone","tomb"     ,"sea" ,"wood","snake","frog"   ,"lizard","dragon","mansion","waxing","waning","cold","heat","gate","garnet"};

    static ArrayList<Card> dealt = new ArrayList<>();

    public Gzouca(Map<String, Cards> playersMap)
    {
        System.out.println("started game of Gzouca!");
        Type t = Type.Gzouca;
        ArrayList<Player> players = new ArrayList<>();
        int j = 0;
        for(Entry<String, Cards> p : playersMap.entrySet())
        {
            System.out.println("choosing obfuscation for " + p.getKey());
            String[] mode;
            switch (j) {
                case 0: mode = color; break;
                case 1: mode = shape; break;
                case 2: mode = beast; break;
                default: mode = null;
            }

            players.add(new Player(p.getKey(), mode, null));
            j++;
        }

        for(int hand = 0; hand < 3; hand++)
        {
            System.out.println("Hand " + (hand + 1) + "!");
            for(Player p : players) 
            {
                System.out.println(p.name + " receiving cards...");
                p.hand = new ArrayList<>();
                ArrayList<Card> submission = new ArrayList<>(playersMap.get(p.name).cards());
                while(p.hand.size() < 5)
                    for(Card c : new ArrayList<>(submission))
                    {
                        if(Utils.rand.nextInt(2) == 1)
                        {
                            submission.remove(c);
                            p.hand.add(c);
                        }
                        if(p.hand.size() == 5) break;
                    }

                p.played = new ArrayList<>();
            }
            dealt.clear();
            
            boolean doBreak = false;
            for(int round = 0; round < 5; round++)
            {
                System.out.println("Round " + (round + 1) + "!");
                Card.Type[] cardvalues = Card.Type.values();
                dealt.add(new Card(t, cardvalues[Utils.rand.nextInt(cardvalues.length-5)+5]));
                
                for(Player p : players)
                {
                    if(p.isSet) continue;
                    System.out.println(p.name + "'s turn!\ndealt:");
                    for(Card c : dealt) System.out.print(stringOf(c.type(), p) + ", ");
                    System.out.println();
                    for(Player p1 : players)
                    {
                        System.out.println(p1.name+"'s played cards: ");
                        for(Card c : p1.played) System.out.print(stringOf(c.type(), p) + ", ");
                        System.out.println();
                    }

                    String[] cardsStr = new String[p.hand.size()];
                    for(int i = 0; i < p.hand.size(); i++) cardsStr[i] = stringOf(p.hand.get(i).type(), p);
                    System.out.println();
                    
                    Card cardPlayed = p.hand.get(Utils.promptList("This is your hand, which do you play?", cardsStr));
                    p.played.add(cardPlayed);
                    p.hand.remove(cardPlayed);

                    int v = valueOf(cardPlayed.type()); 
                    if(v > 10)
                    {
                        switch (v) {
                            case 11: p.hasBeast = true; break;
                            case 12: p.hasBeing = true; break;
                            case 13: p.isFlipped = true;break;
                            case 14: p.tiebreaks += 1;  break;
                        }
                    }

                    p.setRoundPoints(round, hand);

                    if(Utils.promptList("Do you settle?", new String[] {"Yes","No"}) == 0) p.set(round, hand);

                    boolean allSet = true;
                    for(Player p1 : players) if(!p1.isSet) { allSet = false; break; }

                    if(allSet)
                    {
                        System.out.println("That's everyone!");
                        getWinner(players);
                        doBreak = true;
                    }
                }
                if(doBreak) break;
                if(round == 4)
                {
                    System.out.println("That's the Round!");
                    getWinner(players);
                }
            }
            for(Player p : players)
            {
                p.isSet = false;
                p.hasBeast = false;
                p.hasBeing = false;
                p.isFlipped = false;
                p.tiebreaks = 0;
            }
        }
    }

    private Player getWinner(ArrayList<Player> players)
    {
        boolean first = true;
        int bestPts = 0;
        Player winner = null;
        for(Player p : players)
        {
            if(!p.lockScore) p.roundPoints = p.setRoundPoints(-1,-1);

            if(first) 
            {
                if(p.roundPoints <= 18)
                {
                    first = false; 
                    winner = p;
                }
            }
            else
            {
                if(winner == null)
                {
                    if(p.roundPoints > bestPts && p.roundPoints <= 18) 
                        winner = p;
                }
                else
                {
                    if(p.roundPoints > winner.roundPoints && p.roundPoints <= 18) 
                        winner = p;
                    else if(p.roundPoints == winner.roundPoints)
                    {
                        if(p.tiebreaks > winner.tiebreaks)
                            winner = p;
                        else if(p.tiebreaks == winner.tiebreaks)
                        {
                            bestPts = winner.roundPoints;
                            winner = null;
                        }
                    }
                }
            }
        }

        if(winner == null) System.out.println("Tie!! nobody wins.");
        else 
        {
            System.out.println(winner.name + " wins! They gain 1 point.");
            winner.points++;
        }
        return winner;
    }

    static class Player
    {
        public final String name;
        public final String[] arr;
        public ArrayList<Card> hand;
        public ArrayList<Card> played;
        public int points = 0;
        public int tiebreaks = 0;
        public boolean hasBeast = false;
        public boolean hasBeing = false;
        public boolean isFlipped = false;
        public boolean isSet = false;
        public int roundPoints = 0;
        private int setRound;
        private int setHand;
        public boolean lockScore = false;

        Player(String name, String[] array, ArrayList<Card> cards)
        {
            this.name = name;
            arr = array;
            this.hand = cards;
        }

        public void set(int round, int hand)
        {
            System.out.println("settling!");
            isSet = true;
            setHand = hand;
            setRound = round;
            roundPoints = setRoundPoints(round, hand);
            lockScore = true;
        }

        public int setRoundPoints(int round, int hand)
        {
            int curHand = hand > 0 ? hand : setHand;
            int curRound = round > 0 ? round : setRound;

            int winmin = 18;
            int winmax = 18;
            int sum = 0;
            for(Card c : played) 
            {
                int v = valueOf(c.type());
                switch (v) {
                    case 11: case 12: case 13: case 14: break;
                    default: sum += v;
                }
            }
            for(Card c : Gzouca.dealt)
            {
                int v = valueOf(c.type());
                switch (v) {
                    case 11: case 12: case 13: case 14: break;
                    default: sum += v;
                }
            }

            if(hasBeast && hasBeing)
            {
                if(sum == 18) return sum;

                if(curHand > curRound)
                {
                    winmin -= curHand;
                    winmax += curHand;
                }
                else
                {
                    winmin -= curRound;
                    winmax += curRound;
                }
            }
            else
            {
                if(hasBeing) 
                {
                    // the Being: +(roundnum) or -(anywhere from handnum to roundnum)
                    winmin -= curRound;
                    winmax -= curRound;
                    
                    if(sum > winmax)
                    {
                        if(curHand > curRound)
                        {
                            winmin += curRound;
                            winmax += curHand;
                        }
                        else
                        {
                            winmin += curHand;
                            winmax += curRound;
                        }
                    }
                    else if(sum < winmin) return sum;
                }
                else if(hasBeast)
                {
                    // the Beast: -(roundnum) or +(anywhere from handnum to roundnum)
                    winmin += curRound;
                    winmax += curRound;
                    
                    if(sum > winmax)
                    {
                        if(curHand > curRound)
                        {
                            winmin -= curHand;
                            winmax -= curRound;
                        }
                        else
                        {
                            winmin -= curRound;
                            winmax -= setHand;
                        }
                    }
                    else if(sum < winmin) return sum;
                }
            }

            System.out.println(name + " sum: "+ sum);
            if(sum >= winmin && sum <= winmax) return 18;
            else return sum;
        }
    }

    private String stringOf(Card.Type t, Player p)
    {
        //currently obfuscation is turned off
        int first = 0, second = 0;
        switch (t) {
            case Broken: return "Bro";//p.arr[16];
            case Arrow : return "Arr";//p.arr[15];
            case Being : return "Bei";//p.arr[14];
            case Beast : return "Bea";//p.arr[13];
            case Zero  : return "0";//p.arr[0];
            case n1    : first = p.isFlipped ? 11 : 12; second = 1 ; break;
            case n2    : first = p.isFlipped ? 11 : 12; second = 2 ; break;
            case n3    : first = p.isFlipped ? 11 : 12; second = 3 ; break;
            case n4    : first = p.isFlipped ? 11 : 12; second = 4 ; break;
            case n5    : first = p.isFlipped ? 11 : 12; second = 5 ; break;
            case n6    : first = p.isFlipped ? 11 : 12; second = 6 ; break;
            case n7    : first = p.isFlipped ? 11 : 12; second = 7 ; break;
            case n8    : first = p.isFlipped ? 11 : 12; second = 8 ; break;
            case n9    : first = p.isFlipped ? 11 : 12; second = 9 ; break;
            case n10   : first = p.isFlipped ? 11 : 12; second = 10; break;
            case p1    : first = p.isFlipped ? 12 : 11; second = 1 ; break;
            case p2    : first = p.isFlipped ? 12 : 11; second = 2 ; break;
            case p3    : first = p.isFlipped ? 12 : 11; second = 3 ; break;
            case p4    : first = p.isFlipped ? 12 : 11; second = 4 ; break;
            case p5    : first = p.isFlipped ? 12 : 11; second = 5 ; break;
            case p6    : first = p.isFlipped ? 12 : 11; second = 6 ; break;
            case p7    : first = p.isFlipped ? 12 : 11; second = 7 ; break;
            case p8    : first = p.isFlipped ? 12 : 11; second = 8 ; break;
            case p9    : first = p.isFlipped ? 12 : 11; second = 9 ; break;
            case p10   : first = p.isFlipped ? 12 : 11; second = 10; break;
        }
        return (first == 12 ? "-" : "+") + second;
        // return p.arr[first] + " " + p.arr[second];
    }

    static int valueOf(Card.Type t)
    {
        switch(t)
        {
            case Broken: return 14;
            case Arrow : return 13;
            case Being : return 12;
            case Beast : return 11;
            case Zero  : return 0;
            case n1    : return -1;
            case n2    : return -2;
            case n3    : return -3;
            case n4    : return -4;
            case n5    : return -5;
            case n6    : return -6;
            case n7    : return -7;
            case n8    : return -8;
            case n9    : return -9;
            case n10   : return -10;
            case p1    : return 1;
            case p2    : return 2;
            case p3    : return 3;
            case p4    : return 4;
            case p5    : return 5;
            case p6    : return 6;
            case p7    : return 7;
            case p8    : return 8;
            case p9    : return 9;
            case p10   : return 10;
            default    : return 15;
        }
    }
}
