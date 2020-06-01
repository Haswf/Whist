package whist.interfaces;

import ch.aplu.jcardgame.Hand;
import whist.NPC;

import java.util.List;

public interface IWhistModel {
    boolean addNPC(NPC npc);

    int getNbPlayers();

    int getNbStartCards();

    Hand[] getHands();

    List<NPC> getNpcs();

    void resetNPC();

    void dealingOut();
}
