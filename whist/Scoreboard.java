import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scoreboard implements ISubject {
    private final Map<Integer, Integer> scores;
    private boolean changed;
    private int nbPlayers;
    private List<IObserver> observers;

    public Scoreboard(int nbPlayers) {
        this.nbPlayers = nbPlayers;
        this.scores = new HashMap<>();
        this.observers = new ArrayList<>();
        initScores();
    }

    public void updateScore(int player, int score) {
        scores.put(player, score);
        this.changed = true;
        notifyObservers();
    }

    public int getScoreByPlayer(int player) {
        return this.scores.get(player);
    }

    private void initScores() {
        for (int player=0; player<nbPlayers; player++) {
            this.scores.put(player, 0);
        }
        // Notify the observers once scoreboard has been initialised
        notifyObservers();
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
