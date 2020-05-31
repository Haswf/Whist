import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class SmartNPC extends NPC {

    private Card selected;
    public SmartNPC(int playerNumber, Hand hand){
        super(playerNumber, hand);
    }

    @Override
    public Card selectCardLead(Whist.Suit lead) {
        // lead with 'best card'
        return this.randomCard(this.getHand());
    }

    @Override
    public Card selectCardFollow(Whist.Suit lead) {

        // follow with card to beat current winning card
        return null;
    }
}
