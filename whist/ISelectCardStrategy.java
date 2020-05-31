import ch.aplu.jcardgame.Card;

public interface ISelectCardStrategy {

    Card selectCardLead(Whist.Suit lead);

    Card selectCardFollow(Whist.Suit lead);
}
