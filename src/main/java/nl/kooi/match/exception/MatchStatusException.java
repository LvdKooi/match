package nl.kooi.match.exception;

import nl.kooi.match.enums.MatchStatus;

public class MatchStatusException extends RuntimeException {

    public static MatchStatusException matchIsNotActive() {
        return new MatchStatusException("Match is not active");
    }

    public static MatchStatusException matchDoesntAcceptLineUps() {
        return new MatchStatusException("Lineup is only allowed when match has started or is yet to start");
    }

    public static MatchStatusException newStatusNotAllowed(MatchStatus from, MatchStatus to) {
        return new MatchStatusException(String.format("Advancing match from status %s to status %s is not allowed", from.name(), to.name()));
    }

    private MatchStatusException(String msg) {
        super(msg);
    }
}
