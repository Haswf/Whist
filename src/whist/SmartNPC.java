package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.CardUtil.Suit;
import whist.interfaces.ISelectCardStrategy;
import whist.interfaces.ITrickModel;

public class SmartNPC extends NPC {

    private Card selected;

    private ISelectCardStrategy strategy;

    public SmartNPC(int playerNumber, ITrickModel model, int numPlayers){
        super(playerNumber, model, numPlayers);
        strategy = new SmartSelectCardHandle();
    }

    @Override
    public Card selectCardLead(Hand hand) {
        selected = strategy.selectCardLead(hand);
        return selected;
    }

    @Override
    public Card selectCardFollow(Hand hand, Suit lead, Card winningCard, Suit trump) {
        selected = strategy.selectCardFollow(hand, lead, winningCard, trump);
        return selected;
    }
}
