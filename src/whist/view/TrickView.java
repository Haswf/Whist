package whist.view;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import whist.Whist;
import whist.controller.TrickController;
import whist.interfaces.IObserver;
import whist.interfaces.ITrickModel;

public class TrickView implements IObserver {
    public static final Location hideLocation = new Location(-500, -500);
    public static final Location trickLocation = new Location(350, 350);
    private final TrickController controller;
    private final ITrickModel model;
    public final int trickWidth = 40;


    public TrickView(TrickController trickController, ITrickModel model) {
        this.controller = trickController;
        this.model = model;
        model.register(this);
    }

    public void clear() {
        GameGrid.delay(600);
        this.model.getCards().setView(Whist.getInstance(), new RowLayout(hideLocation, 0));
    }

    @Override
    public void update() {
        this.model.getCards().draw();
    }

    public void transfer(Card selected) {
        increaseWidth();
        selected.setVerso(false);  // In case it is upside down
    }

    private void increaseWidth() {
        this.model.getCards().setView(Whist.getInstance(), new RowLayout(trickLocation, (model.getCards().getNumberOfCards() + 2) * trickWidth));
        this.model.getCards().draw();
    }

}
