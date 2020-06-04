package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.CardUtil.Suit;
import whist.interfaces.ISelectCardStrategy;
import whist.interfaces.ITrickModel;

public class SmartNPC extends NPC {

    private Card selected;

    private ISelectCardStrategy strategy;

    public SmartNPC(int playerNumber, ITrickModel model, int numPlayers){
        super(playerNumber, model, numPlayers);
        strategy = new SmartSelectCardHandle();
    }

    @Override
    public Card selectCardLead() {
        selected = strategy.selectCardLead(this);
        return selected;
    }

    @Override
    public Card selectCardFollow(Card winningCard, Suit trump) {
        selected = strategy.selectCardFollow(this, winningCard, trump);
        return selected;
    }
}
