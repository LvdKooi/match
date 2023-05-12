package nl.kooi.match.api.dto.player;

import nl.kooi.match.enums.PlayerEventType;

public record PlayerEventDto(Long playerId, PlayerEventType eventType, int minute) {
}
