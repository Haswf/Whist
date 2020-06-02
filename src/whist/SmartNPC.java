package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.CardUtil.Suit;
import whist.interfaces.ITrickModel;

public class SmartNPC extends NPC {

    private Card selected;
    public SmartNPC(int playerNumber, ITrickModel model, int numPlayers){
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
    public Card selectCardFollow(Suit lead, Card winningCard, Suit trump) {
        System.out.print("The lead suit is ");
        System.out.println(lead);
        // follow with card to beat current winning card
        Hand validHand = getHand().extractCardsWithSuit(lead);
        System.out.println(validHand);

        // if can follow suit, play card to beat winningCard
        if (!validHand.isEmpty()) {

            int bestValidCardIndex = validHand.getMaxPosition(Hand.SortType.RANKPRIORITY);
            Card selectedToBe = validHand.get(bestValidCardIndex);
            System.out.println(selectedToBe);

            // if can beat the card => beat the card
            if (selectedToBe.compareTo(winningCard) < 1) {
                System.out.print("Playing best card of valid suit to win\n");
                selected = getHand().getCard(selectedToBe.getCardNumber());
            }
            // if can't beat the card => use a low card
            else {
                System.out.print("Can't win play lowest card of suit\n");
                selectedToBe = validHand.sort(Hand.SortType.RANKPRIORITY, true);
                selected = getHand().getCard(selectedToBe.getCardNumber());
            }

            // for now just play best card of the valid suit

            selected = getHand().getCard(selectedToBe.getCardNumber());
            return selected;
        }
        // if cant follow suit, try to play a trump
        else{
            // currently playing lowest, trump
            // however if a trump is currently winning beat that trump
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
                if(winningCard.getSuit().equals(trump)){
                    // beat the trump
                    int selectedToBeIndex = trumpHand.getMaxPosition(Hand.SortType.RANKPRIORITY);
                    Card selectedToBe = trumpHand.getCard(selectedToBeIndex);
                    selected = getHand().getCard(selectedToBe.getCardNumber());
                }
                else {
                    Card selectedToBe = trumpHand.sort(Hand.SortType.RANKPRIORITY, true);
                    System.out.println(selectedToBe);
                    selected = getHand().getCard(selectedToBe.getCardNumber());
                }
                return selected;
            }
        }
    }
}
