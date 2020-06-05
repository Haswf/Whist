package whist.interfaces;

import java.util.Map;

public interface IScoreboardModel extends IObservable {
    void initialise();

    int get(int player);

    Map<Integer, Integer> getScores();

    void score(int player);
}
