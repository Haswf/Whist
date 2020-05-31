import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class NPC implements ISelectCardStrategy {

    private int playerNumber;
    private Hand hand;

    private INPCState state;

    public NPC(int playerNumber, Hand hand){
        this.playerNumber = playerNumber;
        this.hand = hand;
    }

    /*@Override
    public Card selectCard(){
        return this.state.selectCard(this, null);
    }*/
    @Override
    public Card selectCard(Whist.Suit lead){
        return this.state.selectCard(this, lead);
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
        System.out.print(hand.getNumberOfCards());
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }

    //return a random card with additional conditions in LeadState
    public abstract Card selectRandomCard(Whist.Suit lead);

    // returns a random card with additional con
    public abstract Card selectRandomCard();

}
