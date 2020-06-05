package whist.controller;

import ch.aplu.jcardgame.Card;
import whist.interfaces.ITrickModel;
import whist.view.TrickView;

public class TrickController {
    private final ITrickModel model;
    private final TrickView view;

    public TrickController(ITrickModel trickModel) {
        this.model = trickModel;
        this.view = new TrickView(this, model);
    }

    public ITrickModel getModel() {
        return model;
    }

    public void clear() {
        view.clear();
        model.clear();
    }

    public void transfer(Card selected, int playerNum) {
        view.transfer(selected);
        model.transfer(selected, playerNum);
    }
}
