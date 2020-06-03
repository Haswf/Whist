package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.interfaces.ISelectCardStrategy;
import whist.model.TrickModel;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class LegalNPC extends NPC {

    private Card selected;

    private ISelectCardStrategy strategy;

    public LegalNPC(int playerNumber, TrickModel model, int numPlayers) {
        super(playerNumber, model, numPlayers);
        strategy = new LegalSelectCardHandle();

    }

    @Override
    public Card selectCardLead(Hand hand) {
        selected = strategy.selectCardLead(hand);
        return selected;
    }

    @Override
    public Card selectCardFollow(Hand hand, CardUtil.Suit lead, Card winningCard, CardUtil.Suit trump) {
        selected = strategy.selectCardFollow(hand, lead, winningCard, trump);
        return selected;
    }
}
