package whist;

import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public class Dealer {


    public Dealer() {
    }

    public Hand[] deal(Deck deck, int nbPlayers, int nbStartCards){
        return deck.dealingOut(nbPlayers, nbStartCards);
    }
}
