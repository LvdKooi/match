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

    public static Match getDefaultMatchForPlayerWithId(Long id) {
        return getDefaultMatchForPlayerWithId(id, Collections.emptySet());
    }

    public static Match getDefaultMatchForPlayerWithId(Long id, Set<PlayerEvent> additionalPlayerEvents) {
        var playerEvents = new HashSet<PlayerEvent>();
        playerEvents.add(PlayerEvent.builder().matchId(1L).minute(0).playerId(id).eventType(PlayerEventType.LINED_UP).build());
        playerEvents.addAll(additionalPlayerEvents);

        return new Match(1L, MatchStatus.STARTED, null, playerEvents);
    }

}
