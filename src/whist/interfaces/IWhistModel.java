package whist.interfaces;

import ch.aplu.jcardgame.Hand;
import whist.NPC;

import java.util.List;

public interface IWhistModel {
    boolean addPlayer(IPlayerAction player);

    int getNbPlayers();

    int getNbStartCards();

    Hand[] getHands();

    List<NPC> getNpcs();

    void reset();

    void dealingOut();

    List<IPlayerAction> getPlayers();
}
