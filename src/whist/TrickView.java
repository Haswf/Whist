package whist;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jcardgame.TargetArea;
import ch.aplu.jgamegrid.Location;
import whist.interfaces.IObserver;
import whist.interfaces.ISubject;
import whist.interfaces.IView;

public class TrickView implements IView, IObserver {
    private Whist game;
    private Trick model;
    private ISubject topic;
    private Location hideLocation = new Location(-500, - 500);
    public final Location trickLocation = new Location(350, 350);
    private final int trickWidth = 40;

    public TrickView(Whist game, Trick model) {
        this.game = game;
        this.model = model;
        setSubject(model);
        topic.register(this);
    }
    public void clearTrick(Hand trick) {
        trick.setView(game, new RowLayout(hideLocation, 0));
        trick.draw();
    }

    @Override
    public void update() {
        Trick trick = (Trick)topic.getUpdate(this);
        if (trick.isHidden) {
            clearTrick(trick.cards);
        } else {
            showTrick(trick.cards);
        }
    }

    @Override
    public void setSubject(ISubject subject) {
        this.topic = subject;
    }

    public void showTrick(Hand trick) {
        // Follow with selected card
        trick.setView(game, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
        trick.draw();
    }

}
