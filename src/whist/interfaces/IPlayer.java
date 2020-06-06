package whist.interfaces;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.CardUtil;
import whist.exceptions.BrokeRuleException;

public interface IPlayer {
    Card selectCardLead() throws BrokeRuleException;

    Card selectCardFollow(Card winningCard, CardUtil.Suit trump) throws BrokeRuleException;

    void setHand(Hand hand);

    void thinking();

    void reset();
}
