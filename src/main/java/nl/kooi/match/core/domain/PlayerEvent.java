package nl.kooi.match.core.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import nl.kooi.match.enums.PlayerEventType;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
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
