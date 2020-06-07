package whist.model;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import whist.CardUtil;
import whist.DeckFactory;
import whist.Whist;
import whist.interfaces.IPlayer;
import whist.interfaces.IWhistModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WhistModel implements IWhistModel {
    private final Deck deck = DeckFactory.getInstance().createStandardDeck();
    private final int nbPlayers = Whist.getInstance().getNbPlayers();

    private CardUtil.Suit trumps;

    private final int nbStartCards = Whist.getInstance().getNbStartCards();
    private final Random random;
    private Card winningCard = null;
    private Card selected = null;
    private int currentPlayerId;
    private IPlayer currentPlayer;
    private Hand[] hands;
    private final List<IPlayer> players;
    private int winnerId;

    public int getWinnerId() {
        return winnerId;
    }

    public void randomPlayerStartsRound() {
        this.currentPlayerId = random.nextInt(getNbPlayers());
        this.currentPlayer = players.get(this.currentPlayerId);
        this.winnerId = currentPlayerId;
    }

    public void initRound() {
        trumps = CardUtil.randomEnum(CardUtil.Suit.class);
        randomPlayerStartsRound();
    }

    public CardUtil.Suit getTrumps() {
        return trumps;
    }

    public void setTrumps(CardUtil.Suit trumps) {
        this.trumps = trumps;
    }

    public IPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(int currentPlayer) {
        this.currentPlayerId = currentPlayer;
        this.currentPlayer = players.get(this.currentPlayerId);

    }

    public Card getWinningCard() {
        return winningCard;
    }

    public void setWinningCard(Card winningCard) {
        this.winningCard = winningCard;
    }

    public Card getSelected() {
        return selected;
    }

    public void setSelected(Card selected) {
        this.selected = selected;
    }

    public WhistModel() {
        random = new Random(Whist.getInstance().getSeed());
        players = new ArrayList<>();
    }


    public void nextPlayer() {
        if (++this.currentPlayerId >= getNbPlayers()) {
            this.currentPlayerId = 0;  // From last back to first
        }
        currentPlayer = players.get(this.currentPlayerId);
    }

    public List<IPlayer> getPlayers() {
        return players;
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

    public void beatCurrentWinner() {
        if ( // beat current winner with higher card
                (selected.getSuit() == winningCard.getSuit() && CardUtil.rankGreater(selected, winningCard)) ||
                        // trumped when non-trump was winning
                        (selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
            System.out.println("winning: suit = " + getWinningCard().getSuit() + ", rank = " + getWinningCard().getRankId());
            System.out.println("played: suit = " + getSelected().getSuit() + ", rank = " + getSelected().getRankId());
            System.out.println("NEW WINNER");
            this.winnerId = currentPlayerId;
            this.winningCard = selected;
        }
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
    public boolean addPlayer(IPlayer player) {
        return players.add(player);
    }

    @Override
    public Hand[] getHands() {
        return hands;
    }
}
