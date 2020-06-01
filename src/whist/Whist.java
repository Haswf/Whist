package whist;

import ch.aplu.jcardgame.CardGame;
import whist.controller.WhistController;
import whist.model.WhistModel;

import java.util.Optional;

public class Whist extends CardGame {

    private volatile static Whist uniqueInstance;
    public final String version = "1.0";
    public final int nbPlayers = 4;
    public final int nbStartCards = 13;
    public final int winningScore = 11;
    public final int thinkingTime = 2000;
    public boolean enforceRules = false;

    private Whist() {
        super(700, 700, 30);
    }

    public static Whist getInstance() {
        if (uniqueInstance == null) {
            synchronized (Whist.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Whist();
                }
            }
        }
        return uniqueInstance;
    }

    public static void main(String[] args) {
        // System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Whist.getInstance().run();
    }

    public void run() {
        WhistController whistController = new WhistController(new WhistModel());
        Optional<Integer> winner;
        do {
            whistController.initialise();
            winner = whistController.playRound();
            whistController.resetNPC();
        } while (!winner.isPresent());
        whistController.gameOver(winner.get());

    }

}
