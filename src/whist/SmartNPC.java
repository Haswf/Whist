package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.interfaces.IObservable;

public class SmartNPC extends NPC {

    private Card selected;
    public SmartNPC(int playerNumber, Trick model, int numPlayers){
        super(playerNumber, model, numPlayers);
    }

    @Override
    public Card selectCardLead() {
        // lead with 'best card'
        int bestCardIndex = this.getHand().getMaxPosition(Hand.SortType.RANKPRIORITY);
        Card selected = this.getHand().get(bestCardIndex);
        System.out.println(selected.getRank());
        System.out.println(selected.getSuit());
        return selected;
    }

    @Override
    public Card selectCardFollow(Whist.Suit lead, Card winningCard, Whist.Suit trump) {
        System.out.print("The lead suit is ");
        System.out.println(lead);
        // follow with card to beat current winning card
        Hand validHand = getHand().extractCardsWithSuit(lead);
        System.out.println(validHand);

        // if can follow suit, play card to beat winningCard
        if(!validHand.isEmpty()){

            System.out.print("Playing best card of valid suit\n");

            // for now just play best card of the valid suit
            int bestValidCardIndex = validHand.getMaxPosition(Hand.SortType.RANKPRIORITY);
            Card selectedToBe = validHand.get(bestValidCardIndex);
            System.out.println(selectedToBe);
            selected = getHand().getCard(selectedToBe.getCardNumber());
            return selected;
        }
        // if cant follow suit, try to play a trump
        else{
            System.out.println("Short-Suited");
            Hand trumpHand = getHand().extractCardsWithSuit(trump);

            // if no trumps to play => throw out trash
            if(trumpHand.isEmpty()){
                System.out.println("Throwing trash");
                selected = getHand().sort(Hand.SortType.RANKPRIORITY, true);
                System.out.println(selected);
                return selected;
            }
            // play lowest trump
            else{
                System.out.println("Trumping!");
                Card selectedToBe = trumpHand.sort(Hand.SortType.RANKPRIORITY, true);
                System.out.println(selectedToBe);
                selected = getHand().getCard(selectedToBe.getCardNumber());
                return selected;
            }
        }
    }
}
