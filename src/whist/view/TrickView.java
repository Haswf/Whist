package whist.view;

import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import whist.controller.TrickController;
import whist.interfaces.ITrickModel;
import whist.model.TrickModel;
import whist.Whist;
import whist.interfaces.IObserver;
import whist.interfaces.IObservable;
import whist.interfaces.IView;

public class TrickView implements IView, IObserver {
    private boolean isHidden;
    private TrickController controller;
    private ITrickModel model;
    private IObservable topic;
    public final Location hideLocation = new Location(-500, - 500);
    public final Location trickLocation = new Location(350, 350);
    public final int trickWidth = 40;

    public TrickView(TrickController trickController, ITrickModel model) {
        this.isHidden = false;
        this.controller = trickController;
        this.model = model;
        setSubject(model);
        topic.register(this);
    }

    public void clear() {
        GameGrid.delay(600);
        this.model.getCards().setView(Whist.getInstance(), new RowLayout(hideLocation, 0));
    }

    @Override
    public void update() {
        this.model.getCards().setView(Whist.getInstance(), new RowLayout(trickLocation, (this.model.getCards().getNumberOfCards()+2)*trickWidth));
        this.model.getCards().draw();
    }

    @Override
    public void setSubject(IObservable subject) {
        this.topic = subject;
    }
}
