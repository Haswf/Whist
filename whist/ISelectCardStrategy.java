import ch.aplu.jcardgame.Card;

public interface ISelectCardStrategy {

    //Card selectCard();

    Card selectCard(Whist.Suit lead);
}
