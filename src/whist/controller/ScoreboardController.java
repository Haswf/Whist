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

    public void put(int player, int score) {
        model.put(player, score);
    }
    public void inc(int player) {
        model.put(player, model.get(player) + 1);
    }
    public int get(int player) {
        return model.get(player);
    }
}
