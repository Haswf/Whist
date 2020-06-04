package whist;

import whist.controller.TrickController;
import whist.interfaces.ISelectCardStrategy;
import whist.interfaces.ITrickModel;
import whist.model.TrickModel;

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
        ISelectCardStrategy strategy = new LegalSelectCardHandle();
        return new NPC(playerNumber, trickController.getModel(), Whist.getInstance().getNbPlayers(), strategy);
    }

    public NPC createSmartNPC(int playerNumber, TrickController trickController){
        ISelectCardStrategy strategy = new SmartSelectCardHandle();
        return new NPC(playerNumber, trickController.getModel(), Whist.getInstance().getNbPlayers(), strategy);
    }
}
