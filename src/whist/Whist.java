package whist;

import ch.aplu.jcardgame.CardGame;
import whist.controller.WhistController;
import whist.model.WhistModel;

import java.io.FileInputStream;
import java.util.Optional;
import java.util.Properties;

public class Whist extends CardGame {

    private volatile static Whist uniqueInstance;
    public String version;
    public int nbPlayers = 4;
    public int nbStartCards;
    public int winningScore;
    public int thinkingTime;
    public boolean enforceRules;
    public int seed;
    public int legalNPCs;
    public int player;
    public int smartNPCs;

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
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Whist.getInstance().readPropertiesFile();
        Whist.getInstance().run();
    }

    public void readPropertiesFile() {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println(rootPath);
        String gameConfigPath = rootPath + "original.properties";
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(gameConfigPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.version = props.getProperty("version");
        this.thinkingTime = Integer.parseInt(props.getProperty("thinkingTime"));
        this.seed = Integer.parseInt(props.getProperty("seed"));
        this.winningScore = Integer.parseInt(props.getProperty("winningScore"));
        this.nbStartCards = Integer.parseInt(props.getProperty("nbStartCards"));
        this.legalNPCs = Integer.parseInt(props.getProperty("legalNPCs"));
        this.player = Integer.parseInt(props.getProperty("player"));
        this.smartNPCs = Integer.parseInt(props.getProperty("smartNPCs"));
        this.enforceRules = Boolean.parseBoolean(props.getProperty("enforceRules"));
        assert (legalNPCs + player + smartNPCs == nbPlayers);
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
