package nl.kooi.match.core.command.player;

import nl.kooi.match.enums.CardType;

public record DisciplinePlayerRequest(Long playerId, Long matchId, int minute, CardType card) {
}
