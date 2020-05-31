import ch.aplu.jcardgame.Card;

public class LeadStateSmart implements INPCState {
    @Override
    public Card selectCard(NPC npc, Whist.Suit lead) {
        // play highest card (non trump preferably)
        Card selected;
        selected = npc.selectRandomCard();
        return selected;
    }
}
