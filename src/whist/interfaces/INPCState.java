package whist.interfaces;

import ch.aplu.jcardgame.Card;
import whist.CardUtil;
import whist.NPC;


public interface INPCState {
    Card selectCard(NPC npc, CardUtil.Suit lead);
}

// npc.selectCard()
