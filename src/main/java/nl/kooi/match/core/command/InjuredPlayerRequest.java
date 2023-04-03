package nl.kooi.match.core.command;

import nl.kooi.match.core.enums.InjuryType;

public record InjuredPlayerRequest(Long playerId, Long matchId, int minute, InjuryType injuryType) {
}
