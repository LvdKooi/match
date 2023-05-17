package nl.kooi.match.exception;

import nl.kooi.match.enums.MatchStatus;

public class MatchStatusException extends RuntimeException {

    public static MatchStatusException matchIsNotActive(String matchName) {
        return new MatchStatusException(String.format("Match %s is not active", matchName));
    }

    public static MatchStatusException matchDoesntAcceptLineUps() {
        return new MatchStatusException("Lineup is only allowed when match has started or is yet to start");
    }

    public static MatchStatusException newStatusNotAllowed(String matchName, MatchStatus from, MatchStatus to) {
        return new MatchStatusException(String.format("Advancing match %s from status %s to status %s is not allowed", matchName, from.name(), to.name()));
    }

    private MatchStatusException(String msg) {
        super(msg);
    }
}
