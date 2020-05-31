package whist;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;
import whist.interfaces.IController;
import whist.interfaces.IObserver;
import whist.interfaces.ISubject;
import whist.interfaces.IView;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ScoreboardView implements IView, IObserver {
    private ISubject topic;
    /*
    TODO: View should hold reference to the game.
     */
    private Whist game;
    private Scoreboard model;
    private IController controller;
    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(575, 25),
            new Location(650, 575)
    };
    private Map<Integer, Actor> scoreActors;

    public ScoreboardView(IController controller, Scoreboard model, Whist game) {
        this.scoreActors = new HashMap<>();
        this.model = model;
        this.controller = controller;
        this.game = game;
        createView();
        model.register(this);
        setSubject(model);
    }

    public void createView() {
        for (int player=0; player<game.nbPlayers; player++) {
            scoreActors.put(player, new TextActor("0", Color.WHITE, game.bgColor, game.bigFont));
            game.addActor(scoreActors.get(player), scoreLocations[player]);
        }
    }

    @Override
    public void update() {
        HashMap<Integer, Integer> newScore = (HashMap<Integer, Integer>) topic.getUpdate(this);
        for (Integer player : newScore.keySet()) {
            if (scoreActors.containsKey(player)) {
                game.removeActor(scoreActors.get(player));
            }
            scoreActors.put(player, new TextActor(String.valueOf(newScore.get(player)), Color.WHITE, game.bgColor, game.bigFont));
            game.addActor(scoreActors.get(player), scoreLocations[player]);
        }
    }

    @Override
    public void setSubject(ISubject subject) {
        this.topic=subject;
    }
}
