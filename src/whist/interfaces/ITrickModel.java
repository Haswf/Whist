package whist.interfaces;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public interface ITrickModel extends IObservable {
    Hand getCards();

    void transfer(Card selected, int playerNum);

    void clear();

    Card getRecentCard();

    int getRecentCardPlayerNum();

}
