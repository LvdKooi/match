package nl.kooi.match.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import nl.kooi.match.core.enums.PlayerEventType;

@Getter
@Builder
@ToString
public class PlayerEvent {

    private Long matchId;

    private Long playerId;

    private PlayerEventType eventType;

    private int minute;
}
