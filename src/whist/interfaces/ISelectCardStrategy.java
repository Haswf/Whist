package whist.interfaces;

import ch.aplu.jcardgame.Card;
import whist.Whist;

public interface ISelectCardStrategy {

    Card selectCardLead();

    Card selectCardFollow(Whist.Suit lead, Card winningCard, Whist.Suit trump);
}
