package whist.view;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;
import whist.FontSingleton;
import whist.controller.ScoreboardController;
import whist.interfaces.*;
import whist.model.ScoreboardModel;
import whist.Whist;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ScoreboardView implements IView, IObserver { /*
    TODO: View should hold reference to the game.
     */
    private IObservable topic;
    private IScoreboardModel model;
    private ScoreboardController controller;
    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(575, 25),
            new Location(650, 575)
    };
    private Map<Integer, Actor> scoreActors;


    public ScoreboardView(ScoreboardController controller, IScoreboardModel model) {
        this.scoreActors = new HashMap<>();
        this.model = model;
        this.controller = controller;
        createView();
        model.register(this);
        setSubject(model);
    }

    public void createView() {
        for (int player=0; player<Whist.getInstance().nbPlayers; player++) {
            scoreActors.put(player, new TextActor("0", Color.WHITE, Whist.getInstance().bgColor, FontSingleton.getInstance().getBigFont()));
            Whist.getInstance().addActor(scoreActors.get(player), scoreLocations[player]);
        }
    }

    @Override
    public void update() {
        Iterator<Map.Entry<Integer, Integer>> it = ((IScoreboardModel)topic).getScores().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> pair = it.next();
            int player = pair.getKey();
            int score = pair.getValue();
            if (scoreActors.containsKey(player)) {
                Whist.getInstance().removeActor(scoreActors.get(player));
            }
            scoreActors.put(player, new TextActor(String.valueOf(score), Color.WHITE, Whist.getInstance().bgColor, FontSingleton.getInstance().getBigFont()));
            Whist.getInstance().addActor(scoreActors.get(player), scoreLocations[player]);
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    @Override
    public void setSubject(IObservable subject) {
        this.topic = subject;
    }

}
