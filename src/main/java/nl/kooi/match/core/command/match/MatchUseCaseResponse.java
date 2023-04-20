package nl.kooi.match.core.command.match;

import nl.kooi.match.enums.MatchStatus;
import nl.kooi.match.enums.ResponseType;

public record MatchUseCaseResponse(ResponseType response, MatchStatus status, Long matchId, String matchName) {

    public static MatchUseCaseResponse notExistingTeam() {
        return new MatchUseCaseResponse(ResponseType.MATCH_REQUESTED_FOR_NON_EXISTING_TEAM, null, null, null);
    }

    public static MatchUseCaseResponse successful(MatchStatus status, Long matchId, String matchName) {
        return new MatchUseCaseResponse(ResponseType.PROCESSED_SUCCESSFULLY, status, matchId, matchName);
    }

    public static MatchUseCaseResponse notFound() {
        return new MatchUseCaseResponse(ResponseType.MATCH_NOT_FOUND, null, null, null);
    }

    public static MatchUseCaseResponse statusUpdateNotAllowed(MatchStatus status, Long matchId, String matchName) {
        return new MatchUseCaseResponse(ResponseType.NEW_MATCH_STATUS_NOT_ALLOWED, status, matchId, matchName);
    }
}


