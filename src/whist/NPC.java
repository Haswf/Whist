package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.interfaces.ISelectCardStrategy;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class NPC implements ISelectCardStrategy {

    private int playerNumber;
    private Hand hand;

    public NPC(int playerNumber, Hand hand){
        this.playerNumber = playerNumber;
        this.hand = hand;
    }

    /**
     * Concrete whist.NPC will implement strategy
     * @return
     */
    @Override
    public abstract Card selectCardLead();

    /**
     * Concrete whist.NPC wil implement strategy
     * @param lead
     * @return
     */
    @Override
    public abstract Card selectCardFollow(Whist.Suit lead, Card winningCard, Whist.Suit trump);


    public int getPlayerNumber() {
        return playerNumber;
    }

    public Hand getHand() {
        return hand;
    }

    // return random Card from Hand
    public Card randomCard(Hand hand){
        final Random random = ThreadLocalRandom.current();
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }
}
