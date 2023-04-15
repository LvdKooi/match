package nl.kooi.match.core.command;

import nl.kooi.match.enums.MatchStatus;
import nl.kooi.match.enums.ResponseType;

public record MatchUseCaseResponse(ResponseType response, MatchStatus status, String matchName) {

    public static MatchUseCaseResponse notExistingTeam() {
        return new MatchUseCaseResponse(ResponseType.MATCH_REQUESTED_FOR_NON_EXISTING_TEAM, null, null);
    }

    public static MatchUseCaseResponse successful(MatchStatus status, String matchName) {
        return new MatchUseCaseResponse(ResponseType.PROCESSED_SUCCESSFULLY, status, matchName);
    }

    public static MatchUseCaseResponse notFound() {
        return new MatchUseCaseResponse(ResponseType.MATCH_NOT_FOUND, null, null);
    }

    public static MatchUseCaseResponse statusUpdateNotAllowed(MatchStatus status, String matchName) {
        return new MatchUseCaseResponse(ResponseType.NEW_MATCH_STATUS_NOT_ALLOWED, status, matchName);
    }
}


