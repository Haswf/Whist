package whist.model;

import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import whist.DeckFactory;
import whist.NPC;
import whist.Whist;
import whist.interfaces.IWhistModel;

import java.util.ArrayList;
import java.util.List;

// MOVE NPC INSTANTIATION TO WHIST CONSTRUCTOR
public class WhistModel implements IWhistModel {
    private final Deck deck = DeckFactory.getInstance().createStandardDeck();
    private final int nbPlayers = Whist.getInstance().nbPlayers;
    private final int nbStartCards = Whist.getInstance().nbStartCards;
    public Hand[] hands;
    private List<NPC> npcs;

    public WhistModel() {
        npcs = new ArrayList<>();
    }

    public List<NPC> getNpcs() {
        return npcs;
    }

    public void setNpcs(List<NPC> npcs) {
        this.npcs = npcs;
    }

    @Override
    public int getNbPlayers() {
        return nbPlayers;
    }

    @Override
    public int getNbStartCards() {
        return nbStartCards;
    }

    @Override
    public void dealingOut() {
        hands = deck.dealingOut(nbPlayers, nbStartCards); // Last element of hands is leftover cards; these are ignored
    }

    @Override
    public void resetNPC() {
        for (int k = 0; k < nbPlayers; k++) {
            npcs.get(k).getHand().removeAll(true);
        }
    }

    @Override
    public boolean addNPC(NPC npc) {
        return npcs.add(npc);
    }

    @Override
    public Hand[] getHands() {
        return hands;
    }
}
