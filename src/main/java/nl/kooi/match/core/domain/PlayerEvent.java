package nl.kooi.match.core.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import nl.kooi.match.core.enums.PlayerEventType;

@Getter
@Builder
@ToString
public class PlayerEvent implements Comparable<PlayerEvent> {

    @NotNull
    private Long matchId;

    @NotNull
    private Long playerId;

    @NotNull
    private PlayerEventType eventType;

    private int minute;

    @Override
    public int compareTo(PlayerEvent o) {
        return Integer.compare(this.minute, o.getMinute());
    }
}
