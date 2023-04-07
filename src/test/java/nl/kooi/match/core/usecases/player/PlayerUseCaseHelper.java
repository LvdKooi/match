package nl.kooi.match.core.usecases.player;

import lombok.experimental.UtilityClass;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.core.enums.PlayerEventType;

@UtilityClass
public class PlayerUseCaseHelper {

    public static Match getDefaultMatchForPlayerWithId(Long id) {
        var match = new Match(1L, null, null, null);
        match.playerEvents()
                .add(PlayerEvent.builder().matchId(1L).minute(0).playerId(id).eventType(PlayerEventType.LINED_UP).build());

        return match;
    }

}
