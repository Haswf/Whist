package whist.interfaces;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public interface ITrickModel extends IObservable {
    void initialise();

    Hand getCards();

    void transfer(Card selected, int playerNum);

    void clear();
}
