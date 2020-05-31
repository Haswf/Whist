import ch.aplu.jcardgame.Card;

public class LeadStateLegal implements INPCState{

    @Override
    public Card selectCard(NPC npc, Whist.Suit lead) {
        assert(npc instanceof LegalNPC);
        // play random legal card
        // no extra conditions as no suit to follow
        return npc.randomCard(npc.getHand());
    }
}
