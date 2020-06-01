package whist;

import ch.aplu.jcardgame.Card;
import whist.CardUtil.Suit;
import whist.interfaces.INPCState;

public class FollowStateSmart implements INPCState {

    @Override
    public Card selectCard(NPC npc, Suit lead) {
        return null;
    }
}
