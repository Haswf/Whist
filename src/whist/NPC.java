package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.CardUtil.Suit;
import whist.interfaces.*;

import java.util.HashMap;

public class NPC implements IObserver, IPlayerAction {

    private final ISelectCardStrategy strategy;
    private Card selected;

    private final int playerNumber;
    private Hand hand;

    private final IObservable topic;
    private Hand info;
    private final HashMap<Integer, HashMap<CardUtil.Suit, Boolean>> playerSuits;
    private HashMap<Suit, Boolean> nest;

    public NPC(int playerNumber, ITrickModel model, int numPlayers, ISelectCardStrategy strategy) {
        this.playerNumber = playerNumber;
        this.info = null;
        this.strategy = strategy;
        topic = model;
        topic.register(this);

        playerSuits = new HashMap<>();
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
            playerSuits.put(i, nest);
        }
    }

    public Card selectCardLead() {
        thinking();
        selected = strategy.selectCardLead(this);
        return selected;
    }

    public Card selectCardFollow(Card winningCard, Suit trump) {
        thinking();
        selected = strategy.selectCardFollow(this, winningCard, trump);
        return selected;
    }

    public void thinking() {
        Whist.getInstance().setStatusText("Player " + playerNumber + " thinking...");
        Whist.getInstance().delay(Whist.getInstance().getThinkingTime());
    }

    @Override
    public void reset() {
        hand.removeAll(true);
    }

    @Override
    public void update() {

        // store current state of the hand
        this.info = ((ITrickModel) topic).getCards();
        System.out.println("State of hand info:");
        System.out.println(info);

        // if card played did not follow suit, update player suit map
        Suit trumpSuit = (Suit) ((ITrickModel) topic).getCards().getFirst().getSuit();
        Suit recentCardSuit = (Suit) ((ITrickModel) topic).getRecentCard().getSuit();

        if (!recentCardSuit.equals(trumpSuit)) {
            nest = playerSuits.get(((ITrickModel) topic).getRecentCardPlayerNum());
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

    public Hand getInfo() {
        return info;
    }
}
