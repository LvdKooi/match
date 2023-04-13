package nl.kooi.match.core.command;

import nl.kooi.match.core.enums.MatchStatus;
import nl.kooi.match.core.enums.ResponseType;

public record MatchUseCaseResponse(ResponseType response, MatchStatus status, String matchName) {

    public static MatchUseCaseResponse notExistingTeam() {
        return new MatchUseCaseResponse(ResponseType.MATCH_REQUESTED_FOR_NON_EXISTING_TEAM, null, null);
    }

    public static MatchUseCaseResponse successful(MatchStatus status, String matchName) {
        return new MatchUseCaseResponse(ResponseType.PROCESSED_SUCCESSFULLY, status, matchName);
    }
}


