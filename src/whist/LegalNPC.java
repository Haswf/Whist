package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;


public class LegalNPC extends NPC{

    private Card selected;
    public LegalNPC(int playerNumber, Hand hand, Trick model, int numPlayers){
        super(playerNumber, hand, model, numPlayers);
    }

    @Override
    public Card selectCardLead() {
        // lead with any random card
        return this.randomCard(this.getHand());
    }

    @Override
    public Card selectCardFollow(Whist.Suit lead, Card winningCard,  Whist.Suit trump) {
        // Until we select a valid card, loop
        do {

            selected = randomCard(this.getHand());
        } while (selected.getSuit() != lead && getHand().getNumberOfCardsWithSuit(lead) > 0);
        return selected;
    }

}
