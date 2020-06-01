package whist;

import ch.aplu.jcardgame.*;
import whist.controller.ScoreboardController;
import whist.controller.WhistController;
import whist.interfaces.IScoreboardModel;
import whist.model.ScoreboardModel;
import whist.view.ScoreboardView;
import whist.view.TrickView;
import whist.view.WhistView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

// MOVE NPC INSTANTIATION TO WHIST CONSTRUCTOR
public class Whist extends CardGame {

    private volatile static Whist uniqueInstance;

    public static Whist getInstance() {
        if (uniqueInstance == null) {
            synchronized (Whist.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Whist();
                }
            }
        }
        return uniqueInstance;
    }
    ScoreboardController scoreboardController;
    private WhistView whistView;
    private Trick trick;
    private TrickView trickView;
    private ArrayList<NPC> npcs;

    public enum Suit {
        SPADES, HEARTS, DIAMONDS, CLUBS
    }

    public enum Rank {
        // Reverse order of rank importance (see rankGreater() below)
        // Order of cards is tied to card images
        ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO
    }


    static final Random random = ThreadLocalRandom.current();

    // return random Enum value
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    // return random Card from Hand
    public static Card randomCard(Hand hand) {
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }

    // return random Card from ArrayList
    public static Card randomCard(List<Card> list) {
        int x = random.nextInt(list.size());
        return list.get(x);
    }

    public boolean rankGreater(Card card1, Card card2) {
        return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
    }

    public final String version = "1.0";
    public final int nbPlayers = 4;
    public final int nbStartCards = 13;
    public final int winningScore = 11;
    private final Deck deck = DeckFactory.getInstance().createStandardDeck();
    private final int thinkingTime = 2000;
    public Hand[] hands;
    private boolean enforceRules = false;
    private Card selected;

    private void initRound() {
        hands = deck.dealingOut(nbPlayers, nbStartCards); // Last element of hands is leftover cards; these are ignored
        for (int i = 0; i < nbPlayers; i++) {
            // TODO: Wrong delegation, model shouldn't know anything about view
            whistView.bindLayout(hands[i], i);
            hands[i].sort(Hand.SortType.SUITPRIORITY, true);
            npcs.get(i).setHand(hands[i]);
        }

        // Set up human player for interaction
        CardListener cardListener = new CardAdapter()  // Human Player plays card
        {
            public void leftDoubleClicked(Card card) {
                selected = card;
                hands[0].setTouchEnabled(false);
            }
        };
        hands[0].addCardListener(cardListener);

    }

    private Optional<Integer> playRound() {  // Returns winner, if any
        final Whist.Suit trumps = randomEnum(Whist.Suit.class);
        whistView.showTrump(trumps);

        // End trump suit
        int winner;
        Card winningCard;
        // randomly select player to lead for this round
        int nextPlayer = random.nextInt(nbPlayers);

        // until all cards have been played
        for (int i = 0; i < nbStartCards; i++) {
            //trick = new Trick();
            trickView = new TrickView(this, trick);
            selected = null;
            if (0 == nextPlayer) {  // Select lead depending on player type
                hands[0].setTouchEnabled(true);
                setStatusText("Player 0 double-click on card to lead.");
                while (null == selected) delay(100);
            }
            // npc
            else {
                setStatusText("Player " + nextPlayer + " thinking...");
                delay(thinkingTime);
                selected = npcs.get(nextPlayer).selectCardLead();
            }
            Suit lead = (Suit) selected.getSuit();
            trick.transfer(selected, nextPlayer);
            winner = nextPlayer;
            winningCard = selected;

            // End Lead
            for (int j = 1; j < nbPlayers; j++) {
                if (++nextPlayer >= nbPlayers) nextPlayer = 0;  // From last back to first
                selected = null;
                if (0 == nextPlayer) {
                    hands[0].setTouchEnabled(true);
                    setStatusText("Player 0 double-click on card to follow.");
                    while (null == selected) delay(100);
                } else {
                    setStatusText("Player " + nextPlayer + " thinking...");
                    delay(thinkingTime);
                    selected = npcs.get(nextPlayer).selectCardFollow(lead, winningCard, trumps);
                }

                trick.transfer(selected, nextPlayer);
                selected.setVerso(false);  // In case it is upside down

                // transfer to trick (includes graphic effect)
                System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + winningCard.getRankId());
                System.out.println("played: suit = " + selected.getSuit() + ", rank = " + selected.getRankId());
                if ( // beat current winner with higher card
                        (selected.getSuit() == winningCard.getSuit() && rankGreater(selected, winningCard)) ||
                                // trumped when non-trump was winning
                                (selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
                    System.out.println("NEW WINNER");
                    winner = nextPlayer;
                    winningCard = selected;
                }
                // End Follow
            }
            System.out.println("End of trick");
            // reset Trick hand
            trick.getCards().removeAll(true);
            System.out.println(trick.getCards());

            //trick.setHidden(true);

            nextPlayer = winner;
            scoreboardController.inc(winner);
            setStatusText("Player " + nextPlayer + " wins trick.");
            if (winningScore == scoreboardController.get(nextPlayer)) {
                return Optional.of(nextPlayer);
            }

        }
        whistView.clearTrump();
        return Optional.empty();
    }

    private Whist() {
        super(700, 700, 30);

    }

    private void run() {
        this.scoreboardController = new ScoreboardController(new ScoreboardModel());
        whistView = new WhistView(new WhistController(), this);
        trick = new Trick();
        trickView = new TrickView(this, trick);
        npcs = new ArrayList<>();
        // Set up NPCs
        for (int i = 0; i < nbPlayers; i++){
            npcs.add(new SmartNPC(i, trick, nbPlayers));
        }
        setStatusText("Initializing...");
        Optional<Integer> winner;
        do {
            initRound();
            winner = playRound();
            // reset npc hands
            for (int k = 0; k < nbPlayers; k++){
                npcs.get(k).getHand().removeAll(true);
            }
        } while (!winner.isPresent());
    }

    public static void main(String[] args) {
        // System.out.println("Working Directory = " + System.getProperty("user.dir"));
       Whist.getInstance().run();
    }


}
