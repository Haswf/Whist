package whist.model;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import whist.DeckFactory;
import whist.NPC;
import whist.Whist;
import whist.interfaces.IWhistModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// MOVE NPC INSTANTIATION TO WHIST CONSTRUCTOR
public class WhistModel implements IWhistModel {
    private final Deck deck = DeckFactory.getInstance().createStandardDeck();
    private final int nbPlayers = Whist.getInstance().getNbPlayers();
    private final int nbStartCards = Whist.getInstance().getNbStartCards();
    private final Random random;

    public Hand[] hands;
    private List<NPC> npcs;

    public WhistModel() {
        random = new Random(Whist.getInstance().getSeed());
        npcs = new ArrayList<>();
    }

    public Hand[] deal(Deck deck, int nbPlayers, int nbStartCards) {
        Hand complete = deck.toHand();
        complete.sort(Hand.SortType.RANKPRIORITY, false);
        List<Hand> handList = new ArrayList<>();

        for (int player = 0; player < nbPlayers; player++) {
            handList.add(new Hand(deck));

            for (int card = 0; card < nbStartCards; card++) {
                int cardIndex = random.nextInt(complete.getNumberOfCards());
                Card choice = complete.get(cardIndex);
                handList.get(player).insert(choice, false);
                complete.remove(choice, false);
            }
        }
        handList.add(complete);
        return handList.toArray(new Hand[0]);
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
        hands = deal(deck, nbPlayers, nbStartCards); // Last element of hands is leftover cards; these are ignored
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
