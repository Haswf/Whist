

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;


public class LegalNPC extends NPC{

    private Card selected;
    public LegalNPC(int playerNumber, Hand hand){
        super(playerNumber, hand);
    }

    @Override
    public Card selectRandomCard(Whist.Suit lead){
        // Until we select a valid card loop
        while(true) {
            selected = randomCard(this.getHand());
            if (selected.getSuit() != lead && getHand().getNumberOfCardsWithSuit(lead) > 0) {
                //throw new BrokeRuleException("Follow rule broken by player " + getPlayerNumber() + " attempting to play " + selected);
                // select new card
            }
            else{
                break;
            }
        }
        return selected;
    }

    @Override
    public Card selectRandomCard(){
        // Until we select a valid card loop
        selected = randomCard(this.getHand());
        return selected;
    }

}
