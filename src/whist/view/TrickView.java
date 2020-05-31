package whist.view;

import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jgamegrid.Location;
import whist.Trick;
import whist.Whist;
import whist.interfaces.IObserver;
import whist.interfaces.IObservable;
import whist.interfaces.IView;

public class TrickView implements IView, IObserver {
    private Whist game;
    private Trick model;
    private IObservable topic;
    public final Location hideLocation = new Location(-500, - 500);
    public final Location trickLocation = new Location(350, 350);
    public final int trickWidth = 40;

    public TrickView(Whist game, Trick model) {
        this.game = game;
        this.model = model;
        setSubject(model);
        topic.register(this);
    }
    public void clearTrick() {
        this.model.cards.setView(game, new RowLayout(hideLocation, 0));
        this.model.cards.draw();
    }

    @Override
    public void update() {
        Trick trick = (Trick)topic.getUpdate(this);
        if (trick.isHidden) {
            clearTrick();
        } else {
            showTrick();
        }
    }

    @Override
    public void setSubject(IObservable subject) {
        this.topic = subject;
    }

    public void showTrick() {
        // Follow with selected card
        this.model.cards.setView(game, new RowLayout(trickLocation, (this.model.cards.getNumberOfCards()+2)*trickWidth));
        this.model.cards.draw();
    }

}
