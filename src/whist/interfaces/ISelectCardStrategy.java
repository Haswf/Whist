package whist.interfaces;

import ch.aplu.jcardgame.Card;
import whist.CardUtil.Suit;
public interface ISelectCardStrategy {

    Card selectCardLead();

    Card selectCardFollow(Suit lead, Card winningCard, Suit trump);
}
