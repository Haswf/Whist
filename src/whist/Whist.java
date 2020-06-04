package whist;

import ch.aplu.jcardgame.CardGame;
import whist.controller.WhistController;
import whist.model.WhistModel;

import java.io.FileInputStream;
import java.util.Optional;
import java.util.Properties;

public class Whist extends CardGame {

    private volatile static Whist uniqueInstance;
    private final int nbPlayers = 4;
    private String gameVersion;
    private int nbStartCards;
    private int winningScore;
    private int thinkingTime;
    private boolean enforceRules;
    private int seed;
    private int legalNPCs;
    private int player;
    private int smartNPCs;

    public String getGameVersion() {
        return gameVersion;
    }

    public int getNbPlayers() {
        return nbPlayers;
    }

    public int getNbStartCards() {
        return nbStartCards;
    }

    public int getWinningScore() {
        return winningScore;
    }

    public int getThinkingTime() {
        return thinkingTime;
    }

    public boolean isEnforceRules() {
        return enforceRules;
    }

    public int getSeed() {
        return seed;
    }

    public int getLegalNPCs() {
        return legalNPCs;
    }

    public int getPlayer() {
        return player;
    }

    public int getSmartNPCs() {
        return smartNPCs;
    }

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
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String configFile = "whist.properties";
        if (args.length == 1) {
            configFile = args[0];
        }
        String gameConfigPath = rootPath + configFile;
        Whist.getInstance().readPropertiesFile(gameConfigPath);
        Whist.getInstance().run();
    }

    public void readPropertiesFile(String gameConfigPath) {

        Properties props = new Properties();
        try {
            props.load(new FileInputStream(gameConfigPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.gameVersion = props.getProperty("version");
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
            whistController.reset();
        } while (!winner.isPresent());
        whistController.gameOver(winner.get());

    }

}
