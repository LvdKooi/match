package nl.kooi.match.core.domain.exception;

public class PlayerException extends RuntimeException {

    public PlayerException(String msg) {
        super(msg);
    }


    public static PlayerException notActiveInMatch(){
        return new PlayerNotActiveInMatchException("PLayer is currently not active in this match");
    }
}
