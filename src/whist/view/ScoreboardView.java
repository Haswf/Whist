package whist.view;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;
import whist.FontSingleton;
import whist.Whist;
import whist.controller.ScoreboardController;
import whist.interfaces.IObserver;
import whist.interfaces.IScoreboardModel;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ScoreboardView implements IObserver { /*
     */
    private final IScoreboardModel model;
    private final ScoreboardController controller;
    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(575, 25),
            new Location(650, 575)
    };
    private final Map<Integer, Actor> scoreActors;

    public ScoreboardView(ScoreboardController controller, IScoreboardModel model) {
        this.scoreActors = new HashMap<>();
        this.model = model;
        this.controller = controller;
        createView();
        model.register(this);
    }

    public void createView() {
        for (int player = 0; player < Whist.getInstance().getNbPlayers(); player++) {
            scoreActors.put(player, new TextActor("0", Color.WHITE, Whist.getInstance().bgColor, FontSingleton.getInstance().getBigFont()));
            Whist.getInstance().addActor(scoreActors.get(player), scoreLocations[player]);
        }
    }

    @Override
    public void update() {
        for (Map.Entry<Integer, Integer> pair : model.getScores().entrySet()) {
            int player = pair.getKey();
            int score = pair.getValue();
            if (scoreActors.containsKey(player)) {
                Whist.getInstance().removeActor(scoreActors.get(player));
            }
            scoreActors.put(player, new TextActor(String.valueOf(score), Color.WHITE, Whist.getInstance().bgColor, FontSingleton.getInstance().getBigFont()));
            Whist.getInstance().addActor(scoreActors.get(player), scoreLocations[player]);
        }
    }

    public void showWinner(int winner) {
        Whist.getInstance().setStatusText("Player " + winner + " wins trick.");
    }

}
