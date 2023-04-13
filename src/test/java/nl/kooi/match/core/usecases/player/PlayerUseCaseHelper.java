package nl.kooi.match.core.usecases.player;

import lombok.experimental.UtilityClass;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.core.enums.MatchStatus;
import nl.kooi.match.core.enums.PlayerEventType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class PlayerUseCaseHelper {

    public static Match getDefaultMatchForPlayerWithId(Long playerId) {
        return getDefaultMatchForPlayerWithIdAndMatchStatus(playerId, MatchStatus.STARTED);
    }

    public static Match getDefaultMatchForPlayerWithIdAndMatchStatus(Long playerId, MatchStatus status) {
        return getDefaultMatchForPlayerWithId(playerId, Collections.emptySet(), status);
    }

    public static Match getDefaultMatchForPlayerWithId(Long playerId, Set<PlayerEvent> additionalPlayerEvents, MatchStatus status) {
        var playerEvents = new HashSet<PlayerEvent>();
        playerEvents.add(PlayerEvent.builder().matchId(1L).minute(0).playerId(playerId).eventType(PlayerEventType.LINED_UP).build());
        playerEvents.addAll(additionalPlayerEvents);

        return new Match(1L, status, null, null, null, playerEvents);
    }

    public static Match getDefaultMatchForPlayerWithId(Long playerId, Set<PlayerEvent> additionalPlayerEvents) {
        return getDefaultMatchForPlayerWithId(playerId, additionalPlayerEvents, MatchStatus.STARTED);
    }
}
