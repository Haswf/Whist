package whist;

import ch.aplu.jcardgame.Card;
import whist.interfaces.INPCState;

public class FollowStateLegal implements INPCState {
    @Override
    public Card selectCard(NPC npc, CardUtil.Suit lead) {
        // play random legal card
//        npc.selectRandomCard(lead);
        return null;
    }
}

