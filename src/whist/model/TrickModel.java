package whist.model;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.DeckFactory;
import whist.interfaces.IObservable;
import whist.interfaces.IObserver;
import whist.interfaces.ITrickModel;

import java.util.ArrayList;
import java.util.List;

public class TrickModel implements IObservable, ITrickModel {
    public Hand cards;
    private Card recentCard;
    private int recentCardPlayerNum;
    private boolean changed;
    private final List<IObserver> observers;

    public TrickModel() {
        this.cards = new Hand(DeckFactory.getInstance().createStandardDeck());
        this.observers = new ArrayList<>();
    }

    public void clear() {
        this.cards.removeAll(true);
    }

    public void initialise() {

    }

    public Hand getCards(){
        return this.cards;
    }

    @Override
    public void transfer(Card selected, int playerNum) {

        selected.transfer(this.cards, true);
        this.recentCard = selected;
        this.recentCardPlayerNum = playerNum;
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


    public Card getRecentCard() {
        return recentCard;
    }

    public int getRecentCardPlayerNum() {
        return recentCardPlayerNum;
    }
}
