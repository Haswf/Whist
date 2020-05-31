package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import whist.interfaces.IObserver;
import whist.interfaces.ISubject;

import java.util.ArrayList;
import java.util.List;

public class Trick implements ISubject {
    public boolean isHidden = false;
    public final Hand cards;
    private boolean changed;
    private List<IObserver> observers;

    public Trick() {
        // TODO: Use a factory or singleton to build Deck
        this.cards = new Hand(new Deck(Whist.Suit.values(), Whist.Rank.values(), "cover"));
        this.observers = new ArrayList<>();
    }

    public void addToTrick(Card selected) {
        selected.transfer(this.cards, true);
        changed = true;
        notifyObservers();
    }

    @Override
    public void register(IObserver obj) {
        if(obj == null) throw new NullPointerException("Null Observer");
        synchronized (cards) {
            if(!observers.contains(obj)) {
                observers.add(obj);
            }
        }
    }

    @Override
    public void unregister(IObserver obj) {
        synchronized (cards) {
            observers.remove(obj);
        }
    }

    @Override
    public void notifyObservers() {
        ArrayList<IObserver> observersLocal;
        synchronized (cards) {
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
        return this;
    }
}
