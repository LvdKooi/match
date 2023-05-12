package nl.kooi.match.api;

import nl.kooi.match.api.dto.player.PlayerEventDto;
import nl.kooi.match.enums.MatchStatus;

import java.time.Instant;
import java.util.Set;

public record ViewMatchResponseDto(Long matchId,
                                   MatchStatus matchStatus,
                                   Instant startTimestamp,
                                   Instant endTimestamp,
                                   String matchName,
                                   Set<PlayerEventDto> playerEvents) {
}
