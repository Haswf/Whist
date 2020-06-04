package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.interfaces.ISelectCardStrategy;

public class LegalStrategy implements ISelectCardStrategy {

    private Card selected;

    public LegalStrategy() {
        this.selected = null;
    }

    @Override
    public Card selectCardLead(NPC npc) {
        // lead with any random card
        return CardUtil.randomCard(npc.getHand());
    }

    @Override
    public Card selectCardFollow(NPC npc, Card winningCard, CardUtil.Suit trump) {

        Hand hand = npc.getHand();
        CardUtil.Suit lead = (CardUtil.Suit) npc.getInfo().getFirst().getSuit();
        do {
            selected = CardUtil.randomCard(hand);
        } while (selected.getSuit() != lead && hand.getNumberOfCardsWithSuit(lead) > 0);
        return selected;
    }
}
