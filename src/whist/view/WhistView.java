package whist.view;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jcardgame.TargetArea;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import whist.interfaces.IController;
import whist.interfaces.IView;
import whist.Whist;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;


public class WhistView implements IView {
    final String trumpImage[] = {"bigspade.gif","bigheart.gif","bigdiamond.gif","bigclub.gif"};
    Actor trumpsActor;
    private Map<Integer, RowLayout> layouts;
    private final Location trickLocation = new Location(350, 350);
    private Location hideLocation = new Location(-500, - 500);
    private Location trumpsActorLocation = new Location(50, 50);
    private IController controller;
    private final int handWidth = 400;
    private final int trickWidth = 40;
    private final Location[] handLocations = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };

    public void bindLayout(Hand hand, int player) {
        hand.setView(model, layouts.get(player));
        hand.setTargetArea(new TargetArea(trickLocation));
        hand.draw();
    }


    private final Location textLocation = new Location(350, 450);
    private Whist model;

    public WhistView(IController controller, Whist model) {
        this.controller = controller;
        this.model = model;
        this.layouts = new HashMap<>();
        model.setTitle("Whist (V" + model.version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        createView();
    }

    public void createView() {
        for (int i = 0; i < model.nbPlayers; i++) {
            RowLayout playerLayout = new RowLayout(handLocations[i], handWidth);
            playerLayout.setRotationAngle(90 * i);
            layouts.put(i, playerLayout);
        }
    }
    public void showTrick(Hand trick, Card selected) {
        // Follow with selected card
        trick.setView(model, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
        trick.draw();
        selected.setVerso(false);  // In case it is upside down
    }

    public void clearTrick(Hand trick) {
        trick.setView(model, new RowLayout(hideLocation, 0));
        trick.draw();
    }

    public void onGameOver(Optional<Integer> winner) {
        model.addActor(new Actor("sprites/gameover.gif"), textLocation);
        model.setStatusText("Game over. Winner is player: " + winner.get());
        model.refresh();
    }
    public void showTrump(Whist.Suit trumps) {
        // Select and display trump suit
        this.trumpsActor = new Actor("sprites/"+trumpImage[trumps.ordinal()]);
        model.addActor(trumpsActor, trumpsActorLocation);
    }
    public void clearTrump() {
        model.removeActor(trumpsActor);
    }
}
