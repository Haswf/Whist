package whist.model;

import whist.Whist;
import whist.interfaces.IObserver;
import whist.interfaces.IObservable;
import whist.interfaces.IScoreboardModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardModel implements IScoreboardModel, IObservable {
    private final Map<Integer, Integer> scores;
    private boolean changed;
    private List<IObserver> observers;

    public ScoreboardModel() {
        this.scores = new HashMap<>();
        this.observers = new ArrayList<>();
    }



    public void put(int player, int score) {
        this.scores.put(player, score);
        this.changed = true;
        notifyObservers();
    }

    public int get(int player) {
        return this.scores.get(player);
    }

    public Map<Integer, Integer> getScores() {
        return scores;
    }

    public void initialise() {
        // Notify the observers once scoreboard has been initialised
        for (int player=0; player< Whist.getInstance().nbPlayers; player++) {
            this.scores.put(player, 0);
        }
    }

    @Override
    public void register(IObserver obj) {
        if(obj == null) throw new NullPointerException("Null Observer");
        synchronized (scores) {
            if(!observers.contains(obj)) {
                observers.add(obj);
            }
        }
    }

    @Override
    public void unregister(IObserver obj) {
        synchronized (scores) {
            observers.remove(obj);
        }
    }

    @Override
    public void notifyObservers() {
        ArrayList<IObserver> observersLocal;
        synchronized (scores) {
            if (!changed)
                return;
            observersLocal = new ArrayList<>(this.observers);
            this.changed = false;
        }
        for (IObserver obj : observersLocal) {
            obj.update();
        }
    }

    @Override
    public Object getUpdate(IObserver obj) {
        return this.scores;
    }
}
