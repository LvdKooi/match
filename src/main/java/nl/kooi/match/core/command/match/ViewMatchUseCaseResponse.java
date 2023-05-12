package nl.kooi.match.core.command.match;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.enums.MatchStatus;
import nl.kooi.match.enums.ResponseType;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public final class ViewMatchUseCaseResponse extends MatchUseCaseResponse {
    private final Instant startTimestamp;
    private final Instant endTimestamp;
    private final Set<PlayerEvent> playerEvents;

    public ViewMatchUseCaseResponse(ResponseType response,
                                    Long matchId,
                                    String matchName,
                                    MatchStatus status,
                                    Instant startTimestamp,
                                    Instant endTimestamp,
                                    Set<PlayerEvent> playerEvents) {
        super(response, status, matchId, matchName);
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.playerEvents = playerEvents;
    }


    public static ViewMatchUseCaseResponse successful(Long matchId,
                                                      String matchName,
                                                      MatchStatus status,
                                                      Instant startTimestamp,
                                                      Instant endTimestamp, Set<PlayerEvent> playerEvents) {

        return new ViewMatchUseCaseResponse(ResponseType.PROCESSED_SUCCESSFULLY, matchId, matchName, status, startTimestamp, endTimestamp, playerEvents);

    }

    public static ViewMatchUseCaseResponse notFound(Long matchId) {
        return new ViewMatchUseCaseResponse(ResponseType.MATCH_NOT_FOUND, matchId, null, null, null, null, null);
    }
}
