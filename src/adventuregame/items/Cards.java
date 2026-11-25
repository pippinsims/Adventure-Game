package adventuregame.items;

import java.util.ArrayList;
import java.util.List;

import adventuregame.CardGame;
import adventuregame.abstractclasses.Unit;
import adventuregame.abstractclasses.Item.Actor;

public class Cards extends Actor{
    public static final ArrayList<Card> standard = new ArrayList<>(List.of(
        new Card(CardGame.Type.Gzouca, Cards.Type.Zero),
        new Card(CardGame.Type.Gzouca, Cards.Type.p1),
        new Card(CardGame.Type.Gzouca, Cards.Type.p2),
        new Card(CardGame.Type.Gzouca, Cards.Type.p3),
        new Card(CardGame.Type.Gzouca, Cards.Type.p4),
        new Card(CardGame.Type.Gzouca, Cards.Type.p5),
        new Card(CardGame.Type.Gzouca, Cards.Type.p6),
        new Card(CardGame.Type.Gzouca, Cards.Type.p7),
        new Card(CardGame.Type.Gzouca, Cards.Type.p8),
        new Card(CardGame.Type.Gzouca, Cards.Type.p9),
        new Card(CardGame.Type.Gzouca, Cards.Type.p10)
    ));

    ArrayList<Card> cards = new ArrayList<>();
    public ArrayList<Card> cards() { return cards; }

    public Cards(ArrayList<Card> cards)
    {
        descMap.put("Laur", "writhing mass of pictorial pantomimes");
        description = "bundle of playing shells";
        name = "shell bundle";
        this.cards = cards;
    }

    @Override
    public void action(Unit u, boolean isFinal) {

    }

    public static class Card
    {
        private CardGame.Type game;
        private Type type;

        public Card(CardGame.Type game, Type type)
        {
            this.game = game;
            this.type = type;
        }

        public CardGame.Type game()    { return game;    }
        public Type        type()    { return type;    }
    }

    public static enum Type
    {
            Zero, Being, Beast, Broken, Arrow,
            n1, n2, n3, n4, n5, n6, n7, n8, n9, n10,
            p1, p2, p3, p4, p5, p6, p7, p8, p9, p10
    }
}
