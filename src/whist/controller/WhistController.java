package whist.controller;

import ch.aplu.jcardgame.Card;
import whist.CardUtil;
import whist.InteractivePlayer;
import whist.NPCFactory;
import whist.Whist;
import whist.interfaces.IPlayer;
import whist.interfaces.IWhistModel;
import whist.model.ScoreboardModel;
import whist.model.TrickModel;
import whist.view.WhistView;

import java.util.Optional;
import java.util.Random;

public class WhistController {
    static final Random random = new Random(Whist.getInstance().getSeed());
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
        createPlayer();
    }

    public void dealingOut() {
        model.dealingOut();
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

    private int nextPlayer(int currentPlayer) {
        if (++currentPlayer >= model.getNbPlayers()) {
            currentPlayer = 0;  // From last back to first
        }
        return currentPlayer;
    }

    public Optional<Integer> playRound() {  // Returns winner, if any
        Card selected;
        // randomly select player to lead for this round
        int currentPlayer = random.nextInt(model.getNbPlayers());

        final CardUtil.Suit trumps = CardUtil.randomEnum(CardUtil.Suit.class);
        view.showTrump(trumps);

        int winner;
        Card winningCard;

        // until all cards have been played
        for (int i = 0; i < model.getNbStartCards(); i++) {
            IPlayer player = model.getPlayers().get(currentPlayer);
            selected = player.selectCardLead();
            trickController.transfer(selected, currentPlayer);
            winner = currentPlayer;
            winningCard = selected;
            currentPlayer = nextPlayer(currentPlayer);

            // End Lead
            for (int j = 1; j < model.getNbPlayers(); j++) {
                player = model.getPlayers().get(currentPlayer);
                selected = player.selectCardFollow(winningCard, trumps);
                trickController.transfer(selected, currentPlayer);
                // transfer to trick (includes graphic effect)
                System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + winningCard.getRankId());
                System.out.println("played: suit = " + selected.getSuit() + ", rank = " + selected.getRankId());
                if ( // beat current winner with higher card
                        (selected.getSuit() == winningCard.getSuit() && CardUtil.rankGreater(selected, winningCard)) ||
                                // trumped when non-trump was winning
                                (selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
                    System.out.println("NEW WINNER");
                    winner = currentPlayer;
                    winningCard = selected;
                }
                currentPlayer = nextPlayer(currentPlayer);
                // End Follow
            }
            System.out.println("End of trick");
            currentPlayer = winner;

            // reset trick hand
            trickController.clear();
            scoreboardController.inc(winner);
            view.showWinner(winner);
            if (Whist.getInstance().getWinningScore() == scoreboardController.get(currentPlayer)) {
                return Optional.of(currentPlayer);
            }
        }
        view.clearTrump();
        return Optional.empty();
    }

}
