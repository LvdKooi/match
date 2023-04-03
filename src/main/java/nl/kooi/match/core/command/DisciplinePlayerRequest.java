package nl.kooi.match.core.command;

import nl.kooi.match.core.enums.CardType;

public record DisciplinePlayerRequest(Long playerId, Long matchId, int minute, CardType card) {
}
