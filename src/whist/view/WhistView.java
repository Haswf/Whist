package whist.view;

import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jcardgame.TargetArea;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import whist.CardUtil;
import whist.Whist;
import whist.controller.WhistController;
import whist.interfaces.IWhistModel;

import java.util.HashMap;
import java.util.Map;


public class WhistView {
    final String[] trumpImage = {"bigspade.gif", "bigheart.gif", "bigdiamond.gif", "bigclub.gif"};
    Actor trumpsActor;
    private final Map<Integer, RowLayout> layouts;
    private final Location trumpsActorLocation = new Location(50, 50);
    private final WhistController controller;

    private final int handWidth = 400;
    private final IWhistModel model;

    private final Location[] handLocations = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };

    private final Location textLocation = new Location(350, 450);

    public WhistView(WhistController controller, IWhistModel model) {
        this.controller = controller;
        this.model = model;
        this.layouts = new HashMap<>();
        Whist.getInstance().setTitle("whist.Whist (V" + Whist.getInstance().getGameVersion() + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
    }

    public Map<Integer, RowLayout> getLayouts() {
        return layouts;
    }

    public void bindHandToLayout() {
        for (int i = 0; i < Whist.getInstance().getNbPlayers(); i++) {
            Hand hand = model.getHands()[i];
            model.getPlayers().get(i).setHand(hand);
            hand.setView(Whist.getInstance(), layouts.get(i));
            hand.setTargetArea(new TargetArea(TrickView.trickLocation));
            hand.sort(Hand.SortType.SUITPRIORITY, true);
            hand.draw();
        }
    }

    public void createLayout() {
        for (int i = 0; i < Whist.getInstance().getNbPlayers(); i++) {
            RowLayout playerLayout = new RowLayout(handLocations[i], handWidth);
            playerLayout.setRotationAngle(90 * i);
            layouts.put(i, playerLayout);
        }
    }

    public void createView() {
        Whist.getInstance().setStatusText("Initializing...");
        createLayout();
        bindHandToLayout();
    }

    public void onGameOver(int winner) {
        Whist.getInstance().addActor(new Actor("sprites/gameover.gif"), textLocation);
        Whist.getInstance().setStatusText("Game over. Winner is player: " + winner);
        Whist.getInstance().refresh();
    }

    public void showTrump(CardUtil.Suit trumps) {
        this.trumpsActor = new Actor("sprites/" + trumpImage[trumps.ordinal()]);
        Whist.getInstance().addActor(trumpsActor, trumpsActorLocation);
    }

    public void clearTrump() {
        Whist.getInstance().removeActor(trumpsActor);
    }
}
