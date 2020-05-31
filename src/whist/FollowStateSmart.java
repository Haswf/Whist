package whist;

import ch.aplu.jcardgame.Card;
import whist.interfaces.INPCState;

public class FollowStateSmart implements INPCState {
    @Override
    public Card selectCard(NPC npc, Whist.Suit lead) {
        // play card to beat current winner
        return null;
    }
}
