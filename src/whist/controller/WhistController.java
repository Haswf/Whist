package whist.controller;

import whist.InteractivePlayer;
import whist.NPCFactory;
import whist.Whist;
import whist.exceptions.BrokeRuleException;
import whist.interfaces.IPlayer;
import whist.interfaces.IWhistModel;
import whist.model.ScoreboardModel;
import whist.model.TrickModel;
import whist.view.WhistView;

import java.util.Optional;

public class WhistController {
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
        for (; playerNumber < Whist.getInstance().getNbPlayers(); playerNumber++) {
            model.addPlayer(npcFactory.createRandomNPC(playerNumber, trickController));
        }
    }

    public void gameOver(int winner) {
        view.onGameOver(winner);
    }

    public Optional<Integer> playRound() {  // Returns winner, if any// randomly select player to lead for this round

        model.initRound();
        view.showTrump(model.getTrumps());

        try {
            for (int i = 0; i < model.getNbStartCards(); i++) {
                leadTurn();
                for (int j = 1; j < model.getNbPlayers(); j++) {
                    followTurn();
                }
                if (Whist.getInstance().getWinningScore() == scoreboardController.get(model.getCurrentPlayerId())) {
                    return Optional.of(model.getCurrentPlayerId());
                }
                endTrick();
            }
        } catch (BrokeRuleException e) {
            if (Whist.getInstance().isEnforceRules()) {
                e.printStackTrace();
            }
        }
        // until all cards have been played
        view.clearTrump();
        return Optional.empty();
    }

    public void endTrick() {
        System.out.println("End of trick");
        model.setCurrentPlayerId(model.getWinnerId());
        // reset trick hand
        trickController.clear();
        scoreboardController.score(model.getWinnerId());
    }

    public void leadTurn() throws BrokeRuleException {
        IPlayer leadPlayer = model.getCurrentPlayer();
        model.setSelected(leadPlayer.selectCardLead());
        trickController.transfer(model.getSelected(), model.getCurrentPlayerId());
        model.setWinningCard(model.getSelected());
        model.nextPlayer();
    }

    public void followTurn() throws BrokeRuleException {
        IPlayer followPlayer = model.getCurrentPlayer();
        model.setSelected(followPlayer.selectCardFollow(model.getWinningCard(), model.getTrumps()));
        trickController.transfer(model.getSelected(), model.getCurrentPlayerId());
        model.beatCurrentWinner();
        model.nextPlayer();
    }

}
