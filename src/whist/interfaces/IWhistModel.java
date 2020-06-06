package whist.interfaces;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import whist.CardUtil;

import java.util.List;

public interface IWhistModel {
    boolean addPlayer(IPlayer player);

    int getNbPlayers();

    int getNbStartCards();

    Hand[] getHands();

    void dealingOut();

    List<IPlayer> getPlayers();

    Card getSelected();

    void setSelected(Card selected);

    Card getWinningCard();

    void setWinningCard(Card winningCard);

    int getCurrentPlayerId();

    void setCurrentPlayerId(int currentPlayer);

    void nextPlayer();

    IPlayer getCurrentPlayer();

    CardUtil.Suit getTrumps();

    void setTrumps(CardUtil.Suit trumps);

    void randomPlayerStartsRound();

    void initRound();

    void beatCurrentWinner();

    int getWinnerId();


}

