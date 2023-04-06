package nl.kooi.match.core.domain.exception;

public class PlayerException extends RuntimeException {

    public PlayerException(String msg) {
        super(msg);
    }


    public static PlayerException notActiveInMatch() {
        return new PlayerNotActiveInMatchException("Player is currently not active in this match.");
    }

    public static PlayerException lineUpNotAllowed() {
        return new LineUpNotAllowedException("Player is already part of match and was not injured.");
    }

    public static PlayerException alreadyHadYellowCard() {
        return new YellowCardException("Player already had a yellow card in this game.");
    }
}
