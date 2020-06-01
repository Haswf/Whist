package whist.controller;

import ch.aplu.jcardgame.Card;
import whist.interfaces.IObservable;
import whist.interfaces.ITrickModel;
import whist.model.TrickModel;
import whist.view.TrickView;

public class TrickController {
    private ITrickModel model;
    private IObservable subject;
    private TrickView view;
    public TrickController(ITrickModel trickModel) {
        this.model = trickModel;
        this.view = new TrickView(this, model);
//        view.createView();
        model.initialise();
    }

    public ITrickModel getModel() {
        return model;
    }

    public void clear() {
        view.clear();
        model.clear();
    }

    public void transfer(Card selected, int playerNum) {
        model.transfer(selected, playerNum);
    }
}
