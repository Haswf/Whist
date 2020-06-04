package whist.controller;

import ch.aplu.jcardgame.Card;
import whist.CardUtil;
import whist.InteractivePlayer;
import whist.NPCFactory;
import whist.Whist;
import whist.interfaces.IPlayerAction;
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
    private final NPCFactory npcFactory;

    public WhistController(IWhistModel model) {
        this.model = model;
        this.view = new WhistView(this, model);
        scoreboardController = new ScoreboardController(new ScoreboardModel());
        trickController = new TrickController(new TrickModel());
        npcFactory = NPCFactory.getInstance();
    }

    public void initialise() {
        model.dealingOut();
        createPlayer();
        view.createView();
    }

    public void reset() {
        model.reset();
    }

    public void createPlayer() {
        int playerNumber = 0;
        if (Whist.getInstance().getPlayer() == 1) {
            model.addPlayer(new InteractivePlayer());
            playerNumber++;
        }
        for (int i = 0; i < Whist.getInstance().getSmartNPCs(); i++) {
            model.addPlayer(npcFactory.createSmartNPC(playerNumber, trickController));
            playerNumber++;
        }
        for (int j = 0; j < Whist.getInstance().getLegalNPCs(); j++) {
            model.addPlayer(npcFactory.createLegalNPC(playerNumber, trickController));
            playerNumber++;
        }
    }

    public void gameOver(int winner) {
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
            IPlayerAction player = model.getPlayers().get(nextPlayer);
            selected = player.selectCardLead();
            trickController.transfer(selected, nextPlayer);
            winner = nextPlayer;
            winningCard = selected;

            // End Lead
            for (int j = 1; j < model.getNbPlayers(); j++) {
                if (++nextPlayer >= model.getNbPlayers()) {
                    nextPlayer = 0;  // From last back to first
                }
                player = model.getPlayers().get(nextPlayer);
                selected = player.selectCardFollow(winningCard, trumps);
                trickController.transfer(selected, nextPlayer);
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
                // End Follow
            }
            System.out.println("End of trick");
            nextPlayer = winner;


            // reset trick hand
            trickController.clear();
            scoreboardController.inc(winner);
            view.showWinner(winner);
            if (Whist.getInstance().getWinningScore() == scoreboardController.get(nextPlayer)) {
                return Optional.of(nextPlayer);
            }
        }
        view.clearTrump();
        return Optional.empty();
    }
}
