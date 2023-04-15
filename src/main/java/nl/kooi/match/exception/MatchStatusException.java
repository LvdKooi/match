package nl.kooi.match.exception;

public class MatchStatusException extends RuntimeException {

    public static MatchStatusException matchIsNotActive() {
        return new MatchStatusException("Match is not active");
    }

    public static MatchStatusException matchDoesntAcceptLineUps() {
        return new MatchStatusException("Lineup is only allowed when match has started or is yet to start");
    }

    private MatchStatusException(String msg) {
        super(msg);
    }
}
