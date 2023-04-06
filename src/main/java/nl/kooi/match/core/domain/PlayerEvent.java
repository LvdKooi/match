package nl.kooi.match.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import nl.kooi.match.core.enums.PlayerEventType;

@Getter
@Builder
@ToString
public class PlayerEvent implements Comparable<PlayerEvent> {

    private Long matchId;

    private Long playerId;

    private PlayerEventType eventType;

    private int minute;

    @Override
    public int compareTo(PlayerEvent o) {
        return Integer.compare(this.minute, o.getMinute());
    }
}
