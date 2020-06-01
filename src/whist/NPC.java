package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.CardUtil.Suit;
import whist.interfaces.IObservable;
import whist.interfaces.IObserver;
import whist.interfaces.ISelectCardStrategy;
import whist.interfaces.ITrickModel;
import whist.model.TrickModel;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class NPC implements ISelectCardStrategy, IObserver {

    private final int playerNumber;
    private Hand hand;

    private IObservable topic;
    private Hand info;
    private final HashMap<Integer, HashMap<CardUtil.Suit, Boolean>> player_suits;
    private HashMap<Suit, Boolean> nest;

    public NPC(int playerNumber, ITrickModel model, int numPlayers) {
        this.playerNumber = playerNumber;
        setSubject(model);
        // setting topic = Trick
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
        // get access to the trick
        TrickModel trickModel = (TrickModel) topic.getUpdate(this);

        // store current state of the hand
        this.info = trickModel.getCards();
        //System.out.println("State of hand info:");
        //System.out.println(info);

        // if card played did not follow suit, update player suit map
        Suit trumpSuit = (Suit) trickModel.getCards().getFirst().getSuit();
        Suit recentCardSuit = (Suit) trickModel.getRecentCard().getSuit();

        if (!recentCardSuit.equals(trumpSuit)) {
            //System.out.println("Updating playerSuits");
            //System.out.println(trick.getRecentCard());
            //System.out.println(trick.getRecentCardPlayerNum());
            nest = player_suits.get(trickModel.getRecentCardPlayerNum());
            //System.out.println(nest);
            nest.put(trumpSuit, false);
            //System.out.println(nest);
            //System.out.println(player_suits);
        }
    }

    @Override
    public void setSubject(IObservable subject) {
        this.topic = subject;
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
