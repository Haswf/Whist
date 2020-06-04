package whist;

import whist.controller.TrickController;

public class NPCFactory{

    private volatile static NPCFactory uniqueInstance;

    private NPCFactory() {
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

    public NPC createLegalNPC(int playerNumber, TrickController trickController){
        return new NPC(playerNumber, trickController.getModel(), Whist.getInstance().getNbPlayers(), new LegalSelectCardHandle());
    }

    public NPC createSmartNPC(int playerNumber, TrickController trickController){
        return new NPC(playerNumber, trickController.getModel(), Whist.getInstance().getNbPlayers(), new SmartSelectCardHandle());
    }
}
