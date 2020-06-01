package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.interfaces.IObservable;
import whist.interfaces.IObserver;
import whist.interfaces.ISelectCardStrategy;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

// Need to register NPCs as observers to Trick
public abstract class NPC implements ISelectCardStrategy, IObserver {

    private int playerNumber;
    private Hand hand;

    private Trick model;
    private IObservable topic;
    private Hand info;

    public NPC(int playerNumber, Hand hand, Trick model){
        this.playerNumber = playerNumber;
        this.hand = hand;
        this.model = model;
        // setting topic = Trick
        setSubject(model);
        topic.register(this);
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

    @Override
    public void update() {
        // get access to the trick
        Trick trick = (Trick)topic.getUpdate(this);

        // store current state of the hand
        this.hand = trick.getCards();
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

    // return random Card from Hand
    public Card randomCard(Hand hand){
        final Random random = ThreadLocalRandom.current();
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }
}
