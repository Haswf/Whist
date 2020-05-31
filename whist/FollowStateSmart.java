import ch.aplu.jcardgame.Card;

public class FollowStateSmart implements INPCState {
    @Override
    public Card selectCard(NPC npc, Whist.Suit lead) {
        // play card to beat current winner
        return null;
    }
}
