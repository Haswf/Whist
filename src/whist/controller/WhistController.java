package whist.controller;

import ch.aplu.jcardgame.Card;
import whist.CardUtil;
import whist.SmartNPC;
import whist.Whist;
import whist.interfaces.IWhistModel;
import whist.model.ScoreboardModel;
import whist.model.TrickModel;
import whist.view.WhistView;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class WhistController {
    static final Random random = ThreadLocalRandom.current();
    private final IWhistModel model;
    private final WhistView view;
    private final ScoreboardController scoreboardController;
    private final TrickController trickController;

    public WhistController(IWhistModel model) {
        this.model = model;
        this.view = new WhistView(this, model);
        scoreboardController = new ScoreboardController(new ScoreboardModel());
        trickController = new TrickController(new TrickModel());
    }

    public void initialise() {
        model.dealingOut();
        createNPC();
        view.setListener();
        view.createView();
    }

    public void resetNPC() {
        model.resetNPC();
    }

    public void createNPC() {
        for (int i = 0; i < Whist.getInstance().nbPlayers; i++) {
            model.addNPC(new SmartNPC(i, trickController.getModel(), Whist.getInstance().nbPlayers));
        }
    }

    public void gameOver(int winner) {
        view.onGameOver(winner);
    }

    private Card playerSelectCard() {
        view.selectCard();
        return view.getSelected();
    }

    private Card NPCSelectCardLead(int player) {
        Whist.getInstance().setStatusText("Player " + player + " thinking...");
        Whist.getInstance().delay(Whist.getInstance().thinkingTime);
        return model.getNpcs().get(player).selectCardLead();
    }

    private Card NPCSelectCardFollow(int player, CardUtil.Suit lead, Card winningCard, CardUtil.Suit trump) {
        Whist.getInstance().setStatusText("Player " + player + " thinking...");
        Whist.getInstance().delay(Whist.getInstance().thinkingTime);
        return model.getNpcs().get(player).selectCardFollow(lead, winningCard, trump);
    }

    public Optional<Integer> playRound() {  // Returns winner, if any
        Card selected;

        final CardUtil.Suit trumps = CardUtil.randomEnum(CardUtil.Suit.class);
        view.showTrump(trumps);

        // End trump suit
        int winner;
        Card winningCard;
        // randomly select player to lead for this round
        int nextPlayer = random.nextInt(model.getNbPlayers());

        // until all cards have been played
        for (int i = 0; i < model.getNbStartCards(); i++) {

            if (0 == nextPlayer) {  // Select lead depending on player type
                selected = playerSelectCard();
            }
            // npc
            else {
                selected = NPCSelectCardLead(nextPlayer);
            }
//            CardUtil.Suit lead = (CardUtil.Suit) selected.getSuit();
            trickController.transfer(selected, nextPlayer);
            winner = nextPlayer;
            winningCard = selected;
            view.setSelected(null);

            // End Lead
            for (int j = 1; j < model.getNbPlayers(); j++) {
                if (++nextPlayer >= model.getNbPlayers()) {
                    nextPlayer = 0;  // From last back to first
                }
                if (0 == nextPlayer) {
                    selected = playerSelectCard();
                } else {
                    selected = NPCSelectCardFollow(nextPlayer,
                            (CardUtil.Suit) trickController.getModel().getCards().getFirst().getSuit(), winningCard, trumps);
                }
                trickController.transfer(selected, nextPlayer);
                selected.setVerso(false);  // In case it is upside down

                // transfer to trick (includes graphic effect)
                System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + winningCard.getRankId());
                System.out.println("played: suit = " + selected.getSuit() + ", rank = " + selected.getRankId());
                if ( // beat current winner with higher card
                        (selected.getSuit() == winningCard.getSuit() && CardUtil.rankGreater(selected, winningCard)) ||
                                // trumped when non-trump was winning
                                (selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
                    System.out.println("NEW WINNER");
                    winner = nextPlayer;
                    winningCard = selected;
                }
                view.setSelected(null);
                // End Follow
            }
            System.out.println("End of trick");
            nextPlayer = winner;

            // reset trick hand
            trickController.clear();
            System.out.println(trickController.getModel().getCards());
            scoreboardController.inc(winner);
            Whist.getInstance().setStatusText("Player " + nextPlayer + " wins trick.");
            if (Whist.getInstance().winningScore == scoreboardController.get(nextPlayer)) {
                return Optional.of(nextPlayer);
            }
        }
        view.clearTrump();
        return Optional.empty();
    }
}
