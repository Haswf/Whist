package whist.interfaces;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.CardUtil.Suit;
public interface ISelectCardStrategy {

    Card selectCardLead(Hand hand);

    Card selectCardFollow(Hand hand, Suit lead, Card winningCard, Suit trump);
}
