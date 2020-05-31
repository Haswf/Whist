package whist;

import ch.aplu.jcardgame.Deck;

public class DeckFactory {
    private volatile static DeckFactory uniqueInstance;
    private DeckFactory() {}
    public static DeckFactory getInstance() {
        if (uniqueInstance == null) {
            synchronized (DeckFactory.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new DeckFactory();
                }
            }
        }
        return uniqueInstance;
    }
    public <T extends Enum<T>, R extends Enum<R>> Deck createDeck(T[] suits, R[] ranks, String over) {
        return new Deck(suits, ranks, over);
    }

    public Deck createStandardDeck() {
        return new Deck(Whist.Suit.values(), Whist.Rank.values(), "cover");
    }
}
