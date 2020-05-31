package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class SmartNPC extends NPC {

    private Card selected;
    public SmartNPC(int playerNumber, Hand hand){
        super(playerNumber, hand);
    }

    @Override
    public Card selectCardLead() {
        // lead with 'best card'
        int bestCardIndex = this.getHand().getMaxPosition(Hand.SortType.RANKPRIORITY);
        return this.getHand().get(bestCardIndex);
    }

    @Override
    public Card selectCardFollow(Whist.Suit lead, Card winningCard, Whist.Suit trump) {
        // follow with card to beat current winning card
        Hand validHand = getHand().extractCardsWithSuit(lead);

        // if can follow suit, play card to beat winningCard
        if(!validHand.isEmpty()){
            // for now just play best card of the valid suit
            int bestValidCardIndex = validHand.getMaxPosition(Hand.SortType.RANKPRIORITY);
            return validHand.get(bestValidCardIndex);
        }
        // if cant follow suit, try to play a trump
        else{
            Hand trumpHand = getHand().extractCardsWithSuit(trump);

            // if no trumps to play => throw out trash
            if(trumpHand.isEmpty()){
                return getHand().reverseSort(Hand.SortType.RANKPRIORITY, true);
            }
            // play lowest trump
            else{
                return trumpHand.reverseSort(Hand.SortType.RANKPRIORITY, true);
            }
        }
    }
}
