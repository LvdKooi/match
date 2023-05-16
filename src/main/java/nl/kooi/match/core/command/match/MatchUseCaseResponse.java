package nl.kooi.match.core.command.match;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import nl.kooi.match.enums.MatchStatus;
import nl.kooi.match.enums.ResponseType;

@RequiredArgsConstructor
@Data
public class MatchUseCaseResponse {
    private final ResponseType response;
    private final MatchStatus status;
    private final Long matchId;
    private final String matchName;

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

    public static MatchUseCaseResponse fail() {
        return new MatchUseCaseResponse(ResponseType.PROCESSED_UNSUCCESSFULLY, null, null, null);
    }
}


