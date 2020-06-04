package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.interfaces.ISelectCardStrategy;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LegalSelectCardHandle implements ISelectCardStrategy {

    private Card selected;

    public LegalSelectCardHandle() {
        this.selected = null;
    }

    @Override
    public Card selectCardLead(NPC npc) {
        // lead with any random card
        return this.randomCard(npc.getHand());
    }

    @Override
    public Card selectCardFollow(NPC npc, Card winningCard, CardUtil.Suit trump) {

        Hand hand = npc.getHand();
        CardUtil.Suit lead = (CardUtil.Suit) npc.getInfo().getFirst().getSuit();
        do {
            selected = randomCard(hand);
        } while (selected.getSuit() != lead && hand.getNumberOfCardsWithSuit(lead) > 0);
        return selected;
    }

    // return random Card from Hand
    public Card randomCard(Hand hand){
        final Random random = ThreadLocalRandom.current();
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }
}
