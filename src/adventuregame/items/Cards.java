package adventuregame.items;

import java.util.ArrayList;

import adventuregame.Gzouca;
import adventuregame.abstractclasses.Item;
import adventuregame.abstractclasses.Unit;

public class Cards extends Item{

    ArrayList<Card> cards = new ArrayList<>();
    public ArrayList<Card> cards() { return cards; }

    public Cards(ArrayList<Card> cards)
    {
        this.cards = cards;
    }

    @Override
    public void action(Unit u, boolean isFinal) {

    }

    public static class Card
    {
        private Gzouca.Type game;
        private Type type;

        public Card(Gzouca.Type game, Type type)
        {
            this.game = game;
            this.type = type;
        }

        public Gzouca.Type game()    { return game;    }
        public Type        type()    { return type;    }

        public static enum Type
        {
            Zero, Being, Beast, Broken, Arrow,
            p1,  n1,
            p2,  n2,  
            p3,  n3,  
            p4,  n4,  
            p5,  n5,  
            p6,  n6,  
            p7,  n7,  
            p8,  n8,  
            p9,  n9,  
            p10, n10
        }
    }
}
