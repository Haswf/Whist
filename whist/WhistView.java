import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

import java.util.Optional;


public class WhistView implements IView {
    private final Location textLocation = new Location(350, 450);
    private final IController controller;
    private Whist model;

    public WhistView(IController controller, Whist model) {
        this.controller = controller;
        this.model = model;
        model.setTitle("Whist (V" + model.version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
    }
    public void onGameOver(Optional<Integer> winner) {
        model.addActor(new Actor("sprites/gameover.gif"), textLocation);
        model.setStatusText("Game over. Winner is player: " + winner.get());
        model.refresh();
    }





}
