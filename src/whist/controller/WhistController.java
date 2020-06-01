package whist.controller;

import ch.aplu.jcardgame.Card;
import ch.aplu.jgamegrid.GameGrid;
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

    public void gameover(int winner) {
        view.onGameOver(winner);
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
                view.selectCard();
                selected = view.getSelected();
            }
            // npc
            else {
                Whist.getInstance().setStatusText("Player " + nextPlayer + " thinking...");
                Whist.getInstance().delay(Whist.getInstance().thinkingTime);
                selected = model.getNpcs().get(nextPlayer).selectCardLead();
            }
            CardUtil.Suit lead = (CardUtil.Suit) selected.getSuit();
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
                    view.selectCard();
                    selected = view.getSelected();
                } else {
                    Whist.getInstance().setStatusText("Player " + nextPlayer + " thinking...");
                    GameGrid.delay(Whist.getInstance().thinkingTime);
                    selected = model.getNpcs().get(nextPlayer).selectCardFollow(lead, winningCard, trumps);
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
            // reset Trick hand
            System.out.println(trickController.getModel().getCards());
            nextPlayer = winner;
            trickController.clear();
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
