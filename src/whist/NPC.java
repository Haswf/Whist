package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.CardUtil.Suit;
import whist.interfaces.IObservable;
import whist.interfaces.IObserver;
import whist.interfaces.ISelectCardStrategy;
import whist.interfaces.ITrickModel;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class NPC implements ISelectCardStrategy, IObserver {

    private final int playerNumber;
    private Hand hand;

    private final IObservable topic;
    private Hand info;
    private final HashMap<Integer, HashMap<CardUtil.Suit, Boolean>> player_suits;
    private HashMap<Suit, Boolean> nest;

    public NPC(int playerNumber, ITrickModel model, int numPlayers) {
        this.playerNumber = playerNumber;
        // setting topic = Trick
        topic = model;
        topic.register(this);

        player_suits = new HashMap<>();
        for (int i = 0; i < numPlayers; i++) {
            nest = new HashMap<>();
            for (int j = 0; j < 4; j++) {
                if (j == 0) {
                    nest.put(Suit.HEARTS, true);
                } else if (j == 1) {
                    nest.put(Suit.DIAMONDS, true);
                } else if (j == 2) {
                    nest.put(Suit.SPADES, true);
                } else {
                    nest.put(Suit.CLUBS, true);
                }
            }
            player_suits.put(i, nest);
        }
    }

    /**
     * Concrete whist.NPC will implement strategy
     * @return
     */
    @Override
    public abstract Card selectCardLead();

    /**
     * Concrete whist.NPC wil implement strategy
     *
     * @param lead
     * @return
     */
    public abstract Card selectCardFollow(Suit lead, Card winningCard, Suit trump);

    @Override
    public void update() {

        // store current state of the hand
        this.info = ((ITrickModel) topic).getCards();
        //System.out.println("State of hand info:");
        //System.out.println(info);

        // if card played did not follow suit, update player suit map
        Suit trumpSuit = (Suit) ((ITrickModel) topic).getCards().getFirst().getSuit();
        Suit recentCardSuit = (Suit) ((ITrickModel) topic).getRecentCard().getSuit();

        if (!recentCardSuit.equals(trumpSuit)) {
            nest = player_suits.get(((ITrickModel) topic).getRecentCardPlayerNum());
            nest.put(trumpSuit, false);
        }
    }
    public int getPlayerNumber() {
        return playerNumber;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    // return random Card from Hand
    public Card randomCard(Hand hand){
        final Random random = ThreadLocalRandom.current();
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }
}
