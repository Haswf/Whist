package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;
import whist.interfaces.IPlayer;

public class InteractivePlayer implements IPlayer {
    private Hand hand;
    private Card selected;

    public InteractivePlayer() {
    }

    public void setListener() {
        // Set up human player for interaction
        CardListener cardListener = new CardAdapter()  // Human Player plays card
        {
            public void leftDoubleClicked(Card card) {
                selected = card;
                hand.setTouchEnabled(false);
            }
        };
        hand.addCardListener(cardListener);
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
        setListener();
    }

    @Override
    public void thinking() {

    }

    @Override
    public void reset() {
        hand = null;
    }

    private Card select() {
        hand.setTouchEnabled(true);
        Whist.getInstance().setStatusText("Player 0 double-click on card to follow.");
        while (null == selected) {
            Whist.getInstance().delay(100);
        }
        Card card = selected;
        selected = null;
        return card;
    }

    @Override
    public Card selectCardLead() {
        return select();
    }

    @Override
    public Card selectCardFollow(Card winningCard, CardUtil.Suit trump) {
        return select();
    }
}
