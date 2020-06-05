package whist.controller;

import whist.interfaces.IScoreboardModel;
import whist.view.ScoreboardView;

public class ScoreboardController {
    IScoreboardModel model;
    ScoreboardView view;

    public ScoreboardController(IScoreboardModel model) {
        this.model = model;
        this.view = new ScoreboardView(this, model);
        view.createView();
        model.initialise();
    }

    public void score(int player) {
        view.showWinner(player);
        model.score(player);
    }

    public int get(int player) {
        return model.get(player);
    }
}
