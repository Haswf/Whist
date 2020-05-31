package whist.interfaces;

import ch.aplu.jcardgame.Card;
import whist.NPC;
import whist.Whist;

public interface INPCState {
    Card selectCard(NPC npc, Whist.Suit lead);
}

// npc.selectCard()
