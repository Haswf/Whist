import ch.aplu.jcardgame.Card;

public interface INPCState {
    Card selectCard(NPC npc, Whist.Suit lead);
}

// npc.selectCard()
