package whist;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import whist.controller.WhistController;
import whist.interfaces.IController;
import whist.view.WhistView;

import java.awt.Font;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Whist extends CardGame {
    Scoreboard scoreboard;
    ScoreboardView scoreboardView;
    WhistView whistView;
    Trick trick;
    TrickView trickView;
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
    private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
    private final int thinkingTime = 2000;
    public Hand[] hands;
    private boolean enforceRules = false;
    private Card selected;
    Font bigFont = new Font("Serif", Font.BOLD, 36);

    private void initRound() {
        hands = deck.dealingOut(nbPlayers, nbStartCards); // Last element of hands is leftover cards; these are ignored
        for (int i = 0; i < nbPlayers; i++) {
            whistView.bindLayout(hands[i], i);
            hands[i].sort(Hand.SortType.SUITPRIORITY, true);

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

        // Set up NPCs
        for (int i = 0; i < nbPlayers; i++){
            npcs.add(new SmartNPC(i, hands[i]));
        }
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

            trick = new Trick();
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

            // No restrictions on the card being lead
            trick.getCards().setView(this, new RowLayout(trickView.trickLocation,
                    (trick.getCards().getNumberOfCards()+2)*trickView.trickWidth));
            trick.getCards().draw();
            Suit lead = (Suit) selected.getSuit();
            selected.transfer(trick.cards, true); // transfer to trick (includes graphic effect)
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

                trick.getCards().setView(this, new RowLayout(trickView.trickLocation,
                        (trick.getCards().getNumberOfCards()+2)*trickView.trickWidth));
                trick.getCards().draw();
                selected.setVerso(false);  // In case it is upside down
                // Check: Following card must follow suit if possible
                /*if (selected.getSuit() != lead && hands[nextPlayer].getNumberOfCardsWithSuit(lead) > 0) {
                    // Rule violation
                    String violation = "Follow rule broken by player " + nextPlayer + " attempting to play " + selected;
                    System.out.println(violation);
                    if (enforceRules)
                        try {
                            throw (new BrokeRuleException(violation));
                        } catch (BrokeRuleException e) {
                            e.printStackTrace();
                            System.out.println("A cheating player spoiled the game!");
                            System.exit(0);
                        }
                }*/
                // End Check
                // transfer to trick (includes graphic effect)
//                selected.transfer(trick.cards, true);
                System.out.println(selected);
                trick.addToTrick(selected);
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
            System.out.printf("End of trick\n");
            delay(600);

            //trick.isHidden = true;

            trick.getCards().setView(this, new RowLayout(trickView.hideLocation, 0));
            trick.getCards().draw();

            nextPlayer = winner;
            setStatusText("Player " + nextPlayer + " wins trick.");
            scoreboard.updateScore(nextPlayer, scoreboard.getScoreByPlayer(nextPlayer) + 1);
            if (winningScore == scoreboard.getScoreByPlayer(nextPlayer)) {
                return Optional.of(nextPlayer);
            }

        }
        whistView.clearTrump();
        return Optional.empty();
    }

    public Whist() {
        super(700, 700, 30);
        scoreboard = new Scoreboard(nbPlayers);
        scoreboardView = new ScoreboardView(null, scoreboard, this);
        whistView = new WhistView(new WhistController(), this);
        trick = new Trick();
        trickView = new TrickView(this, trick);
        npcs = new ArrayList<>();
        setStatusText("Initializing...");
        Optional<Integer> winner;
        do {
            initRound();
            winner = playRound();
        } while (!winner.isPresent());
    }

    public static void main(String[] args) {
        // System.out.println("Working Directory = " + System.getProperty("user.dir"));
        new Whist();
    }


}
