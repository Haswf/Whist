package whist;

import whist.controller.TrickController;

import java.util.Random;

public class NPCFactory{

    private volatile static NPCFactory uniqueInstance;
    private final Random random;

    private NPCFactory() {
        random = new Random(Whist.getInstance().getSeed());
    }
    public static NPCFactory getInstance() {
        if (uniqueInstance == null) {
            synchronized (DeckFactory.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new NPCFactory();
                }
            }
        }
        return uniqueInstance;
    }

    public NPC createLegalNPC(int playerNumber, TrickController trickController) {
        return new NPC(playerNumber, trickController.getModel(), Whist.getInstance().getNbPlayers(), new LegalStrategy());
    }

    public NPC createSmartNPC(int playerNumber, TrickController trickController) {
        return new NPC(playerNumber, trickController.getModel(), Whist.getInstance().getNbPlayers(), new SmartStrategy());
    }

    public NPC createRandomNPC(int playerNumber, TrickController trickController) {
        if (random.nextBoolean()) {
            return createLegalNPC(playerNumber, trickController);
        } else {
            return createSmartNPC(playerNumber, trickController);
        }
    }
}
