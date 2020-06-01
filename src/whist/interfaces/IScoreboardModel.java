package whist.interfaces;

import java.util.Iterator;
import java.util.Map;

public interface IScoreboardModel extends IObservable {
    void initialise();
    void put(int player, int score);
    int get(int player);
    Map<Integer, Integer> getScores();
}
