package whist.view;

import ch.aplu.jcardgame.*;
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
    private Card selected;

    public WhistView(WhistController controller, IWhistModel model) {
        this.controller = controller;
        this.model = model;
        this.layouts = new HashMap<>();
        Whist.getInstance().setTitle("whist.Whist (V" + Whist.getInstance().version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
    }

    public Card getSelected() {
        return selected;
    }

    public void setSelected(Card selected) {
        this.selected = selected;
    }

    public void selectCard() {
        model.getHands()[0].setTouchEnabled(true);
        Whist.getInstance().setStatusText("Player 0 double-click on card to follow.");
        while (null == selected) {
            Whist.getInstance().delay(100);
        }
    }

    public void setListener() {
        // Set up human player for interaction
        CardListener cardListener = new CardAdapter()  // Human Player plays card
        {
            public void leftDoubleClicked(Card card) {
                selected = card;
                model.getHands()[0].setTouchEnabled(false);
            }
        };
        model.getHands()[0].addCardListener(cardListener);
    }

    public Map<Integer, RowLayout> getLayouts() {
        return layouts;
    }

    public void initialise() {
        for (int i = 0; i < Whist.getInstance().nbPlayers; i++) {
            model.getNpcs().get(i).setHand(model.getHands()[i]);
            Hand hand = model.getHands()[i];
            hand.setView(Whist.getInstance(), layouts.get(i));
            hand.setTargetArea(new TargetArea(TrickView.trickLocation));
            hand.sort(Hand.SortType.SUITPRIORITY, true);
            hand.draw();
        }
    }

    public void createLayout() {
        for (int i = 0; i < Whist.getInstance().nbPlayers; i++) {
            RowLayout playerLayout = new RowLayout(handLocations[i], handWidth);
            playerLayout.setRotationAngle(90 * i);
            layouts.put(i, playerLayout);
        }
    }

    public void createView() {
        Whist.getInstance().setStatusText("Initializing...");
        createLayout();
        initialise();
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
