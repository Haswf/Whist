package whist.interfaces;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.CardUtil;

public interface IPlayerAction {
    Card selectCardLead();

    Card selectCardFollow(Card winningCard, CardUtil.Suit trump);

    void setHand(Hand hand);

    void thinking();

    void reset();
}
