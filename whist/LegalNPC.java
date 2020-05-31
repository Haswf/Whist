

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;


public class LegalNPC extends NPC{

    private Card selected;
    public LegalNPC(int playerNumber, Hand hand){
        super(playerNumber, hand);
    }

    @Override
    public Card selectCardLead(Whist.Suit lead) {
        // lead with any random card
        return this.randomCard(this.getHand());
    }

    @Override
    public Card selectCardFollow(Whist.Suit lead, Card winningCard) {
        // Until we select a valid card loop
        do {

            selected = randomCard(this.getHand());
        } while (selected.getSuit() != lead && getHand().getNumberOfCardsWithSuit(lead) > 0);
        return selected;
    }

}
