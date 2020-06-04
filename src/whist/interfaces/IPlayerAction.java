package whist.interfaces;

import ch.aplu.jcardgame.Card;
import whist.CardUtil;

public interface IPlayerAction {
    Card selectCardLead();

    Card selectCardFollow(Card winningCard, CardUtil.Suit trump);
}
