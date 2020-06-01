package whist;

import ch.aplu.jcardgame.Card;
import whist.model.TrickModel;


public class LegalNPC extends NPC{

    private Card selected;
    public LegalNPC(int playerNumber, TrickModel model, int numPlayers){
        super(playerNumber, model, numPlayers);
    }

    @Override
    public Card selectCardLead() {
        // lead with any random card
        return this.randomCard(this.getHand());
    }

    @Override
    public Card selectCardFollow(CardUtil.Suit lead, Card winningCard, CardUtil.Suit trump) {
        do {
            selected = randomCard(this.getHand());
        } while (selected.getSuit() != lead && getHand().getNumberOfCardsWithSuit(lead) > 0);
        return selected;
    }
}
