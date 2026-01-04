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
            n1, n2, n3, n4, n5, n6, n7, n8, n9, n10,
            p1, p2, p3, p4, p5, p6, p7, p8, p9, p10
        }
    }
}
