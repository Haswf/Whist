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
    public Card selectCardLead() {
        selected = strategy.selectCardLead(this);
        return selected;
    }

    @Override
    public Card selectCardFollow(Card winningCard, CardUtil.Suit trump) {
        selected = strategy.selectCardFollow(this, winningCard, trump);
        return selected;
    }
}
