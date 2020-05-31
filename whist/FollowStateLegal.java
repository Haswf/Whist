import ch.aplu.jcardgame.Card;

public class FollowStateLegal implements INPCState {
    @Override
    public Card selectCard(NPC npc, Whist.Suit lead) {
        // play random legal card
        npc.selectRandomCard(lead);
        return null;
    }
}

