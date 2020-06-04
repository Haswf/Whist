package whist.interfaces;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.CardUtil.Suit;
import whist.NPC;

public interface ISelectCardStrategy {

    Card selectCardLead(NPC npc);

    Card selectCardFollow(NPC npc, Card winningCard, Suit trump);
}
